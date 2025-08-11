package canaryprism.slavacord.annotations.processor;

import canaryprism.discordbridge.api.DiscordBridge;
import canaryprism.discordbridge.api.channel.ChannelType;
import canaryprism.discordbridge.api.data.interaction.slash.SlashCommandData;
import canaryprism.discordbridge.api.data.interaction.slash.SlashCommandOptionData;
import canaryprism.discordbridge.api.interaction.slash.SlashCommandOptionType;
import canaryprism.slavacord.CustomChoiceName;
import canaryprism.slavacord.annotations.Command;
import canaryprism.slavacord.annotations.Option;
import canaryprism.slavacord.annotations.optionbounds.ChannelTypeBounds;
import canaryprism.slavacord.annotations.optionbounds.DoubleBounds;
import canaryprism.slavacord.annotations.optionbounds.LongBounds;
import canaryprism.slavacord.annotations.optionbounds.StringLengthBounds;
import canaryprism.slavacord.autocomplete.annotations.Autocompletes;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static canaryprism.discordbridge.api.interaction.slash.SlashCommandOption.*;

@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes({
        "canaryprism.slavacord.annotations.Option",
})
public final class OptionAnnotationProcessor extends AbstractProcessor {
    @Override
    protected void process(TypeElement annotation, Element element, AnnotationMirror annotation_mirror) {
        if (!(element instanceof VariableElement parameter)) {
            message(Diagnostic.Kind.ERROR, "@%s may only be applied to method parameters".formatted(Option.class.getSimpleName()),
                    element, annotation_mirror);
            return;
        }

        if (parameter.getEnclosingElement() instanceof ExecutableElement executable && executable.getAnnotation(Command.class) == null) {
            message(Diagnostic.Kind.ERROR, "method with @%s parameters must have @%s"
                    .formatted(Option.class.getSimpleName(), Command.class.getSimpleName()), executable);
        }

        var optional_type = bridge((bridge) -> inferType(bridge, parameter.asType()))
                .flatMap(Optional::stream)
                .findAny();

        var option = parameter.getAnnotation(Option.class);
        var description_element = ((ExecutableElement) annotation.getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("description"))
                .findAny()
                .orElseThrow());
        var longChoices_element = ((ExecutableElement) annotation.getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("longChoices"))
                .findAny()
                .orElseThrow());
        var doubleChoices_element = ((ExecutableElement) annotation.getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("doubleChoices"))
                .findAny()
                .orElseThrow());
        var stringChoices_element = ((ExecutableElement) annotation.getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("stringChoices"))
                .findAny()
                .orElseThrow());
        var defined_values = parameter.getAnnotationMirrors()
                .stream()
                .filter((e) -> e.getAnnotationType().equals(annotation.asType()))
                .findAny()
                .orElseThrow()
                .getElementValues();

        if (((Object) optional_type.orElse(null)) instanceof SlashCommandOptionType type) {

            message(Diagnostic.Kind.NOTE, "inferred type is %s".formatted(type), parameter, annotation_mirror);

            if (((Object) defined_values.get(description_element)) instanceof AnnotationValue value && option.description().isEmpty())
                message(Diagnostic.Kind.WARNING,
                        "empty description value for @%s, slavacord will generate a description from the name"
                                .formatted(Option.class.getSimpleName()),
                        parameter, annotation_mirror, value);

            if (type != SlashCommandOptionType.INTEGER && ((Object) defined_values.get(longChoices_element)) instanceof AnnotationValue value)
                message(Diagnostic.Kind.ERROR, "longChoices not allowed for type %s".formatted(type), parameter, annotation_mirror, value);
            if (type != SlashCommandOptionType.NUMBER && ((Object) defined_values.get(doubleChoices_element)) instanceof AnnotationValue value)
                message(Diagnostic.Kind.ERROR, "doubleChoices not allowed for type %s".formatted(type), parameter, annotation_mirror, value);
            if (type != SlashCommandOptionType.STRING && ((Object) defined_values.get(stringChoices_element)) instanceof AnnotationValue value)
                message(Diagnostic.Kind.ERROR, "stringChoices not allowed for type %s".formatted(type), parameter, annotation_mirror, value);

            var has_choices = false;

            AnnotationValue choices_value = null;
            if (((Object) defined_values.get(longChoices_element)) instanceof AnnotationValue value) {
                if (option.longChoices().length == 0)
                    message(Diagnostic.Kind.ERROR, "longChoices may not be empty", parameter, annotation_mirror, value);
                choices_value = value;
                has_choices = true;
            }
            if (((Object) defined_values.get(doubleChoices_element)) instanceof AnnotationValue value) {
                if (option.doubleChoices().length == 0)
                    message(Diagnostic.Kind.ERROR, "doubleChoices may not be empty", parameter, annotation_mirror, value);
                choices_value = value;
                has_choices = true;
            }
            if (((Object) defined_values.get(stringChoices_element)) instanceof AnnotationValue value) {
                if (option.stringChoices().length == 0)
                    message(Diagnostic.Kind.ERROR, "stringChoices may not be empty", parameter, annotation_mirror, value);
                choices_value = value;
                has_choices = true;
            }

            if (has_choices) {
                var keys = new HashSet<String>();
                @SuppressWarnings("unchecked")
                var choices = ((List<? extends AnnotationValue>) choices_value.getValue());
                for (var choice : choices) {
                    var choice_mirror = ((AnnotationMirror) choice.getValue());
                    var name_element = ((ExecutableElement) choice_mirror.getAnnotationType()
                            .asElement()
                            .getEnclosedElements()
                            .stream()
                            .filter((e) -> e.getSimpleName().contentEquals("name"))
                            .findAny()
                            .orElseThrow());
                    var name_value = choice_mirror.getElementValues().get(name_element);
                    if (!keys.add(((String) name_value.getValue()))) {
                        message(Diagnostic.Kind.ERROR, "duplicate @%s name '%s'"
                                        .formatted(choice_mirror.getAnnotationType().asElement().getSimpleName(), name_value.getValue()),
                                parameter, choice_mirror, name_value);
                    }
                }
            }


            var has_bounds = false;
            if (((Object) parameter.getAnnotationMirrors()
                    .stream()
                    .filter((e) -> e.getAnnotationType().equals(getTypeMirror(ChannelTypeBounds.class)))
                    .findAny()
                    .orElse(null)) instanceof AnnotationMirror mirror) {
                has_bounds = true;
                if (has_choices)
                    message(Diagnostic.Kind.ERROR, "@%s cannot be present when @%s has choices defined"
                                    .formatted(ChannelTypeBounds.class.getSimpleName(), Option.class.getSimpleName()),
                            parameter, mirror);
                if (type == SlashCommandOptionType.CHANNEL)
                    validateChannelTypeBounds(parameter, mirror, parameter.asType());
                else
                    message(Diagnostic.Kind.ERROR, "@%s not allowed for option type %s"
                                    .formatted(ChannelTypeBounds.class.getSimpleName(), type),
                            parameter, mirror);
            }
            if (((Object) parameter.getAnnotationMirrors()
                    .stream()
                    .filter((e) -> e.getAnnotationType().equals(getTypeMirror(DoubleBounds.class)))
                    .findAny()
                    .orElse(null)) instanceof AnnotationMirror mirror) {
                has_bounds = true;
                if (has_choices)
                    message(Diagnostic.Kind.ERROR, "@%s cannot be present when @%s has choices defined"
                                    .formatted(DoubleBounds.class.getSimpleName(), Option.class.getSimpleName()),
                            parameter, mirror);
                if (type == SlashCommandOptionType.NUMBER)
                    validateDoubleBounds(parameter, mirror);
                else
                    message(Diagnostic.Kind.ERROR, "@%s not allowed for option type %s"
                                    .formatted(DoubleBounds.class.getSimpleName(), type),
                            parameter, mirror);
            }
            if (((Object) parameter.getAnnotationMirrors()
                    .stream()
                    .filter((e) -> e.getAnnotationType().equals(getTypeMirror(LongBounds.class)))
                    .findAny()
                    .orElse(null)) instanceof AnnotationMirror mirror) {
                has_bounds = true;
                if (has_choices)
                    message(Diagnostic.Kind.ERROR, "@%s cannot be present when @%s has choices defined"
                                    .formatted(LongBounds.class.getSimpleName(), Option.class.getSimpleName()),
                            parameter, mirror);
                if (type == SlashCommandOptionType.INTEGER)
                    validateLongBounds(parameter, mirror);
                else
                    message(Diagnostic.Kind.ERROR, "@%s not allowed for option type %s"
                                    .formatted(LongBounds.class.getSimpleName(), type),
                            parameter, mirror);
            }
            if (((Object) parameter.getAnnotationMirrors()
                    .stream()
                    .filter((e) -> e.getAnnotationType().equals(getTypeMirror(StringLengthBounds.class)))
                    .findAny()
                    .orElse(null)) instanceof AnnotationMirror mirror) {
                has_bounds = true;
                if (has_choices)
                    message(Diagnostic.Kind.ERROR, "@%s cannot be present when @%s has choices defined"
                                    .formatted(StringLengthBounds.class.getSimpleName(), Option.class.getSimpleName()),
                            parameter, mirror);
                if (type == SlashCommandOptionType.STRING)
                    validateStringLengthBounds(parameter, mirror);
                else
                    message(Diagnostic.Kind.ERROR, "@%s not allowed for option type %s"
                                    .formatted(StringLengthBounds.class.getSimpleName(), type),
                            parameter, mirror);
            }

            var data = new SlashCommandOptionData("test", "test", type);

            try {
                data.setName(option.name());
            } catch (IllegalArgumentException e) {
                message(Diagnostic.Kind.ERROR,
                        "invalid option name '%s', option names must be between 1 and %s characters long, and match the pattern /%s/ "
                                .formatted(option.name(), SlashCommandData.MAX_NAME_LENGTH, SlashCommandData.NAME_PATTERN), parameter);
            }

            var description = (option.description().isEmpty()) ? option.name() : option.description();

            try {
                data.setDescription(description);
            } catch (IllegalArgumentException e) {
                message(Diagnostic.Kind.ERROR,
                        "invalid option description '%s', option descriptions must be between 1 and %s characters long"
                                .formatted(description, SlashCommandData.MAX_DESCRIPTION_LENGTH), parameter);
            }

            if (((Object) parameter.getAnnotationMirrors()
                    .stream()
                    .filter((mirror) -> mirror.getAnnotationType().equals(getTypeMirror(Autocompletes.class)))
                    .findAny()
                    .orElse(null)) instanceof AnnotationMirror autocompletes_mirror) {
                if (has_choices)
                    message(Diagnostic.Kind.ERROR, "@%s cannot be present when @%s has choices defined"
                                    .formatted(Autocompletes.class.getSimpleName(), Option.class.getSimpleName()),
                            parameter, autocompletes_mirror);
                if (has_bounds)
                    message(Diagnostic.Kind.ERROR, "@%s cannot be present when @%s has option bounds defined"
                                    .formatted(Autocompletes.class.getSimpleName(), Option.class.getSimpleName()),
                            parameter, autocompletes_mirror);

                if (!type.canBeChoices())
                    message(Diagnostic.Kind.ERROR, "@%s not allowed for type %s"
                                    .formatted(Autocompletes.class.getSimpleName(), type),
                            parameter, autocompletes_mirror);
            }
        } else if (types.isAssignable(parameter.asType(), types.erasure(getTypeMirror(Enum.class)))
                && !types.isSameType(types.erasure(parameter.asType()), types.erasure(getTypeMirror(Enum.class)))) {

            message(Diagnostic.Kind.NOTE, "inferred type is enum", parameter);
            // checks for enums :3

            validateEnumOption(parameter, annotation_mirror, (TypeElement) types.asElement(parameter.asType()));

            if (((Object) defined_values.get(longChoices_element)) instanceof AnnotationValue value)
                message(Diagnostic.Kind.ERROR, "longChoices not allowed for enum options", parameter, annotation_mirror, value);
            if (((Object) defined_values.get(doubleChoices_element)) instanceof AnnotationValue value)
                message(Diagnostic.Kind.ERROR, "doubleChoices not allowed for enum options", parameter, annotation_mirror, value);
            if (((Object) defined_values.get(stringChoices_element)) instanceof AnnotationValue value)
                message(Diagnostic.Kind.ERROR, "stringChoices not allowed for enum options", parameter, annotation_mirror, value);

            if (((Object) parameter.getAnnotationMirrors()
                    .stream()
                    .filter((e) -> e.getAnnotationType().equals(getTypeMirror(ChannelTypeBounds.class)))
                    .findAny()
                    .orElse(null)) instanceof AnnotationMirror mirror) {
                message(Diagnostic.Kind.ERROR, "@%s not allowed for enum options"
                                .formatted(ChannelTypeBounds.class.getSimpleName()),
                        parameter, mirror);
            }
            if (((Object) parameter.getAnnotationMirrors()
                    .stream()
                    .filter((e) -> e.getAnnotationType().equals(getTypeMirror(DoubleBounds.class)))
                    .findAny()
                    .orElse(null)) instanceof AnnotationMirror mirror) {
                message(Diagnostic.Kind.ERROR, "@%s not allowed for enum options"
                                .formatted(DoubleBounds.class.getSimpleName()),
                        parameter, mirror);
            }
            if (((Object) parameter.getAnnotationMirrors()
                    .stream()
                    .filter((e) -> e.getAnnotationType().equals(getTypeMirror(LongBounds.class)))
                    .findAny()
                    .orElse(null)) instanceof AnnotationMirror mirror) {
                message(Diagnostic.Kind.ERROR, "@%s not allowed for enum options"
                                .formatted(LongBounds.class.getSimpleName()),
                        parameter, mirror);
            }
            if (((Object) parameter.getAnnotationMirrors()
                    .stream()
                    .filter((e) -> e.getAnnotationType().equals(getTypeMirror(StringLengthBounds.class)))
                    .findAny()
                    .orElse(null)) instanceof AnnotationMirror mirror) {
                message(Diagnostic.Kind.ERROR, "@%s not allowed for enum options"
                                .formatted(StringLengthBounds.class.getSimpleName()),
                        parameter, mirror);
            }

            if (((Object) parameter.getAnnotationMirrors()
                    .stream()
                    .filter((mirror) -> mirror.getAnnotationType().equals(getTypeMirror(Autocompletes.class)))
                    .findAny()
                    .orElse(null)) instanceof AnnotationMirror autocompletes_mirror) {
                message(Diagnostic.Kind.ERROR, "@%s may not be used with enum options"
                                .formatted(Autocompletes.class.getSimpleName()),
                        parameter, autocompletes_mirror);
            }
        } else {
            message(Diagnostic.Kind.ERROR, "can't infer SlashCommandOptionType %s %s", parameter, annotation_mirror);
        }

    }

    private Optional<SlashCommandOptionType> inferType(DiscordBridge bridge, TypeMirror type) {
        var types = bridge.getSupportedValues(SlashCommandOptionType.class)
                .stream()
                .filter((e) ->
                        e != SlashCommandOptionType.UNKNOWN
                                && e != SlashCommandOptionType.SUBCOMMAND
                                && e != SlashCommandOptionType.SUBCOMMAND_GROUP)
                .collect(Collectors.toSet());

        if (type instanceof PrimitiveType primitive)
            type = this.types.boxedClass(primitive).asType();

        var final_type = type;

        return inferDiscordBridgeType(types, type)
                .or(() -> inferImplementationType(bridge, types, final_type));
    }

    @SuppressWarnings("unchecked")
    private Optional<SlashCommandOptionType> inferDiscordBridgeType(Set<? extends SlashCommandOptionType> types, TypeMirror type) {
        var compatible = types.stream()
                .filter((e) -> this.types.isAssignable(type,
                        getTypeMirror(e.getTypeRepresentation())))
                .collect(Collectors.toSet());

        return (Optional<SlashCommandOptionType>) compatible.stream()
                .max(Comparator.comparing((option_type) ->
                        compatible.stream()
                                .filter((e) -> e.getTypeRepresentation()
                                        .isAssignableFrom(option_type.getTypeRepresentation()))
                                .count()));
    }


    @SuppressWarnings("unchecked")
    private Optional<SlashCommandOptionType> inferImplementationType(DiscordBridge bridge, Set<? extends SlashCommandOptionType> types, TypeMirror type) {
        var compatible = types.stream()
                .filter((e) -> this.types.isAssignable(type,
                        getTypeMirror(e.getInternalTypeRepresentation(bridge))))
                .collect(Collectors.toSet());

        return (Optional<SlashCommandOptionType>) compatible.stream()
                .max(Comparator.comparing((option_type) ->
                        compatible.stream()
                                .filter((e) -> e.getTypeRepresentation()
                                        .isAssignableFrom(option_type.getTypeRepresentation()))
                                .count()));
    }

    /// the only real requirement is that everything specified in [ChannelTypeBounds#value()] is assignable to the passed type
    /// oh also it can't be empty
    private void validateChannelTypeBounds(VariableElement parameter, AnnotationMirror annotation_mirror, TypeMirror type) {

        var value_element = ((ExecutableElement) annotation_mirror.getAnnotationType()
                .asElement()
                .getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("value"))
                .findAny()
                .orElseThrow());

        var value = annotation_mirror.getElementValues()
                .get(value_element);

        var types = new HashSet<ChannelType>();

        for (var e : parameter.getAnnotation(ChannelTypeBounds.class).value()) {
            if (!types.add(e)) {
                message(Diagnostic.Kind.WARNING, "duplicate %s in @%s"
                                .formatted(ChannelType.class.getSimpleName(), ChannelTypeBounds.class.getSimpleName()),
                        parameter, annotation_mirror, value);
            }
        }

        if (types.isEmpty()) {
            message(Diagnostic.Kind.ERROR, "empty @%s not allowed"
                            .formatted(ChannelTypeBounds.class.getSimpleName()),
                    parameter, annotation_mirror, value);
            return;
        }

        var bridges = bridge(Function.identity())
                .filter((bridge) -> this.types.isAssignable(
                        type,
                        getTypeMirror(bridge.getInternalTypeRepresentation(SlashCommandOptionType.CHANNEL))))
                .collect(Collectors.toSet());

        // holy shit what is this line of code
        var unassignable = types.stream()
                .filter((e) -> bridges.stream()
                        .map(e::getInternalTypeRepresentation)
                        .map(this::getTypeMirror)
                        .noneMatch((mirror) -> this.types.isAssignable(mirror, type)))
                .collect(Collectors.toSet());
        if (!unassignable.isEmpty()) {
            message(Diagnostic.Kind.ERROR, "%ss %s not assignable to %s"
                            .formatted(SlashCommandOptionType.class.getSimpleName(), unassignable, type),
                    parameter, annotation_mirror, value);
        }
    }

    private void validateDoubleBounds(VariableElement parameter, AnnotationMirror annotation_mirror) {

        var bounds = parameter.getAnnotation(DoubleBounds.class);

        var min_value = ((ExecutableElement) annotation_mirror.getAnnotationType()
                .asElement()
                .getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("min"))
                .findAny()
                .orElseThrow());
        var max_value = ((ExecutableElement) annotation_mirror.getAnnotationType()
                .asElement()
                .getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("max"))
                .findAny()
                .orElseThrow());

        var defined_values = annotation_mirror.getElementValues();

        var used = false;

        var min = Double.NEGATIVE_INFINITY;
        var max = Double.POSITIVE_INFINITY;

        if (((Object) defined_values.get(min_value)) instanceof AnnotationValue value) {
            used = true;
            min = bounds.min();

            if (!(MIN_NUMBER <= min && min <= MAX_NUMBER))
                message(Diagnostic.Kind.ERROR, "%s values must be between %s and %s"
                                .formatted(SlashCommandOptionType.NUMBER, MIN_NUMBER, MAX_NUMBER),
                        parameter, annotation_mirror, value);
        }
        if (((Object) defined_values.get(max_value)) instanceof AnnotationValue value) {
            used = true;
            max = bounds.max();

            if (!(MIN_NUMBER <= max && max <= MAX_NUMBER))
                message(Diagnostic.Kind.ERROR, "%s values must be between %s and %s"
                                .formatted(SlashCommandOptionType.NUMBER, MIN_NUMBER, MAX_NUMBER),
                        parameter, annotation_mirror, value);
        }

        if (min > max)
            message(Diagnostic.Kind.ERROR, "min value %s greater than max value %s"
                            .formatted(min, max),
                    parameter, annotation_mirror);

        if (!used)
            message(Diagnostic.Kind.WARNING, "redundant @%s, neither min nor max value specified"
                            .formatted(DoubleBounds.class.getSimpleName()),
                    parameter, annotation_mirror);
    }

    private void validateLongBounds(VariableElement parameter, AnnotationMirror annotation_mirror) {

        var bounds = parameter.getAnnotation(LongBounds.class);

        var min_value = ((ExecutableElement) annotation_mirror.getAnnotationType()
                .asElement()
                .getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("min"))
                .findAny()
                .orElseThrow());
        var max_value = ((ExecutableElement) annotation_mirror.getAnnotationType()
                .asElement()
                .getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("max"))
                .findAny()
                .orElseThrow());

        var defined_values = annotation_mirror.getElementValues();

        var used = false;

        var min = Long.MIN_VALUE;
        var max = Long.MAX_VALUE;

        if (((Object) defined_values.get(min_value)) instanceof AnnotationValue value) {
            used = true;
            min = bounds.min();

            if (!(MIN_NUMBER <= min && min <= MAX_NUMBER))
                message(Diagnostic.Kind.ERROR, "%s values must be between %s and %s"
                                .formatted(SlashCommandOptionType.INTEGER, MIN_NUMBER, MAX_NUMBER),
                        parameter, annotation_mirror, value);
        }
        if (((Object) defined_values.get(max_value)) instanceof AnnotationValue value) {
            used = true;
            max = bounds.max();

            if (!(MIN_NUMBER <= max && max <= MAX_NUMBER))
                message(Diagnostic.Kind.ERROR, "%s values must be between %s and %s"
                                .formatted(SlashCommandOptionType.INTEGER, MIN_NUMBER, MAX_NUMBER),
                        parameter, annotation_mirror, value);
        }

        if (min > max)
            message(Diagnostic.Kind.ERROR, "min value %s greater than max value %s"
                            .formatted(min, max),
                    parameter, annotation_mirror);

        if (!used)
            message(Diagnostic.Kind.WARNING, "redundant @%s, neither min nor max value specified"
                            .formatted(LongBounds.class.getSimpleName()),
                    parameter, annotation_mirror);
    }
    private void validateStringLengthBounds(VariableElement parameter, AnnotationMirror annotation_mirror) {

        var bounds = parameter.getAnnotation(StringLengthBounds.class);

        var min_value = ((ExecutableElement) annotation_mirror.getAnnotationType()
                .asElement()
                .getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("min"))
                .findAny()
                .orElseThrow());
        var max_value = ((ExecutableElement) annotation_mirror.getAnnotationType()
                .asElement()
                .getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("max"))
                .findAny()
                .orElseThrow());

        var defined_values = annotation_mirror.getElementValues();

        var used = false;

        var min = 0L;
        var max = Long.MAX_VALUE;

        if (((Object) defined_values.get(min_value)) instanceof AnnotationValue value) {
            used = true;
            min = bounds.min();

            if (!(0 <= min && min <= MAX_STRING_LENGTH))
                message(Diagnostic.Kind.ERROR, "%s length must be between %s and %s"
                                .formatted(SlashCommandOptionType.STRING, 0, MAX_STRING_LENGTH),
                        parameter, annotation_mirror, value);
        }
        if (((Object) defined_values.get(max_value)) instanceof AnnotationValue value) {
            used = true;
            max = bounds.max();

            if (!(0 <= max && max <= MAX_STRING_LENGTH))
                message(Diagnostic.Kind.ERROR, "%s length must be between %s and %s"
                                .formatted(SlashCommandOptionType.STRING, 0, MAX_STRING_LENGTH),
                        parameter, annotation_mirror, value);
        }

        if (min > max)
            message(Diagnostic.Kind.ERROR, "min value %s greater than max value %s"
                            .formatted(min, max),
                    parameter, annotation_mirror);

        if (!used)
            message(Diagnostic.Kind.WARNING, "redundant @%s, neither min nor max value specified"
                            .formatted(StringLengthBounds.class.getSimpleName()),
                    parameter, annotation_mirror);
    }

    private void validateEnumOption(VariableElement parameter, AnnotationMirror annotation_mirror, TypeElement type) {
        if (type.getEnclosedElements().stream().noneMatch((e) -> e.getKind() == ElementKind.ENUM_CONSTANT))
            message(Diagnostic.Kind.ERROR, "enum %s has no values".formatted(type.getQualifiedName()), parameter, annotation_mirror);
        if (!types.isAssignable(type.asType(), getTypeMirror(CustomChoiceName.class))) {
            // automatic option choices from declaration name
            // check if names are unique after trimming to 25 characters

            var name_map = new HashMap<String, Set<String>>();

            for (var e : type.getEnclosedElements())
                if (e instanceof VariableElement element && element.getKind() == ElementKind.ENUM_CONSTANT) {
                    var name = element.getSimpleName().toString();
                    var short_name = name.substring(0, Math.min(25, name.length()));
                    name_map.computeIfAbsent(short_name, (key) -> new HashSet<>())
                            .add(name);
                }

            if (name_map.values().stream().anyMatch((e) -> e.size() > 1)) {
                var repeats = name_map.entrySet()
                        .stream()
                        .filter((e) -> e.getValue().size() > 1)
                        .map((e) -> "values [%s] trim to %s"
                                .formatted(e.getValue()
                                                .stream()
                                                .map("'%s'"::formatted)
                                                .collect(Collectors.joining(", ")),
                                        e.getKey()))
                        .collect(Collectors.joining(", "));

                message(Diagnostic.Kind.ERROR,
                        "enum %s contains values that contain duplicate names after truncating to 25 characters, %s; rename the values or implement %s to override this behaviour"
                                .formatted(type.getQualifiedName(), repeats, CustomChoiceName.class.getName()),
                        parameter, annotation_mirror);
            }
        }
    }
}

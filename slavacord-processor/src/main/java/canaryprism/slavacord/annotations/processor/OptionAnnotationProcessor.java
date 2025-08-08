package canaryprism.slavacord.annotations.processor;

import canaryprism.discordbridge.api.DiscordBridge;
import canaryprism.discordbridge.api.data.interaction.slash.SlashCommandData;
import canaryprism.discordbridge.api.data.interaction.slash.SlashCommandOptionData;
import canaryprism.discordbridge.api.interaction.slash.SlashCommandAutocompleteInteraction;
import canaryprism.discordbridge.api.interaction.slash.SlashCommandOptionType;
import canaryprism.slavacord.annotations.Option;
import canaryprism.slavacord.autocomplete.AutocompleteSuggestion;
import canaryprism.slavacord.autocomplete.annotations.Autocompleter;
import canaryprism.slavacord.autocomplete.annotations.Autocompletes;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        this.element = parameter;

        var optional_type = bridge((bridge) -> inferType(bridge, parameter.asType()))
                .stream()
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
                if (!type.canBeChoices())
                    message(Diagnostic.Kind.ERROR, "@%s not allowed for type %s"
                                    .formatted(Autocompletes.class.getSimpleName(), type),
                            parameter, autocompletes_mirror);
                else
                    processAutocompletes(element, autocompletes_mirror, type);
            }
        } else if (types.isAssignable(parameter.asType(), types.erasure(getTypeMirror(Enum.class)))) {

            message(Diagnostic.Kind.NOTE, "inferred type is enum", parameter);
            // checks for enums :3

            if (((Object) defined_values.get(longChoices_element)) instanceof AnnotationValue value)
                message(Diagnostic.Kind.ERROR, "longChoices not allowed for enum options", parameter, annotation_mirror, value);
            if (((Object) defined_values.get(doubleChoices_element)) instanceof AnnotationValue value)
                message(Diagnostic.Kind.ERROR, "doubleChoices not allowed for enum options", parameter, annotation_mirror, value);
            if (((Object) defined_values.get(stringChoices_element)) instanceof AnnotationValue value)
                message(Diagnostic.Kind.ERROR, "stringChoices not allowed for enum options", parameter, annotation_mirror, value);

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

    /// we have to process autocompletes here because we need the [SlashCommandOptionType]
    /// which only OptionAnnotationProcessor does the inference for
    @SuppressWarnings("ConstantValue")
    private void processAutocompletes(Element element, AnnotationMirror annotation_mirror, SlashCommandOptionType type) {
        if (!(element instanceof VariableElement)) {
            message(Diagnostic.Kind.ERROR, "@%s may only be applied to method parameters".formatted(Autocompletes.class.getSimpleName()),
                    element, annotation_mirror);
            return;
        }
        var annotation = annotation_mirror.getAnnotationType().asElement();

        var autocompletes = element.getAnnotation(Autocompletes.class);


        var autocompleterClass_element = ((ExecutableElement) annotation.getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("autocompleterClass"))
                .findAny()
                .orElseThrow());
        var autocompleter_element = ((ExecutableElement) annotation.getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("autocompleter"))
                .findAny()
                .orElseThrow());

        var autocompleter_class = ((TypeElement) element.getEnclosingElement().getEnclosingElement());

        if (annotation_mirror.getElementValues().containsKey(autocompleterClass_element)) {
            autocompleter_class = elements.getTypeElement(autocompletes.autocompleterClass().getName());
        }

        var autocompleter_value = annotation_mirror.getElementValues().get(autocompleter_element);

        // okay this is really annoying because morally i *have* to put some effort of inferring what could possibly have been intended

        var methods = autocompleter_class.getEnclosedElements()
                .stream()
                .filter(ExecutableElement.class::isInstance)
                .map(ExecutableElement.class::cast)
                .collect(Collectors.toSet());

        var name_matches = methods.stream()
                .filter((e) -> e.getSimpleName().contentEquals(autocompletes.autocompleter()))
                .collect(Collectors.toSet());

        var has_autocompleter = methods.stream()
                .filter((e) -> e.getAnnotation(Autocompleter.class) != null)
                .collect(Collectors.toSet());

        var valid_signature = methods.stream()
                .filter((e) -> validateAutocompleter(e, type))
                .collect(Collectors.toSet());

        var correct_dispatch = (autocompleter_class.equals(element.getEnclosingElement().getEnclosingElement())) ?
                methods
                : methods.stream()
                        .filter((e) -> e.getModifiers().contains(Modifier.STATIC))
                        .collect(Collectors.toSet());

        var passed = name_matches.stream()
                .filter(has_autocompleter::contains)
                .filter(valid_signature::contains)
                .filter(correct_dispatch::contains)
                .collect(Collectors.toSet());

        if (passed.size() > 1) {
            message(Diagnostic.Kind.ERROR,
                    "multiple valid autocompleter methods found in type '%s' with name '%s' for type %s"
                            .formatted(autocompleter_class.getQualifiedName(), autocompletes.autocompleter(), type),
                    element, annotation_mirror, autocompleter_value);
        }

        if (passed.isEmpty()) {
            var sb = new StringBuilder("no valid autocompleter method found in type '%s' with name '%s' for type %s, "
                    .formatted(autocompleter_class.getQualifiedName(), autocompletes.autocompleter(), type));

            if (((Object) intersection(name_matches, valid_signature, has_autocompleter)) instanceof Set<?> set && !set.isEmpty()) {
                sb.append("found ").append(" otherwise valid autocompleter methods with provided name but aren't static, " +
                        "autocompleter methods may only be nonstatic if they are in the same class as this option");
            } else if (((Object) intersection(name_matches, valid_signature, correct_dispatch)) instanceof Set<?> set && !set.isEmpty()) {
                sb.append("found ").append(set.size()).append(" otherwise valid autocompleter methods with provided name with missing @Autocompleter");
            } else if (!intersection(valid_signature, has_autocompleter, correct_dispatch).isEmpty()) {
                var set = intersection(valid_signature, has_autocompleter, correct_dispatch);
                sb
                        .append("found valid autocompleter methods [")
                        .append(set.stream()
                                .map(ExecutableElement::getSimpleName)
                                .map(Objects::toString)
                                .collect(Collectors.joining(", ")))
                        .append("]");
            } else if (!name_matches.isEmpty()) {
                sb.append("found ").append(name_matches.size()).append(" methods with provided name but none are valid autocompleter methods");
            } else {
                sb.append("no valid autocompleter methods found in class");
            }

            message(Diagnostic.Kind.ERROR, sb.toString(), element, annotation_mirror);
        }
    }


    @SafeVarargs
    private <E> Set<E> intersection(Set<E> first, Set<E>... more) {
        var result = new HashSet<E>();
        loop:
        for (var e : first) {
            for (var set : more)
                if (!set.contains(e))
                    continue loop;
            result.add(e);
        }
        return result;
    }


    private boolean validateAutocompleter(ExecutableElement executable, SlashCommandOptionType type) {
        return Stream.concat(
                        Stream.of(type.getTypeRepresentation()),
                        bridge(type::getInternalTypeRepresentation).stream())
                .map(this::getTypeMirror)
                .anyMatch((e) -> validateAutocompleter(executable, e));
    }

    private boolean validateAutocompleter(ExecutableElement executable, TypeMirror type) {
        return validateAutocompleterParameters(executable, type) && validateAutocompleterReturnType(executable, type);
    }

    private boolean validateAutocompleterParameters(ExecutableElement executable, TypeMirror type) {
        enum AutocompleterParameter {
            INTERACTION, VALUE
        }

        var autocomplete_interaction_types = Stream.concat(
                        bridge((e) -> e.getImplementationType(SlashCommandAutocompleteInteraction.class))
                                .stream()
                                .flatMap(Optional::stream),
                        Stream.of(SlashCommandAutocompleteInteraction.class))
                .map(this::getTypeMirror)
                .collect(Collectors.toSet());

        var parameters = new HashSet<AutocompleterParameter>();

        for (var parameter : executable.getParameters()) {
            if (type.equals(parameter.asType()))
                if (!parameters.add(AutocompleterParameter.VALUE))
                    return false;
            if (autocomplete_interaction_types.contains(parameter.asType()))
                if (!parameters.add(AutocompleterParameter.INTERACTION))
                    return false;
        }

        return true;
    }

    private boolean validateAutocompleterReturnType(ExecutableElement executable, TypeMirror type) {
        var list_of_autocomplete_suggestion_of_type = types.getDeclaredType(
                elements.getTypeElement(List.class.getName()),
                types.getWildcardType(
                        types.getDeclaredType(
                                elements.getTypeElement(AutocompleteSuggestion.class.getName()),
                                types.getWildcardType(
                                        type, null)), null));

        return types.isAssignable(executable.getReturnType(), list_of_autocomplete_suggestion_of_type);
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

    Element element;
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
}

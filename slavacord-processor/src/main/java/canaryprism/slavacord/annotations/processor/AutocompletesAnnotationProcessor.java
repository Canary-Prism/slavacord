package canaryprism.slavacord.annotations.processor;


import canaryprism.discordbridge.api.interaction.slash.SlashCommandAutocompleteInteraction;
import canaryprism.slavacord.annotations.Option;
import canaryprism.slavacord.autocomplete.AutocompleteSuggestion;
import canaryprism.slavacord.autocomplete.annotations.Autocompleter;
import canaryprism.slavacord.autocomplete.annotations.Autocompletes;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes({
        "canaryprism.slavacord.autocomplete.annotations.Autocompletes",
})
public final class AutocompletesAnnotationProcessor extends AbstractProcessor {
    @SuppressWarnings("ConstantValue")
    @Override
    protected void process(TypeElement annotation, Element element, AnnotationMirror annotation_mirror) {
        if (!(element instanceof VariableElement parameter)) {
            message(Diagnostic.Kind.ERROR, "@%s may only be applied to method parameters".formatted(Autocompletes.class.getSimpleName()),
                    element, annotation_mirror);
            return;
        }

        if (parameter.getAnnotation(Option.class) == null)
            message(Diagnostic.Kind.ERROR, "@%s cannot be used without @%s"
                    .formatted(Autocompletes.class.getSimpleName(), Option.class.getSimpleName()), element, annotation_mirror);

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

        var type = parameter.asType();

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
            if (type.equals(parameter.asType())) {
                if (!parameters.add(AutocompleterParameter.VALUE))
                    return false;
            } else if (autocomplete_interaction_types.contains(parameter.asType())) {
                if (!parameters.add(AutocompleterParameter.INTERACTION))
                    return false;
            } else {
                return false;
            }
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
}

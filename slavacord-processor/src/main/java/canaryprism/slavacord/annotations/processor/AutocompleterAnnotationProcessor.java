package canaryprism.slavacord.annotations.processor;

import canaryprism.discordbridge.api.interaction.slash.SlashCommandAutocompleteInteraction;
import canaryprism.discordbridge.api.interaction.slash.SlashCommandOptionType;
import canaryprism.slavacord.autocomplete.AutocompleteSuggestion;
import canaryprism.slavacord.autocomplete.annotations.Autocompleter;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes({
        "canaryprism.slavacord.autocomplete.annotations.Autocompleter",
})
public final class AutocompleterAnnotationProcessor extends AbstractProcessor {
    @Override
    protected void process(TypeElement annotation, Element element, AnnotationMirror annotation_mirror) {
        if (!(element instanceof ExecutableElement executable)) {
            message(Diagnostic.Kind.ERROR, "@%s may only be applied to methods".formatted(Autocompleter.class.getSimpleName()),
                    element, annotation_mirror);
            return;
        }



        if (((Object) getAutocompleterReturnType(executable).orElse(null)) instanceof TypeMirror type) {
            validateAutocompleterParameters(executable, type);
        } else {
            message(Diagnostic.Kind.ERROR, "invalid autocompleter method return type '%s'".formatted(executable.getReturnType()),
                    executable, annotation_mirror);
        }
    }



    private void validateAutocompleterParameters(ExecutableElement executable, TypeMirror type) {
        enum AutocompleterParameter {
            INTERACTION, VALUE
        }

        var autocomplete_interaction_types = bridge((e) -> e.getImplementationType(SlashCommandAutocompleteInteraction.class))
                .flatMap(Optional::stream)
                .map(this::getTypeMirror)
                .collect(Collectors.toSet());

        var parameters = new HashSet<AutocompleterParameter>();

        for (var parameter : executable.getParameters()) {
            if (types.isAssignable(type, parameter.asType()) && types.isAssignable(parameter.asType(), type)) {
                if (!parameters.add(AutocompleterParameter.VALUE))
                    message(Diagnostic.Kind.ERROR, "parameter taking partial input may appear at most once in autocompleter method", parameter);
            } else if (autocomplete_interaction_types.contains(parameter.asType())) {
                if (!parameters.add(AutocompleterParameter.INTERACTION))
                    message(Diagnostic.Kind.ERROR, "parameter taking autocomplete interaction may appear at most once in autocompleter method", parameter);
            } else {
                message(Diagnostic.Kind.ERROR, "invalid parameter for autocomplter method for type %s".formatted(type), parameter);
            }
        }

    }

    private Optional<TypeMirror> getAutocompleterReturnType(ExecutableElement executable) {
        return bridge((bridge) -> Arrays.stream(SlashCommandOptionType.values())
                .filter((e) -> e != SlashCommandOptionType.UNKNOWN
                        && e != SlashCommandOptionType.SUBCOMMAND
                        && e != SlashCommandOptionType.SUBCOMMAND_GROUP)
                .map(bridge::getInternalTypeRepresentation))
                .flatMap(Function.identity())
                .map(this::getTypeMirror)
                .filter((e) -> validateAutocompleterReturnType(executable, e))
                .findAny();
    }

    private boolean validateAutocompleterReturnType(ExecutableElement executable, TypeMirror type) {
        var list_of_autocomplete_suggestion_of_type = types.getDeclaredType(
                elements.getTypeElement(List.class.getName()),
                types.getWildcardType(
                        types.getDeclaredType(
                                elements.getTypeElement(AutocompleteSuggestion.class.getName()),
                                types.getWildcardType(
                                        (type instanceof PrimitiveType primitive) ? types.boxedClass(primitive).asType() : type, null)), null));

        return types.isAssignable(executable.getReturnType(), list_of_autocomplete_suggestion_of_type);
    }
}

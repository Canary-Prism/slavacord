package canaryprism.slavacord.annotations.processor;

import canaryprism.discordbridge.api.data.interaction.slash.SlashCommandData;
import canaryprism.slavacord.Commands;
import canaryprism.slavacord.annotations.Command;
import canaryprism.slavacord.annotations.CommandGroup;
import canaryprism.slavacord.annotations.Interaction;
import canaryprism.slavacord.annotations.Option;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.HashSet;

@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes({
        "canaryprism.slavacord.annotations.Command",
})
public final class CommandAnnotationProcessor extends AbstractProcessor {
    @Override
    protected void process(TypeElement annotation, Element element, AnnotationMirror annotation_mirror) {
        if (!(element instanceof ExecutableElement executable)) {
            message(Diagnostic.Kind.ERROR, "@%s may only be applied to methods".formatted(Command.class), element, annotation_mirror);
            return;
        }

        var name_element = ((ExecutableElement) annotation.getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("name"))
                .findAny()
                .orElseThrow());
        var description_element = ((ExecutableElement) annotation.getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("description"))
                .findAny()
                .orElseThrow());

        var defined_values = annotation_mirror.getElementValues();

        var command = executable.getAnnotation(Command.class);

        var data = new SlashCommandData("data", "data");

        try {
            data.setName(command.name());
        } catch (IllegalArgumentException e) {
            message(Diagnostic.Kind.ERROR,
                    "invalid command name '%s', command names must be between 1 and %s characters long, and match the pattern /%s/ "
                            .formatted(command.name(), SlashCommandData.MAX_NAME_LENGTH, SlashCommandData.NAME_PATTERN),
                    executable, annotation_mirror, defined_values.get(name_element));
        }

        try {
            data.setDescription(command.description());
        } catch (IllegalArgumentException e) {
            message(Diagnostic.Kind.ERROR,
                    "invalid command description '%s', command descriptions must be between 1 and %s characters long"
                            .formatted(command.description(), SlashCommandData.MAX_DESCRIPTION_LENGTH),
                    executable, annotation_mirror, defined_values.get(description_element));
        }

        var parameters = executable.getParameters();
        var name_set = new HashSet<String>();
        for (int i = 0; i < parameters.size(); i++) {
            var e = parameters.get(i);

            if (e.getAnnotation(Option.class) != null && e.getAnnotation(Interaction.class) != null)
                message(Diagnostic.Kind.ERROR, "parameter in @%s method must not be annotated both @%s and @%s"
                        .formatted(Command.class.getSimpleName(), Option.class.getSimpleName(), Interaction.class.getSimpleName()), e);

            if (i == 0) {
                if (e.getAnnotation(Option.class) == null && e.getAnnotation(Interaction.class) == null)
                    message(Diagnostic.Kind.ERROR, "parameter in @%s method must be annotated @%s or @%s"
                            .formatted(Command.class.getSimpleName(), Option.class.getSimpleName(), Interaction.class.getSimpleName()), e);
            } else {
                if (e.getAnnotation(Interaction.class) != null) {
                    message(Diagnostic.Kind.ERROR, "only the first parameter in a @%s method may be annotated @%s"
                            .formatted(Command.class.getSimpleName(), Option.class.getSimpleName()), e);
                } else if (e.getAnnotation(Option.class) == null) {
                    message(Diagnostic.Kind.ERROR, "parameter in @%s method must be annotated @%s"
                            .formatted(Command.class.getSimpleName(), Option.class.getSimpleName()), e);
                }
            }

            if (((Object) e.getAnnotation(Option.class)) instanceof Option option) {
                if (!name_set.add(option.name())) {
                    var option_mirror = e.getAnnotationMirrors()
                            .stream()
                            .filter((mirror) -> mirror.getAnnotationType().equals(getTypeMirror(Option.class)))
                            .findAny()
                            .orElseThrow();
                    var option_name_value = ((ExecutableElement) option_mirror.getAnnotationType()
                            .asElement()
                            .getEnclosedElements()
                            .stream()
                            .filter((mewo) -> mewo.getSimpleName().contentEquals("name"))
                            .findAny()
                            .orElseThrow());

                    message(Diagnostic.Kind.ERROR, "duplicate @%s name %s".formatted(Option.class.getSimpleName(), option.name()),
                            e, option_mirror, option_mirror.getElementValues().get(option_name_value));
                }
            }

        }


        if (executable.getEnclosingElement() instanceof TypeElement type
                && type.getAnnotation(CommandGroup.class) == null
                && !type.getInterfaces().contains(getTypeMirror(Commands.class))) {
            message(Diagnostic.Kind.ERROR,
                    "method with @%s not nested in %s class or command group"
                            .formatted(Command.class.getSimpleName(), Commands.class.getSimpleName())
                    , executable, annotation_mirror);
        }
    }
}

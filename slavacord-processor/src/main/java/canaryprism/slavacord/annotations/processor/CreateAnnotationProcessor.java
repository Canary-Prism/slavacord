package canaryprism.slavacord.annotations.processor;

import canaryprism.slavacord.Commands;
import canaryprism.slavacord.annotations.Command;
import canaryprism.slavacord.annotations.CommandGroup;
import canaryprism.slavacord.annotations.CreateGlobal;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic;
import java.util.List;

@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes({
        "canaryprism.slavacord.annotations.CreateGlobal",
        "canaryprism.slavacord.annotations.CreateServer",
})
public final class CreateAnnotationProcessor extends AbstractProcessor {

    @Override
    public void process(TypeElement annotation, Element e, AnnotationMirror annotation_mirror) {

        if (!(e instanceof TypeElement type)) {
            message(Diagnostic.Kind.ERROR,
                    "@%s may only be applied to types"
                            .formatted(annotation.getSimpleName()), e, annotation_mirror);
            return;
        }

        if (type.getEnclosedElements()
                .stream()
                .noneMatch((element) -> element.getAnnotation(CommandGroup.class) != null || e.getAnnotation(Command.class) != null))
            message(Diagnostic.Kind.WARNING, "%s type has no @%s methods or @%s types"
                            .formatted(Commands.class.getSimpleName(), Command.class.getSimpleName(), CommandGroup.class.getSimpleName()),
                    type, annotation_mirror);

        if (type.getInterfaces()
                .stream()
                .noneMatch((mirror) -> (mirror instanceof DeclaredType declared)
                        && (declared.asElement() instanceof TypeElement element)
                        && element.getQualifiedName().contentEquals(Commands.class.getName())))
            message(Diagnostic.Kind.ERROR, "type %s must implement %s"
                    .formatted(type.getSimpleName(), Commands.class.getName()), type);

        if (annotation.getQualifiedName().contentEquals(CreateGlobal.class.getName())) {
            var contexts_element = ((ExecutableElement) annotation.getEnclosedElements()
                    .stream()
                    .filter((element) -> element.getSimpleName().contentEquals("contexts"))
                    .findAny()
                    .orElseThrow());

            var install_element = ((ExecutableElement) annotation.getEnclosedElements()
                    .stream()
                    .filter((element) -> element.getSimpleName().contentEquals("install"))
                    .findAny()
                    .orElseThrow());


            var values = type.getAnnotationMirrors()
                    .stream()
                    .filter((mirror) -> mirror.getAnnotationType().equals(annotation.asType()))
                    .findAny()
                    .orElseThrow()
                    .getElementValues();

            if (((Object) values.get(contexts_element)) instanceof AnnotationValue value && value.getValue() instanceof List<?> contexts && contexts.isEmpty())
                message(Diagnostic.Kind.WARNING,
                        "empty contexts value for @%s, discord-bridge will interpret this as an implementation-specific default value"
                                .formatted(CreateGlobal.class.getSimpleName()), type, annotation_mirror, value);

            if (((Object) values.get(install_element)) instanceof AnnotationValue value && value.getValue() instanceof List<?> installs && installs.isEmpty())
                message(Diagnostic.Kind.WARNING,
                        "empty install value for @%s, discord-bridge will interpret this as an implementation-specific default value"
                                .formatted(CreateGlobal.class.getSimpleName()), type, annotation_mirror, value);
        }
    }
}

package canaryprism.slavacord.annotations.processor;

import canaryprism.slavacord.annotations.Command;
import canaryprism.slavacord.annotations.CommandGroup;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;

@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes({
        "canaryprism.slavacord.annotations.CommandGroup",
})
public final class CommandGroupAnnotationProcessor extends AbstractProcessor {
    @Override
    protected void process(TypeElement annotation, Element element, AnnotationMirror annotation_mirror) {
        if (!(element instanceof TypeElement type)) {
            message(Diagnostic.Kind.ERROR, "@%s may only be applied to methods".formatted(Command.class), element, annotation_mirror);
            return;
        }

        if (type.getEnclosedElements()
                .stream()
                .noneMatch((e) -> (e instanceof ExecutableElement ex)
                        && ex.getKind() == ElementKind.CONSTRUCTOR && ex.getParameters().isEmpty())) {

            if (hasNonstaticCommandMethod(type)) {
                message(Diagnostic.Kind.ERROR, "no-args constructor required for @%s containing nonstatic commands"
                        .formatted(CommandGroup.class.getSimpleName()), type, annotation_mirror);
            }
        }
    }

    private boolean hasNonstaticCommandMethod(TypeElement type) {
        for (var element : type.getEnclosedElements()) {
            if (element instanceof TypeElement nested_type) {
                if (hasNonstaticCommandMethod(nested_type))
                    return true;
            } else if (element instanceof ExecutableElement executable && executable.getAnnotation(Command.class) != null) {
                if (!executable.getModifiers().contains(Modifier.STATIC))
                    return true;
            }
        }
        return false;
    }
}

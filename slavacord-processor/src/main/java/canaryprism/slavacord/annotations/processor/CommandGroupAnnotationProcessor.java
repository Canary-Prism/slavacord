package canaryprism.slavacord.annotations.processor;

import canaryprism.slavacord.Commands;
import canaryprism.slavacord.annotations.Command;
import canaryprism.slavacord.annotations.CommandGroup;
import canaryprism.slavacord.annotations.CreateGlobal;
import canaryprism.slavacord.annotations.CreateServer;

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

        var depth = 0;

        if (type.getEnclosingElement() instanceof TypeElement enclosing) {
            while (true) {
                if (enclosing.getAnnotation(CommandGroup.class) != null) {
                    depth++;
                    if (depth == 2) {
                        message(Diagnostic.Kind.ERROR, "@%s may not be nested more than 2 levels"
                                .formatted(CommandGroup.class.getSimpleName()), type, annotation_mirror);
                    }
                    if (enclosing.getEnclosingElement() instanceof TypeElement e) {
                        enclosing = e;
                        continue;
                    }
                } else if (enclosing.getInterfaces().contains(getTypeMirror(Commands.class))) {
                    if (enclosing.getAnnotation(CreateGlobal.class) == null && enclosing.getAnnotation(CreateServer.class) == null)
                        message(Diagnostic.Kind.ERROR,
                                "%s type must be annotated with @%s or @%s"
                                        .formatted(Commands.class.getSimpleName(), CreateGlobal.class.getSimpleName(), CreateServer.class.getSimpleName()),
                                enclosing);
                } else {
                    message(Diagnostic.Kind.ERROR,
                            "@%s type not nested in %s class or command group"
                                    .formatted(CommandGroup.class.getSimpleName(), Commands.class.getSimpleName())
                            , type, annotation_mirror);
                }
                break;
            }
        } else {
            message(Diagnostic.Kind.ERROR, "@%s type must be nested in another type"
                    .formatted(CommandGroup.class.getSimpleName()), type, annotation_mirror);
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

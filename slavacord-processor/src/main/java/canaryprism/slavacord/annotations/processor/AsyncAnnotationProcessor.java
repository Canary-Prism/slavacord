package canaryprism.slavacord.annotations.processor;

import canaryprism.slavacord.CommandHandler;
import canaryprism.slavacord.ThreadingMode;
import canaryprism.slavacord.annotations.Async;
import canaryprism.slavacord.annotations.ReturnsResponse;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;

@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes({
        "canaryprism.slavacord.annotations.Async",
})
public final class AsyncAnnotationProcessor extends AbstractProcessor {
    @Override
    protected void process(TypeElement annotation, Element element, AnnotationMirror annotation_mirror) {
        var threadingMode_element = ((ExecutableElement) annotation.getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("threadingMode"))
                .findAny()
                .orElseThrow());

        var respondLater_element = ((ExecutableElement) annotation.getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("threadingMode"))
                .findAny()
                .orElseThrow());

        var values = element.getAnnotationMirrors()
                .stream()
                .filter((e) -> e.getAnnotationType().equals(annotation.asType()))
                .findAny()
                .orElseThrow()
                .getElementValues();

        if (((Object) values.get(threadingMode_element)) instanceof AnnotationValue value
                && value.getValue() instanceof VariableElement variable
                && variable.getSimpleName().contentEquals(ThreadingMode.NONE.name()))
            message(Diagnostic.Kind.WARNING,
                    "@%s with threadingMode set to NONE is redundant and will use the %s's threading mode"
                            .formatted(Async.class.getSimpleName(), CommandHandler.class.getSimpleName()),
                    element, annotation_mirror, value);

        if (((Object) values.get(respondLater_element)) instanceof AnnotationValue value
                && element.getAnnotation(ReturnsResponse.class) == null)
            message(Diagnostic.Kind.WARNING, "@%s with respondLater set only has any effect if @%s is also present"
                            .formatted(Async.class.getSimpleName(), ReturnsResponse.class.getSimpleName()),
                    element, annotation_mirror, value);
    }
}

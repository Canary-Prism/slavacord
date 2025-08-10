package canaryprism.slavacord.annotations.processor;

import canaryprism.slavacord.annotations.Option;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;

@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes({
        "canaryprism.slavacord.annotations.optionbounds.ChannelTypeBounds",
        "canaryprism.slavacord.annotations.optionbounds.DoubleBounds",
        "canaryprism.slavacord.annotations.optionbounds.LongBounds",
        "canaryprism.slavacord.annotations.optionbounds.StringLengthBounds",
})
public final class OptionBoundAnnotationProcessor extends AbstractProcessor {
    @Override
    protected void process(TypeElement annotation, Element element, AnnotationMirror annotation_mirror) {
        if (!(element instanceof VariableElement parameter)) {
            message(Diagnostic.Kind.ERROR, "@%s may only be applied to method parameters".formatted(Option.class.getSimpleName()),
                    element, annotation_mirror);
            return;
        }

        if (parameter.getAnnotation(Option.class) == null)
            message(Diagnostic.Kind.ERROR, "@%s cannot be used without @%s"
                            .formatted(annotation_mirror.getAnnotationType().asElement().getSimpleName(), Option.class.getSimpleName()),
                    parameter, annotation_mirror);
    }
}

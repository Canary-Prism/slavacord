package canaryprism.slavacord.annotations.processor;

import canaryprism.slavacord.annotations.ReturnsResponse;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Optional;

@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes({
        "canaryprism.slavacord.annotations.ReturnsResponse",
})
public final class ReturnsResponseAnnotationProcessor extends AbstractProcessor {

    @Override
    protected void process(TypeElement annotation, Element element, AnnotationMirror annotation_mirror) {
        if (!(element instanceof ExecutableElement executable)) {
            message(Diagnostic.Kind.ERROR, "@%s may only be applied to methods".formatted(ReturnsResponse.class.getSimpleName()), element, annotation_mirror);
            return;
        }

        if (!(types.isAssignable(executable.getReturnType(),
                types.getDeclaredType(elements.getTypeElement(Optional.class.getName()),
                        types.getWildcardType(elements.getTypeElement(String.class.getName()).asType(), null))))
                && !(types.isAssignable(executable.getReturnType(),
                elements.getTypeElement(String.class.getName()).asType()))) {
            message(Diagnostic.Kind.ERROR,
                    "method with @%s must have a return type assignable to java.lang.String or java.util.Optional<? extends java.lang.String>".formatted(ReturnsResponse.class.getSimpleName()), executable, annotation_mirror);
        }

        var returns_response = executable.getAnnotation(ReturnsResponse.class);
        if (returns_response.ephemeral() && returns_response.silent())
            message(Diagnostic.Kind.ERROR, "@%s can't have both ephemeral = true and silent = true".formatted(ReturnsResponse.class.getSimpleName()), executable, annotation_mirror);
    }
}

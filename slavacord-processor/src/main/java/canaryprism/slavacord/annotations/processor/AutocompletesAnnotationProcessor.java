package canaryprism.slavacord.annotations.processor;


import canaryprism.slavacord.annotations.Option;
import canaryprism.slavacord.autocomplete.annotations.Autocompletes;

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
        "canaryprism.slavacord.annotations.Autocompletes",
})
public final class AutocompletesAnnotationProcessor extends AbstractProcessor {
    @Override
    protected void process(TypeElement annotation, Element element, AnnotationMirror annotation_mirror) {
        if (!(element instanceof VariableElement parameter)) {
            message(Diagnostic.Kind.ERROR, "@%s may only be applied to method parameters".formatted(Autocompletes.class.getSimpleName()),
                    element, annotation_mirror);
            return;
        }
        // only thing we can really do here is check @Option is present since that's where @Autocompletes is actually processed
        if (parameter.getAnnotation(Option.class) == null)
            message(Diagnostic.Kind.ERROR, "@%s cannot be used without @%s"
                    .formatted(Autocompletes.class.getSimpleName(), Option.class.getSimpleName()), element, annotation_mirror);
    }
}

package canaryprism.slavacord.annotations.processor;


import canaryprism.slavacord.autocomplete.annotations.Autocompleter;
import canaryprism.slavacord.autocomplete.annotations.Autocompletes;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.util.stream.Collectors;

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

    }
}

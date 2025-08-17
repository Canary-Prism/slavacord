package canaryprism.slavacord.annotations.processor;

import canaryprism.discordbridge.api.DiscordBridge;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class AbstractProcessor extends javax.annotation.processing.AbstractProcessor {

    protected Elements elements;
    protected Types types;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        this.elements = processingEnv.getElementUtils();
        this.types = processingEnv.getTypeUtils();
    }

    protected <T> Stream<? extends T> bridge(Function<? super DiscordBridge, ? extends T> function) {

        class Holder {
            static final ServiceLoader<DiscordBridge> loader = ServiceLoader.load(DiscordBridge.class, DiscordBridge.class.getClassLoader());
        }
        return Holder.loader
                .stream()
                .mapMulti((e, downstream) -> {
                    try {
                        downstream.accept(function.apply(e.get()));
                    } catch (Throwable n) {
                        // nothing ig?
                    }
                });
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (var annotation : annotations) {
            for (var e : roundEnv.getElementsAnnotatedWith(annotation)) {
                var optional_annotation_mirror = e.getAnnotationMirrors()
                        .stream()
                        .filter((mirror) -> types.isSameType(mirror.getAnnotationType(), annotation.asType()))
                        .findAny();
                if (((Object) optional_annotation_mirror.orElse(null)) instanceof AnnotationMirror annotation_mirror)
                    try {
                        process(annotation, e, annotation_mirror);
                    } catch (Exception n) {
                        var writer = new StringWriter();
                        n.printStackTrace(new PrintWriter(writer));
                        message(Diagnostic.Kind.ERROR, writer.toString(), e);
                    }
            }
        }
        return true;
    }

    protected TypeMirror getTypeMirror(Type type) {
        if (type instanceof Class<?> clazz) {
            if (((Object) elements.getModuleElement(Optional.ofNullable(clazz.getModule().getName()).orElse(""))) instanceof ModuleElement module)
                return Optional.ofNullable(elements.getTypeElement(module, clazz.getName()))
                        .orElse(elements.getTypeElement(module, clazz.getCanonicalName()))
                        .asType();
            else
                return elements.getTypeElement(clazz.getName()).asType();
        } else if (type instanceof ParameterizedType parameterized) {
            DeclaredType containing = null;
            if (((Object) parameterized.getOwnerType()) instanceof Type owner) {
                containing = ((DeclaredType) getTypeMirror(owner));
            }
            return types.getDeclaredType(containing,
                    ((TypeElement) types.asElement(getTypeMirror(parameterized.getRawType()))),
                    Arrays.stream(parameterized.getActualTypeArguments())
                            .map(this::getTypeMirror)
                            .toArray(TypeMirror[]::new));
        } else if (type instanceof WildcardType wildcard) {
            return types.getWildcardType(
                    getFirst(wildcard.getUpperBounds()).map(this::getTypeMirror).orElse(null),
                    getFirst(wildcard.getLowerBounds()).map(this::getTypeMirror).orElse(null)
            );
        } else if (type instanceof GenericArrayType generic_array) {
            return types.getArrayType(getTypeMirror(generic_array.getGenericComponentType()));
        }
        return null;
    }

    private <T> Optional<T> getFirst(T[] array) {
        if (array.length < 1)
            return Optional.empty();
        else
            return Optional.ofNullable(array[0]);
    }

    protected abstract void process(TypeElement annotation, Element element, AnnotationMirror annotation_mirror);

    protected void message(Diagnostic.Kind kind, String message, Element element) {
        processingEnv.getMessager()
                .printMessage(kind, message, element);
    }
    protected void message(Diagnostic.Kind kind, String message, Element element, AnnotationMirror annotation_mirror) {
        processingEnv.getMessager()
                .printMessage(kind, message, element, annotation_mirror);
    }
    protected void message(Diagnostic.Kind kind, String message, Element element, AnnotationMirror annotation_mirror, AnnotationValue annotation_value) {
        processingEnv.getMessager()
                .printMessage(kind, message, element, annotation_mirror, annotation_value);
    }
}

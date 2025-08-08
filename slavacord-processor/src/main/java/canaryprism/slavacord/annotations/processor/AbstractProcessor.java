package canaryprism.slavacord.annotations.processor;

import canaryprism.discordbridge.api.DiscordBridge;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractProcessor extends javax.annotation.processing.AbstractProcessor {

    protected Elements elements;
    protected Types types;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        this.elements = processingEnv.getElementUtils();
        this.types = processingEnv.getTypeUtils();
    }

    protected <T> Set<? extends T> bridge(Function<? super DiscordBridge, ? extends T> function) {

        class Holder {
            static final ServiceLoader<DiscordBridge> loader = ServiceLoader.load(DiscordBridge.class, DiscordBridge.class.getClassLoader());
        }
        return Holder.loader
                .stream()
                .<T>mapMulti((e, downstream) -> {
                    try {
                        downstream.accept(function.apply(e.get()));
                    } catch (Throwable n) {
                        // nothing ig?
                    }
                })
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (var annotation : annotations) {
            for (var e : roundEnv.getElementsAnnotatedWith(annotation)) {
                var annotation_mirror = e.getAnnotationMirrors()
                        .stream()
                        .filter((mirror) -> mirror.getAnnotationType().equals(annotation.asType()))
                        .findAny()
                        .orElseThrow();
                try {
                    process(annotation, e, annotation_mirror);
                } catch (NoSuchElementException n) {
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

package canaryprism.slavacord.annotations.processor;

import canaryprism.discordbridge.api.data.interaction.slash.SlashCommandData;
import canaryprism.discordbridge.api.misc.DiscordLocale;
import canaryprism.slavacord.annotations.Trans;
import canaryprism.slavacord.annotations.Translations;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.util.*;

@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes({
        "canaryprism.slavacord.annotations.Trans",
        "canaryprism.slavacord.annotations.Translations",
})
public final class TranslationsAnnotationProcessor extends AbstractProcessor {

    private final Set<Element> processed = new HashSet<>();

    @Override
    protected void process(TypeElement annotation, Element element, AnnotationMirror h) {
        if (!processed.add(element)) {
            return;
        }

        var translations_value_element = ((ExecutableElement) elements.getTypeElement(Translations.class.getName())
                .getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("value"))
                .findAny()
                .orElseThrow());

        var translations = element.getAnnotationMirrors()
                .stream()
                .<AnnotationMirror>mapMulti((e, downstream) -> {
                    if (types.isSameType(e.getAnnotationType(), getTypeMirror(Trans.class)))
                        downstream.accept(e);
                    else if (types.isSameType(e.getAnnotationType(), getTypeMirror(Translations.class)))
                        //noinspection unchecked
                        ((List<? extends AnnotationValue>) e.getElementValues()
                                .get(translations_value_element)
                                .getValue())
                                .stream()
                                .map(AnnotationValue::getValue)
                                .map(AnnotationMirror.class::cast)
                                .forEach(downstream);
                })
                .toList();

        var locales = new HashMap<DiscordLocale, AnnotationMirror>();

        for (var mirror : translations) {
            var trans = parse(mirror);
            if (locales.containsKey(trans.locale)) {
                var other = locales.get(trans.locale);
                var msg = "duplicate locale for @%s '%s'"
                        .formatted(Trans.class.getSimpleName(), trans.locale);
                message(Diagnostic.Kind.ERROR, msg, element, mirror, mirror.getElementValues().get(locale_element));
                message(Diagnostic.Kind.ERROR, msg, element, other, other.getElementValues().get(locale_element));
            }
            locales.putIfAbsent(trans.locale, mirror);

            var data = new SlashCommandData("data", "data");

            if (((Object) trans.name.orElse(null)) instanceof String name)
                try {
                    data.setName(name);
                } catch (IllegalArgumentException e) {
                    message(Diagnostic.Kind.ERROR,
                            "invalid translation name '%s', names must be between 1 and %s characters long, and match the pattern /%s/ "
                                    .formatted(name, SlashCommandData.MAX_NAME_LENGTH, SlashCommandData.NAME_PATTERN),
                            element, mirror, mirror.getElementValues().get(name_element));
                }
            if (((Object) trans.description.orElse(null)) instanceof String description)
                try {
                    data.setDescription(description);
                } catch (IllegalArgumentException e) {
                    message(Diagnostic.Kind.ERROR,
                            "invalid command description '%s', command descriptions must be between 1 and %s characters long"
                                    .formatted(description, SlashCommandData.MAX_NAME_LENGTH),
                            element, mirror, mirror.getElementValues().get(name_element));
                }

            if (trans.name.isEmpty() && trans.description.isEmpty())
                message(Diagnostic.Kind.ERROR, "name and/or description must be present in @%s"
                                .formatted(Trans.class.getSimpleName()),
                        element, mirror);
        }
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        locale_element = ((ExecutableElement) elements.getTypeElement(Trans.class.getName())
                .getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("locale"))
                .findAny()
                .orElseThrow());
        name_element = ((ExecutableElement) elements.getTypeElement(Trans.class.getName())
                .getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("name"))
                .findAny()
                .orElseThrow());
        description_element = ((ExecutableElement) elements.getTypeElement(Trans.class.getName())
                .getEnclosedElements()
                .stream()
                .filter((e) -> e.getSimpleName().contentEquals("description"))
                .findAny()
                .orElseThrow());
    }

    private ExecutableElement locale_element, name_element, description_element;

    record TransData(DiscordLocale locale, Optional<String> name, Optional<String> description) {}

    private TransData parse(AnnotationMirror mirror) {
        var locale = DiscordLocale.valueOf(((VariableElement) mirror.getElementValues().get(locale_element).getValue()).getSimpleName().toString());
        var name = Optional.ofNullable(mirror.getElementValues().get(name_element))
                .map(AnnotationValue::getValue)
                .map(Object::toString);
        var description = Optional.ofNullable(mirror.getElementValues().get(description_element))
                .map(AnnotationValue::getValue)
                .map(Object::toString);

        return new TransData(locale, name, description);
    }
}

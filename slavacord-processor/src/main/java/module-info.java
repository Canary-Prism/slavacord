import canaryprism.slavacord.annotations.processor.*;

module canaryprism.slavacord.processor {
    uses canaryprism.discordbridge.api.DiscordBridge;
    requires java.compiler;
    requires canaryprism.slavacord;
    requires canaryprism.discordbridge.api;
    requires org.apache.commons.lang3;

    provides javax.annotation.processing.Processor with
            CreateAnnotationProcessor,
            CommandAnnotationProcessor,
            AsyncAnnotationProcessor,
            ReturnsResponseAnnotationProcessor,
            OptionAnnotationProcessor,
            AutocompletesAnnotationProcessor,
            AutocompleterAnnotationProcessor;
}
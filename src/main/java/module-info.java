module canaryprism.slavacord {
    requires transitive org.javacord.api;
    requires org.apache.logging.log4j;

    exports canaryprism.slavacord;
    exports canaryprism.slavacord.annotations;
    exports canaryprism.slavacord.exceptions;
}

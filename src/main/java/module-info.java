module canaryprism.slavacord {
    requires transitive org.javacord.api;
    requires org.apache.logging.log4j;
    requires annotations;

    exports canaryprism.slavacord;
    exports canaryprism.slavacord.annotations;
    exports canaryprism.slavacord.annotations.optionbounds;
    exports canaryprism.slavacord.exceptions;
    exports canaryprism.slavacord.autocomplete;
    exports canaryprism.slavacord.autocomplete.annotations;
    exports canaryprism.slavacord.autocomplete.filteroptions;

    opens canaryprism.slavacord.data;
    opens canaryprism.slavacord.data.optionbounds;
    opens canaryprism.slavacord.data.autocomplete;
    opens canaryprism.slavacord.data.autocomplete.filter;
}

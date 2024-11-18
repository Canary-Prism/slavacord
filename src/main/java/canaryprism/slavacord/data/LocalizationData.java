package canaryprism.slavacord.data;

import java.util.Map;

import org.javacord.api.interaction.DiscordLocale;

/**
 * combined name and description localizations for use by commands and options
 */
public record LocalizationData(Map<DiscordLocale, String> names, Map<DiscordLocale, String> descriptions) {
}

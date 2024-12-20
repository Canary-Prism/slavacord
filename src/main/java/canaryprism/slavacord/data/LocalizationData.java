package canaryprism.slavacord.data;

import canaryprism.discordbridge.api.misc.DiscordLocale;

import java.util.Map;

/**
 * combined name and description localizations for use by commands and options
 */
public record LocalizationData(Map<DiscordLocale, String> names, Map<DiscordLocale, String> descriptions) {
}

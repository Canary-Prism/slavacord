package canaryprism.slavacord.data;

import java.util.Map;

import org.javacord.api.interaction.DiscordLocale;

public record LocalizationData(Map<DiscordLocale, String> names, Map<DiscordLocale, String> descriptions) {
}

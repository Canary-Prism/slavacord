package canaryprism.slavacord.data;

import canaryprism.discordbridge.api.server.permission.PermissionType;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public record SlashCommandData(
    String name,
    String description,
    LocalizationData localizations,
    boolean enabled_in_DMs,
    boolean nsfw,
    Set<? extends PermissionType> required_permissions,
    /**
     * The server ID to register the command in. (0 for global)
     */
    long server_id,
    List<? extends SlashCommandOptionData<?>> options,
    Method method,
    Object instance,
    boolean requires_interaction
) implements Data {
    @Override
    public String toString() {
        return toString(0);
    }

    @SuppressWarnings("StringConcatenationInLoop")
    public String toString(int indent) {
        String indent_str = "    ".repeat(indent);

        String value = "";

        value += indent_str + "SlashCommandData [name = " + name + ", description = " + description
                + ", enabled_in_DMs = " + enabled_in_DMs + ", nsfw = " + nsfw + ", required_permissions = " + required_permissions + ", server_id = " + server_id + ", requires_interaction = "
                + requires_interaction + ", method = " + method + ", options = [";
        


        if (options != null && !options.isEmpty()) {
            for (SlashCommandOptionData<?> option : options) {
                value += "\n" + option.toString(indent + 1) + ",";
            }
            value += "\n" + indent_str + "]";
        } else {
            value += "]";
        }

        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SlashCommandData other) {

            return name.equals(other.name) && description.equals(other.description)
                    && enabled_in_DMs == other.enabled_in_DMs && nsfw == other.nsfw && server_id == other.server_id
                    && Objects.equals(required_permissions, other.required_permissions)
                    && (((options == null || options.isEmpty()) && (other.options() == null || other.options().isEmpty())) || Objects.equals(options, other.options));
        } else {
            return false;
        }
    }

    public canaryprism.discordbridge.api.data.interaction.slash.SlashCommandData toSlashCommandBuilder() {
        var builder = new canaryprism.discordbridge.api.data.interaction.slash.SlashCommandData(name, description);

        builder.setEnabledInDMs(enabled_in_DMs);
        builder.setNSFW(nsfw);

        builder.setNameLocalizations(localizations.names());
        builder.setDescriptionLocalizations(localizations.descriptions());

        if (required_permissions != null) {
            builder.setRequiredPermissions(required_permissions);
        }

        if (options != null) {
            builder.setOptions(options.stream()
                    .map(SlashCommandOptionData::toSlashCommandOptionBuilder)
                    .toList());
        }

        return builder;
    }
}

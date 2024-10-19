package canaryprism.slavacord.data;

import java.lang.reflect.Method;

import java.util.List;

import org.javacord.api.interaction.SlashCommandBuilder;

public record SlashCommandData(
    String name,
    String description,
    boolean enabled_in_DMs,
    /**
     * The server ID to register the command in. (0 for global)
     */
    long server_id,
    List<SlashCommandOptionData<?>> options,
    Method method,
    Object instance,
    boolean requires_interaction
) implements Data {
    @Override
    public String toString() {
        return toString(0);
    }

    public String toString(int indent) {
        String indent_str = "    ".repeat(indent);

        String value = "";

        value += indent_str + "SlashCommandData [name = " + name + ", description = " + description
                + ", enabled_in_DMs = " + enabled_in_DMs + ", server_id = " + server_id + ", requires_interaction = "
                + requires_interaction + ", method = " + method + ", options = [";
        


        if (options != null && options.size() > 0) {
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
        if (obj instanceof SlashCommandData) {
            SlashCommandData other = (SlashCommandData) obj;

            return name.equals(other.name) && description.equals(other.description)
                    && enabled_in_DMs == other.enabled_in_DMs && server_id == other.server_id
                    && (((options == null || options.isEmpty()) && (other.options() == null || other.options().isEmpty())) || options.equals(other.options));
        } else {
            return false;
        }
    }

    public SlashCommandBuilder toSlashCommandBuilder() {
        SlashCommandBuilder builder = new SlashCommandBuilder();

        builder.setName(name);
        builder.setDescription(description);

        if (options != null && options.size() > 0) {
            for (SlashCommandOptionData<?> option : options) {
                builder.addOption(option.toSlashCommandOptionBuilder().build());
            }
        }

        return builder;
    }
}

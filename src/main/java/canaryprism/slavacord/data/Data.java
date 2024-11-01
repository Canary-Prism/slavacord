package canaryprism.slavacord.data;

/**
 * various slash command related data used internally by the {@link canaryprism.slavacord.CommandHandler CommandHandler}
 */
public sealed interface Data permits SlashCommandData, SlashCommandOptionData, SlashCommandOptionChoiceData {
    
}

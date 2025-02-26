package canaryprism.slavacord.data.createtarget;

import canaryprism.discordbridge.api.interaction.ContextType;
import canaryprism.discordbridge.api.interaction.InstallationType;

import java.util.EnumSet;

public record CreateGlobalData(EnumSet<ContextType> contexts, EnumSet<InstallationType> installs) implements CreateData {
}

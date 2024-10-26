package canaryprism.slavacord.mock;

import java.time.Instant;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.Region;
import org.javacord.api.entity.VanityUrlCode;
import org.javacord.api.entity.auditlog.AuditLog;
import org.javacord.api.entity.auditlog.AuditLogActionType;
import org.javacord.api.entity.auditlog.AuditLogEntry;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.RegularServerChannel;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.ActiveThreads;
import org.javacord.api.entity.server.Ban;
import org.javacord.api.entity.server.BoostLevel;
import org.javacord.api.entity.server.DefaultMessageNotificationLevel;
import org.javacord.api.entity.server.ExplicitContentFilterLevel;
import org.javacord.api.entity.server.MultiFactorAuthenticationLevel;
import org.javacord.api.entity.server.NsfwLevel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.ServerFeature;
import org.javacord.api.entity.server.SystemChannelFlag;
import org.javacord.api.entity.server.VerificationLevel;
import org.javacord.api.entity.server.invite.RichInvite;
import org.javacord.api.entity.server.invite.WelcomeScreen;
import org.javacord.api.entity.sticker.Sticker;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.webhook.IncomingWebhook;
import org.javacord.api.entity.webhook.Webhook;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.server.ServerChannelChangeNameListener;
import org.javacord.api.listener.channel.server.ServerChannelChangeNsfwFlagListener;
import org.javacord.api.listener.channel.server.ServerChannelChangeOverwrittenPermissionsListener;
import org.javacord.api.listener.channel.server.ServerChannelChangePositionListener;
import org.javacord.api.listener.channel.server.ServerChannelCreateListener;
import org.javacord.api.listener.channel.server.ServerChannelDeleteListener;
import org.javacord.api.listener.channel.server.invite.ServerChannelInviteCreateListener;
import org.javacord.api.listener.channel.server.invite.ServerChannelInviteDeleteListener;
import org.javacord.api.listener.channel.server.text.ServerTextChannelChangeDefaultAutoArchiveDurationListener;
import org.javacord.api.listener.channel.server.text.ServerTextChannelChangeSlowmodeListener;
import org.javacord.api.listener.channel.server.text.ServerTextChannelChangeTopicListener;
import org.javacord.api.listener.channel.server.text.WebhooksUpdateListener;
import org.javacord.api.listener.channel.server.thread.ServerThreadChannelMembersUpdateListener;
import org.javacord.api.listener.channel.server.thread.ServerThreadChannelUpdateListener;
import org.javacord.api.listener.channel.server.thread.ServerThreadListSyncListener;
import org.javacord.api.listener.channel.server.voice.ServerStageVoiceChannelChangeTopicListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelChangeBitrateListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelChangeNsfwListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelChangeUserLimitListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberJoinListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberLeaveListener;
import org.javacord.api.listener.interaction.AutocompleteCreateListener;
import org.javacord.api.listener.interaction.ButtonClickListener;
import org.javacord.api.listener.interaction.InteractionCreateListener;
import org.javacord.api.listener.interaction.MessageComponentCreateListener;
import org.javacord.api.listener.interaction.MessageContextMenuCommandListener;
import org.javacord.api.listener.interaction.ModalSubmitListener;
import org.javacord.api.listener.interaction.SelectMenuChooseListener;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.javacord.api.listener.interaction.UserContextMenuCommandListener;
import org.javacord.api.listener.message.CachedMessagePinListener;
import org.javacord.api.listener.message.CachedMessageUnpinListener;
import org.javacord.api.listener.message.ChannelPinsUpdateListener;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.listener.message.MessageDeleteListener;
import org.javacord.api.listener.message.MessageEditListener;
import org.javacord.api.listener.message.MessageReplyListener;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import org.javacord.api.listener.message.reaction.ReactionRemoveAllListener;
import org.javacord.api.listener.message.reaction.ReactionRemoveListener;
import org.javacord.api.listener.server.ApplicationCommandPermissionsUpdateListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.server.ServerBecomesUnavailableListener;
import org.javacord.api.listener.server.ServerChangeAfkChannelListener;
import org.javacord.api.listener.server.ServerChangeAfkTimeoutListener;
import org.javacord.api.listener.server.ServerChangeBoostCountListener;
import org.javacord.api.listener.server.ServerChangeBoostLevelListener;
import org.javacord.api.listener.server.ServerChangeDefaultMessageNotificationLevelListener;
import org.javacord.api.listener.server.ServerChangeDescriptionListener;
import org.javacord.api.listener.server.ServerChangeDiscoverySplashListener;
import org.javacord.api.listener.server.ServerChangeExplicitContentFilterLevelListener;
import org.javacord.api.listener.server.ServerChangeIconListener;
import org.javacord.api.listener.server.ServerChangeModeratorsOnlyChannelListener;
import org.javacord.api.listener.server.ServerChangeMultiFactorAuthenticationLevelListener;
import org.javacord.api.listener.server.ServerChangeNameListener;
import org.javacord.api.listener.server.ServerChangeNsfwLevelListener;
import org.javacord.api.listener.server.ServerChangeOwnerListener;
import org.javacord.api.listener.server.ServerChangePreferredLocaleListener;
import org.javacord.api.listener.server.ServerChangeRegionListener;
import org.javacord.api.listener.server.ServerChangeRulesChannelListener;
import org.javacord.api.listener.server.ServerChangeServerFeatureListener;
import org.javacord.api.listener.server.ServerChangeSplashListener;
import org.javacord.api.listener.server.ServerChangeSystemChannelListener;
import org.javacord.api.listener.server.ServerChangeVanityUrlCodeListener;
import org.javacord.api.listener.server.ServerChangeVerificationLevelListener;
import org.javacord.api.listener.server.ServerLeaveListener;
import org.javacord.api.listener.server.VoiceServerUpdateListener;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiChangeNameListener;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiChangeWhitelistedRolesListener;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiCreateListener;
import org.javacord.api.listener.server.emoji.KnownCustomEmojiDeleteListener;
import org.javacord.api.listener.server.member.ServerMemberBanListener;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;
import org.javacord.api.listener.server.member.ServerMemberLeaveListener;
import org.javacord.api.listener.server.member.ServerMemberUnbanListener;
import org.javacord.api.listener.server.member.ServerMembersChunkListener;
import org.javacord.api.listener.server.role.RoleChangeColorListener;
import org.javacord.api.listener.server.role.RoleChangeHoistListener;
import org.javacord.api.listener.server.role.RoleChangeMentionableListener;
import org.javacord.api.listener.server.role.RoleChangeNameListener;
import org.javacord.api.listener.server.role.RoleChangePermissionsListener;
import org.javacord.api.listener.server.role.RoleChangePositionListener;
import org.javacord.api.listener.server.role.RoleCreateListener;
import org.javacord.api.listener.server.role.RoleDeleteListener;
import org.javacord.api.listener.server.role.UserRoleAddListener;
import org.javacord.api.listener.server.role.UserRoleRemoveListener;
import org.javacord.api.listener.server.sticker.StickerChangeDescriptionListener;
import org.javacord.api.listener.server.sticker.StickerChangeNameListener;
import org.javacord.api.listener.server.sticker.StickerChangeTagsListener;
import org.javacord.api.listener.server.sticker.StickerCreateListener;
import org.javacord.api.listener.server.sticker.StickerDeleteListener;
import org.javacord.api.listener.server.thread.ServerPrivateThreadJoinListener;
import org.javacord.api.listener.server.thread.ServerThreadChannelChangeArchiveTimestampListener;
import org.javacord.api.listener.server.thread.ServerThreadChannelChangeArchivedListener;
import org.javacord.api.listener.server.thread.ServerThreadChannelChangeAutoArchiveDurationListener;
import org.javacord.api.listener.server.thread.ServerThreadChannelChangeInvitableListener;
import org.javacord.api.listener.server.thread.ServerThreadChannelChangeLastMessageIdListener;
import org.javacord.api.listener.server.thread.ServerThreadChannelChangeLockedListener;
import org.javacord.api.listener.server.thread.ServerThreadChannelChangeMemberCountListener;
import org.javacord.api.listener.server.thread.ServerThreadChannelChangeMessageCountListener;
import org.javacord.api.listener.server.thread.ServerThreadChannelChangeRateLimitPerUserListener;
import org.javacord.api.listener.server.thread.ServerThreadChannelChangeTotalMessageSentListener;
import org.javacord.api.listener.user.UserChangeActivityListener;
import org.javacord.api.listener.user.UserChangeAvatarListener;
import org.javacord.api.listener.user.UserChangeDeafenedListener;
import org.javacord.api.listener.user.UserChangeDiscriminatorListener;
import org.javacord.api.listener.user.UserChangeMutedListener;
import org.javacord.api.listener.user.UserChangeNameListener;
import org.javacord.api.listener.user.UserChangeNicknameListener;
import org.javacord.api.listener.user.UserChangePendingListener;
import org.javacord.api.listener.user.UserChangeSelfDeafenedListener;
import org.javacord.api.listener.user.UserChangeSelfMutedListener;
import org.javacord.api.listener.user.UserChangeServerAvatarListener;
import org.javacord.api.listener.user.UserChangeStatusListener;
import org.javacord.api.listener.user.UserChangeTimeoutListener;
import org.javacord.api.listener.user.UserStartTypingListener;
import org.javacord.api.util.event.ListenerManager;

public class MockServer implements Server {

    @Override
    public DiscordApi getApi() {

        throw new UnsupportedOperationException("Unimplemented method 'getApi'");
    }

    @Override
    public long getId() {

        throw new UnsupportedOperationException("Unimplemented method 'getId'");
    }

    @Override
    public String getName() {

        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }

    @Override
    public CompletableFuture<Void> delete(String reason) {

        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public ListenerManager<InteractionCreateListener> addInteractionCreateListener(InteractionCreateListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addInteractionCreateListener'");
    }

    @Override
    public List<InteractionCreateListener> getInteractionCreateListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getInteractionCreateListeners'");
    }

    @Override
    public ListenerManager<SlashCommandCreateListener> addSlashCommandCreateListener(
            SlashCommandCreateListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addSlashCommandCreateListener'");
    }

    @Override
    public List<SlashCommandCreateListener> getSlashCommandCreateListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getSlashCommandCreateListeners'");
    }

    @Override
    public ListenerManager<AutocompleteCreateListener> addAutocompleteCreateListener(
            AutocompleteCreateListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addAutocompleteCreateListener'");
    }

    @Override
    public List<AutocompleteCreateListener> getAutocompleteCreateListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getAutocompleteCreateListeners'");
    }

    @Override
    public ListenerManager<ModalSubmitListener> addModalSubmitListener(ModalSubmitListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addModalSubmitListener'");
    }

    @Override
    public List<ModalSubmitListener> getModalSubmitListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getModalSubmitListeners'");
    }

    @Override
    public ListenerManager<MessageContextMenuCommandListener> addMessageContextMenuCommandListener(
            MessageContextMenuCommandListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addMessageContextMenuCommandListener'");
    }

    @Override
    public List<MessageContextMenuCommandListener> getMessageContextMenuCommandListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getMessageContextMenuCommandListeners'");
    }

    @Override
    public ListenerManager<MessageComponentCreateListener> addMessageComponentCreateListener(
            MessageComponentCreateListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addMessageComponentCreateListener'");
    }

    @Override
    public List<MessageComponentCreateListener> getMessageComponentCreateListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getMessageComponentCreateListeners'");
    }

    @Override
    public ListenerManager<UserContextMenuCommandListener> addUserContextMenuCommandListener(
            UserContextMenuCommandListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addUserContextMenuCommandListener'");
    }

    @Override
    public List<UserContextMenuCommandListener> getUserContextMenuCommandListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getUserContextMenuCommandListeners'");
    }

    @Override
    public ListenerManager<SelectMenuChooseListener> addSelectMenuChooseListener(SelectMenuChooseListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addSelectMenuChooseListener'");
    }

    @Override
    public List<SelectMenuChooseListener> getSelectMenuChooseListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getSelectMenuChooseListeners'");
    }

    @Override
    public ListenerManager<ButtonClickListener> addButtonClickListener(ButtonClickListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addButtonClickListener'");
    }

    @Override
    public List<ButtonClickListener> getButtonClickListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getButtonClickListeners'");
    }

    @Override
    public ListenerManager<ServerChangeIconListener> addServerChangeIconListener(ServerChangeIconListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeIconListener'");
    }

    @Override
    public List<ServerChangeIconListener> getServerChangeIconListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeIconListeners'");
    }

    @Override
    public ListenerManager<ServerChangeNameListener> addServerChangeNameListener(ServerChangeNameListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeNameListener'");
    }

    @Override
    public List<ServerChangeNameListener> getServerChangeNameListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeNameListeners'");
    }

    @Override
    public ListenerManager<ServerThreadChannelChangeLastMessageIdListener> addServerThreadChannelChangeLastMessageIdListener(
            ServerThreadChannelChangeLastMessageIdListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerThreadChannelChangeLastMessageIdListener'");
    }

    @Override
    public List<ServerThreadChannelChangeLastMessageIdListener> getServerThreadChannelChangeLastMessageIdListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerThreadChannelChangeLastMessageIdListeners'");
    }

    @Override
    public ListenerManager<ServerThreadChannelChangeArchivedListener> addServerThreadChannelChangeArchivedListener(
            ServerThreadChannelChangeArchivedListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerThreadChannelChangeArchivedListener'");
    }

    @Override
    public List<ServerThreadChannelChangeArchivedListener> getServerThreadChannelChangeArchivedListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerThreadChannelChangeArchivedListeners'");
    }

    @Override
    public ListenerManager<ServerThreadChannelChangeMemberCountListener> addServerThreadChannelChangeMemberCountListener(
            ServerThreadChannelChangeMemberCountListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerThreadChannelChangeMemberCountListener'");
    }

    @Override
    public List<ServerThreadChannelChangeMemberCountListener> getServerThreadChannelChangeMemberCountListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerThreadChannelChangeMemberCountListeners'");
    }

    @Override
    public ListenerManager<ServerPrivateThreadJoinListener> addServerPrivateThreadJoinListener(
            ServerPrivateThreadJoinListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerPrivateThreadJoinListener'");
    }

    @Override
    public List<ServerPrivateThreadJoinListener> getServerPrivateThreadJoinListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerPrivateThreadJoinListeners'");
    }

    @Override
    public ListenerManager<ServerThreadChannelChangeInvitableListener> addServerThreadChannelChangeInvitableListener(
            ServerThreadChannelChangeInvitableListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerThreadChannelChangeInvitableListener'");
    }

    @Override
    public List<ServerThreadChannelChangeInvitableListener> getServerThreadChannelChangeInvitableListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerThreadChannelChangeInvitableListeners'");
    }

    @Override
    public ListenerManager<ServerThreadChannelChangeAutoArchiveDurationListener> addServerThreadChannelChangeAutoArchiveDurationListener(
            ServerThreadChannelChangeAutoArchiveDurationListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerThreadChannelChangeAutoArchiveDurationListener'");
    }

    @Override
    public List<ServerThreadChannelChangeAutoArchiveDurationListener> getServerThreadChannelChangeAutoArchiveDurationListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerThreadChannelChangeAutoArchiveDurationListeners'");
    }

    @Override
    public ListenerManager<ServerThreadChannelChangeRateLimitPerUserListener> addServerThreadChannelChangeRateLimitPerUserListener(
            ServerThreadChannelChangeRateLimitPerUserListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerThreadChannelChangeRateLimitPerUserListener'");
    }

    @Override
    public List<ServerThreadChannelChangeRateLimitPerUserListener> getServerThreadChannelChangeRateLimitPerUserListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerThreadChannelChangeRateLimitPerUserListeners'");
    }

    @Override
    public ListenerManager<ServerThreadChannelChangeLockedListener> addServerThreadChannelChangeLockedListener(
            ServerThreadChannelChangeLockedListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerThreadChannelChangeLockedListener'");
    }

    @Override
    public List<ServerThreadChannelChangeLockedListener> getServerThreadChannelChangeLockedListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerThreadChannelChangeLockedListeners'");
    }

    @Override
    public ListenerManager<ServerThreadChannelChangeArchiveTimestampListener> addServerThreadChannelChangeArchiveTimestampListener(
            ServerThreadChannelChangeArchiveTimestampListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerThreadChannelChangeArchiveTimestampListener'");
    }

    @Override
    public List<ServerThreadChannelChangeArchiveTimestampListener> getServerThreadChannelChangeArchiveTimestampListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerThreadChannelChangeArchiveTimestampListeners'");
    }

    @Override
    public ListenerManager<ServerThreadChannelChangeTotalMessageSentListener> addServerThreadChannelChangeTotalMessageSentListener(
            ServerThreadChannelChangeTotalMessageSentListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerThreadChannelChangeTotalMessageSentListener'");
    }

    @Override
    public List<ServerThreadChannelChangeTotalMessageSentListener> getServerThreadChannelChangeTotalMessageSentListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerThreadChannelChangeTotalMessageSentListeners'");
    }

    @Override
    public ListenerManager<ServerThreadChannelChangeMessageCountListener> addServerThreadChannelChangeMessageCountListener(
            ServerThreadChannelChangeMessageCountListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerThreadChannelChangeMessageCountListener'");
    }

    @Override
    public List<ServerThreadChannelChangeMessageCountListener> getServerThreadChannelChangeMessageCountListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerThreadChannelChangeMessageCountListeners'");
    }

    @Override
    public ListenerManager<ServerChangeAfkTimeoutListener> addServerChangeAfkTimeoutListener(
            ServerChangeAfkTimeoutListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeAfkTimeoutListener'");
    }

    @Override
    public List<ServerChangeAfkTimeoutListener> getServerChangeAfkTimeoutListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeAfkTimeoutListeners'");
    }

    @Override
    public ListenerManager<StickerChangeTagsListener> addStickerChangeTagsListener(StickerChangeTagsListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addStickerChangeTagsListener'");
    }

    @Override
    public List<StickerChangeTagsListener> getStickerChangeTagsListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getStickerChangeTagsListeners'");
    }

    @Override
    public ListenerManager<StickerChangeDescriptionListener> addStickerChangeDescriptionListener(
            StickerChangeDescriptionListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addStickerChangeDescriptionListener'");
    }

    @Override
    public List<StickerChangeDescriptionListener> getStickerChangeDescriptionListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getStickerChangeDescriptionListeners'");
    }

    @Override
    public ListenerManager<StickerCreateListener> addStickerCreateListener(StickerCreateListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addStickerCreateListener'");
    }

    @Override
    public List<StickerCreateListener> getStickerCreateListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getStickerCreateListeners'");
    }

    @Override
    public ListenerManager<StickerChangeNameListener> addStickerChangeNameListener(StickerChangeNameListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addStickerChangeNameListener'");
    }

    @Override
    public List<StickerChangeNameListener> getStickerChangeNameListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getStickerChangeNameListeners'");
    }

    @Override
    public ListenerManager<StickerDeleteListener> addStickerDeleteListener(StickerDeleteListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addStickerDeleteListener'");
    }

    @Override
    public List<StickerDeleteListener> getStickerDeleteListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getStickerDeleteListeners'");
    }

    @Override
    public ListenerManager<ServerChangeSplashListener> addServerChangeSplashListener(
            ServerChangeSplashListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeSplashListener'");
    }

    @Override
    public List<ServerChangeSplashListener> getServerChangeSplashListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeSplashListeners'");
    }

    @Override
    public ListenerManager<ServerChangeAfkChannelListener> addServerChangeAfkChannelListener(
            ServerChangeAfkChannelListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeAfkChannelListener'");
    }

    @Override
    public List<ServerChangeAfkChannelListener> getServerChangeAfkChannelListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeAfkChannelListeners'");
    }

    @Override
    public ListenerManager<ServerChangeVanityUrlCodeListener> addServerChangeVanityUrlCodeListener(
            ServerChangeVanityUrlCodeListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeVanityUrlCodeListener'");
    }

    @Override
    public List<ServerChangeVanityUrlCodeListener> getServerChangeVanityUrlCodeListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeVanityUrlCodeListeners'");
    }

    @Override
    public ListenerManager<ServerChangeDiscoverySplashListener> addServerChangeDiscoverySplashListener(
            ServerChangeDiscoverySplashListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeDiscoverySplashListener'");
    }

    @Override
    public List<ServerChangeDiscoverySplashListener> getServerChangeDiscoverySplashListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeDiscoverySplashListeners'");
    }

    @Override
    public ListenerManager<ApplicationCommandPermissionsUpdateListener> addApplicationCommandPermissionsUpdateListener(
            ApplicationCommandPermissionsUpdateListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addApplicationCommandPermissionsUpdateListener'");
    }

    @Override
    public List<ApplicationCommandPermissionsUpdateListener> getApplicationCommandPermissionsUpdateListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getApplicationCommandPermissionsUpdateListeners'");
    }

    @Override
    public ListenerManager<ServerBecomesUnavailableListener> addServerBecomesUnavailableListener(
            ServerBecomesUnavailableListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerBecomesUnavailableListener'");
    }

    @Override
    public List<ServerBecomesUnavailableListener> getServerBecomesUnavailableListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerBecomesUnavailableListeners'");
    }

    @Override
    public ListenerManager<VoiceServerUpdateListener> addVoiceServerUpdateListener(VoiceServerUpdateListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addVoiceServerUpdateListener'");
    }

    @Override
    public List<VoiceServerUpdateListener> getVoiceServerUpdateListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getVoiceServerUpdateListeners'");
    }

    @Override
    public ListenerManager<ServerChangeDescriptionListener> addServerChangeDescriptionListener(
            ServerChangeDescriptionListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeDescriptionListener'");
    }

    @Override
    public List<ServerChangeDescriptionListener> getServerChangeDescriptionListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeDescriptionListeners'");
    }

    @Override
    public ListenerManager<ServerChangeVerificationLevelListener> addServerChangeVerificationLevelListener(
            ServerChangeVerificationLevelListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeVerificationLevelListener'");
    }

    @Override
    public List<ServerChangeVerificationLevelListener> getServerChangeVerificationLevelListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeVerificationLevelListeners'");
    }

    @Override
    public ListenerManager<ServerLeaveListener> addServerLeaveListener(ServerLeaveListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerLeaveListener'");
    }

    @Override
    public List<ServerLeaveListener> getServerLeaveListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerLeaveListeners'");
    }

    @Override
    public ListenerManager<ServerChangeBoostCountListener> addServerChangeBoostCountListener(
            ServerChangeBoostCountListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeBoostCountListener'");
    }

    @Override
    public List<ServerChangeBoostCountListener> getServerChangeBoostCountListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeBoostCountListeners'");
    }

    @Override
    public ListenerManager<ServerChangeDefaultMessageNotificationLevelListener> addServerChangeDefaultMessageNotificationLevelListener(
            ServerChangeDefaultMessageNotificationLevelListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeDefaultMessageNotificationLevelListener'");
    }

    @Override
    public List<ServerChangeDefaultMessageNotificationLevelListener> getServerChangeDefaultMessageNotificationLevelListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeDefaultMessageNotificationLevelListeners'");
    }

    @Override
    public ListenerManager<ServerChangeRegionListener> addServerChangeRegionListener(
            ServerChangeRegionListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeRegionListener'");
    }

    @Override
    public List<ServerChangeRegionListener> getServerChangeRegionListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeRegionListeners'");
    }

    @Override
    public ListenerManager<ServerMemberJoinListener> addServerMemberJoinListener(ServerMemberJoinListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerMemberJoinListener'");
    }

    @Override
    public List<ServerMemberJoinListener> getServerMemberJoinListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerMemberJoinListeners'");
    }

    @Override
    public ListenerManager<ServerMemberLeaveListener> addServerMemberLeaveListener(ServerMemberLeaveListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerMemberLeaveListener'");
    }

    @Override
    public List<ServerMemberLeaveListener> getServerMemberLeaveListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerMemberLeaveListeners'");
    }

    @Override
    public ListenerManager<ServerMemberBanListener> addServerMemberBanListener(ServerMemberBanListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerMemberBanListener'");
    }

    @Override
    public List<ServerMemberBanListener> getServerMemberBanListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerMemberBanListeners'");
    }

    @Override
    public ListenerManager<ServerMembersChunkListener> addServerMembersChunkListener(
            ServerMembersChunkListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerMembersChunkListener'");
    }

    @Override
    public List<ServerMembersChunkListener> getServerMembersChunkListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerMembersChunkListeners'");
    }

    @Override
    public ListenerManager<ServerMemberUnbanListener> addServerMemberUnbanListener(ServerMemberUnbanListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerMemberUnbanListener'");
    }

    @Override
    public List<ServerMemberUnbanListener> getServerMemberUnbanListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerMemberUnbanListeners'");
    }

    @Override
    public ListenerManager<KnownCustomEmojiChangeNameListener> addKnownCustomEmojiChangeNameListener(
            KnownCustomEmojiChangeNameListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addKnownCustomEmojiChangeNameListener'");
    }

    @Override
    public List<KnownCustomEmojiChangeNameListener> getKnownCustomEmojiChangeNameListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getKnownCustomEmojiChangeNameListeners'");
    }

    @Override
    public ListenerManager<KnownCustomEmojiDeleteListener> addKnownCustomEmojiDeleteListener(
            KnownCustomEmojiDeleteListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addKnownCustomEmojiDeleteListener'");
    }

    @Override
    public List<KnownCustomEmojiDeleteListener> getKnownCustomEmojiDeleteListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getKnownCustomEmojiDeleteListeners'");
    }

    @Override
    public ListenerManager<KnownCustomEmojiChangeWhitelistedRolesListener> addKnownCustomEmojiChangeWhitelistedRolesListener(
            KnownCustomEmojiChangeWhitelistedRolesListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addKnownCustomEmojiChangeWhitelistedRolesListener'");
    }

    @Override
    public List<KnownCustomEmojiChangeWhitelistedRolesListener> getKnownCustomEmojiChangeWhitelistedRolesListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getKnownCustomEmojiChangeWhitelistedRolesListeners'");
    }

    @Override
    public ListenerManager<KnownCustomEmojiCreateListener> addKnownCustomEmojiCreateListener(
            KnownCustomEmojiCreateListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addKnownCustomEmojiCreateListener'");
    }

    @Override
    public List<KnownCustomEmojiCreateListener> getKnownCustomEmojiCreateListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getKnownCustomEmojiCreateListeners'");
    }

    @Override
    public ListenerManager<ServerChangeSystemChannelListener> addServerChangeSystemChannelListener(
            ServerChangeSystemChannelListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeSystemChannelListener'");
    }

    @Override
    public List<ServerChangeSystemChannelListener> getServerChangeSystemChannelListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeSystemChannelListeners'");
    }

    @Override
    public ListenerManager<ServerChangePreferredLocaleListener> addServerChangePreferredLocaleListener(
            ServerChangePreferredLocaleListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangePreferredLocaleListener'");
    }

    @Override
    public List<ServerChangePreferredLocaleListener> getServerChangePreferredLocaleListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangePreferredLocaleListeners'");
    }

    @Override
    public ListenerManager<ServerChangeBoostLevelListener> addServerChangeBoostLevelListener(
            ServerChangeBoostLevelListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeBoostLevelListener'");
    }

    @Override
    public List<ServerChangeBoostLevelListener> getServerChangeBoostLevelListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeBoostLevelListeners'");
    }

    @Override
    public ListenerManager<ServerChangeRulesChannelListener> addServerChangeRulesChannelListener(
            ServerChangeRulesChannelListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeRulesChannelListener'");
    }

    @Override
    public List<ServerChangeRulesChannelListener> getServerChangeRulesChannelListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeRulesChannelListeners'");
    }

    @Override
    public ListenerManager<ServerChangeServerFeatureListener> addServerChangeServerFeatureListener(
            ServerChangeServerFeatureListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeServerFeatureListener'");
    }

    @Override
    public List<ServerChangeServerFeatureListener> getServerChangeServerFeatureListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeServerFeatureListeners'");
    }

    @Override
    public ListenerManager<ServerChangeOwnerListener> addServerChangeOwnerListener(ServerChangeOwnerListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeOwnerListener'");
    }

    @Override
    public List<ServerChangeOwnerListener> getServerChangeOwnerListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeOwnerListeners'");
    }

    @Override
    public ListenerManager<ServerChangeMultiFactorAuthenticationLevelListener> addServerChangeMultiFactorAuthenticationLevelListener(
            ServerChangeMultiFactorAuthenticationLevelListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeMultiFactorAuthenticationLevelListener'");
    }

    @Override
    public List<ServerChangeMultiFactorAuthenticationLevelListener> getServerChangeMultiFactorAuthenticationLevelListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeMultiFactorAuthenticationLevelListeners'");
    }

    @Override
    public ListenerManager<ServerChangeExplicitContentFilterLevelListener> addServerChangeExplicitContentFilterLevelListener(
            ServerChangeExplicitContentFilterLevelListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeExplicitContentFilterLevelListener'");
    }

    @Override
    public List<ServerChangeExplicitContentFilterLevelListener> getServerChangeExplicitContentFilterLevelListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeExplicitContentFilterLevelListeners'");
    }

    @Override
    public ListenerManager<RoleChangePositionListener> addRoleChangePositionListener(
            RoleChangePositionListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addRoleChangePositionListener'");
    }

    @Override
    public List<RoleChangePositionListener> getRoleChangePositionListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getRoleChangePositionListeners'");
    }

    @Override
    public ListenerManager<RoleChangeMentionableListener> addRoleChangeMentionableListener(
            RoleChangeMentionableListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addRoleChangeMentionableListener'");
    }

    @Override
    public List<RoleChangeMentionableListener> getRoleChangeMentionableListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getRoleChangeMentionableListeners'");
    }

    @Override
    public ListenerManager<RoleChangeColorListener> addRoleChangeColorListener(RoleChangeColorListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addRoleChangeColorListener'");
    }

    @Override
    public List<RoleChangeColorListener> getRoleChangeColorListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getRoleChangeColorListeners'");
    }

    @Override
    public ListenerManager<RoleChangeNameListener> addRoleChangeNameListener(RoleChangeNameListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addRoleChangeNameListener'");
    }

    @Override
    public List<RoleChangeNameListener> getRoleChangeNameListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getRoleChangeNameListeners'");
    }

    @Override
    public ListenerManager<RoleChangeHoistListener> addRoleChangeHoistListener(RoleChangeHoistListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addRoleChangeHoistListener'");
    }

    @Override
    public List<RoleChangeHoistListener> getRoleChangeHoistListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getRoleChangeHoistListeners'");
    }

    @Override
    public ListenerManager<RoleCreateListener> addRoleCreateListener(RoleCreateListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addRoleCreateListener'");
    }

    @Override
    public List<RoleCreateListener> getRoleCreateListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getRoleCreateListeners'");
    }

    @Override
    public ListenerManager<RoleChangePermissionsListener> addRoleChangePermissionsListener(
            RoleChangePermissionsListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addRoleChangePermissionsListener'");
    }

    @Override
    public List<RoleChangePermissionsListener> getRoleChangePermissionsListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getRoleChangePermissionsListeners'");
    }

    @Override
    public ListenerManager<UserRoleRemoveListener> addUserRoleRemoveListener(UserRoleRemoveListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addUserRoleRemoveListener'");
    }

    @Override
    public List<UserRoleRemoveListener> getUserRoleRemoveListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getUserRoleRemoveListeners'");
    }

    @Override
    public ListenerManager<UserRoleAddListener> addUserRoleAddListener(UserRoleAddListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addUserRoleAddListener'");
    }

    @Override
    public List<UserRoleAddListener> getUserRoleAddListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getUserRoleAddListeners'");
    }

    @Override
    public ListenerManager<RoleDeleteListener> addRoleDeleteListener(RoleDeleteListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addRoleDeleteListener'");
    }

    @Override
    public List<RoleDeleteListener> getRoleDeleteListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getRoleDeleteListeners'");
    }

    @Override
    public ListenerManager<ServerChangeModeratorsOnlyChannelListener> addServerChangeModeratorsOnlyChannelListener(
            ServerChangeModeratorsOnlyChannelListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeModeratorsOnlyChannelListener'");
    }

    @Override
    public List<ServerChangeModeratorsOnlyChannelListener> getServerChangeModeratorsOnlyChannelListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeModeratorsOnlyChannelListeners'");
    }

    @Override
    public ListenerManager<ServerChangeNsfwLevelListener> addServerChangeNsfwLevelListener(
            ServerChangeNsfwLevelListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChangeNsfwLevelListener'");
    }

    @Override
    public List<ServerChangeNsfwLevelListener> getServerChangeNsfwLevelListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChangeNsfwLevelListeners'");
    }

    @Override
    public ListenerManager<ServerChannelChangePositionListener> addServerChannelChangePositionListener(
            ServerChannelChangePositionListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChannelChangePositionListener'");
    }

    @Override
    public List<ServerChannelChangePositionListener> getServerChannelChangePositionListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChannelChangePositionListeners'");
    }

    @Override
    public ListenerManager<ServerThreadListSyncListener> addServerThreadListSyncListener(
            ServerThreadListSyncListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerThreadListSyncListener'");
    }

    @Override
    public List<ServerThreadListSyncListener> getServerThreadListSyncListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerThreadListSyncListeners'");
    }

    @Override
    public ListenerManager<ServerThreadChannelUpdateListener> addServerThreadChannelUpdateListener(
            ServerThreadChannelUpdateListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerThreadChannelUpdateListener'");
    }

    @Override
    public List<ServerThreadChannelUpdateListener> getServerThreadChannelUpdateListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerThreadChannelUpdateListeners'");
    }

    @Override
    public ListenerManager<ServerThreadChannelMembersUpdateListener> addServerThreadChannelMembersUpdateListener(
            ServerThreadChannelMembersUpdateListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerThreadChannelMembersUpdateListener'");
    }

    @Override
    public List<ServerThreadChannelMembersUpdateListener> getServerThreadChannelMembersUpdateListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerThreadChannelMembersUpdateListeners'");
    }

    @Override
    public ListenerManager<WebhooksUpdateListener> addWebhooksUpdateListener(WebhooksUpdateListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addWebhooksUpdateListener'");
    }

    @Override
    public List<WebhooksUpdateListener> getWebhooksUpdateListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getWebhooksUpdateListeners'");
    }

    @Override
    public ListenerManager<ServerTextChannelChangeDefaultAutoArchiveDurationListener> addServerTextChannelChangeDefaultAutoArchiveDurationListener(
            ServerTextChannelChangeDefaultAutoArchiveDurationListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerTextChannelChangeDefaultAutoArchiveDurationListener'");
    }

    @Override
    public List<ServerTextChannelChangeDefaultAutoArchiveDurationListener> getServerTextChannelChangeDefaultAutoArchiveDurationListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerTextChannelChangeDefaultAutoArchiveDurationListeners'");
    }

    @Override
    public ListenerManager<ServerTextChannelChangeSlowmodeListener> addServerTextChannelChangeSlowmodeListener(
            ServerTextChannelChangeSlowmodeListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerTextChannelChangeSlowmodeListener'");
    }

    @Override
    public List<ServerTextChannelChangeSlowmodeListener> getServerTextChannelChangeSlowmodeListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerTextChannelChangeSlowmodeListeners'");
    }

    @Override
    public ListenerManager<ServerTextChannelChangeTopicListener> addServerTextChannelChangeTopicListener(
            ServerTextChannelChangeTopicListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerTextChannelChangeTopicListener'");
    }

    @Override
    public List<ServerTextChannelChangeTopicListener> getServerTextChannelChangeTopicListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerTextChannelChangeTopicListeners'");
    }

    @Override
    public ListenerManager<ServerChannelChangeOverwrittenPermissionsListener> addServerChannelChangeOverwrittenPermissionsListener(
            ServerChannelChangeOverwrittenPermissionsListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChannelChangeOverwrittenPermissionsListener'");
    }

    @Override
    public List<ServerChannelChangeOverwrittenPermissionsListener> getServerChannelChangeOverwrittenPermissionsListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChannelChangeOverwrittenPermissionsListeners'");
    }

    @Override
    public ListenerManager<ServerChannelInviteDeleteListener> addServerChannelInviteDeleteListener(
            ServerChannelInviteDeleteListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChannelInviteDeleteListener'");
    }

    @Override
    public List<ServerChannelInviteDeleteListener> getServerChannelInviteDeleteListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChannelInviteDeleteListeners'");
    }

    @Override
    public ListenerManager<ServerChannelInviteCreateListener> addServerChannelInviteCreateListener(
            ServerChannelInviteCreateListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChannelInviteCreateListener'");
    }

    @Override
    public List<ServerChannelInviteCreateListener> getServerChannelInviteCreateListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChannelInviteCreateListeners'");
    }

    @Override
    public ListenerManager<ServerChannelChangeNsfwFlagListener> addServerChannelChangeNsfwFlagListener(
            ServerChannelChangeNsfwFlagListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChannelChangeNsfwFlagListener'");
    }

    @Override
    public List<ServerChannelChangeNsfwFlagListener> getServerChannelChangeNsfwFlagListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChannelChangeNsfwFlagListeners'");
    }

    @Override
    public ListenerManager<ServerChannelDeleteListener> addServerChannelDeleteListener(
            ServerChannelDeleteListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChannelDeleteListener'");
    }

    @Override
    public List<ServerChannelDeleteListener> getServerChannelDeleteListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChannelDeleteListeners'");
    }

    @Override
    public ListenerManager<ServerChannelCreateListener> addServerChannelCreateListener(
            ServerChannelCreateListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChannelCreateListener'");
    }

    @Override
    public List<ServerChannelCreateListener> getServerChannelCreateListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChannelCreateListeners'");
    }

    @Override
    public ListenerManager<ServerStageVoiceChannelChangeTopicListener> addServerStageVoiceChannelChangeTopicListener(
            ServerStageVoiceChannelChangeTopicListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerStageVoiceChannelChangeTopicListener'");
    }

    @Override
    public List<ServerStageVoiceChannelChangeTopicListener> getServerStageVoiceChannelChangeTopicListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerStageVoiceChannelChangeTopicListeners'");
    }

    @Override
    public ListenerManager<ServerVoiceChannelChangeBitrateListener> addServerVoiceChannelChangeBitrateListener(
            ServerVoiceChannelChangeBitrateListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerVoiceChannelChangeBitrateListener'");
    }

    @Override
    public List<ServerVoiceChannelChangeBitrateListener> getServerVoiceChannelChangeBitrateListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerVoiceChannelChangeBitrateListeners'");
    }

    @Override
    public ListenerManager<ServerVoiceChannelChangeUserLimitListener> addServerVoiceChannelChangeUserLimitListener(
            ServerVoiceChannelChangeUserLimitListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerVoiceChannelChangeUserLimitListener'");
    }

    @Override
    public List<ServerVoiceChannelChangeUserLimitListener> getServerVoiceChannelChangeUserLimitListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerVoiceChannelChangeUserLimitListeners'");
    }

    @Override
    public ListenerManager<ServerVoiceChannelMemberLeaveListener> addServerVoiceChannelMemberLeaveListener(
            ServerVoiceChannelMemberLeaveListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerVoiceChannelMemberLeaveListener'");
    }

    @Override
    public List<ServerVoiceChannelMemberLeaveListener> getServerVoiceChannelMemberLeaveListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerVoiceChannelMemberLeaveListeners'");
    }

    @Override
    public ListenerManager<ServerVoiceChannelChangeNsfwListener> addServerVoiceChannelChangeNsfwListener(
            ServerVoiceChannelChangeNsfwListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerVoiceChannelChangeNsfwListener'");
    }

    @Override
    public List<ServerVoiceChannelChangeNsfwListener> getServerVoiceChannelChangeNsfwListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerVoiceChannelChangeNsfwListeners'");
    }

    @Override
    public ListenerManager<ServerVoiceChannelMemberJoinListener> addServerVoiceChannelMemberJoinListener(
            ServerVoiceChannelMemberJoinListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerVoiceChannelMemberJoinListener'");
    }

    @Override
    public List<ServerVoiceChannelMemberJoinListener> getServerVoiceChannelMemberJoinListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerVoiceChannelMemberJoinListeners'");
    }

    @Override
    public ListenerManager<ServerChannelChangeNameListener> addServerChannelChangeNameListener(
            ServerChannelChangeNameListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerChannelChangeNameListener'");
    }

    @Override
    public List<ServerChannelChangeNameListener> getServerChannelChangeNameListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChannelChangeNameListeners'");
    }

    @Override
    public ListenerManager<UserChangeDeafenedListener> addUserChangeDeafenedListener(
            UserChangeDeafenedListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addUserChangeDeafenedListener'");
    }

    @Override
    public List<UserChangeDeafenedListener> getUserChangeDeafenedListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getUserChangeDeafenedListeners'");
    }

    @Override
    public ListenerManager<UserChangeNicknameListener> addUserChangeNicknameListener(
            UserChangeNicknameListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addUserChangeNicknameListener'");
    }

    @Override
    public List<UserChangeNicknameListener> getUserChangeNicknameListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getUserChangeNicknameListeners'");
    }

    @Override
    public ListenerManager<UserChangePendingListener> addUserChangePendingListener(UserChangePendingListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addUserChangePendingListener'");
    }

    @Override
    public List<UserChangePendingListener> getUserChangePendingListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getUserChangePendingListeners'");
    }

    @Override
    public ListenerManager<UserStartTypingListener> addUserStartTypingListener(UserStartTypingListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addUserStartTypingListener'");
    }

    @Override
    public List<UserStartTypingListener> getUserStartTypingListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getUserStartTypingListeners'");
    }

    @Override
    public ListenerManager<UserChangeDiscriminatorListener> addUserChangeDiscriminatorListener(
            UserChangeDiscriminatorListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addUserChangeDiscriminatorListener'");
    }

    @Override
    public List<UserChangeDiscriminatorListener> getUserChangeDiscriminatorListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getUserChangeDiscriminatorListeners'");
    }

    @Override
    public ListenerManager<UserChangeStatusListener> addUserChangeStatusListener(UserChangeStatusListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addUserChangeStatusListener'");
    }

    @Override
    public List<UserChangeStatusListener> getUserChangeStatusListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getUserChangeStatusListeners'");
    }

    @Override
    public ListenerManager<UserChangeServerAvatarListener> addUserChangeServerAvatarListener(
            UserChangeServerAvatarListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addUserChangeServerAvatarListener'");
    }

    @Override
    public List<UserChangeServerAvatarListener> getUserChangeServerAvatarListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getUserChangeServerAvatarListeners'");
    }

    @Override
    public ListenerManager<UserChangeSelfMutedListener> addUserChangeSelfMutedListener(
            UserChangeSelfMutedListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addUserChangeSelfMutedListener'");
    }

    @Override
    public List<UserChangeSelfMutedListener> getUserChangeSelfMutedListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getUserChangeSelfMutedListeners'");
    }

    @Override
    public ListenerManager<UserChangeNameListener> addUserChangeNameListener(UserChangeNameListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addUserChangeNameListener'");
    }

    @Override
    public List<UserChangeNameListener> getUserChangeNameListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getUserChangeNameListeners'");
    }

    @Override
    public ListenerManager<UserChangeTimeoutListener> addUserChangeTimeoutListener(UserChangeTimeoutListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addUserChangeTimeoutListener'");
    }

    @Override
    public List<UserChangeTimeoutListener> getUserChangeTimeoutListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getUserChangeTimeoutListeners'");
    }

    @Override
    public ListenerManager<UserChangeAvatarListener> addUserChangeAvatarListener(UserChangeAvatarListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addUserChangeAvatarListener'");
    }

    @Override
    public List<UserChangeAvatarListener> getUserChangeAvatarListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getUserChangeAvatarListeners'");
    }

    @Override
    public ListenerManager<UserChangeSelfDeafenedListener> addUserChangeSelfDeafenedListener(
            UserChangeSelfDeafenedListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addUserChangeSelfDeafenedListener'");
    }

    @Override
    public List<UserChangeSelfDeafenedListener> getUserChangeSelfDeafenedListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getUserChangeSelfDeafenedListeners'");
    }

    @Override
    public ListenerManager<UserChangeMutedListener> addUserChangeMutedListener(UserChangeMutedListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addUserChangeMutedListener'");
    }

    @Override
    public List<UserChangeMutedListener> getUserChangeMutedListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getUserChangeMutedListeners'");
    }

    @Override
    public ListenerManager<UserChangeActivityListener> addUserChangeActivityListener(
            UserChangeActivityListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addUserChangeActivityListener'");
    }

    @Override
    public List<UserChangeActivityListener> getUserChangeActivityListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getUserChangeActivityListeners'");
    }

    @Override
    public ListenerManager<MessageEditListener> addMessageEditListener(MessageEditListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addMessageEditListener'");
    }

    @Override
    public List<MessageEditListener> getMessageEditListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getMessageEditListeners'");
    }

    @Override
    public ListenerManager<ChannelPinsUpdateListener> addChannelPinsUpdateListener(ChannelPinsUpdateListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addChannelPinsUpdateListener'");
    }

    @Override
    public List<ChannelPinsUpdateListener> getChannelPinsUpdateListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getChannelPinsUpdateListeners'");
    }

    @Override
    public ListenerManager<ReactionRemoveListener> addReactionRemoveListener(ReactionRemoveListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addReactionRemoveListener'");
    }

    @Override
    public List<ReactionRemoveListener> getReactionRemoveListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getReactionRemoveListeners'");
    }

    @Override
    public ListenerManager<ReactionAddListener> addReactionAddListener(ReactionAddListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addReactionAddListener'");
    }

    @Override
    public List<ReactionAddListener> getReactionAddListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getReactionAddListeners'");
    }

    @Override
    public ListenerManager<ReactionRemoveAllListener> addReactionRemoveAllListener(ReactionRemoveAllListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addReactionRemoveAllListener'");
    }

    @Override
    public List<ReactionRemoveAllListener> getReactionRemoveAllListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getReactionRemoveAllListeners'");
    }

    @Override
    public ListenerManager<MessageCreateListener> addMessageCreateListener(MessageCreateListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addMessageCreateListener'");
    }

    @Override
    public List<MessageCreateListener> getMessageCreateListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getMessageCreateListeners'");
    }

    @Override
    public ListenerManager<CachedMessageUnpinListener> addCachedMessageUnpinListener(
            CachedMessageUnpinListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addCachedMessageUnpinListener'");
    }

    @Override
    public List<CachedMessageUnpinListener> getCachedMessageUnpinListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getCachedMessageUnpinListeners'");
    }

    @Override
    public ListenerManager<CachedMessagePinListener> addCachedMessagePinListener(CachedMessagePinListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addCachedMessagePinListener'");
    }

    @Override
    public List<CachedMessagePinListener> getCachedMessagePinListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getCachedMessagePinListeners'");
    }

    @Override
    public ListenerManager<MessageReplyListener> addMessageReplyListener(MessageReplyListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addMessageReplyListener'");
    }

    @Override
    public List<MessageReplyListener> getMessageReplyListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getMessageReplyListeners'");
    }

    @Override
    public ListenerManager<MessageDeleteListener> addMessageDeleteListener(MessageDeleteListener listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addMessageDeleteListener'");
    }

    @Override
    public List<MessageDeleteListener> getMessageDeleteListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getMessageDeleteListeners'");
    }

    @Override
    public <T extends ServerAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>> addServerAttachableListener(
            T listener) {

        throw new UnsupportedOperationException("Unimplemented method 'addServerAttachableListener'");
    }

    @Override
    public <T extends ServerAttachableListener & ObjectAttachableListener> void removeServerAttachableListener(
            T listener) {

        throw new UnsupportedOperationException("Unimplemented method 'removeServerAttachableListener'");
    }

    @Override
    public <T extends ServerAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>> getServerAttachableListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerAttachableListeners'");
    }

    @Override
    public <T extends ServerAttachableListener & ObjectAttachableListener> void removeListener(Class<T> listenerClass,
            T listener) {

        throw new UnsupportedOperationException("Unimplemented method 'removeListener'");
    }

    @Override
    public Optional<AudioConnection> getAudioConnection() {

        throw new UnsupportedOperationException("Unimplemented method 'getAudioConnection'");
    }

    @Override
    public Set<ServerFeature> getFeatures() {

        throw new UnsupportedOperationException("Unimplemented method 'getFeatures'");
    }

    @Override
    public BoostLevel getBoostLevel() {

        throw new UnsupportedOperationException("Unimplemented method 'getBoostLevel'");
    }

    @Override
    public int getBoostCount() {

        throw new UnsupportedOperationException("Unimplemented method 'getBoostCount'");
    }

    @Override
    public Optional<ServerTextChannel> getRulesChannel() {

        throw new UnsupportedOperationException("Unimplemented method 'getRulesChannel'");
    }

    @Override
    public Optional<String> getDescription() {

        throw new UnsupportedOperationException("Unimplemented method 'getDescription'");
    }

    @Override
    public NsfwLevel getNsfwLevel() {

        throw new UnsupportedOperationException("Unimplemented method 'getNsfwLevel'");
    }

    @Override
    public Optional<ServerTextChannel> getModeratorsOnlyChannel() {

        throw new UnsupportedOperationException("Unimplemented method 'getModeratorsOnlyChannel'");
    }

    @Override
    public Optional<VanityUrlCode> getVanityUrlCode() {

        throw new UnsupportedOperationException("Unimplemented method 'getVanityUrlCode'");
    }

    @Override
    public Optional<Icon> getDiscoverySplash() {

        throw new UnsupportedOperationException("Unimplemented method 'getDiscoverySplash'");
    }

    @Override
    public Locale getPreferredLocale() {

        throw new UnsupportedOperationException("Unimplemented method 'getPreferredLocale'");
    }

    @Override
    public Region getRegion() {

        throw new UnsupportedOperationException("Unimplemented method 'getRegion'");
    }

    @Override
    public Optional<String> getNickname(User user) {

        throw new UnsupportedOperationException("Unimplemented method 'getNickname'");
    }

    @Override
    public Optional<Instant> getServerBoostingSinceTimestamp(User user) {

        throw new UnsupportedOperationException("Unimplemented method 'getServerBoostingSinceTimestamp'");
    }

    @Override
    public Optional<Instant> getTimeout(User user) {

        throw new UnsupportedOperationException("Unimplemented method 'getTimeout'");
    }

    @Override
    public Optional<String> getUserServerAvatarHash(User user) {

        throw new UnsupportedOperationException("Unimplemented method 'getUserServerAvatarHash'");
    }

    @Override
    public Optional<Icon> getUserServerAvatar(User user) {

        throw new UnsupportedOperationException("Unimplemented method 'getUserServerAvatar'");
    }

    @Override
    public Optional<Icon> getUserServerAvatar(User user, int size) {

        throw new UnsupportedOperationException("Unimplemented method 'getUserServerAvatar'");
    }

    @Override
    public boolean isPending(long userId) {

        throw new UnsupportedOperationException("Unimplemented method 'isPending'");
    }

    @Override
    public boolean isSelfMuted(long userId) {

        throw new UnsupportedOperationException("Unimplemented method 'isSelfMuted'");
    }

    @Override
    public boolean isSelfDeafened(long userId) {

        throw new UnsupportedOperationException("Unimplemented method 'isSelfDeafened'");
    }

    @Override
    public boolean isMuted(long userId) {

        throw new UnsupportedOperationException("Unimplemented method 'isMuted'");
    }

    @Override
    public boolean isDeafened(long userId) {

        throw new UnsupportedOperationException("Unimplemented method 'isDeafened'");
    }

    @Override
    public Optional<Instant> getJoinedAtTimestamp(User user) {

        throw new UnsupportedOperationException("Unimplemented method 'getJoinedAtTimestamp'");
    }

    @Override
    public boolean isLarge() {

        throw new UnsupportedOperationException("Unimplemented method 'isLarge'");
    }

    @Override
    public int getMemberCount() {

        throw new UnsupportedOperationException("Unimplemented method 'getMemberCount'");
    }

    @Override
    public Optional<User> getOwner() {

        throw new UnsupportedOperationException("Unimplemented method 'getOwner'");
    }

    @Override
    public long getOwnerId() {

        throw new UnsupportedOperationException("Unimplemented method 'getOwnerId'");
    }

    @Override
    public Optional<Long> getApplicationId() {

        throw new UnsupportedOperationException("Unimplemented method 'getApplicationId'");
    }

    @Override
    public VerificationLevel getVerificationLevel() {

        throw new UnsupportedOperationException("Unimplemented method 'getVerificationLevel'");
    }

    @Override
    public ExplicitContentFilterLevel getExplicitContentFilterLevel() {

        throw new UnsupportedOperationException("Unimplemented method 'getExplicitContentFilterLevel'");
    }

    @Override
    public DefaultMessageNotificationLevel getDefaultMessageNotificationLevel() {

        throw new UnsupportedOperationException("Unimplemented method 'getDefaultMessageNotificationLevel'");
    }

    @Override
    public MultiFactorAuthenticationLevel getMultiFactorAuthenticationLevel() {

        throw new UnsupportedOperationException("Unimplemented method 'getMultiFactorAuthenticationLevel'");
    }

    @Override
    public Optional<Icon> getIcon() {

        throw new UnsupportedOperationException("Unimplemented method 'getIcon'");
    }

    @Override
    public Optional<Icon> getSplash() {

        throw new UnsupportedOperationException("Unimplemented method 'getSplash'");
    }

    @Override
    public Optional<ServerTextChannel> getSystemChannel() {

        throw new UnsupportedOperationException("Unimplemented method 'getSystemChannel'");
    }

    @Override
    public Optional<ServerVoiceChannel> getAfkChannel() {

        throw new UnsupportedOperationException("Unimplemented method 'getAfkChannel'");
    }

    @Override
    public int getAfkTimeoutInSeconds() {

        throw new UnsupportedOperationException("Unimplemented method 'getAfkTimeoutInSeconds'");
    }

    @Override
    public CompletableFuture<Integer> getPruneCount(int days) {

        throw new UnsupportedOperationException("Unimplemented method 'getPruneCount'");
    }

    @Override
    public CompletableFuture<Integer> pruneMembers(int days, String reason) {

        throw new UnsupportedOperationException("Unimplemented method 'pruneMembers'");
    }

    @Override
    public CompletableFuture<Set<RichInvite>> getInvites() {

        throw new UnsupportedOperationException("Unimplemented method 'getInvites'");
    }

    @Override
    public boolean hasAllMembersInCache() {

        throw new UnsupportedOperationException("Unimplemented method 'hasAllMembersInCache'");
    }

    @Override
    public void requestMembersChunks() {

        throw new UnsupportedOperationException("Unimplemented method 'requestMembersChunks'");
    }

    @Override
    public Set<User> getMembers() {

        throw new UnsupportedOperationException("Unimplemented method 'getMembers'");
    }

    @Override
    public Optional<User> getMemberById(long id) {

        throw new UnsupportedOperationException("Unimplemented method 'getMemberById'");
    }

    @Override
    public boolean isMember(User user) {

        throw new UnsupportedOperationException("Unimplemented method 'isMember'");
    }

    @Override
    public List<Role> getRoles() {

        throw new UnsupportedOperationException("Unimplemented method 'getRoles'");
    }

    @Override
    public List<Role> getRoles(User user) {

        throw new UnsupportedOperationException("Unimplemented method 'getRoles'");
    }

    @Override
    public Optional<Role> getRoleById(long id) {

        throw new UnsupportedOperationException("Unimplemented method 'getRoleById'");
    }

    @Override
    public CompletableFuture<Void> leave() {

        throw new UnsupportedOperationException("Unimplemented method 'leave'");
    }

    @Override
    public CompletableFuture<Void> addRoleToUser(User user, Role role, String reason) {

        throw new UnsupportedOperationException("Unimplemented method 'addRoleToUser'");
    }

    @Override
    public CompletableFuture<Void> removeRoleFromUser(User user, Role role, String reason) {

        throw new UnsupportedOperationException("Unimplemented method 'removeRoleFromUser'");
    }

    @Override
    public CompletableFuture<Void> reorderRoles(List<Role> roles, String reason) {

        throw new UnsupportedOperationException("Unimplemented method 'reorderRoles'");
    }

    @Override
    public void selfMute() {

        throw new UnsupportedOperationException("Unimplemented method 'selfMute'");
    }

    @Override
    public void selfUnmute() {

        throw new UnsupportedOperationException("Unimplemented method 'selfUnmute'");
    }

    @Override
    public void selfDeafen() {

        throw new UnsupportedOperationException("Unimplemented method 'selfDeafen'");
    }

    @Override
    public void selfUndeafen() {

        throw new UnsupportedOperationException("Unimplemented method 'selfUndeafen'");
    }

    @Override
    public CompletableFuture<User> requestMember(long userId) {

        throw new UnsupportedOperationException("Unimplemented method 'requestMember'");
    }

    @Override
    public CompletableFuture<Void> kickUser(User user, String reason) {

        throw new UnsupportedOperationException("Unimplemented method 'kickUser'");
    }

    @Override
    public CompletableFuture<Void> banUser(String userId, long deleteMessageDuration, TimeUnit unit, String reason) {

        throw new UnsupportedOperationException("Unimplemented method 'banUser'");
    }

    @Override
    public CompletableFuture<Void> unbanUser(long userId, String reason) {

        throw new UnsupportedOperationException("Unimplemented method 'unbanUser'");
    }

    @Override
    public CompletableFuture<Ban> requestBan(long userId) {

        throw new UnsupportedOperationException("Unimplemented method 'requestBan'");
    }

    @Override
    public CompletableFuture<Set<Ban>> getBans() {

        throw new UnsupportedOperationException("Unimplemented method 'getBans'");
    }

    @Override
    public CompletableFuture<Set<Ban>> getBans(Integer limit, Long after) {

        throw new UnsupportedOperationException("Unimplemented method 'getBans'");
    }

    @Override
    public CompletableFuture<List<Webhook>> getWebhooks() {

        throw new UnsupportedOperationException("Unimplemented method 'getWebhooks'");
    }

    @Override
    public CompletableFuture<List<Webhook>> getAllIncomingWebhooks() {

        throw new UnsupportedOperationException("Unimplemented method 'getAllIncomingWebhooks'");
    }

    @Override
    public CompletableFuture<List<IncomingWebhook>> getIncomingWebhooks() {

        throw new UnsupportedOperationException("Unimplemented method 'getIncomingWebhooks'");
    }

    @Override
    public CompletableFuture<AuditLog> getAuditLog(int limit) {

        throw new UnsupportedOperationException("Unimplemented method 'getAuditLog'");
    }

    @Override
    public CompletableFuture<AuditLog> getAuditLog(int limit, AuditLogActionType type) {

        throw new UnsupportedOperationException("Unimplemented method 'getAuditLog'");
    }

    @Override
    public CompletableFuture<AuditLog> getAuditLogBefore(int limit, AuditLogEntry before) {

        throw new UnsupportedOperationException("Unimplemented method 'getAuditLogBefore'");
    }

    @Override
    public CompletableFuture<AuditLog> getAuditLogBefore(int limit, AuditLogEntry before, AuditLogActionType type) {

        throw new UnsupportedOperationException("Unimplemented method 'getAuditLogBefore'");
    }

    @Override
    public Set<KnownCustomEmoji> getCustomEmojis() {

        throw new UnsupportedOperationException("Unimplemented method 'getCustomEmojis'");
    }

    @Override
    public CompletableFuture<Set<SlashCommand>> getSlashCommands() {

        throw new UnsupportedOperationException("Unimplemented method 'getSlashCommands'");
    }

    @Override
    public CompletableFuture<SlashCommand> getSlashCommandById(long commandId) {

        throw new UnsupportedOperationException("Unimplemented method 'getSlashCommandById'");
    }

    @Override
    public List<ServerChannel> getChannels() {

        throw new UnsupportedOperationException("Unimplemented method 'getChannels'");
    }

    @Override
    public Set<ServerChannel> getUnorderedChannels() {

        throw new UnsupportedOperationException("Unimplemented method 'getUnorderedChannels'");
    }

    @Override
    public List<RegularServerChannel> getRegularChannels() {

        throw new UnsupportedOperationException("Unimplemented method 'getRegularChannels'");
    }

    @Override
    public List<ChannelCategory> getChannelCategories() {

        throw new UnsupportedOperationException("Unimplemented method 'getChannelCategories'");
    }

    @Override
    public List<ServerTextChannel> getTextChannels() {

        throw new UnsupportedOperationException("Unimplemented method 'getTextChannels'");
    }

    @Override
    public List<ServerForumChannel> getForumChannels() {

        throw new UnsupportedOperationException("Unimplemented method 'getForumChannels'");
    }

    @Override
    public List<ServerVoiceChannel> getVoiceChannels() {

        throw new UnsupportedOperationException("Unimplemented method 'getVoiceChannels'");
    }

    @Override
    public List<ServerThreadChannel> getThreadChannels() {

        throw new UnsupportedOperationException("Unimplemented method 'getThreadChannels'");
    }

    @Override
    public Optional<ServerChannel> getChannelById(long id) {

        throw new UnsupportedOperationException("Unimplemented method 'getChannelById'");
    }

    @Override
    public Optional<RegularServerChannel> getRegularChannelById(long id) {

        throw new UnsupportedOperationException("Unimplemented method 'getRegularChannelById'");
    }

    @Override
    public CompletableFuture<Void> joinServerThreadChannel(long channelId) {

        throw new UnsupportedOperationException("Unimplemented method 'joinServerThreadChannel'");
    }

    @Override
    public CompletableFuture<Void> leaveServerThreadChannel(long channelId) {

        throw new UnsupportedOperationException("Unimplemented method 'leaveServerThreadChannel'");
    }

    @Override
    public CompletableFuture<ActiveThreads> getActiveThreads() {

        throw new UnsupportedOperationException("Unimplemented method 'getActiveThreads'");
    }

    @Override
    public Set<Sticker> getStickers() {

        throw new UnsupportedOperationException("Unimplemented method 'getStickers'");
    }

    @Override
    public CompletableFuture<Set<Sticker>> requestStickers() {

        throw new UnsupportedOperationException("Unimplemented method 'requestStickers'");
    }

    @Override
    public CompletableFuture<Sticker> requestStickerById(long id) {

        throw new UnsupportedOperationException("Unimplemented method 'requestStickerById'");
    }

    @Override
    public boolean isWidgetEnabled() {

        throw new UnsupportedOperationException("Unimplemented method 'isWidgetEnabled'");
    }

    @Override
    public Optional<Long> getWidgetChannelId() {

        throw new UnsupportedOperationException("Unimplemented method 'getWidgetChannelId'");
    }

    @Override
    public Optional<Integer> getMaxPresences() {

        throw new UnsupportedOperationException("Unimplemented method 'getMaxPresences'");
    }

    @Override
    public Optional<Integer> getMaxMembers() {

        throw new UnsupportedOperationException("Unimplemented method 'getMaxMembers'");
    }

    @Override
    public Optional<Integer> getMaxVideoChannelUsers() {

        throw new UnsupportedOperationException("Unimplemented method 'getMaxVideoChannelUsers'");
    }

    @Override
    public Optional<WelcomeScreen> getWelcomeScreen() {

        throw new UnsupportedOperationException("Unimplemented method 'getWelcomeScreen'");
    }

    @Override
    public boolean isPremiumProgressBarEnabled() {

        throw new UnsupportedOperationException("Unimplemented method 'isPremiumProgressBarEnabled'");
    }

    @Override
    public EnumSet<SystemChannelFlag> getSystemChannelFlags() {

        throw new UnsupportedOperationException("Unimplemented method 'getSystemChannelFlags'");
    }
    
}

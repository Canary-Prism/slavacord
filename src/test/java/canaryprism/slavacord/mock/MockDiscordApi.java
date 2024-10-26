package canaryprism.slavacord.mock;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.ApplicationInfo;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.channel.RegularServerChannel;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.entity.channel.ServerStageVoiceChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.channel.VoiceChannel;
import org.javacord.api.entity.emoji.CustomEmoji;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageSet;
import org.javacord.api.entity.message.UncachedMessageUtil;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.invite.Invite;
import org.javacord.api.entity.sticker.Sticker;
import org.javacord.api.entity.sticker.StickerPack;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.user.UserStatus;
import org.javacord.api.entity.webhook.IncomingWebhook;
import org.javacord.api.entity.webhook.Webhook;
import org.javacord.api.interaction.ApplicationCommand;
import org.javacord.api.interaction.ApplicationCommandBuilder;
import org.javacord.api.interaction.MessageContextMenu;
import org.javacord.api.interaction.ServerApplicationCommandPermissions;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.UserContextMenu;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.util.concurrent.ThreadPool;
import org.javacord.api.util.event.ListenerManager;
import org.javacord.api.util.ratelimit.Ratelimiter;

public class MockDiscordApi implements DiscordApi {

    @Override
    public <T extends GloballyAttachableListener> ListenerManager<T> addListener(Class<T> listenerClass, T listener) {
        return null;
    }

    @Override
    public Collection<ListenerManager<? extends GloballyAttachableListener>> addListener(
            GloballyAttachableListener listener) {

        return Set.of();
    }

    @Override
    public void removeListener(GloballyAttachableListener listener) {
    }

    @Override
    public <T extends GloballyAttachableListener> void removeListener(Class<T> listenerClass, T listener) {

        throw new UnsupportedOperationException("Unimplemented method 'removeListener'");
    }

    @Override
    public <T extends GloballyAttachableListener> Map<T, List<Class<T>>> getListeners() {

        throw new UnsupportedOperationException("Unimplemented method 'getListeners'");
    }

    @Override
    public <T extends GloballyAttachableListener> List<T> getListeners(Class<T> listenerClass) {

        throw new UnsupportedOperationException("Unimplemented method 'getListeners'");
    }

    @Override
    public String getToken() {

        throw new UnsupportedOperationException("Unimplemented method 'getToken'");
    }

    @Override
    public String getPrefixedToken() {

        throw new UnsupportedOperationException("Unimplemented method 'getPrefixedToken'");
    }

    @Override
    public Set<Intent> getIntents() {

        throw new UnsupportedOperationException("Unimplemented method 'getIntents'");
    }

    @Override
    public ThreadPool getThreadPool() {

        throw new UnsupportedOperationException("Unimplemented method 'getThreadPool'");
    }

    @Override
    public void setEventsDispatchable(boolean dispatchEvents) {

        throw new UnsupportedOperationException("Unimplemented method 'setEventsDispatchable'");
    }

    @Override
    public boolean canDispatchEvents() {

        throw new UnsupportedOperationException("Unimplemented method 'canDispatchEvents'");
    }

    @Override
    public CompletableFuture<Set<ApplicationCommand>> getGlobalApplicationCommands() {

        throw new UnsupportedOperationException("Unimplemented method 'getGlobalApplicationCommands'");
    }

    @Override
    public CompletableFuture<ApplicationCommand> getGlobalApplicationCommandById(long applicationCommandId) {

        throw new UnsupportedOperationException("Unimplemented method 'getGlobalApplicationCommandById'");
    }

    @Override
    public CompletableFuture<Set<ApplicationCommand>> getServerApplicationCommands(Server server) {

        throw new UnsupportedOperationException("Unimplemented method 'getServerApplicationCommands'");
    }

    @Override
    public CompletableFuture<ApplicationCommand> getServerApplicationCommandById(Server server,
            long applicationCommandId) {

        throw new UnsupportedOperationException("Unimplemented method 'getServerApplicationCommandById'");
    }

    @Override
    public CompletableFuture<Set<SlashCommand>> getGlobalSlashCommands() {
        return CompletableFuture.completedFuture(Set.of());
    }

    @Override
    public CompletableFuture<SlashCommand> getGlobalSlashCommandById(long commandId) {

        throw new UnsupportedOperationException("Unimplemented method 'getGlobalSlashCommandById'");
    }

    @Override
    public CompletableFuture<Set<SlashCommand>> getServerSlashCommands(Server server) {

        return CompletableFuture.completedFuture(Set.of());
    }

    @Override
    public CompletableFuture<SlashCommand> getServerSlashCommandById(Server server, long commandId) {

        throw new UnsupportedOperationException("Unimplemented method 'getServerSlashCommandById'");
    }

    @Override
    public CompletableFuture<Set<UserContextMenu>> getGlobalUserContextMenus() {

        throw new UnsupportedOperationException("Unimplemented method 'getGlobalUserContextMenus'");
    }

    @Override
    public CompletableFuture<UserContextMenu> getGlobalUserContextMenuById(long commandId) {

        throw new UnsupportedOperationException("Unimplemented method 'getGlobalUserContextMenuById'");
    }

    @Override
    public CompletableFuture<Set<UserContextMenu>> getServerUserContextMenus(Server server) {

        throw new UnsupportedOperationException("Unimplemented method 'getServerUserContextMenus'");
    }

    @Override
    public CompletableFuture<UserContextMenu> getServerUserContextMenuById(Server server, long commandId) {

        throw new UnsupportedOperationException("Unimplemented method 'getServerUserContextMenuById'");
    }

    @Override
    public CompletableFuture<Set<MessageContextMenu>> getGlobalMessageContextMenus() {

        throw new UnsupportedOperationException("Unimplemented method 'getGlobalMessageContextMenus'");
    }

    @Override
    public CompletableFuture<MessageContextMenu> getGlobalMessageContextMenuById(long commandId) {

        throw new UnsupportedOperationException("Unimplemented method 'getGlobalMessageContextMenuById'");
    }

    @Override
    public CompletableFuture<Set<MessageContextMenu>> getServerMessageContextMenus(Server server) {

        throw new UnsupportedOperationException("Unimplemented method 'getServerMessageContextMenus'");
    }

    @Override
    public CompletableFuture<MessageContextMenu> getServerMessageContextMenuById(Server server, long commandId) {

        throw new UnsupportedOperationException("Unimplemented method 'getServerMessageContextMenuById'");
    }

    @Override
    public CompletableFuture<Set<ServerApplicationCommandPermissions>> getServerApplicationCommandPermissions(
            Server server) {

        throw new UnsupportedOperationException("Unimplemented method 'getServerApplicationCommandPermissions'");
    }

    @Override
    public CompletableFuture<ServerApplicationCommandPermissions> getServerApplicationCommandPermissionsById(
            Server server, long commandId) {

        throw new UnsupportedOperationException("Unimplemented method 'getServerApplicationCommandPermissionsById'");
    }

    @Override
    public CompletableFuture<Set<ApplicationCommand>> bulkOverwriteGlobalApplicationCommands(
            Set<? extends ApplicationCommandBuilder<?, ?, ?>> applicationCommandBuilderList) {
        return CompletableFuture.completedFuture(Set.of());
    }

    @Override
    public CompletableFuture<Set<ApplicationCommand>> bulkOverwriteServerApplicationCommands(long server,
            Set<? extends ApplicationCommandBuilder<?, ?, ?>> applicationCommandBuilderList) {
        return CompletableFuture.completedFuture(Set.of());
    }

    @Override
    public UncachedMessageUtil getUncachedMessageUtil() {

        throw new UnsupportedOperationException("Unimplemented method 'getUncachedMessageUtil'");
    }

    @Override
    public Optional<Ratelimiter> getGlobalRatelimiter() {

        throw new UnsupportedOperationException("Unimplemented method 'getGlobalRatelimiter'");
    }

    @Override
    public Ratelimiter getGatewayIdentifyRatelimiter() {

        throw new UnsupportedOperationException("Unimplemented method 'getGatewayIdentifyRatelimiter'");
    }

    @Override
    public Duration getLatestGatewayLatency() {

        throw new UnsupportedOperationException("Unimplemented method 'getLatestGatewayLatency'");
    }

    @Override
    public CompletableFuture<Duration> measureRestLatency() {

        throw new UnsupportedOperationException("Unimplemented method 'measureRestLatency'");
    }

    @Override
    public void setMessageCacheSize(int capacity, int storageTimeInSeconds) {

        throw new UnsupportedOperationException("Unimplemented method 'setMessageCacheSize'");
    }

    @Override
    public int getDefaultMessageCacheCapacity() {

        throw new UnsupportedOperationException("Unimplemented method 'getDefaultMessageCacheCapacity'");
    }

    @Override
    public int getDefaultMessageCacheStorageTimeInSeconds() {

        throw new UnsupportedOperationException("Unimplemented method 'getDefaultMessageCacheStorageTimeInSeconds'");
    }

    @Override
    public void setAutomaticMessageCacheCleanupEnabled(boolean automaticMessageCacheCleanupEnabled) {

        throw new UnsupportedOperationException("Unimplemented method 'setAutomaticMessageCacheCleanupEnabled'");
    }

    @Override
    public boolean isDefaultAutomaticMessageCacheCleanupEnabled() {

        throw new UnsupportedOperationException("Unimplemented method 'isDefaultAutomaticMessageCacheCleanupEnabled'");
    }

    @Override
    public int getCurrentShard() {

        throw new UnsupportedOperationException("Unimplemented method 'getCurrentShard'");
    }

    @Override
    public int getTotalShards() {

        throw new UnsupportedOperationException("Unimplemented method 'getTotalShards'");
    }

    @Override
    public boolean isWaitingForServersOnStartup() {

        throw new UnsupportedOperationException("Unimplemented method 'isWaitingForServersOnStartup'");
    }

    @Override
    public boolean isWaitingForUsersOnStartup() {

        throw new UnsupportedOperationException("Unimplemented method 'isWaitingForUsersOnStartup'");
    }

    @Override
    public void updateStatus(UserStatus status) {

        throw new UnsupportedOperationException("Unimplemented method 'updateStatus'");
    }

    @Override
    public UserStatus getStatus() {

        throw new UnsupportedOperationException("Unimplemented method 'getStatus'");
    }

    @Override
    public void updateActivity(String name) {

        throw new UnsupportedOperationException("Unimplemented method 'updateActivity'");
    }

    @Override
    public void updateActivity(ActivityType type, String name) {

        throw new UnsupportedOperationException("Unimplemented method 'updateActivity'");
    }

    @Override
    public void updateActivity(String name, String streamingUrl) {

        throw new UnsupportedOperationException("Unimplemented method 'updateActivity'");
    }

    @Override
    public void unsetActivity() {

        throw new UnsupportedOperationException("Unimplemented method 'unsetActivity'");
    }

    @Override
    public Optional<Activity> getActivity() {

        throw new UnsupportedOperationException("Unimplemented method 'getActivity'");
    }

    @Override
    public User getYourself() {

        throw new UnsupportedOperationException("Unimplemented method 'getYourself'");
    }

    @Override
    public CompletableFuture<Void> disconnect() {

        throw new UnsupportedOperationException("Unimplemented method 'disconnect'");
    }

    @Override
    public void setReconnectDelay(Function<Integer, Integer> reconnectDelayProvider) {

        throw new UnsupportedOperationException("Unimplemented method 'setReconnectDelay'");
    }

    @Override
    public int getReconnectDelay(int attempt) {

        throw new UnsupportedOperationException("Unimplemented method 'getReconnectDelay'");
    }

    @Override
    public ApplicationInfo getCachedApplicationInfo() {

        throw new UnsupportedOperationException("Unimplemented method 'getCachedApplicationInfo'");
    }

    @Override
    public CompletableFuture<ApplicationInfo> requestApplicationInfo() {

        throw new UnsupportedOperationException("Unimplemented method 'requestApplicationInfo'");
    }

    @Override
    public CompletableFuture<Webhook> getWebhookById(long id) {

        throw new UnsupportedOperationException("Unimplemented method 'getWebhookById'");
    }

    @Override
    public CompletableFuture<IncomingWebhook> getIncomingWebhookByIdAndToken(String id, String token) {

        throw new UnsupportedOperationException("Unimplemented method 'getIncomingWebhookByIdAndToken'");
    }

    @Override
    public Set<Long> getUnavailableServers() {

        throw new UnsupportedOperationException("Unimplemented method 'getUnavailableServers'");
    }

    @Override
    public CompletableFuture<Invite> getInviteByCode(String code) {

        throw new UnsupportedOperationException("Unimplemented method 'getInviteByCode'");
    }

    @Override
    public CompletableFuture<Invite> getInviteWithMemberCountsByCode(String code) {

        throw new UnsupportedOperationException("Unimplemented method 'getInviteWithMemberCountsByCode'");
    }

    @Override
    public boolean isUserCacheEnabled() {

        throw new UnsupportedOperationException("Unimplemented method 'isUserCacheEnabled'");
    }

    @Override
    public Set<User> getCachedUsers() {

        throw new UnsupportedOperationException("Unimplemented method 'getCachedUsers'");
    }

    @Override
    public Optional<User> getCachedUserById(long id) {

        throw new UnsupportedOperationException("Unimplemented method 'getCachedUserById'");
    }

    @Override
    public CompletableFuture<User> getUserById(long id) {

        throw new UnsupportedOperationException("Unimplemented method 'getUserById'");
    }

    @Override
    public MessageSet getCachedMessages() {

        throw new UnsupportedOperationException("Unimplemented method 'getCachedMessages'");
    }

    @Override
    public Optional<Message> getCachedMessageById(long id) {

        throw new UnsupportedOperationException("Unimplemented method 'getCachedMessageById'");
    }

    @Override
    public Set<Server> getServers() {
        return Set.of(new MockServer());
    }

    @Override
    public Set<KnownCustomEmoji> getCustomEmojis() {

        throw new UnsupportedOperationException("Unimplemented method 'getCustomEmojis'");
    }

    @Override
    public CustomEmoji getKnownCustomEmojiOrCreateCustomEmoji(long id, String name, boolean animated) {

        throw new UnsupportedOperationException("Unimplemented method 'getKnownCustomEmojiOrCreateCustomEmoji'");
    }

    @Override
    public CompletableFuture<Set<StickerPack>> getNitroStickerPacks() {

        throw new UnsupportedOperationException("Unimplemented method 'getNitroStickerPacks'");
    }

    @Override
    public Optional<Sticker> getStickerById(long id) {

        throw new UnsupportedOperationException("Unimplemented method 'getStickerById'");
    }

    @Override
    public CompletableFuture<Sticker> requestStickerById(long id) {

        throw new UnsupportedOperationException("Unimplemented method 'requestStickerById'");
    }

    @Override
    public Set<Channel> getChannels() {

        throw new UnsupportedOperationException("Unimplemented method 'getChannels'");
    }

    @Override
    public Set<PrivateChannel> getPrivateChannels() {

        throw new UnsupportedOperationException("Unimplemented method 'getPrivateChannels'");
    }

    @Override
    public Set<ServerChannel> getServerChannels() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerChannels'");
    }

    @Override
    public Set<RegularServerChannel> getRegularServerChannels() {

        throw new UnsupportedOperationException("Unimplemented method 'getRegularServerChannels'");
    }

    @Override
    public Set<ChannelCategory> getChannelCategories() {

        throw new UnsupportedOperationException("Unimplemented method 'getChannelCategories'");
    }

    @Override
    public Set<ServerTextChannel> getServerTextChannels() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerTextChannels'");
    }

    @Override
    public Set<ServerForumChannel> getServerForumChannels() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerForumChannels'");
    }

    @Override
    public Set<ServerThreadChannel> getServerThreadChannels() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerThreadChannels'");
    }

    @Override
    public Set<ServerThreadChannel> getPrivateServerThreadChannels() {

        throw new UnsupportedOperationException("Unimplemented method 'getPrivateServerThreadChannels'");
    }

    @Override
    public Set<ServerThreadChannel> getPublicServerThreadChannels() {

        throw new UnsupportedOperationException("Unimplemented method 'getPublicServerThreadChannels'");
    }

    @Override
    public Set<ServerVoiceChannel> getServerVoiceChannels() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerVoiceChannels'");
    }

    @Override
    public Set<ServerStageVoiceChannel> getServerStageVoiceChannels() {

        throw new UnsupportedOperationException("Unimplemented method 'getServerStageVoiceChannels'");
    }

    @Override
    public Set<TextChannel> getTextChannels() {

        throw new UnsupportedOperationException("Unimplemented method 'getTextChannels'");
    }

    @Override
    public Set<VoiceChannel> getVoiceChannels() {

        throw new UnsupportedOperationException("Unimplemented method 'getVoiceChannels'");
    }

    @Override
    public Optional<Channel> getChannelById(long id) {

        throw new UnsupportedOperationException("Unimplemented method 'getChannelById'");
    }
    
}

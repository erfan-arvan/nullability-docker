package com.namelessmc.java_api;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.namelessmc.java_api.Notification.NotificationType;
import com.namelessmc.java_api.exception.AlreadyHasOpenReportException;
import com.namelessmc.java_api.exception.CannotReportSelfException;
import com.namelessmc.java_api.exception.ReportUserBannedException;
import com.namelessmc.java_api.integrations.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public final class NamelessUser implements LanguageEntity {

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessAPI api;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RequestHandler requests;

    // -1 if not known
    private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int id;

    // null if not known
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String username;

    private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean uuidKnown;

    // null if not known or not present
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable UUID uuid;

    private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean discordIdKnown;

    // -1 if not known or not present
    private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long discordId;

    // Do not use directly, instead use getUserInfo() and getIntegrations()
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable JsonObject _cachedUserInfo;

    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Map<String, DetailedIntegrationData> _cachedIntegrationData;

    /**
     * Create a Nameless user. Only one of 'id', 'uuid', 'discordId' has to be provided.
     * @param api Nameless API
     * @param id The user's id, or -1 if not known
     * @param username The user's username, or null if not known
     * @param uuidKnown True if it is known whether this user has a UUID or not
     * @param uuid The user's uuid, or null if the user doesn't have a UUID, or it is not known whether the user has a UUID
     * @param discordIdKnown True if it is known whether this user has a linked Discord id or not
     * @param discordId The user's discord id, or -1 if the user doesn't have a linked Discord id, or it is not known whether the user has a Discord id
     */
    @org.checkerframework.dataflow.qual.SideEffectFree
    NamelessUser(final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessAPI api, final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int id, final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String username,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean uuidKnown, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable UUID uuid,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean discordIdKnown,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull long discordId) {
        this.api = api;
        this.requests = api.getRequestHandler();
        if (id == -1 && username == null && !uuidKnown && !discordIdKnown) {
            throw new IllegalArgumentException("You must specify at least one of ID, uuid, username, discordId");
        }
        this.id = id;
        this.username = username;
        this.uuidKnown = uuidKnown;
        this.uuid = uuid;
        this.discordIdKnown = discordIdKnown;
        this.discordId = discordId;
    }

    @org.checkerframework.dataflow.qual.Impure
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull JsonObject getUserInfo() throws NamelessException {
        if (this._cachedUserInfo == null) {
            final JsonObject response;
            try {
                response = this.requests.get("users/" + this.getUserTransformer());
            } catch (final ApiError e) {
                if (e.getError() == ApiError.UNABLE_TO_FIND_USER) {
                    throw new UserNotExistException();
                } else {
                    throw e;
                }
            }
            if (!response.get("exists").getAsBoolean()) {
                throw new UserNotExistException();
            }
            this._cachedUserInfo = response;
        }
        return this._cachedUserInfo;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getUserTransformer() {
        if (id != -1) {
            return "id:" + this.id;
        } else if (this.uuidKnown && this.uuid != null) {
            return "integration_id:minecraft:" + this.uuid;
        } else if (this.discordIdKnown && this.discordId != -1) {
            return "integration_id:discord:" + this.discordId;
        } else if (this.username != null) {
            return "username:" + username;
        } else {
            throw new IllegalStateException("ID, uuid, and username not known for this player. This should be impossible, the constructor checks for this.");
        }
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessAPI getApi() {
        return this.api;
    }

    /**
     * The API method `userInfo` is only called once to improve performance.
     * This means that if something changes on the website, methods that use
     * data from the `userInfo` API method will keep returning the old data.
     * Calling this method will invalidate the cache and require making a new
     * API request. It will not make a new API request immediately. Calling
     * this method multiple times while the cache is already cleared has no
     * effect.
     */
    @org.checkerframework.dataflow.qual.Impure
    public void invalidateCache() {
        this._cachedUserInfo = null;
        this._cachedIntegrationData = null;
        if (this.id != -1) {
            // Only clear if we know the user's NamelessMC user id, otherwise we remove
            // the only way to identify this user
            this.uuidKnown = false;
            this.uuid = null;
            this.discordIdKnown = false;
            this.username = null;
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int getId() throws NamelessException {
        if (this.id == -1) {
            this.id = this.getUserInfo().get("id").getAsInt();
        }
        return this.id;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getUsername() throws NamelessException {
        if (this.username == null) {
            this.username = this.getUserInfo().get("username").getAsString();
        }
        return this.username;
    }

    @org.checkerframework.dataflow.qual.Impure
    public void updateUsername(final String username) throws NamelessException {
        JsonObject post = new JsonObject();
        post.addProperty("username", username);
        this.requests.post("users/" + this.getUserTransformer() + "/update-username", post);
    }

    @org.checkerframework.dataflow.qual.Impure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean exists() throws NamelessException {
        try {
            this.getUserInfo();
            return true;
        } catch (final UserNotExistException e) {
            return false;
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getDisplayName() throws NamelessException {
        return this.getUserInfo().get("displayname").getAsString();
    }

    /**
     * @return The date the user registered on the website.
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Date getRegisteredDate() throws NamelessException {
        return new Date(this.getUserInfo().get("registered_timestamp").getAsLong() * 1000);
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Date getLastOnline() throws NamelessException {
        return new Date(this.getUserInfo().get("last_online_timestamp").getAsLong() * 1000);
    }

    /**
     * @return Whether this account is banned from the website.
     */
    @org.checkerframework.dataflow.qual.Impure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isBanned() throws NamelessException {
        return this.getUserInfo().get("banned").getAsBoolean();
    }

    @org.checkerframework.dataflow.qual.Impure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isVerified() throws NamelessException {
        return this.getUserInfo().get("validated").getAsBoolean();
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String getLanguage(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessUser this) throws NamelessException {
        return this.getUserInfo().get("language").getAsString();
    }

    /**
     * Get POSIX code for user language (uses lookup table)
     * @return Language code or null if the user's language does not exist in our lookup table
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String getLanguagePosix(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NamelessUser this) throws NamelessException {
        return LanguageCodeMap.getLanguagePosix(this.getLanguage());
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull VerificationInfo getVerificationInfo() throws NamelessException {
        final boolean verified = isVerified();
        final JsonObject verification = this.getUserInfo().getAsJsonObject("verification");
        return new VerificationInfo(verified, verification);
    }

    /**
     * @return True if the user is member of at least one staff group, otherwise false
     */
    @org.checkerframework.dataflow.qual.Impure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isStaff() throws NamelessException {
        JsonArray groups = this.getUserInfo().getAsJsonArray("groups");
        for (JsonElement elem : groups) {
            JsonObject group = elem.getAsJsonObject();
            if (group.has("staff") && group.get("staff").getAsBoolean()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return Set of user's groups
     * @see #getSortedGroups()
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Set<Group> getGroups() throws NamelessException {
        return Collections.unmodifiableSet(StreamSupport.stream(this.getUserInfo().getAsJsonArray("groups").spliterator(), false).map(JsonElement::getAsJsonObject).map(Group::new).collect(Collectors.toSet()));
    }

    /**
     * @return List of the user's groups, sorted from low order to high order.
     * @see #getGroups()
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<Group> getSortedGroups() throws NamelessException {
        return Collections.unmodifiableList(StreamSupport.stream(this.getUserInfo().getAsJsonArray("groups").spliterator(), false).map(JsonElement::getAsJsonObject).map(Group::new).sorted().collect(Collectors.toList()));
    }

    /**
     * Same as doing {@link #getGroups()}.get(0), but with better performance
     * since it doesn't need to create and sort a list of group objects.
     * Empty if the user is not in any groups.
     *
     * @return Player's group with the lowest order
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Optional<Group> getPrimaryGroup() throws NamelessException {
        final JsonArray groups = this.getUserInfo().getAsJsonArray("groups");
        if (groups.size() > 0) {
            return Optional.of(new Group(groups.get(0).getAsJsonObject()));
        } else {
            return Optional.empty();
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void addGroups(final Group... groups) throws NamelessException {
        final JsonObject post = new JsonObject();
        post.add("groups", groupsToJsonArray(groups));
        this.requests.post("users/" + this.getUserTransformer() + "/groups/add", post);
        // Groups modified, invalidate cache
        invalidateCache();
    }

    @org.checkerframework.dataflow.qual.Impure
    public void removeGroups(final Group... groups) throws NamelessException {
        final JsonObject post = new JsonObject();
        post.add("groups", groupsToJsonArray(groups));
        this.requests.post("users/" + this.getUserTransformer() + "/groups/remove", post);
        // Groups modified, invalidate cache
        invalidateCache();
    }

    @org.checkerframework.dataflow.qual.Impure
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull JsonArray groupsToJsonArray(final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Group @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull [] groups) {
        final JsonArray array = new JsonArray();
        for (final Group group : groups) {
            array.add(group.getId());
        }
        return array;
    }

    @org.checkerframework.dataflow.qual.Impure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int getNotificationCount() throws NamelessException {
        final JsonObject response = this.requests.get("users/" + this.getUserTransformer() + "/notifications");
        return response.getAsJsonArray("notifications").size();
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<Notification> getNotifications() throws NamelessException {
        final JsonObject response = this.requests.get("users/" + this.getUserTransformer() + "/notifications");
        final List<Notification> notifications = new ArrayList<>();
        response.getAsJsonArray("notifications").forEach((element) -> {
            final String message = element.getAsJsonObject().get("message").getAsString();
            final String url = element.getAsJsonObject().get("url").getAsString();
            final NotificationType type = NotificationType.fromString(element.getAsJsonObject().get("type").getAsString());
            notifications.add(new Notification(message, url, type));
        });
        return notifications;
    }

    /**
     * Creates a report for a website user
     * @param user User to report. Lazy loading possible, only the ID is used.
     * @param reason Reason why this player has been reported
     * @throws IllegalArgumentException Report reason is too long (>255 characters)
     * @throws IllegalArgumentException Report reason is too long (>255 characters)
     * @throws NamelessException Unexpected http or api error
     * @throws ReportUserBannedException If the user creating this report is banned
     * @throws AlreadyHasOpenReportException If the user creating this report already has an open report for this user
     * @throws CannotReportSelfException If the user tries to report themselves
     */
    @org.checkerframework.dataflow.qual.Impure
    public void createReport(final NamelessUser user, final String reason) throws NamelessException, ReportUserBannedException, AlreadyHasOpenReportException, CannotReportSelfException {
        Objects.requireNonNull(user, "User to report is null");
        Objects.requireNonNull(reason, "Report reason is null");
        Preconditions.checkArgument(reason.length() < 255, "Report reason too long, it's %s characters but must be less than 255", reason.length());
        final JsonObject post = new JsonObject();
        post.addProperty("reporter", this.getId());
        post.addProperty("reported", user.getId());
        post.addProperty("content", reason);
        try {
            this.requests.post("reports/create", post);
        } catch (final ApiError e) {
            switch(e.getError()) {
                case ApiError.USER_CREATING_REPORT_BANNED:
                    throw new ReportUserBannedException();
                case ApiError.REPORT_CONTENT_TOO_LARGE:
                    throw new IllegalStateException("Website said report reason is too long, but we have client-side validation for this so it should be impossible");
                case ApiError.USER_ALREADY_HAS_OPEN_REPORT:
                    throw new AlreadyHasOpenReportException();
                case ApiError.CANNOT_REPORT_YOURSELF:
                    throw new CannotReportSelfException();
                default:
                    throw e;
            }
        }
    }

    /**
     * Create a report for a user who may or may not have a website account
     * @param reportedUuid The Mojang UUID of the Minecraft player to report
     * @param reportedName The Minecraft username of this player
     * @param reason Report reason
     * @throws IllegalArgumentException Report reason is too long (>255 characters)
     * @throws NamelessException Unexpected http or api error
     * @throws ReportUserBannedException If the user creating this report is banned
     * @throws AlreadyHasOpenReportException If the user creating this report already has an open report for this user
     * @throws CannotReportSelfException If the user tries to report themselves
     */
    @org.checkerframework.dataflow.qual.Impure
    public void createReport(final UUID reportedUuid, final String reportedName, final String reason) throws NamelessException, ReportUserBannedException, AlreadyHasOpenReportException, CannotReportSelfException {
        Objects.requireNonNull(reportedUuid, "Reported uuid is null");
        Objects.requireNonNull(reportedName, "Reported name is null");
        Objects.requireNonNull(reason, "Report reason is null");
        Preconditions.checkArgument(reason.length() < 255, "Report reason too long, it's %s characters but must be less than 255", reason.length());
        final JsonObject post = new JsonObject();
        post.addProperty("reporter", this.getId());
        post.addProperty("reported_uid", reportedUuid.toString());
        post.addProperty("reported_username", reportedName);
        post.addProperty("content", reason);
        try {
            this.requests.post("reports/create", post);
        } catch (final ApiError e) {
            switch(e.getError()) {
                case ApiError.USER_CREATING_REPORT_BANNED:
                    throw new ReportUserBannedException();
                case ApiError.REPORT_CONTENT_TOO_LARGE:
                    throw new IllegalStateException("Website said report reason is too long, but we have client-side validation for this so it should be impossible");
                case ApiError.USER_ALREADY_HAS_OPEN_REPORT:
                    throw new AlreadyHasOpenReportException();
                case ApiError.CANNOT_REPORT_YOURSELF:
                    throw new CannotReportSelfException();
                default:
                    throw e;
            }
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void setDiscordRoles(final long[] roleIds) throws NamelessException {
        final JsonObject post = new JsonObject();
        post.addProperty("user", this.getId());
        post.add("roles", NamelessAPI.GSON.toJsonTree(roleIds));
        this.requests.post("discord/set-roles", post);
    }

    /**
     * Get announcements visible to this user
     * @return List of announcements visible to this user
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<Announcement> getAnnouncements() throws NamelessException {
        final JsonObject response = this.requests.get("users/" + this.getUserTransformer() + "/announcements");
        return NamelessAPI.getAnnouncements(response);
    }

    /**
     * Ban this user
     * @since 2021-10-24 commit <code>cce8d262b0be3f70818c188725cd7e7fc4fdbb9a</code>
     */
    @org.checkerframework.dataflow.qual.Impure
    public void banUser() throws NamelessException {
        this.requests.post("users/" + this.getUserTransformer() + "/ban", new JsonObject());
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Collection<CustomProfileFieldValue> getProfileFields() throws NamelessException {
        if (!this.getUserInfo().has("profile_fields")) {
            return Collections.emptyList();
        }
        final JsonObject fieldsJson = this.getUserInfo().getAsJsonObject("profile_fields");
        final List<CustomProfileFieldValue> fieldValues = new ArrayList<>(fieldsJson.size());
        for (final Map.Entry<String, JsonElement> e : fieldsJson.entrySet()) {
            int id = Integer.parseInt(e.getKey());
            final JsonObject values = e.getValue().getAsJsonObject();
            fieldValues.add(new CustomProfileFieldValue(new CustomProfileField(id, values.get("name").getAsString(), CustomProfileFieldType.fromNamelessTypeInt(values.get("type").getAsInt()), values.get("public").getAsBoolean(), values.get("required").getAsBoolean(), values.get("description").getAsString()), values.get("value").getAsString()));
        }
        return fieldValues;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Map<String, DetailedIntegrationData> getIntegrations() throws NamelessException {
        if (this._cachedIntegrationData != null) {
            return this._cachedIntegrationData;
        }
        final JsonObject userInfo = this.getUserInfo();
        final JsonArray integrationsJsonArray = userInfo.getAsJsonArray("integrations");
        this._cachedIntegrationData = new HashMap<>(integrationsJsonArray.size());
        for (JsonElement integrationElement : integrationsJsonArray) {
            JsonObject integrationJson = integrationElement.getAsJsonObject();
            String integrationName = integrationJson.get("integration").getAsString();
            DetailedIntegrationData integrationData;
            switch(integrationName) {
                case StandardIntegrationTypes.MINECRAFT:
                    integrationData = new DetailedMinecraftIntegrationData(integrationJson);
                    break;
                case StandardIntegrationTypes.DISCORD:
                    integrationData = new DetailedDiscordIntegrationData(integrationJson);
                    break;
                default:
                    integrationData = new DetailedIntegrationData(integrationJson);
            }
            this._cachedIntegrationData.put(integrationName, integrationData);
        }
        return this._cachedIntegrationData;
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Optional<UUID> getMinecraftUuid() throws NamelessException {
        if (this.uuidKnown) {
            return Optional.ofNullable(this.uuid);
        }
        final DetailedIntegrationData integration = this.getIntegrations().get(StandardIntegrationTypes.MINECRAFT);
        this.uuidKnown = true;
        if (integration == null) {
            this.uuid = null;
        } else {
            this.uuid = ((IMinecraftIntegrationData) integration).getUniqueId();
        }
        return this.getMinecraftUuid();
    }

    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Optional<Long> getDiscordId() throws NamelessException {
        if (this.discordIdKnown) {
            if (this.discordId == -1) {
                return Optional.empty();
            } else {
                return Optional.of(this.discordId);
            }
        }
        final DetailedIntegrationData integration = this.getIntegrations().get(StandardIntegrationTypes.DISCORD);
        this.discordIdKnown = true;
        if (integration == null) {
            this.discordId = -1;
        } else {
            this.discordId = ((IDiscordIntegrationData) integration).getIdLong();
        }
        return this.getDiscordId();
    }
}

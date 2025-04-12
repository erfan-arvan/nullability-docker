package com.namelessmc.java_api;
public final class NamelessUser implements LanguageEntity {
	private final  NamelessAPI api;
	private final  RequestHandler requests;
	private int id; 
	private  String username; 
	private boolean uuidKnown;
	private  UUID uuid; 
	private boolean discordIdKnown;
	private long discordId; 
	private  JsonObject _cachedUserInfo;
	private  Map<String, DetailedIntegrationData> _cachedIntegrationData;
	NamelessUser(final  NamelessAPI api,
				 final int id,
				 final  String username,
				 final boolean uuidKnown,
				 final  UUID uuid,
				 final boolean discordIdKnown,
				 final long discordId
	) {
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
	private  JsonObject getUserInfo() throws NamelessException {
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
	public  String getUserTransformer() {
		if (id != -1) {
			return "id:" + this.id;
		} else if (this.uuidKnown && this.uuid != null) {
			return "integration_id:minecraft:" + this.uuid;
		} else if (this.discordIdKnown && this.discordId != -1) {
			return "integration_id:discord:" + this.discordId;
		} else if (this.username != null) {
			return "username:" + username;
		} else {
			throw new IllegalStateException("ID, uuid, and username not known for this player. " +
					"This should be impossible, the constructor checks for this.");
		}
	}
	public  NamelessAPI getApi() {
		return this.api;
	}
	public void invalidateCache() {
		this._cachedUserInfo = null;
		this._cachedIntegrationData = null;
		if (this.id != -1) {
			this.uuidKnown = false;
			this.uuid = null;
			this.discordIdKnown = false;
			this.username = null;
		}
	}
	public int getId() throws NamelessException {
		if (this.id == -1) {
			this.id = this.getUserInfo().get("id").getAsInt();
		}
		return this.id;
	}
	public  String getUsername() throws NamelessException {
		if (this.username == null) {
			this.username = this.getUserInfo().get("username").getAsString();
		}
		return this.username;
	}
	public void updateUsername(final  String username) throws NamelessException {
		JsonObject post = new JsonObject();
		post.addProperty("username", username);
		this.requests.post("users/" + this.getUserTransformer() + "/update-username", post);
	}
	public boolean exists() throws NamelessException {
		try {
			this.getUserInfo();
			return true;
		} catch (final UserNotExistException e) {
			return false;
		}
	}
	public  String getDisplayName() throws NamelessException {
		return this.getUserInfo().get("displayname").getAsString();
	}
	public  Date getRegisteredDate() throws NamelessException {
		return new Date(this.getUserInfo().get("registered_timestamp").getAsLong() * 1000);
	}
	public  Date getLastOnline() throws NamelessException {
		return new Date(this.getUserInfo().get("last_online_timestamp").getAsLong() * 1000);
	}
	public boolean isBanned() throws NamelessException {
		return this.getUserInfo().get("banned").getAsBoolean();
	}
	public boolean isVerified() throws NamelessException {
		return this.getUserInfo().get("validated").getAsBoolean();
	}
	@Override
	public  String getLanguage() throws NamelessException {
		return this.getUserInfo().get("language").getAsString();
	}
	@Override
	public  String getLanguagePosix() throws NamelessException {
		return LanguageCodeMap.getLanguagePosix(this.getLanguage());
	}
	public  VerificationInfo getVerificationInfo() throws NamelessException {
		final boolean verified = isVerified();
		final JsonObject verification = this.getUserInfo().getAsJsonObject("verification");
		return new VerificationInfo(verified, verification);
	}
	public boolean isStaff() throws NamelessException {
		JsonArray groups = this.getUserInfo().getAsJsonArray("groups");
		for (JsonElement elem : groups) {
			JsonObject group = elem.getAsJsonObject();
			if (group.has("staff") &&
					group.get("staff").getAsBoolean()) {
				return true;
			}
		}
		return false;
	}
	public  Set< Group> getGroups() throws NamelessException {
		return Collections.unmodifiableSet(
				StreamSupport.stream(this.getUserInfo().getAsJsonArray("groups").spliterator(), false)
						.map(JsonElement::getAsJsonObject)
						.map(Group::new)
						.collect(Collectors.toSet()));
	}
	public  List< Group> getSortedGroups() throws NamelessException {
		return Collections.unmodifiableList(
				StreamSupport.stream(this.getUserInfo().getAsJsonArray("groups").spliterator(), false)
						.map(JsonElement::getAsJsonObject)
						.map(Group::new)
						.sorted()
						.collect(Collectors.toList()));
	}
	public  Optional< Group> getPrimaryGroup() throws NamelessException {
		final JsonArray groups = this.getUserInfo().getAsJsonArray("groups");
		if (groups.size() > 0) {
			return Optional.of(new Group(groups.get(0).getAsJsonObject()));
		} else {
			return Optional.empty();
		}
	}
	public void addGroups(final  Group ... groups) throws NamelessException {
		final JsonObject post = new JsonObject();
		post.add("groups", groupsToJsonArray(groups));
		this.requests.post("users/" + this.getUserTransformer() + "/groups/add", post);
		invalidateCache(); 
	}
	public void removeGroups(final  Group... groups) throws NamelessException {
		final JsonObject post = new JsonObject();
		post.add("groups", groupsToJsonArray(groups));
		this.requests.post("users/" + this.getUserTransformer() + "/groups/remove", post);
		invalidateCache(); 
	}
	private JsonArray groupsToJsonArray(final  Group [] groups) {
		final JsonArray array = new JsonArray();
		for (final Group group : groups) {
			array.add(group.getId());
		}
		return array;
	}
	public int getNotificationCount() throws NamelessException {
		final JsonObject response = this.requests.get("users/" + this.getUserTransformer() + "/notifications");
		return response.getAsJsonArray("notifications").size();
	}
	public  List<Notification> getNotifications() throws NamelessException {
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
	public void createReport(final  NamelessUser user, final  String reason)
			throws NamelessException, ReportUserBannedException, AlreadyHasOpenReportException, CannotReportSelfException {
		Objects.requireNonNull(user, "User to report is null");
		Objects.requireNonNull(reason, "Report reason is null");
		Preconditions.checkArgument(reason.length() < 255,
				"Report reason too long, it's %s characters but must be less than 255", reason.length());
		final JsonObject post = new JsonObject();
		post.addProperty("reporter", this.getId());
		post.addProperty("reported", user.getId());
		post.addProperty("content", reason);
		try {
			this.requests.post("reports/create", post);
		} catch (final ApiError e) {
			switch (e.getError()) {
				case ApiError.USER_CREATING_REPORT_BANNED: throw new ReportUserBannedException();
				case ApiError.REPORT_CONTENT_TOO_LARGE:
					throw new IllegalStateException("Website said report reason is too long, but we have " +
							"client-side validation for this so it should be impossible");
				case ApiError.USER_ALREADY_HAS_OPEN_REPORT: throw new AlreadyHasOpenReportException();
				case ApiError.CANNOT_REPORT_YOURSELF: throw new CannotReportSelfException();
				default: throw e;
			}
		}
	}
	public void createReport(final  UUID reportedUuid,
							 final  String reportedName,
							 final  String reason)
			throws NamelessException, ReportUserBannedException, AlreadyHasOpenReportException, CannotReportSelfException {
		Objects.requireNonNull(reportedUuid, "Reported uuid is null");
		Objects.requireNonNull(reportedName, "Reported name is null");
		Objects.requireNonNull(reason, "Report reason is null");
		Preconditions.checkArgument(reason.length() < 255,
				"Report reason too long, it's %s characters but must be less than 255", reason.length());
		final JsonObject post = new JsonObject();
		post.addProperty("reporter", this.getId());
		post.addProperty("reported_uid", reportedUuid.toString());
		post.addProperty("reported_username", reportedName);
		post.addProperty("content", reason);
		try {
			this.requests.post("reports/create", post);
		} catch (final ApiError e) {
			switch (e.getError()) {
				case ApiError.USER_CREATING_REPORT_BANNED: throw new ReportUserBannedException();
				case ApiError.REPORT_CONTENT_TOO_LARGE:
					throw new IllegalStateException("Website said report reason is too long, but we have " +
							"client-side validation for this so it should be impossible");
				case ApiError.USER_ALREADY_HAS_OPEN_REPORT: throw new AlreadyHasOpenReportException();
				case ApiError.CANNOT_REPORT_YOURSELF: throw new CannotReportSelfException();
				default: throw e;
			}
		}
	}
	public void setDiscordRoles(final long[] roleIds) throws NamelessException {
		final JsonObject post = new JsonObject();
		post.addProperty("user", this.getId());
		post.add("roles", NamelessAPI.GSON.toJsonTree(roleIds));
		this.requests.post("discord/set-roles", post);
	}
	public  List< Announcement> getAnnouncements() throws NamelessException {
		final JsonObject response = this.requests.get("users/" + this.getUserTransformer() + "/announcements");
		return NamelessAPI.getAnnouncements(response);
	}
	public void banUser() throws NamelessException {
		this.requests.post("users/" + this.getUserTransformer() + "/ban", new JsonObject());
	}
	public  Collection< CustomProfileFieldValue> getProfileFields() throws NamelessException {
		if (!this.getUserInfo().has("profile_fields")) {
			return Collections.emptyList();
		}
		final JsonObject fieldsJson = this.getUserInfo().getAsJsonObject("profile_fields");
		final List<CustomProfileFieldValue> fieldValues = new ArrayList<>(fieldsJson.size());
		for (final Map.Entry<String, JsonElement> e : fieldsJson.entrySet()) {
			int id = Integer.parseInt(e.getKey());
			final JsonObject values = e.getValue().getAsJsonObject();
			fieldValues.add(new CustomProfileFieldValue(
					new CustomProfileField(
							id,
							values.get("name").getAsString(),
							CustomProfileFieldType.fromNamelessTypeInt(values.get("type").getAsInt()),
							values.get("public").getAsBoolean(),
							values.get("required").getAsBoolean(),
							values.get("description").getAsString()
					),
					values.get("value").getAsString()
			));
		}
		return fieldValues;
	}
	public Map<String, DetailedIntegrationData> getIntegrations() throws NamelessException {
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
	public Optional<UUID> getMinecraftUuid() throws NamelessException {
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
	public Optional<Long> getDiscordId() throws NamelessException {
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

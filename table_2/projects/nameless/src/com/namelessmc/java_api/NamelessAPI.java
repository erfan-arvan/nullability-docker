package com.namelessmc.java_api;
public final class NamelessAPI {
	static final Gson GSON = new Gson();
	private final  RequestHandler requests;
	private final  URL apiUrl;
	private final  String apiKey;
	NamelessAPI(final  RequestHandler requests,
				final  URL apiUrl,
				final  String apiKey) {
		this.requests = Objects.requireNonNull(requests, "Request handler is null");
		this.apiUrl = apiUrl;
		this.apiKey = apiKey;
	}
	 RequestHandler getRequestHandler() {
		return this.requests;
	}
	public  URL getApiUrl() {
		return this.apiUrl;
	}
	public  String getApiKey() {
		return this.apiKey;
	}
	public  List< Announcement> getAnnouncements() throws NamelessException {
		final JsonObject response = this.requests.get("announcements");
		return getAnnouncements(response);
	}
	@Deprecated
	public  List< Announcement> getAnnouncements(final  NamelessUser user) throws NamelessException {
		final JsonObject response = this.requests.get("users/" + user.getUserTransformer() + "/announcements");
		return getAnnouncements(response);
	}
	static  List< Announcement> getAnnouncements(final  JsonObject response) {
		return StreamSupport.stream(response.getAsJsonArray("announcements").spliterator(), false)
					.map(JsonElement::getAsJsonObject)
					.map(Announcement::new)
					.collect(Collectors.toList());
	}
	public void submitServerInfo(final  JsonObject jsonData) throws NamelessException {
		this.requests.post("minecraft/server-info", jsonData);
	}
	public Website getWebsite() throws NamelessException {
		final JsonObject json = this.requests.get("info");
		return new Website(json);
	}
	public FilteredUserListBuilder getRegisteredUsers() {
		return new FilteredUserListBuilder(this);
	}
	public  Optional<NamelessUser> getUser(final int id) throws NamelessException {
		final NamelessUser user = getUserLazy(id);
		if (user.exists()) {
			return Optional.of(user);
		} else {
			return Optional.empty();
		}
	}
	public  Optional<NamelessUser> getUser(final  String username) throws NamelessException {
		final NamelessUser user = getUserLazy(username);
		if (user.exists()) {
			return Optional.of(user);
		} else {
			return Optional.empty();
		}
	}
	public  Optional<NamelessUser> getUser(final  UUID uuid) throws NamelessException {
		final NamelessUser user = getUserLazy(uuid);
		if (user.exists()) {
			return Optional.of(user);
		} else {
			return Optional.empty();
		}
	}
	public  Optional<NamelessUser> getUserByDiscordId(final long discordId) throws NamelessException {
		final NamelessUser user = getUserLazyDiscord(discordId);
		if (user.exists()) {
			return Optional.of(user);
		} else {
			return Optional.empty();
		}
	}
	public  NamelessUser getUserLazy(final int id) {
		return new NamelessUser(this, id, null, false, null, false, -1L);
	}
	public  NamelessUser getUserLazy(final  String username) {
		return new NamelessUser(this, -1, username, false, null, false, -1L);
	}
	public  NamelessUser getUserLazy(final  UUID uuid) {
		return new NamelessUser(this, -1, null, true, uuid, false, -1L);
	}
	public NamelessUser getUserLazy(final  String username, final  UUID uuid) {
		return new NamelessUser(this, -1, username, true, uuid, false,-1L);
	}
	public NamelessUser getUserLazy(final int id, final  String username, final  UUID uuid) {
		return new NamelessUser(this, id, username, true, uuid, false, -1L);
	}
	public NamelessUser getUserLazyDiscord(final long discordId) {
		Preconditions.checkArgument(discordId > 0, "Discord id must be a positive long");
		return new NamelessUser(this, -1, null, false, null, true, discordId);
	}
	public  Optional< Group> getGroup(final int id) throws NamelessException {
		final JsonObject response = this.requests.get("groups", "id", id);
		final JsonArray jsonArray = response.getAsJsonArray("groups");
		if (jsonArray.size() != 1) {
			return Optional.empty();
		} else {
			return Optional.of(new Group(jsonArray.get(0).getAsJsonObject()));
		}
	}
	public  List< Group> getGroup(final  String name) throws NamelessException {
		Objects.requireNonNull(name, "Group name is null");
		final JsonObject response = this.requests.get("groups", "name", name);
		return groupListFromJsonArray(response.getAsJsonArray("groups"));
	}
	public  List<Group> getAllGroups() throws NamelessException {
		final JsonObject response = this.requests.get("groups");
		return groupListFromJsonArray(response.getAsJsonArray("groups"));
	}
	public int [] getAllGroupIds() throws NamelessException {
		final JsonObject response = this.requests.get("groups");
		return StreamSupport.stream(response.getAsJsonArray("groups").spliterator(), false)
				.map(JsonElement::getAsJsonObject)
				.mapToInt(o -> o.get("id").getAsInt())
				.toArray();
	}
	private  List<Group> groupListFromJsonArray(final  JsonArray array) {
		return StreamSupport.stream(array.spliterator(), false)
				.map(JsonElement::getAsJsonObject)
				.map(Group::new)
				.collect(Collectors.toList());
	}
	public  Optional<String> registerUser(final  String username,
												  final  String email,
												  final  IntegrationData ... integrationData)
			throws NamelessException, InvalidUsernameException, UsernameAlreadyExistsException,
					CannotSendEmailException, IntegrationUsernameAlreadyExistsException,
					IntegrationIdAlreadyExistsException, InvalidEmailAddressException, EmailAlreadyUsedException {
		Objects.requireNonNull(username, "Username is null");
		Objects.requireNonNull(email, "Email address is null");
		final JsonObject post = new JsonObject();
		post.addProperty("username", username);
		post.addProperty("email", email);
		if (integrationData != null && integrationData.length > 0) {
			JsonObject integrationsJson = new JsonObject();
			for (IntegrationData integration : integrationData) {
				JsonObject integrationJson = new JsonObject();
				integrationJson.addProperty("identifier", integration.getIdentifier());
				integrationJson.addProperty("username", integration.getUsername());
				integrationsJson.add(integration.getIntegrationType().toString(), integrationJson);
			}
			post.add("integrations", integrationsJson);
		}
		try {
			final JsonObject response = this.requests.post("users/register", post);
			if (response.has("link")) {
				return Optional.of(response.get("link").getAsString());
			} else {
				return Optional.empty();
			}
		} catch (final ApiError e) {
			switch (e.getError()) {
				case ApiError.INVALID_USERNAME: throw new InvalidUsernameException();
				case ApiError.USERNAME_ALREADY_EXISTS: throw new UsernameAlreadyExistsException();
				case ApiError.UNABLE_TO_SEND_REGISTRATION_EMAIL: throw new CannotSendEmailException();
				case ApiError.INTEGRATION_USERNAME_ALREADY_EXISTS: throw new IntegrationUsernameAlreadyExistsException();
				case ApiError.INTEGRATION_ID_ALREADY_EXISTS: throw new IntegrationIdAlreadyExistsException();
				case ApiError.INVALID_EMAIL_ADDRESS: throw new InvalidEmailAddressException();
				case ApiError.EMAIL_ALREADY_EXISTS: throw new EmailAlreadyUsedException();
				default: throw e;
			}
		}
	}
	public void setDiscordBotUrl(final  URL url) throws NamelessException {
		Objects.requireNonNull(url, "Bot url is null");
		final JsonObject json = new JsonObject();
		json.addProperty("url", url.toString());
		this.requests.post("discord/update-bot-settings", json);
	}
	public void setDiscordGuildId(final long guildId) throws NamelessException {
		final JsonObject json = new JsonObject();
		json.addProperty("guild_id", guildId + "");
		this.requests.post("discord/update-bot-settings", json);
	}
	public void setDiscordBotUser(final  String username, final long userId) throws NamelessException {
		Objects.requireNonNull(username, "Bot username is null");
		final JsonObject json = new JsonObject();
		json.addProperty("bot_username", username);
		json.addProperty("bot_user_id", userId + "");
		this.requests.post("discord/update-bot-settings", json);
	}
	public void setDiscordBotSettings(final  URL url,
									  final long guildId,
									  final  String username,
									  final long userId) throws NamelessException {
		Objects.requireNonNull(url, "Bot url is null");
		Objects.requireNonNull(username, "Bot username is null");
		final JsonObject json = new JsonObject();
		json.addProperty("url", url.toString());
		json.addProperty("guild_id", guildId + "");
		json.addProperty("bot_username", username);
		json.addProperty("bot_user_id", userId + "");
		this.requests.post("discord/update-bot-settings", json);
	}
	public void submitDiscordRoleList(final  Map<Long, String> discordRoles) throws NamelessException {
		final JsonArray roles = new JsonArray();
		discordRoles.forEach((id, name) -> {
			final JsonObject role = new JsonObject();
			role.addProperty("id", id);
			role.addProperty("name", name);
			roles.add(role);
		});
		final JsonObject json = new JsonObject();
		json.add("roles", roles);
		this.requests.post("discord/submit-role-list", json);
	}
	public void updateDiscordUsername(final long discordUserId,
									  final  String discordUsername)
			throws NamelessException {
		Objects.requireNonNull(discordUsername, "Discord username is null");
		final JsonObject user = new JsonObject();
		user.addProperty("id", discordUserId);
		user.addProperty("name", discordUsername);
		final JsonArray users = new JsonArray();
		users.add(user);
		final JsonObject json = new JsonObject();
		json.add("users", users);
		this.requests.post("discord/update-usernames", json);
	}
	public void updateDiscordUsernames(final long[] discordUserIds,
									   final   String[] discordUsernames)
			throws NamelessException {
		Objects.requireNonNull(discordUserIds, "User ids array is null");
		Objects.requireNonNull(discordUsernames, "Usernames array is null");
		Preconditions.checkArgument(discordUserIds.length == discordUsernames.length,
				"discord user ids and discord usernames must be of same length");
		if (discordUserIds.length == 0) {
			return;
		}
		final JsonArray users = new JsonArray();
		for (int i = 0; i < discordUserIds.length; i++) {
			final JsonObject user = new JsonObject();
			user.addProperty("id", discordUserIds[i]);
			user.addProperty("name", discordUsernames[i]);
			users.add(user);
		}
		final JsonObject json = new JsonObject();
		json.add("users", users);
		this.requests.post("discord/update-usernames", json);
	}
	public void verifyIntegration(final  IntegrationData integrationData,
								   final  String verificationCode)
			throws NamelessException, InvalidValidateCodeException {
		JsonObject data = new JsonObject();
		data.addProperty("integration", integrationData.getIntegrationType());
		data.addProperty("identifier", integrationData.getIdentifier());
		data.addProperty("username", integrationData.getUsername());
		data.addProperty("code", Objects.requireNonNull(verificationCode, "Verification code is null"));
		try {
			this.requests.post("integration/verify", data);
		} catch (ApiError e) {
			switch (e.getError()) {
				case ApiError.INVALID_VALIDATE_CODE:
					throw new InvalidValidateCodeException();
				default:
					throw e;
			}
		}
	}
	public  WebsendAPI websend() {
		return new WebsendAPI(this.requests);
	}
	public static  UUID websiteUuidToJavaUuid(final  String uuid) {
		Objects.requireNonNull(uuid, "UUID string is null");
		try {
			final BigInteger a = new BigInteger(uuid.substring(0, 16), 16);
			final BigInteger b = new BigInteger(uuid.substring(16, 32), 16);
			return new UUID(a.longValue(), b.longValue());
		} catch (final IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Invalid uuid: '" + uuid + "'", e);
		}
	}
	public static  NamelessApiBuilder builder(final  URL apiUrl,
											 final  String apiKey) {
		return new NamelessApiBuilder(apiUrl, apiKey);
	}
}

package com.namelessmc.java_api;
public class RequestHandler {
	private final  URL apiUrl;
	private final  Methanol httpClient;
	private final  ApiLogger debugLogger;
	private final  Gson gson;
	private final int responseLengthLimit;
	RequestHandler(final  URL apiUrl,
				   final  Methanol httpClient,
				   final  Gson gson,
				   final  ApiLogger debugLogger,
				   final int responseLengthLimit) {
		this.apiUrl = Objects.requireNonNull(apiUrl, "API URL is null");
		this.httpClient = Objects.requireNonNull(httpClient, "http client is null");
		this.gson = gson;
		this.debugLogger = debugLogger;
		this.responseLengthLimit = responseLengthLimit;
	}
	public  JsonObject post(final  String route,
									final  JsonObject postData) throws NamelessException {
		return makeConnection(route, postData);
	}
	public  JsonObject get(final  String route,
								   final  Object ... parameters) throws NamelessException {
		final StringBuilder urlBuilder = new StringBuilder(route);
		if (parameters.length > 0) {
			if (parameters.length % 2 != 0) {
				final String paramString = Arrays.stream(parameters).map(Object::toString).collect(Collectors.joining("|"));
				throw new IllegalArgumentException(String.format("Parameter string varargs array length must be even (length is %s - %s)", parameters.length, paramString));
			}
			for (int i = 0; i < parameters.length; i++) {
				if (i % 2 == 0) {
					urlBuilder.append("&");
					urlBuilder.append(parameters[i]);
				} else {
					urlBuilder.append("=");
					try {
						urlBuilder.append(URLEncoder.encode(parameters[i].toString(), StandardCharsets.UTF_8.toString()));
					} catch (final UnsupportedEncodingException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return makeConnection(urlBuilder.toString(), null);
	}
	private void debug(final  String message,
					   final  Supplier<Object[]> argsSupplier) {
		if (this.debugLogger != null) {
			this.debugLogger.log(String.format(message, argsSupplier.get()));
		}
	}
	private  JsonObject makeConnection(final  String route,
											   final  JsonObject postBody) throws NamelessException {
		Preconditions.checkArgument(!route.startsWith("/"), "Route must not start with a slash");
		final MutableRequest request = MutableRequest.create(URI.create(this.apiUrl.toString() + route));
		debug("Making connection %s to %s",
				() -> new Object[]{ postBody != null ? "POST" : "GET", request.uri()});
		if (postBody != null) {
			byte[] postBytes = gson.toJson(postBody).getBytes(StandardCharsets.UTF_8);
			request.POST(HttpRequest.BodyPublishers.ofByteArray(postBytes));
			request.header("Content-Type", "application/json");
			debug("Post body below\n-----------------\n%s\n-----------------",
					() -> new Object[] { new String(postBytes, StandardCharsets.UTF_8) });
		} else {
			request.GET();
		}
		int statusCode;
		String responseBody;
		try {
			HttpResponse<InputStream> httpResponse = httpClient.send(request,
					HttpResponse.BodyHandlers.ofInputStream());
			statusCode = httpResponse.statusCode();
			responseBody = getBodyAsString(httpResponse);
		} catch (final IOException e) {
			final  String exceptionMessage = e.getMessage();
			final StringBuilder message = new StringBuilder("Network connection error (not a Nameless issue).");
			if (exceptionMessage != null &&
					exceptionMessage.contains("unable to find valid certification path to requested target")) {
				message.append("\n HINT: Your certificate is invalid or incomplete. Ensure your website uses a valid *full chain* SSL/TLS certificate.");
			}
			message.append(" IOException: ");
			message.append(e.getMessage());
			throw new NamelessException(message.toString(), e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		debug("Website response below\n-----------------\n%s\n-----------------",
				() -> new Object[] { regularAsciiOnly(responseBody) });
		if (responseBody.length() == 0) {
			throw new NamelessException("Website sent empty response with status code " + statusCode);
		}
		if (responseBody.equals("API is disabled")) {
			throw new ApiDisabledException();
		}
		JsonObject json;
		try {
			json = JsonParser.parseString(responseBody).getAsJsonObject();
		} catch (final JsonSyntaxException | IllegalStateException e) {
			StringBuilder message = new StringBuilder();
			message.append("Website returned invalid response with code ");
			message.append(statusCode);
			message.append(".\n");
			if (statusCode >= 301 && statusCode <= 303) {
				message.append("HINT: The URL results in a redirect. If your URL uses http:
			} else if (statusCode == 520 || statusCode == 521) {
				message.append("HINT: Status code 520/521 is sent by CloudFlare when the backend webserver is down or having issues.\n");
			} else if (responseBody.contains("/aes.js")) {
				message.append("HINT: It looks like requests are being blocked by your web server or a proxy. ");
				message.append("This is a common occurrence with free web hosting services; they usually don't allow API access.\n");
			} else if (responseBody.contains("<title>Please Wait... | Cloudflare</title>")) {
				message.append("HINT: CloudFlare is blocking our request. Please see https:
			} else if (responseBody.startsWith("\ufeff")) {
				message.append("HINT: The website response contains invisible unicode characters. This seems to be caused by Partydragen's Store module, we have no idea why.\n");
			}
			message.append("Website response:\n");
			message.append("-----------------\n");
			int totalLengthLimit = 1950; 
			String printableResponse = regularAsciiOnly(responseBody);
			message.append(Ascii.truncate(printableResponse, totalLengthLimit - printableResponse.length(), "[truncated]\n"));
			if (message.charAt(message.length()) != '\n') {
				message.append('\n');
			}
			throw new NamelessException(message.toString(), e);
		}
		if (!json.has("error")) {
			throw new NamelessException("Unexpected response from website (missing json key 'error')");
		}
		if (json.get("error").getAsBoolean()) {
			 String meta = null;
			if (json.has("meta") && !json.get("meta").isJsonNull()) {
				meta = json.get("meta").toString();
			}
			throw new ApiError(json.get("code").getAsInt(), meta);
		}
		return json;
	}
	private String getBodyAsString(HttpResponse<InputStream> response) throws IOException {
		try (InputStream in = response.body();
				InputStream limited = ByteStreams.limit(in, this.responseLengthLimit)) {
			byte[] bytes = limited.readAllBytes();
			if (bytes.length == this.responseLengthLimit) {
				throw new IOException("Response larger than limit of " + this.responseLengthLimit + " bytes.");
			}
			return new String(bytes, StandardCharsets.UTF_8);
		}
	}
	private static  String regularAsciiOnly( String message) {
		char[] chars = message.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c >= ' ' && c <= '~' || c == '\n') {
				chars[i] = c;
			} else {
				chars[i] = '.';
			}
		}
		return new String(chars);
	}
}

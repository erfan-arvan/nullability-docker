package com.namelessmc.java_api;
public class Announcement {
	private final int id;
	private final  String header;
	private final  String message;
	private final  Set< String> displayPages;
	private final int [] displayGroups;
	Announcement( JsonObject announcementJson) {
		this.id = announcementJson.get("id").getAsInt();
		this.header = announcementJson.get("header").getAsString();
		this.message = announcementJson.get("message").getAsString();
		this.displayPages = Collections.unmodifiableSet(
				StreamSupport.stream(announcementJson.getAsJsonArray("pages").spliterator(), false)
						.map(JsonElement::getAsString)
						.collect(Collectors.toSet())
				);
		this.displayGroups = StreamSupport.stream(announcementJson.getAsJsonArray("groups").spliterator(), false)
						.mapToInt(JsonElement::getAsInt)
						.toArray();
	}
	public int getId() {
		return id;
	}
	public  String getHeader() {
		return this.header;
	}
	public  String getMessage() {
		return this.message;
	}
	@Deprecated
	public  String getContent() {
		return this.message;
	}
	public  Set< String> getDisplayPages() {
		return this.displayPages;
	}
	public int [] getDisplayGroupIds() {
		return this.displayGroups;
	}
}

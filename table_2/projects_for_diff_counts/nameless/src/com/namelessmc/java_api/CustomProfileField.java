package com.namelessmc.java_api;
public class CustomProfileField {
	private final int id;
	private final  String name;
	private final  CustomProfileFieldType type;
	private final boolean isPublic;
	private final boolean isRequired;
	private final  String description;
	CustomProfileField(final int id,
					   final  String name,
					   final  CustomProfileFieldType type,
					   final boolean isPublic,
					   final boolean isRequired,
					   final  String description) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.isPublic = isPublic;
		this.isRequired = isRequired;
		this.description = description;
	}
	public int getId() {
		return id;
	}
	public  String getName() {
		return name;
	}
	public  CustomProfileFieldType getType() {
		return type;
	}
	public boolean isPublic() {
		return isPublic;
	}
	public boolean isRequired() {
		return isRequired;
	}
	public  String getDescription() {
		return description;
	}
	@Override
	public boolean equals(final  Object other) {
		return other instanceof CustomProfileField &&
				((CustomProfileField) other).id == this.id;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}

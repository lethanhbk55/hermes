package com.gaia.hermes.bean;

public class PlatformBean {
	public static final int APPLE = 1;
	public static final int ANDROID = 2;
	private int id;
	private String name;
	private String displayName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}

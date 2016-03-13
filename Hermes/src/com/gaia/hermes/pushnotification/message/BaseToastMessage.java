package com.gaia.hermes.pushnotification.message;

public class BaseToastMessage implements ToastMessage {
	private String content;
	private String title;

	public BaseToastMessage() {

	}

	public BaseToastMessage(String content) {
		this();
		this.content = content;
	}

	public BaseToastMessage(String content, String title) {
		this(content);
		this.title = title;
	}

	@Override
	public String getContent() {
		return this.content;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}

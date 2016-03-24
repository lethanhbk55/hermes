package com.gaia.hermes.async.jobs;

import com.fullmoon.task.TaskFailureData;

public class HermesFailureData implements TaskFailureData {

	private Throwable exception;
	private String message;

	public HermesFailureData(Throwable exception) {
		this.setException(exception);
	}

	public HermesFailureData(String message) {
		this.message = message;
	}

	public HermesFailureData(String message, Throwable exception) {
		this(message);
		setException(exception);
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

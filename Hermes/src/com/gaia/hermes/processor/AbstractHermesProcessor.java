package com.gaia.hermes.processor;

import com.gaia.hermes.MessengerHandler;

public abstract class AbstractHermesProcessor implements HermesProcessor {

	private MessengerHandler context;

	public MessengerHandler getContext() {
		return context;
	}

	public void setContext(MessengerHandler context) {
		this.context = context;
	}
}

package com.gaia.hermes.processor;

import com.mario.entity.MessageHandler;
import com.nhb.common.BaseLoggable;
import com.nhb.common.data.PuElement;
import com.nhb.common.data.PuObjectRO;

public abstract class HermesProcessor extends BaseLoggable {

	private MessageHandler context;

	public MessageHandler getContext() {
		return context;
	}

	public void setContext(MessageHandler context) {
		this.context = context;
	}

	public abstract PuElement execute(PuObjectRO data);
}

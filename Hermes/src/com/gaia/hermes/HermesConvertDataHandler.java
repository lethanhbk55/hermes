package com.gaia.hermes;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.gaia.hermes.processor.HermesProcessor;
import com.gaia.hermes.statics.Field;
import com.mario.entity.impl.BaseMessageHandler;
import com.mario.entity.message.Message;
import com.nhb.common.data.PuElement;
import com.nhb.common.data.PuObject;
import com.nhb.common.data.PuObjectRO;
import com.nhb.common.data.PuValue;
import com.nhb.common.db.models.ModelFactory;

public class HermesConvertDataHandler extends BaseMessageHandler {
	private ModelFactory modelFactory;
	private Map<String, HermesProcessor> commandRoutingProcessors;
	private String appId;
	private String hermes2HandlerName;
	private String gcmAuthenId;
	private String appleAuthenId;

	@Override
	public void init(PuObjectRO initParams) {
		modelFactory = new ModelFactory(getApi().getDatabaseAdapter(initParams.getString(Field.MYSQL)));
		modelFactory.setClassLoader(this.getClass().getClassLoader());
		commandRoutingProcessors = new ConcurrentHashMap<>();

		if (initParams.variableExists(Field.COMMANDS)) {
			PuObject commands = initParams.getPuObject(Field.COMMANDS);
			for (Entry<String, PuValue> entry : commands) {
				String clazz = entry.getValue().getString();
				HermesProcessor processor;
				try {
					processor = (HermesProcessor) Class.forName(clazz).newInstance();
					processor.setContext(this);
					commandRoutingProcessors.put(entry.getKey(), processor);
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					throw new RuntimeException("error while create instance of processor", e);
				}
			}
		}

		appId = initParams.getString("bai389_appId");
		hermes2HandlerName = initParams.getString("hermes2HandlerName");
		setGcmAuthenId(initParams.getString(Field.GCM_AUTHEN_ID));
		setAppleAuthenId(initParams.getString(Field.APPLE_AUTHEN_ID));
	}

	@Override
	public PuElement handle(Message message) {
		PuObject request = (PuObject) message.getData();
		getLogger().debug("handler request: {}", request);
		if (request.variableExists(Field.COMMAND)) {
			String command = request.getString(Field.COMMAND);
			return commandRoutingProcessors.get(command).execute(request);
		} else {
			return new PuValue("request params was missing command param");
		}
	}

	@Override
	public PuElement interop(PuElement requestParams) {
		if (requestParams instanceof PuObject) {
			PuObject request = (PuObject) requestParams;
			if (request.variableExists(Field.COMMAND)) {
				String command = request.getString(Field.COMMAND);
				return commandRoutingProcessors.get(command).execute(request);
			} else {
				return new PuValue("request params was missing command param");
			}
		}
		return null;
	}

	public String getHermes2HandlerName() {
		return this.hermes2HandlerName;
	}

	public ModelFactory getModelFactory() {
		return this.modelFactory;
	}

	public String getAppId() {
		return appId;
	}

	public String getGcmAuthenId() {
		return gcmAuthenId;
	}

	public void setGcmAuthenId(String gcmAuthenId) {
		this.gcmAuthenId = gcmAuthenId;
	}

	public String getAppleAuthenId() {
		return appleAuthenId;
	}

	public void setAppleAuthenId(String appleAuthenId) {
		this.appleAuthenId = appleAuthenId;
	}
}

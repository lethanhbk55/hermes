package com.gaia.hermes.processor.converter;

import com.gaia.hermes.HermesConvertDataHandler;
import com.gaia.hermes.processor.HermesProcessor;
import com.gaia.hermes.statics.Field;
import com.nhb.common.data.MapTuple;
import com.nhb.common.data.PuElement;
import com.nhb.common.data.PuObject;
import com.nhb.common.data.PuObjectRO;

public class RegisterDeviceTokenProcessor extends HermesProcessor {

	@Override
	public PuElement execute(PuObjectRO data) {
		if (getContext() instanceof HermesConvertDataHandler) {
			HermesConvertDataHandler hanlder = (HermesConvertDataHandler) getContext();
			String token = data.getString(Field.TOKEN);
			int platformId = data.getInteger(Field.PLATFORM_ID);

			PuObject request = new PuObject();
			request.setString(Field.COMMAND, "registerToken");
			request.setString(Field.TOKEN, token);
			String serviceType = "";
			if (platformId == 1) {
				serviceType = "apns";
				request.setString(Field.AUTHENTICATOR_ID, hanlder.getAppleAuthenId());
			} else if (platformId == 2) {
				request.setString(Field.AUTHENTICATOR_ID, hanlder.getGcmAuthenId());
				serviceType = "gcm";
			} else if (platformId == 3) {
				serviceType = "wp";
			}
			request.setString(Field.SERVICE_TYPE, serviceType);
			request.setString(Field.APP_ID, hanlder.getAppId());
			PuElement result = hanlder.getApi().call(hanlder.getHermes2HandlerName(), request);
			return PuObject.fromObject(new MapTuple<>(Field.STATUS, 0, Field.DATA, result));
		}
		return null;
	}

}

package com.gaia.hermes.processor.converter;

import java.util.List;

import com.gaia.hermes.HermesConvertDataHandler;
import com.gaia.hermes.bean.DeviceAndTokenBean;
import com.gaia.hermes.model.DeviceAndTokenModel;
import com.gaia.hermes.processor.HermesProcessor;
import com.gaia.hermes.statics.Field;
import com.nhb.common.data.PuArray;
import com.nhb.common.data.PuArrayList;
import com.nhb.common.data.PuElement;
import com.nhb.common.data.PuObject;
import com.nhb.common.data.PuObjectRO;

public class ConvertAllDeviceTokenProcessor extends HermesProcessor {

	@Override
	public PuElement execute(PuObjectRO data) {
		try {
			HermesConvertDataHandler handler = (HermesConvertDataHandler) getContext();
			DeviceAndTokenModel model = handler.getModelFactory().newModel(DeviceAndTokenModel.class);
			List<DeviceAndTokenBean> tokens = model.findAll();
			String applicationId = handler.getAppId();

			PuArray array = new PuArrayList();
			for (DeviceAndTokenBean token : tokens) {
				PuObject puo = new PuObject();
				puo.setString(Field.TOKEN, token.getToken());
				String serviceType = "";
				if (token.getPlatformId() == 1) {
					serviceType = "apns";
					puo.setString(Field.AUTHENTICATOR_ID, handler.getAppleAuthenId());
				} else if (token.getPlatformId() == 2) {
					serviceType = "gcm";
					puo.setString(Field.AUTHENTICATOR_ID, handler.getGcmAuthenId());					
				}
				puo.setString("serviceType", serviceType);
				puo.setString(Field.APP_ID, applicationId);
				array.addFrom(puo);
			}

			PuObject request = new PuObject();
			request.setPuArray("deviceTokens", array);
			request.setString("command", "batchRegisterToken");

			PuElement result = handler.getApi().call(handler.getHermes2HandlerName(), request);

			return result;
		} catch (Exception e) {
			getLogger().debug("exception", e);
		}

		return null;
	}

}

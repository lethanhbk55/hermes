package com.gaia.hermes.pushnotification;

import java.util.List;

import com.gaia.hermes.pushnotification.message.ToastMessage;
import com.nhb.common.async.Callback;

import ar.com.fernandospr.wns.WnsService;
import ar.com.fernandospr.wns.model.WnsNotificationResponse;
import ar.com.fernandospr.wns.model.builders.WnsToastBuilder;

public class WindowsPhonePNS implements PushNoficationApi {
	private String sid;
	private String clientSecret = "";
	private boolean logging;

	public WindowsPhonePNS(String sid, String clientSecret, boolean logging) {
		this.sid = sid;
		this.clientSecret = clientSecret;
		this.logging = logging;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void push(Object targetToken, ToastMessage message, Callback<PushNotificationResult> callback) {
		WnsService service = new WnsService(sid, clientSecret, logging);
		WnsToastBuilder builder = new WnsToastBuilder();
		if (message.getContent() != null) {
			if (message.getTitle() != null) {
				builder.bindingTemplateToastText02(message.getTitle(), message.getContent());
			} else {
				builder.bindingTemplateToastText01(message.getContent());
			}
		}
		if (targetToken instanceof List<?>) {
			List<WnsNotificationResponse> results = service.pushToast((List<String>) targetToken, builder.build());
			for (WnsNotificationResponse wnsNotificationResponse : results) {
				String status = wnsNotificationResponse.notificationStatus;
				System.out.println("status: " + status);
			}
		} else {
			WnsNotificationResponse status = service.pushToast((String) targetToken, builder.build());
			System.out.println("status: " + status.notificationStatus);
		}

		callback.apply(new PushNotificationResult(0));
	}
}

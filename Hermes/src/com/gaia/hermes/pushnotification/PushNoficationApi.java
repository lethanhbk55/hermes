package com.gaia.hermes.pushnotification;

import com.gaia.hermes.pushnotification.message.ToastMessage;
import com.nhb.common.async.Callback;

public interface PushNoficationApi {

	void push(Object targetToken, ToastMessage message, Callback<PushNotificationResult> callback);
}

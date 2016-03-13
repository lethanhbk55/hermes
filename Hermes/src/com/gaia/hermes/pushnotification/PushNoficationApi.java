package com.gaia.hermes.pushnotification;

import com.gaia.hermes.pushnotification.message.ToastMessage;

public interface PushNoficationApi {
	int push(Object targetToken, ToastMessage message);
}

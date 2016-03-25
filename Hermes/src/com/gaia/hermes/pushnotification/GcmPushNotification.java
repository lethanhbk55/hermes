package com.gaia.hermes.pushnotification;

import java.io.IOException;
import java.util.List;

import com.gaia.hermes.pushnotification.message.ToastMessage;
import com.gaia.hermes.pushnotification.message.Tokens;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Message.Builder;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.nhb.common.BaseLoggable;

public class GcmPushNotification extends BaseLoggable implements PushNoficationApi {
	private String googleApiKey;

	public GcmPushNotification(String googleApiKey) {
		this.googleApiKey = googleApiKey;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int push(Object targetToken, ToastMessage message) {
		Sender sender = new Sender(this.googleApiKey);
		Builder builder = new Message.Builder().timeToLive(30).delayWhileIdle(true);

		if (message.getContent() != null) {
			builder.addData("alert", message.getContent());
		}

		if (message.getTitle() != null) {
			builder.addData("title", message.getTitle());
		}

		try {
			Message gcmMessage = builder.build();
			if (targetToken instanceof List<?>) {
				if (((List<?>) targetToken).size() > 0) {
					List<List<String>> packs = Tokens.splitTokens((List<String>) targetToken, 500);
					int success = 0;
					for (List<String> pack : packs) {
						getLogger().debug("pack size: {}", pack.size());
						getLogger().debug("message: {}", gcmMessage);
						MulticastResult result = sender.send(gcmMessage, pack, 1);
						success += result.getSuccess();
					}
					getLogger().debug("push notification success: {}", success);
					return success;
				} else {
					getLogger().debug("no device has been pushed");
					return 0;
				}
			} else {
				Result result = sender.send(gcmMessage, (String) targetToken, 1);
				getLogger().debug("send status: {}", result);
				if (result.getMessageId() != null) {
					return 1;
				}
			}
		} catch (IOException e) {
			getLogger().debug("error", e);
		}
		return 0;
	}

}

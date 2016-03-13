package com.gaia.hermes.pushnotification;

import java.util.List;

import com.gaia.hermes.pushnotification.message.ToastMessage;
import com.nhb.common.BaseLoggable;

import javapns.Push;
import javapns.notification.PushedNotifications;

public class ApplePushNotificationService extends BaseLoggable implements PushNoficationApi {
	private String filePath;
	private String password = "";
	private boolean debugMode = true;

	public ApplePushNotificationService(String filePath, String password) {
		this.filePath = filePath;
		this.password = password;
	}

	public ApplePushNotificationService(String filePath, String password, boolean debugMode) {
		this(filePath, password);
		this.debugMode = debugMode;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int push(Object targetToken, ToastMessage message) {
		boolean production = !this.debugMode;

		try {
			if (targetToken instanceof List<?>) {
				if (message.getContent() != null) {
					List<String> tokens = (List<String>) targetToken;
					getLogger().debug("begin push...");

					PushedNotifications notifications = Push.alert(message.getContent(), filePath, password, production,
							tokens);
					getLogger().debug("push success: {}", notifications.getSuccessfulNotifications().size());

					getLogger().debug("push notification success: {}",
							notifications.getSuccessfulNotifications().size());
					return notifications.getSuccessfulNotifications().size();
				}
				getLogger().debug("dont't push because message content is null");
			} else {
				if (message.getContent() != null) {
					PushedNotifications result = Push.alert(message.getContent(), this.filePath, this.password,
							production, (String) targetToken);
					getLogger().debug("push notification success: {}", result.getSuccessfulNotifications().size());
					return result.getSuccessfulNotifications().size();
				} else {
					getLogger().debug("dont't push because message content is null");
				}
			}
		} catch (Exception ex) {
			getLogger().debug("push notification error", ex);
		}
		return 0;
	}
}

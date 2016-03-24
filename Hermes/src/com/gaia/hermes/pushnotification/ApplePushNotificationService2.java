package com.gaia.hermes.pushnotification;

import java.io.File;

import com.gaia.hermes.pushnotification.message.ToastMessage;
import com.nhb.common.BaseLoggable;
import com.nhb.common.async.Callback;
import com.relayrides.pushy.apns.ApnsClient;
import com.relayrides.pushy.apns.ClientNotConnectedException;
import com.relayrides.pushy.apns.PushNotificationResponse;
import com.relayrides.pushy.apns.util.ApnsPayloadBuilder;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import com.relayrides.pushy.apns.util.TokenUtil;

import io.netty.util.concurrent.Future;

public class ApplePushNotificationService2 extends BaseLoggable implements PushNoficationApi {
	private String filePath;
	private String password = "";
	private boolean debugMode = true;
	private String bundleId;

	public ApplePushNotificationService2(String filePath, String password) {
		this.filePath = filePath;
		this.password = password;
	}

	public ApplePushNotificationService2(String filePath, String password, boolean debugMode) {
		this(filePath, password);
		this.debugMode = debugMode;
	}

	public ApplePushNotificationService2(String filePath, String password, String bundleId) {
		this(filePath, password);
		this.bundleId = bundleId;
	}

	@Override
	public void push(Object targetToken, ToastMessage message, Callback<PushNotificationResult> callback) {

		ApnsClient<SimpleApnsPushNotification> apnsClient = null;

		try {
			apnsClient = new ApnsClient<>(new File(filePath), password);
			final Future<Void> connectFuture = apnsClient.connect(ApnsClient.DEVELOPMENT_APNS_HOST);
			connectFuture.await();

			final SimpleApnsPushNotification pushNotification;

			final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
			payloadBuilder.setAlertBody(message.getContent());

			final String payload = payloadBuilder.buildWithDefaultMaximumLength();
			final String token = TokenUtil.sanitizeTokenString("<efc7492 bdbd8209>");

			pushNotification = new SimpleApnsPushNotification(token, bundleId, payload);

			final Future<PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = apnsClient
					.sendNotification(pushNotification);

			final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationReponse = sendNotificationFuture
					.get();

			if (pushNotificationReponse.isAccepted()) {
				System.out.println("Push notitification accepted by APNs gateway.");
			} else {
				System.out.println(
						"Notification rejected by the APNs gateway: " + pushNotificationReponse.getRejectionReason());

				if (pushNotificationReponse.getTokenInvalidationTimestamp() != null) {
					System.out.println("\t…and the token is invalid as of "
							+ pushNotificationReponse.getTokenInvalidationTimestamp());
				}
			}
		} catch (final Exception e) {
			getLogger().debug("error while send push notifcation", e);

			if (e.getCause() instanceof ClientNotConnectedException) {
				System.out.println("Waiting for client to reconnect…");
				try {
					apnsClient.getReconnectionFuture().await();
					System.out.println("Reconnected.");
				} catch (InterruptedException e1) {
					getLogger().error("error while reconnect to apns", e1);
				}
			}
		}
	}
}

package com.gaia.hermes.pushnotification;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.gaia.hermes.pushnotification.message.ToastMessage;
import com.nhb.common.BaseLoggable;

@Deprecated
public class GcmPostPushNotification extends BaseLoggable implements PushNoficationApi {
	private static String PUSH_NOTIFICATON_URL = "https://android.googleapis.com/gcm/send";

	private String googleApiKey;

	public GcmPostPushNotification(String googleApiKey) {
		this.googleApiKey = googleApiKey;
	}

	@Override
	public int push(Object targetToken, ToastMessage message) {
		String data = String.format("{\"registration_ids\": [\"%s\"],\"data\": {\"message\": \"%s\"}}", targetToken,
				message);
		try {
			this.sendHttpRequest(PUSH_NOTIFICATON_URL, data);
		} catch (Exception e) {
			getLogger().error("push notication error", e);
		}

		return 0;
	}

	private String sendHttpRequest(String url, String data) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", "key=" + googleApiKey);

		con.setDoOutput(true);
		System.out.println("request properies: " + con.getRequestProperty("Authorization"));
		System.out.println("POST: " + url + " data: " + data);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(data);
		wr.flush();
		wr.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		System.out.println("Response: " + response.toString());
		return response.toString();
	}
}

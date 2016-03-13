package com.gaia.hermes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.gaia.hermes.bean.ApnsCertificateBean;
import com.gaia.hermes.bean.ApplicationBean;
import com.gaia.hermes.bean.ApplicationConfigBean;
import com.gaia.hermes.bean.DeviceBean;
import com.gaia.hermes.bean.DeviceTokenBean;
import com.gaia.hermes.bean.GcmKeyBean;
import com.gaia.hermes.bean.PlatformBean;
import com.gaia.hermes.bean.PushNoficationConfigBean;
import com.gaia.hermes.bean.PushNotificationBean;
import com.gaia.hermes.jobs.CreateApplicationJob;
import com.gaia.hermes.jobs.RegisterDeviceJob;
import com.gaia.hermes.model.ApnsCertificateModel;
import com.gaia.hermes.model.ApplicationConfigModel;
import com.gaia.hermes.model.ApplicationModel;
import com.gaia.hermes.model.DeviceModel;
import com.gaia.hermes.model.DeviceTokenModel;
import com.gaia.hermes.model.GcmKeyModel;
import com.gaia.hermes.model.PushNotificationConfigModel;
import com.gaia.hermes.model.PushNotificationModel;
import com.gaia.hermes.pushnotification.ApplePushNotificationService;
import com.gaia.hermes.pushnotification.GcmPushNotification;
import com.gaia.hermes.pushnotification.PushNoficationApi;
import com.gaia.hermes.pushnotification.message.BaseToastMessage;
import com.gaia.hermes.statics.Command;
import com.gaia.hermes.statics.Field;
import com.mario.entity.impl.BaseCommandRoutingHandler;
import com.mario.entity.message.Message;
import com.mario.schedule.ScheduledCallback;
import com.nhb.common.data.MapTuple;
import com.nhb.common.data.PuElement;
import com.nhb.common.data.PuObject;
import com.nhb.common.data.PuObjectRO;
import com.nhb.common.db.models.ModelFactory;
import com.nhb.common.db.sql.resp.SqlUpdateResponse;
import com.nhb.common.transaction.DistributedTransactionJob;
import com.nhb.common.utils.FileSystemUtils;

public class MessengerHandler extends BaseCommandRoutingHandler {
	private ModelFactory factory;
	private static int lastPushStatus = 0;
	private ExecutorService executorService;
	private boolean debug = false;
	private int lastPushSuccess = 0;

	@Override
	public void init(PuObjectRO initParams) {
		this.factory = new ModelFactory();
		this.factory.setClassLoader(this.getClass().getClassLoader());
		if (initParams.variableExists(Field.DATASOURCE_NAME)) {
			this.factory.setDbAdapter(getApi().getDatabaseAdapter(initParams.getString(Field.DATASOURCE_NAME)));
		}
		// executorService = Executors.newFixedThreadPool(32);
		this.debug = initParams.getBoolean("debug", false);
	}

	@Override
	public PuElement handle(Message message) {
		PuObject data = (PuObject) message.getData();
		Object result = null;
		int status = 1;
		getLogger().debug("handle request: {}", data);
		try {
			String command = data.getString(Field.COMMAND);
			if (command != null) {
				switch (command) {
				case Command.REGISTER: {
					if (data.variableExists(Field.PLATFORM_ID) && data.variableExists(Field.UDID)
							&& data.variableExists(Field.BUNDLE_ID) && data.variableExists(Field.TOKEN)) {
						DistributedTransactionJob job = new RegisterDeviceJob(this.factory.newModel(DeviceModel.class),
								this.factory.newModel(DeviceTokenModel.class),
								this.factory.newModel(ApplicationModel.class));
						if (job.execute(data)) {
							result = job.getResult();
							status = 0;
						} else {
							result = job.getFailureDetails();
						}
					} else {
						result = "paramters are missing for register job";
					}
					break;
				}
				case Command.CREATE_APPLICATION: {
					if (data.variableExists(Field.BUNDLE_ID) && data.variableExists(Field.KEY)
							&& data.variableExists(Field.FILE_PATH) && data.variableExists(Field.NAME)
							&& data.variableExists(Field.PASSWORD)) {
						DistributedTransactionJob job = new CreateApplicationJob(
								this.factory.newModel(ApplicationModel.class),
								this.factory.newModel(ApnsCertificateModel.class),
								this.factory.newModel(GcmKeyModel.class),
								this.factory.newModel(PushNotificationConfigModel.class));
						if (job.execute(data)) {
							result = job.getResult();
							status = 0;
						} else {
							result = job.getFailureDetails();
						}
					} else {
						result = "paramters are missing for create application job";
					}
					break;
				}
				case Command.UDPATE_APPLICATION_CONFIG: {
					if (data.variableExists(Field.BUNDLE_ID) && data.variableExists(Field.SECRET_KEY)) {
						String bundleId = data.getString(Field.BUNDLE_ID);
						String secretKey = data.getString(Field.SECRET_KEY);
						String filePath = null;
						String password = null;
						String googleApiKey = null;

						if (data.variableExists(Field.FILE_PATH) && data.variableExists(Field.PASSWORD)) {
							filePath = data.getString(Field.FILE_PATH);
							password = data.getString(Field.PASSWORD);
						}

						if (data.variableExists(Field.GOOGLE_API_KEY)) {
							googleApiKey = data.getString(Field.GOOGLE_API_KEY);
						}

						ApplicationModel applicationModel = this.factory.newModel(ApplicationModel.class);
						ApplicationBean application = applicationModel.findByBundleIdAndSecretKey(bundleId, secretKey);
						if (application != null) {
							byte[] pushNotificationConfigId = application.getPushNotificationConfigId();
							PushNotificationConfigModel configModel = this.factory
									.newModel(PushNotificationConfigModel.class);
							PushNoficationConfigBean config = configModel.findById(pushNotificationConfigId);
							if (config != null) {
								if (filePath != null && password != null) {
									ApnsCertificateBean bean = new ApnsCertificateBean();
									bean.setFilePath(filePath);
									bean.setPassword(password);
									bean.setId(config.getApnsCertificateId());
									ApnsCertificateModel certificateModel = this.factory
											.newModel(ApnsCertificateModel.class);
									certificateModel.update(bean);
									status = 0;
									result = "updated apns is successful";
								}

								if (googleApiKey != null) {
									GcmKeyBean bean = new GcmKeyBean();
									bean.setId(config.getGcmKeyId());
									bean.setKey(googleApiKey);
									GcmKeyModel gcmKeyModel = this.factory.newModel(GcmKeyModel.class);
									gcmKeyModel.update(bean);
									status = 0;
									result = "updated gcm key is successful";
								}
							}
						}

					}
					break;
				}
				case Command.PUSH: {
					if (data.variableExists(Field.MESSAGE)) {
						String mess = data.getString(Field.MESSAGE);
						BaseToastMessage letter = new BaseToastMessage(mess);
						int platformId = 0;
						if (data.variableExists(Field.PLATFORM_ID)) {
							platformId = data.getInteger(Field.PLATFORM_ID);
						}
						if (data.variableExists(Field.BUNDLE_ID) && data.variableExists(Field.SECRET_KEY)) {
							String bundleId = data.getString(Field.BUNDLE_ID);
							String secretKey = data.getString(Field.SECRET_KEY);

							ApplicationConfigModel appModel = this.factory.newModel(ApplicationConfigModel.class);
							ApplicationConfigBean config = appModel.findByBundleIdAndSecretKey(bundleId, secretKey);
							PushNotificationModel pushModel = this.factory.newModel(PushNotificationModel.class);
							if (config != null) {
								if (data.variableExists(Field.TITLE)) {
									letter.setTitle(data.getString(Field.TITLE));
								} else {
									letter.setTitle(config.getApplicationName());
								}
								String filePath = FileSystemUtils.createPathFrom(
										FileSystemUtils.getBasePathForClass(ApplePushNotificationService.class),
										"resources", config.getFilePath());
								getLogger().debug("filepath: {}", filePath);
								PushNoficationApi appleApi = new ApplePushNotificationService(filePath,
										config.getAppleCertificatePassword(), debug);
								getLogger().debug("google api key: {}", config.getGoogleApiKey());
								PushNoficationApi googleApi = new GcmPushNotification(config.getGoogleApiKey());

								Collection<PushNotificationBean> targets = null;

								if (platformId != 0) {
									targets = pushModel.findByApplicationIdAndPlatformId(config.getApplicationId(),
											platformId);
								} else {
									targets = pushModel.findByApplicationId(config.getApplicationId());
								}
								getLogger().debug("devices: {}", targets.size());
								List<String> googleTokens = new ArrayList<>();
								List<String> appleTokens = new ArrayList<>();
								if (targets != null & targets.size() > 0) {
									for (PushNotificationBean target : targets) {
										if (target.getPlatformId() == 1) {
											appleTokens.add(target.getToken());
										} else if (target.getPlatformId() == 2) {
											googleTokens.add(target.getToken());
										}
									}

									int applePushSuccessful = appleTokens.size();
									if (appleTokens.size() > 0 && lastPushStatus == 0) {
										getApi().getScheduler().schedule(0, new ScheduledCallback() {

											@Override
											public void call() {
												lastPushStatus = 1;
												lastPushSuccess = appleApi.push(appleTokens, letter);
												lastPushStatus = 0;
											}
										});
									}

									int googlePushSuccessful = googleApi.push(googleTokens, letter);
									PuObject resp = PuObject.fromObject(new MapTuple<>(Field.COUNT,
											applePushSuccessful + googlePushSuccessful, Field.NUMBER, targets.size()));
									if (lastPushStatus == 1) {
										resp.getString("message",
												"wait for last push notification ios successful, last push success: "
														+ lastPushSuccess);
									}
									result = resp;
									status = 0;
								} else {
									result = "no targets to push";
								}

							} else {
								result = "application cannot be found";
							}
						} else {
							result = "parameters are missing";
						}
					} else {
						result = "message is missing";
					}
					break;
				}
				case Command.PUSH_TOKENS: {
					String tokens = data.getString(Field.TOKENS);
					String[] listTokens = tokens.split(",");
					List<String> googleTokens = new ArrayList<>();
					List<String> appleTokens = new ArrayList<>();
					ApplicationConfigModel applicationConfigModel = this.factory.newModel(ApplicationConfigModel.class);
					DeviceTokenModel tokenModel = this.factory.newModel(DeviceTokenModel.class);
					DeviceModel deviceModel = this.factory.newModel(DeviceModel.class);

					byte[] applicationId = null;
					if (listTokens.length > 0) {
						DeviceTokenBean deviceToken = tokenModel.findByToken(listTokens[0]);
						if (deviceToken != null) {
							applicationId = deviceToken.getApplicationId();
						}
					}
					BaseToastMessage letter = new BaseToastMessage(data.getString(Field.MESSAGE));

					if (applicationId != null) {
						ApplicationConfigBean config = applicationConfigModel.findById(applicationId);
						if (data.variableExists(Field.TITLE)) {
							letter.setTitle(data.getString(Field.TITLE));
						} else {
							letter.setTitle(config.getApplicationName());
						}
						String filePath = FileSystemUtils.createPathFrom(
								FileSystemUtils.getBasePathForClass(ApplePushNotificationService.class), "resources",
								config.getFilePath());
						getLogger().debug("filepath: {}", filePath);
						PushNoficationApi appleApi = new ApplePushNotificationService(filePath,
								config.getAppleCertificatePassword(), debug);
						PushNoficationApi googleApi = new GcmPushNotification(config.getGoogleApiKey());

						for (String token : listTokens) {
							DeviceTokenBean deviceToken = tokenModel.findByToken(token);
							DeviceBean device = deviceModel.findById(deviceToken.getDeviceId());
							if (device.getPlatformId() == PlatformBean.APPLE) {
								appleTokens.add(token);
							} else if (device.getPlatformId() == PlatformBean.ANDROID) {
								googleTokens.add(token);
							}
						}

						int applePushSuccessful = appleApi.push(appleTokens, letter);
						int googlePushSuccessful = googleApi.push(googleTokens, letter);
						getLogger().debug("apple report: {}/{}", applePushSuccessful, appleTokens.size());
						getLogger().debug("google report: {}/{}", googlePushSuccessful, googleTokens.size());
						result = PuObject.fromObject(new MapTuple<>(Field.COUNT,
								applePushSuccessful + googlePushSuccessful, Field.NUMBER, listTokens.length));
						status = 0;
					} else {
						result = "application cannot be found";
					}
					break;
				}
				case Command.GET_APPLICATION_CONFIG: {
					if (data.variableExists(Field.BUNDLE_ID) && data.variableExists(Field.SECRET_KEY)) {
						String bundleId = data.getString(Field.BUNDLE_ID);
						String secretKey = data.getString(Field.SECRET_KEY);
						ApplicationConfigModel model = this.factory.newModel(ApplicationConfigModel.class);
						ApplicationConfigBean configBean = model.findByBundleIdAndSecretKey(bundleId, secretKey);
						PuObject puo = new PuObject();
						puo.setString(Field.FILE_PATH, configBean.getFilePath());
						puo.setString(Field.APPLE_CERTIFICATE_PASSWORD, configBean.getAppleCertificatePassword());
						puo.setString(Field.GOOGLE_API_KEY, configBean.getGoogleApiKey());
						puo.setString(Field.APPLICATION_NAME, configBean.getApplicationName());
						puo.setString(Field.BUNDLE_ID, configBean.getBundleId());
						puo.setString(Field.SECRET_KEY, configBean.getSecretKey());
						result = puo;
						status = 0;
					} else {
						result = "paramerters are missing";
					}
					break;
				}
				case Command.GET_ALL_APPLICATION: {
					ApplicationModel model = this.factory.newModel(ApplicationModel.class);
					Collection<ApplicationBean> apps = model.findAll();
					Collection<PuObject> puos = new ArrayList<>();
					for (ApplicationBean app : apps) {
						puos.add(app.toPuObject());
					}
					result = PuObject.fromObject(new MapTuple<>(Field.APPS, puos));
					status = 0;
					break;
				}
				case Command.DELETE_APPLICATION: {
					ApplicationModel model = this.factory.newModel(ApplicationModel.class);
					if (data.variableExists(Field.BUNDLE_ID) && data.variableExists(Field.SECRET_KEY)) {
						String bundleId = data.getString(Field.BUNDLE_ID);
						String secretKey = data.getString(Field.SECRET_KEY);
						ApplicationBean application = model.findByBundleIdAndSecretKey(bundleId, secretKey);
						if (application != null) {
							SqlUpdateResponse response = model.delete(application.getId());
							if (response.isSuccess()) {
								status = 0;
								result = "delete application successful";
							} else {
								result = response.getException();
							}
						} else {
							result = "application cannot be found";
						}
					} else {
						result = "parameter is missing";
					}
				}
				default:
					break;
				}
			}
		} catch (Exception ex) {
			getLogger().debug("error", ex);
			result = ex;
		} finally {
		}
		PuObject response = new PuObject();
		response.setInteger(Field.STATUS, status);
		if (result != null) {
			if (result instanceof Throwable) {
				result = ExceptionUtils.getFullStackTrace((Throwable) result);
			}
			response.set(Field.DATA, result);
		}
		return response;
	}

	@Override
	public PuElement interop(PuElement request) {
		PuObject requestParams = (PuObject) request;
		if (requestParams.variableExists(Field.COMMAND)) {
			String command = requestParams.getString(Field.COMMAND);
			PuObject data = requestParams.getPuObject(Field.DATA);
			switch (command) {
			case Command.CREATE_APPLICATION: {
				if (data.variableExists(Field.BUNDLE_ID) && data.variableExists(Field.KEY)
						&& data.variableExists(Field.FILE_PATH) && data.variableExists(Field.NAME)
						&& data.variableExists(Field.PASSWORD)) {
					DistributedTransactionJob job = new CreateApplicationJob(
							this.factory.newModel(ApplicationModel.class),
							this.factory.newModel(ApnsCertificateModel.class), this.factory.newModel(GcmKeyModel.class),
							this.factory.newModel(PushNotificationConfigModel.class));
					int status = 1;
					PuObject result = new PuObject();
					if (job.execute(data)) {
						status = 0;
						result.setPuObject(Field.DATA, job.getResult());
					} else {
						result.setPuObject(Field.DATA, job.getFailureDetails());
					}
					result.setInteger(Field.STATUS, status);
					return result;
				}
				break;
			}
			case Command.UDPATE_APPLICATION_CONFIG: {
				if (data.variableExists(Field.BUNDLE_ID) && data.variableExists(Field.SECRET_KEY)) {
					String bundleId = data.getString(Field.BUNDLE_ID);
					String secretKey = data.getString(Field.SECRET_KEY);
					String filePath = null;
					String password = null;
					String googleApiKey = null;

					if (data.variableExists(Field.FILE_PATH) && data.variableExists(Field.PASSWORD)) {
						filePath = data.getString(Field.FILE_PATH);
						password = data.getString(Field.PASSWORD);
					}

					if (data.variableExists(Field.GOOGLE_API_KEY)) {
						googleApiKey = data.getString(Field.GOOGLE_API_KEY);
					}

					ApplicationModel applicationModel = this.factory.newModel(ApplicationModel.class);
					ApplicationBean application = applicationModel.findByBundleIdAndSecretKey(bundleId, secretKey);
					if (application != null) {
						byte[] pushNotificationConfigId = application.getPushNotificationConfigId();
						PushNotificationConfigModel configModel = this.factory
								.newModel(PushNotificationConfigModel.class);
						PushNoficationConfigBean config = configModel.findById(pushNotificationConfigId);
						int status = 1;
						String result = "";
						if (config != null) {
							if (filePath != null && password != null) {
								ApnsCertificateBean bean = new ApnsCertificateBean();
								bean.setFilePath(filePath);
								bean.setPassword(password);
								bean.setId(config.getApnsCertificateId());
								ApnsCertificateModel certificateModel = this.factory
										.newModel(ApnsCertificateModel.class);
								certificateModel.update(bean);
								status = 0;
								result = "updated apns is successful";
							}

							if (googleApiKey != null) {
								GcmKeyBean bean = new GcmKeyBean();
								bean.setId(config.getGcmKeyId());
								bean.setKey(googleApiKey);
								GcmKeyModel gcmKeyModel = this.factory.newModel(GcmKeyModel.class);
								gcmKeyModel.update(bean);
								status = 0;
								result = "updated gcm key is successful";
							}
						}

						return PuObject.fromObject(new MapTuple<>(Field.STATUS, status, Field.DATA, result));
					} else {
						return PuObject
								.fromObject(new MapTuple<>(Field.STATUS, 1, Field.DATA, "application cannot be found"));
					}
				}
				break;
			}
			default:
				break;
			}
		}

		return null;
	}

	@Override
	public void destroy() throws Exception {
		if (this.executorService != null) {
			this.executorService.shutdown();
			if (this.executorService.awaitTermination(3, TimeUnit.SECONDS)) {
				this.executorService.shutdownNow();
			}
		}
	}
}

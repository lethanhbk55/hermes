package com.gaia.hermes;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.gaia.hermes.statics.Command;
import com.gaia.hermes.statics.Field;
import com.mario.entity.impl.BaseCommandRoutingHandler;
import com.mario.entity.message.Message;
import com.nhb.common.data.PuElement;
import com.nhb.common.data.PuObject;
import com.nhb.common.utils.FileSystemUtils;

public class UploadFileHandler extends BaseCommandRoutingHandler {

	private static final String MARIO_HERMES = "MarioHermes";

	@SuppressWarnings("resource")
	@Override
	public PuElement handle(Message message) {
		Object result = null;
		int status = 1;
		if (message != null) {
			try {
				PuObject requestParams = (PuObject) message.getData();
				String command = requestParams.getString(Field.COMMAND);
				if (command != null) {
					switch (command) {
					case Command.UPLOAD: {
						byte[] fileData = requestParams.getRaw(Field.FILE);
						String fileName = requestParams.getString(Field.FILE_NAME);
						try {
							String filePath = FileSystemUtils.createPathFrom(
									FileSystemUtils.getBasePathForClass(this.getClass()), "resources", fileName);

							File file = new File(filePath);
							FileOutputStream fileWriter = new FileOutputStream(file);
							fileWriter.write(fileData);

							PuObject puo = new PuObject();
							puo.setString(Field.COMMAND, Command.CREATE_APPLICATION);
							PuObject data = new PuObject();
							data.setString(Field.BUNDLE_ID, requestParams.getString(Field.BUNDLE_ID));
							data.setString(Field.NAME, requestParams.getString(Field.NAME));
							data.setString(Field.FILE_PATH, filePath);
							data.setString(Field.KEY, requestParams.getString(Field.KEY));
							data.setString(Field.PASSWORD, requestParams.getString(Field.PASSWORD));
							puo.setPuObject(Field.DATA, data);
							PuObject resp = (PuObject) getApi().call(MARIO_HERMES, puo);
							if (resp != null) {
								status = resp.getInteger(Field.STATUS);
								resp = resp.getPuObject(Field.DATA);
							} else {
								status = 1;
								result = "save file was successful, but create application is fail";
							}
						} catch (Exception e) {
							result = e;
						}
						break;
					}
					case Command.CREATE_APPLICATION: {
						String fileName = "";
						String password = "";
						String bundleId = "";
						String name = "";
						String googleApiKey = "";

						if (requestParams.variableExists(Field.PASSWORD)) {
							password = requestParams.getString(Field.PASSWORD);
						}

						if (requestParams.variableExists(Field.BUNDLE_ID)) {
							bundleId = requestParams.getString(Field.BUNDLE_ID);
						}

						if (requestParams.variableExists(Field.NAME)) {
							name = requestParams.getString(Field.NAME);
						}

						if (requestParams.variableExists(Field.GOOGLE_API_KEY)) {
							googleApiKey = requestParams.getString(Field.GOOGLE_API_KEY);
						}

						if (requestParams.variableExists(Field.FILE) && requestParams.variableExists(Field.FILE_NAME)) {
							byte[] fileData = requestParams.getRaw(Field.FILE);
							fileName = requestParams.getString(Field.FILE_NAME);
							try {
								String filePath = FileSystemUtils.createPathFrom(
										FileSystemUtils.getBasePathForClass(this.getClass()), "resources", fileName);

								File file = new File(filePath);
								FileOutputStream fileWriter = new FileOutputStream(file);
								fileWriter.write(fileData);

								getLogger().debug("save file {} successful", filePath);
							} catch (Exception ex) {
								getLogger().error("saved file get error", ex);
							}
						}

						if (!bundleId.equals("") && !name.equals("")) {
							PuObject puo = new PuObject();
							puo.setString(Field.COMMAND, Command.CREATE_APPLICATION);
							PuObject data = new PuObject();
							data.setString(Field.KEY, googleApiKey);
							data.setString(Field.BUNDLE_ID, bundleId);
							data.setString(Field.NAME, name);
							data.setString(Field.FILE_PATH, fileName);
							data.setString(Field.PASSWORD, password);
							puo.setPuObject(Field.DATA, data);
							PuObject response = (PuObject) getApi().call(MARIO_HERMES, puo);
							getLogger().debug("response: {}", response);
							if (response != null) {
								status = response.getInteger(Field.STATUS);
								result = response.getPuObject(Field.DATA);
							} else {
								result = "create application get error";
							}
						}

						break;
					}
					case Command.UPDATE_CERTIFICATE_FILE: {
						String password = "";
						if (requestParams.variableExists(Field.PASSWORD)) {
							password = requestParams.getString(Field.PASSWORD);
						}
						if (requestParams.variableExists(Field.FILE) && requestParams.variableExists(Field.FILE_NAME)) {
							byte[] fileData = requestParams.getRaw(Field.FILE);
							String fileName = requestParams.getString(Field.FILE_NAME);
							try {
								String filePath = FileSystemUtils.createPathFrom(
										FileSystemUtils.getBasePathForClass(this.getClass()), "resources", fileName);

								File file = new File(filePath);
								FileOutputStream fileWriter = new FileOutputStream(file);
								fileWriter.write(fileData);
							} catch (Exception ex) {
								getLogger().error("saved file get error", ex);
							}

							PuObject puo = new PuObject();
							puo.setString(Field.COMMAND, Command.UDPATE_APPLICATION_CONFIG);
							PuObject data = new PuObject();
							data.setString(Field.FILE_PATH, fileName);
							data.setString(Field.PASSWORD, password);
							data.setString(Field.BUNDLE_ID, requestParams.getString(Field.BUNDLE_ID));
							data.setString(Field.SECRET_KEY, requestParams.getString(Field.SECRET_KEY));
							puo.setPuObject(Field.DATA, data);
							getLogger().debug("call mario hermes plugin: {}", puo);
							PuObject resp = (PuObject) getApi().call(MARIO_HERMES, puo);
							getLogger().debug("response: {}", resp);
							if (resp != null) {
								status = resp.getInteger(Field.STATUS);
								result = resp.getString(Field.DATA);
							} else {
								result = "update certificate file";
							}
						} else {
							result = "missing parameters file and fileName";
						}
						break;
					}
					default:
						break;
					}
				}
			} catch (Exception ex) {
				result = ex;
			} finally {

			}
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
}

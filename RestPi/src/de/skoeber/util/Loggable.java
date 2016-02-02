package de.skoeber.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Loggable {

	private final Logger logger = LogManager.getLogger(getClass());
	
	public void logError(String msg, Throwable cause) {
		logger.error(msg, cause);
	}
	
	public void logError(String msg) {
		logger.error(msg);
	}
	
	public void logWarn(String msg) {
		logger.warn(msg);
	}
	
	public void logInfo(String msg) {
		logger.info(msg);
	}
	
	public void logDebug(String msg) {
		logger.debug(msg);
	}
}

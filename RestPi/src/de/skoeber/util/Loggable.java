package de.skoeber.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Loggable {

	private final Logger logger = Logger.getLogger(getClass().getCanonicalName());
	private final DateFormat df = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss.S");
	
	public void logError(String msg, Throwable cause) {
		logger.log(Level.SEVERE, formatMsg(msg), cause);
	}
	
	public void logError(String msg) {
		logger.log(Level.SEVERE, formatMsg(msg));
	}
	
	public void logWarn(String msg) {
		logger.log(Level.WARNING, formatMsg(msg));
	}
	
	public void logInfo(String msg) {
		logger.log(Level.INFO, formatMsg(msg));
	}
	
	public void logDebug(String msg) {
		logger.log(Level.FINE, formatMsg(msg));
	}
	
	private String formatMsg(String msg) {
		return df.format(new Date()) + " " +  msg;
	}
}

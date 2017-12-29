package de.skoeber;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.skoeber.environment.ConfigurationEnvironment;
import de.skoeber.environment.GpioEnvironment;
import de.skoeber.environment.HttpEnvironment;

/**
 * 
 * @author skoeber
 *
 */
public class RestPiServer {
	
	static {
		System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
		System.setProperty("pi4j.linking", "dynamic");
	}
	
	private static final Logger logger = LogManager.getLogger(RestPiServer.class.getCanonicalName());

	/**
	 * Start the RestPi server
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			synchronized (logger) {
				// Reading configuration
				ConfigurationEnvironment.getInstance();
				
				// Initialising GPIO interface
				GpioEnvironment.getInstance();
				
				// Starting RestPiServer
				HttpEnvironment.getInstance().startServer();
			}
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}

}

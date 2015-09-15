package de.skoeber;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.skoeber.environment.ConfigurationEnvironment;
import de.skoeber.environment.GpioEnvironment;
import de.skoeber.environment.HttpEnvironment;
import de.skoeber.util.Loggable;

/**
 * 
 * @author skoeber
 *
 */
public class RestPiServer extends Loggable {
	
	private static final Logger logger = Logger.getLogger(RestPiServer.class.getCanonicalName());

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
			logger.log(Level.ALL, e.getMessage(), e);
		}
		
	}

}

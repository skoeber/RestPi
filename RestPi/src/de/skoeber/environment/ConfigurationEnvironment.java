package de.skoeber.environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pi4j.io.gpio.PinDirection;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;

import de.skoeber.util.Loggable;

/**
 * Holds and returns of the restpi configuration.
 * @author skoeber
 *
 */
public class ConfigurationEnvironment extends Loggable {
	
	private static ConfigurationEnvironment INSTANCE;
	private final Properties properties = new Properties();
	
	private String propFilePath = "conf" + File.separator + "restpi.properties";
	
	// TODO finalize pattern
	private Pattern pinPattern = Pattern.compile("^gpio\\.pin\\.([0-9]{1,2})\\..*");
	
	private ConfigurationEnvironment() {
		logInfo("Reading configuration");
		
		String currentPath = new File("").getAbsolutePath();
		String absolutePath = currentPath.concat(File.separator).concat(propFilePath);
		
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		
		try {
			fis = new FileInputStream(absolutePath);
		} catch (FileNotFoundException e) {
			logError("Properties file not found: " + absolutePath, e);
			return;
		}
		
		try {
			bis = new BufferedInputStream(fis);
			properties.load(bis);
			bis.close();
		} catch (IOException e) {
			logError("Error reading the properties file: " + absolutePath, e);
			return;
		}
	}

	/**
	 * 
	 * @return INSTANCE
	 */
	public static ConfigurationEnvironment getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new ConfigurationEnvironment();
		}
		return INSTANCE;
	}
	
	/**
	 * Returns the configuration value for the given key.
	 * @param key String
	 * @return value String
	 */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	public Set<Integer> getConfiguredPins() {
		Set<Integer> pins = new HashSet<Integer>();
		for(Object key : properties.keySet()) {
			String keyString = (String) key;
			Matcher keyMatcher = pinPattern.matcher(keyString);
			if(keyMatcher.matches()) {
				int pin = Integer.parseInt(keyMatcher.group(1));
				pins.add(pin);
			}
		}
		return pins;
	}
	
	/**
	 * Read configuration for pin direction
	 * @param pin
	 * @return PinDirection
	 */
	public PinDirection getPinDirection(int pin) {
		String key = "gpio.pin." + pin + ".mode.direction";
		String value = properties.getProperty(key);
		PinDirection pd = PinDirection.IN; // default
		try {
			pd = PinDirection.valueOf(value);
		} catch (IllegalArgumentException ex) {
			logError("Invalid PinDirection configuration: \"" + key + ":" + value + "\" Using default value \"in\".");
		}
		return pd;
	}
	
	/**
	 * Read configuration for pin mode
	 * @param pin
	 * @return PinMode
	 */
	public PinMode getPinMode(int pin) {
		String key = "gpio.pin." + pin + ".mode";
		String mode = properties.getProperty(key);
		
		key = "gpio.pin." + pin + ".mode.direction";
		String direction = properties.getProperty(key);
		
		PinMode pm = PinMode.DIGITAL_INPUT; // default
		
		switch(mode) {
			case "digital":
				switch(direction) {
					case "input":
						pm = PinMode.DIGITAL_INPUT;
						break;
					case "output":
						pm = PinMode.DIGITAL_OUTPUT;
						break;
				}
				break;
				
			case "analog":
				switch(direction) {
					case "input":
						pm = PinMode.ANALOG_INPUT;
						break;
					case "output":
						pm = PinMode.ANALOG_OUTPUT;
						break;
				}
				break;
				
			case "pwm":
				pm = PinMode.PWM_OUTPUT;
				break;
		}
		
		return pm;
	}
	
	/**
	 * Read configuration for pin state
	 * @param pin
	 * @return PinState
	 */
	public PinState getPinState(int pin) {
		String key = "gpio.pin." + pin + ".state";
		String value = properties.getProperty(key);
		int state = 0; // default = LOW
		PinState ps = PinState.getState(state);
		
		try {
			state = Integer.valueOf(value);
		} catch(NumberFormatException ex) {
			logError("Invalid PinState configuration: Only numeric values 0 or 1 are allowed.");
		}
		
		try {
			return PinState.getState(state);
		} catch (IllegalArgumentException ex) {
			logError("Invalid PinState configuration: \"" + key + ":" + value + "\" Using default \"low\".");
		}
		return ps;
	}
	
	/**
	 * Read configuration for pin name
	 * @param pin
	 * @return name String
	 */
	public String getPinName(int pin) {
		String key = "gpio.pin." + pin + ".name";
		String value = properties.getProperty(key);
		return value.isEmpty() ? "Pin" + pin : value;
	}
}

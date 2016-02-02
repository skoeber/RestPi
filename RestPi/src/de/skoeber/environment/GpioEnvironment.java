package de.skoeber.environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinAnalog;
import com.pi4j.io.gpio.GpioPinAnalogOutput;
import com.pi4j.io.gpio.GpioPinDigital;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import de.skoeber.util.Loggable;

/**
 * This class sets up and manages the gpio configuration.
 * @author skoeber
 *
 */
public class GpioEnvironment extends Loggable {
	
	private static GpioEnvironment INSTANCE;
	private final GpioController gpio;
	
	private Map<Integer, GpioPin> pins = new HashMap<Integer, GpioPin>();

	private GpioEnvironment() throws UnsatisfiedLinkError {
		logInfo("Initialising GPIO interface");
		
		gpio = GpioFactory.getInstance();
		
		ConfigurationEnvironment config = ConfigurationEnvironment.getInstance();
		
		for(int pinId : config.getConfiguredPins()) {
			logInfo("Setting up pin " + pinId);
			
			PinMode mode = config.getPinMode(pinId);
			String name = config.getPinName(pinId);
			
			GpioPin gpioPin = null;
			Pin pin = RaspiPin.getPinByName("GPIO " + pinId);
			
			switch(mode) {
				case DIGITAL_INPUT:
					logInfo(" as digital input");
					gpioPin = gpio.provisionDigitalInputPin(pin, name);
					break;
					
				case DIGITAL_OUTPUT:
					logInfo(" as digital output");
					PinState defaultState = config.getPinState(pinId);
					gpioPin = gpio.provisionDigitalOutputPin(pin, name, defaultState);
					break;
					
				case ANALOG_INPUT:
					logInfo(" as analog input");
					gpioPin = gpio.provisionAnalogInputPin(pin, name);
					break;
					
				case ANALOG_OUTPUT:
					logInfo(" as analog output");
					gpioPin = gpio.provisionAnalogOutputPin(pin, name);
					break;
					
				case PWM_OUTPUT:
					logInfo(" as pwm output");
					gpioPin = gpio.provisionPwmOutputPin(pin, name);
					break;
					
				default:
					logError("Pin " + pinId + " seems to be configured not properly.");
			}
			
			pins.put(pinId, gpioPin);
		}
	}
	
	public static GpioEnvironment getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new GpioEnvironment();
		}
		return INSTANCE;
	}
	
	public List<GpioPin> getPins() {
		return new ArrayList<>(pins.values());
	}
	
	/**
	 * Shutdown the gpio
	 */
	public void shutdown() {
		gpio.shutdown();
		INSTANCE = null;
	}
	
	/**
	 * Returns the pin object
	 * @param number
	 * @return <code>GpioPin</code>
	 */
	public GpioPin getPin(int number) {
		return pins.get(number);
	}
	
	/**
	 * Returns the actual mode of the pin
	 * @param number of the pin
	 * @return <code>PinMode</code> or null if pin does not exist
	 */
	public PinMode getMode(int number) {
		GpioPin pin = pins.get(number);
		if(pin != null) {
			return gpio.getMode(pin);
		}
		return null;
	}
	
	/**
	 * Sets the state of the digital output pin
	 * @param number of the pin
	 * @param state (0=LOW or 1=HIGH)
	 */
	public void setState(int number, int state) {
		GpioPin pin = pins.get(number);
		if(pin != null) {
			if(pin instanceof GpioPinDigitalInput) {
				if(state == 0) {
					((GpioPinDigitalOutput) pin).setState(PinState.LOW);
				} else if(state == 1) {
					((GpioPinDigitalOutput) pin).setState(PinState.HIGH);
				}
			}
		}
	}
	
	/**
	 * Returns the actual state (input or output) of the digital pin
	 * @param number of the pin
	 * @return <code>PinState</code> or null if the pin does not exist or is not a digital one
	 */
	public PinState getState(int number) {
		GpioPin pin = pins.get(number);
		return getState(pin);
	}
	
	/**
	 * Returns the actual state (input or output) of the digital pin
	 * @param pin GpioPin
	 * @return <code>PinState</code> or null if the pin does not exist or is not a digital one
	 */
	public PinState getState(GpioPin pin) {
		if(pin != null) {
			if(pin instanceof GpioPinDigital) {
				return gpio.getState((GpioPinDigital) pin);
			}
		}
		return null;
	}
	
	/**
	 * Sets the value of the analog output pin
	 * @param number of the pin
	 * @param value as double
	 */
	public void setValue(int number, double value) {
		GpioPin pin = pins.get(number);
		if(pin != null) {
			if(pin instanceof GpioPinAnalogOutput) {
				((GpioPinAnalogOutput) pin).setValue(value);
			}
		}
	}
	
	/**
	 * Returns the actual value of the analog input pin
	 * @param number of the pin
	 * @return value as <code>Double</code> or null if the pin does not exist or is not a analog
	 */
	public Double getValue(int number) {
		GpioPin pin = pins.get(number);
		if(pin != null) {
			if(pin instanceof GpioPinAnalog) {
				return new Double(gpio.getValue((GpioPinAnalog) pin));
			}
		}
		return null;
	}
	
}

package de.skoeber.resources.responses;

import javax.ws.rs.core.Response.Status;

/**
 * Error messages for exceptions
 * 
 * @author skoeber
 *
 */
public enum Error {
	
	SERVER_ERROR			("Server error", Status.INTERNAL_SERVER_ERROR),
	MISSING_PARAMETER		("Missing parameters", Status.BAD_REQUEST),
	PIN_NOT_FOUND			("The pin is not initialized", Status.NOT_FOUND),
	PIN_NO_DIGITAL			("Operation not possible: The pin is not configured as digital input or output", Status.BAD_REQUEST),
	PIN_NO_ANALOG			("Operation not possible: The pin is not configured as analog input or output", Status.BAD_REQUEST),
	PIN_NO_DIGITAL_OUTPUT	("Operation not possible: The pin is not configured as digital output", Status.BAD_REQUEST),
	PIN_NO_ANALOG_OUTPUT	("Operation not possible: The pin is not configured as analog output", Status.BAD_REQUEST),
	PIN_NO_DIGITAL_INPUT	("Operation not possible: The pin is not configured as digital input", Status.BAD_REQUEST),
	PIN_NO_ANALOG_INPUT		("Operation not possible: The pin is not configured as analog input", Status.BAD_REQUEST);
	
	private final Status status;
	private final String message;
	
	Error(String message) {
		this(message, Status.BAD_REQUEST);
	}
	
	Error(String message, Status status) {
		this.message = message;
		this.status = status;
	}
	
	public String message() {
		return message;
	}
	
	public Status status() {
		return status;
	}
	
	@Override
	public String toString() {
		return message;
	}
}

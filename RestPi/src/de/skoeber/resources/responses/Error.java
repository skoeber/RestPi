package de.skoeber.resources.responses;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Error messages for responses
 * 
 * @author skoeber
 *
 */
public enum Error {
	
	SERVER_ERROR			("Server error"),
	MISSING_PARAMETER		("Missing parameters"),
	PIN_NOT_FOUND			("The pin is not initialized"),
	PIN_NO_DIGITAL			("Operation not possible: The pin is not configured as digital input or output"),
	PIN_NO_ANALOG			("Operation not possible: The pin is not configured as analog input or output"),
	PIN_NO_DIGITAL_OUTPUT	("Operation not possible: The pin is not configured as digital output"),
	PIN_NO_ANALOG_OUTPUT	("Operation not possible: The pin is not configured as analog output"),
	PIN_NO_DIGITAL_INPUT	("Operation not possible: The pin is not configured as digital input"),
	PIN_NO_ANALOG_INPUT		("Operation not possible: The pin is not configured as analog input");
	
	private static final String ERROR = "ERROR: ";
	private static final String MESSAGE = "MESSAGE: ";
	
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
	
	public Response response() {
		return response(null);
	}
	
	public Response response(String additionalMessage) {
		String msg = this.message;
		if(additionalMessage != null && !additionalMessage.isEmpty()) {
			msg = ERROR + msg + "\n" + MESSAGE + additionalMessage;
		}
		
		return Response.status(status).entity(msg).build();
	}
	
	@Override
	public String toString() {
		return message;
	}
}

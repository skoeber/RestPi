package de.skoeber.resources.exceptions;

import javax.ws.rs.core.Response.Status;

import de.skoeber.resources.responses.Error;

public class RestPiException extends Exception {

	private static final long serialVersionUID = 1262440557677888042L;
	
	private static final String ERROR = "ERROR: ";
	private static final String MESSAGE = "MESSAGE: ";
	
	private final Error error;
	private final String additionalMessage;
	
	public RestPiException(Error error) {
		this(error, "");
	}
	
	public RestPiException(Error error, String additionalMessage) {
		this.error = error;
		this.additionalMessage = additionalMessage;
	}

	@Override
	public String getMessage() {
		String msg = error.message();
		if(additionalMessage != null && !additionalMessage.isEmpty()) {
			msg = ERROR + msg + "\n" + MESSAGE + additionalMessage;
		}
		
		return msg;
	}
	
	public Status getStatus() {
		return error.status();
	}
}

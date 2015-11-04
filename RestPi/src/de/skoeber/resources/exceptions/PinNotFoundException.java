package de.skoeber.resources.exceptions;

import de.skoeber.resources.responses.Error;

public class PinNotFoundException extends GpioException {

	private static final long serialVersionUID = -4682810712558822495L;
	
	public PinNotFoundException(Error error) {
		super(error);
	}
	
}

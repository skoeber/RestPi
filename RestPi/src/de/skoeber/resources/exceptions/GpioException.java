package de.skoeber.resources.exceptions;

import de.skoeber.resources.responses.Error;

public class GpioException extends RestPiException {

	private static final long serialVersionUID = 1893239394418680206L;
	
	public GpioException(Error error) {
		super(error);
	}

}

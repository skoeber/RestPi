package de.skoeber.environment.provider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import de.skoeber.resources.exceptions.PinNotFoundException;

@Provider
public class PinNotFoundExceptionMapper implements ExceptionMapper<PinNotFoundException> {

	@Override
	public Response toResponse(PinNotFoundException exception) {
		return Response.status(exception.getStatus()).entity(exception.getMessage()).type(MediaType.TEXT_PLAIN_TYPE).build();
	}

}

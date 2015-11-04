package de.skoeber.environment.provider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import de.skoeber.resources.exceptions.GpioException;

public class GpioExceptionMapper implements ExceptionMapper<GpioException> {

	@Override
	public Response toResponse(GpioException exception) {
		return Response.status(exception.getStatus()).entity(exception.getMessage()).type(MediaType.TEXT_PLAIN_TYPE).build();
	}

}

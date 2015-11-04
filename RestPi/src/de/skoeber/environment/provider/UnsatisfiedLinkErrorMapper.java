package de.skoeber.environment.provider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

public class UnsatisfiedLinkErrorMapper implements ExceptionMapper<UnsatisfiedLinkError> {

	@Override
	public Response toResponse(UnsatisfiedLinkError exception) {
		// TODO better message
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage()).type(MediaType.TEXT_PLAIN_TYPE).build();
	}

}

package de.skoeber.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.skoeber.resources.responses.Error;

/**
 * Simple resource to check the heartbeat
 * @author skoeber
 *
 */
@Path("/")
public class PingResource {
	
	private static final String answer = "Server is up and running";

	@GET
	@Path("ping")
	@Produces({MediaType.TEXT_PLAIN})
	public Response ping() {
		return Response.ok(answer).build();
	}
	
	@GET
	@Path("echo")
	@Produces({MediaType.TEXT_PLAIN})
	public Response echo(@QueryParam("message") final String message) {
		
		if(message == null || message.isEmpty()) {
			return Error.MISSING_PARAMETER.response("message");
		}
		
		return Response.ok(message).build();
	}
	
}

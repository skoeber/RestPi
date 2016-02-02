package de.skoeber.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.skoeber.environment.HttpEnvironment;
import de.skoeber.util.Loggable;

@Path("/admin")
public class AdminResource extends Loggable {

	@GET
	@Path("/shutdown")
	@Produces({MediaType.TEXT_PLAIN})
	public Response shutdown() {
		try {
			logInfo("Received shutdown request.");
			return Response.ok("The server is shutting down now.").build();
		} finally {
			HttpEnvironment.getInstance().stopServer();
		}
	}
}

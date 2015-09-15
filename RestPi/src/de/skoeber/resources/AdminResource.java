package de.skoeber.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.skoeber.util.Loggable;

@Path("/admin")
public class AdminResource extends Loggable {

	@GET
	@Path("/shutdown")
	@Produces({MediaType.TEXT_PLAIN})
	public boolean shutdown() {
		logInfo("Received shutdown request.");
		System.exit(0);
		
		return true;
	}
}

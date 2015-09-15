package de.skoeber.resources;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * RestPi documentation
 * 
 * @author skoeber
 *
 */
@Path("/{parameter: doc|docu|documentation}")
public class DocuResource {
	
	@GET
	@Produces({MediaType.TEXT_XML})
	public Response getDocumentation() {
		// TODO integration help site
		return getWadl();
	}

	@GET
	@Path("wadl")
	@Produces({MediaType.TEXT_XML})
	public Response getWadl() {
		return Response.seeOther(URI.create("/application.wadl")).build();
	}
}

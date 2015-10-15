package de.skoeber.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;

import de.skoeber.environment.GpioEnvironment;
import de.skoeber.resources.responses.Error;

@Path("/gpio")
public class GpioResource {
	
	@GET
	@Path("/pins")
	@Produces({MediaType.TEXT_PLAIN})
	public Response listConfiguration() {
		List<GpioPin> pins = GpioEnvironment.getInstance().getPins();
		StringBuilder sb = new StringBuilder();
		for(GpioPin pin : pins) {
			String name = pin.getName();
			PinMode mode = pin.getMode();
			sb.append(name).append(" - ").append(mode.getName());
		}
		
		return Response.ok(sb.toString()).build();
	}

	@GET
	@Path("/pin/{n}/state")
	@Produces({MediaType.TEXT_PLAIN})
	public Response getState(@PathParam("n") int pinId) {
		if(!pinExists(pinId)) {
			return Error.PIN_NOT_FOUND.response();
		}
		
		PinState state = GpioEnvironment.getInstance().getState(pinId);
		
		if(state == null) {
			return Error.PIN_NO_DIGITAL.response();
		}
		
		return Response.ok(state == PinState.HIGH ? 1 : 0).build();
	}
	
	@PUT
	@Path("/pin/{n}/state")
	@Produces({MediaType.TEXT_PLAIN})
	public Response setState(@PathParam("n") int pinId, @QueryParam("state") int state) {
		if(!pinExists(pinId)) {
			return Error.PIN_NOT_FOUND.response();
		}
		
		if(GpioEnvironment.getInstance().getState(pinId) == null) {
			return Error.PIN_NO_DIGITAL.response();
		}
		
		GpioEnvironment.getInstance().setState(pinId, state);
		
		return Response.ok("OK").build();
	}
	
	@PUT
	@Path("/pin/{n}/switch")
	@Produces({MediaType.TEXT_PLAIN})
	public Response switchState(@PathParam("n") int pinId) {
		if(!pinExists(pinId)) {
			return Error.PIN_NOT_FOUND.response();
		}
		
		PinState state = GpioEnvironment.getInstance().getState(pinId);
		
		if(state == null) {
			return Error.PIN_NO_DIGITAL.response();
		}
		
		state = PinState.getInverseState(state);
		GpioEnvironment.getInstance().setState(pinId, state.getValue());
		
		return Response.ok("OK").build();
	}
	
	@GET
	@Path("/pin/{n}/value")
	@Produces({MediaType.TEXT_PLAIN})
	public Response getValue(@PathParam("n") int pinId) {
		if(!pinExists(pinId)) {
			return Error.PIN_NOT_FOUND.response();
		}
		
		if(!(PinMode.allAnalog().contains(GpioEnvironment.getInstance().getMode(pinId)))) {
			return Error.PIN_NO_ANALOG.response();
		}
		
		Double value = GpioEnvironment.getInstance().getValue(pinId);
		
		return Response.ok(value).build();
	}
	
	@PUT
	@Path("/pin/{n}/value")
	@Produces({MediaType.TEXT_PLAIN})
	public Response getValue(@PathParam("n") int pinId, @QueryParam("value") double value) {
		if(!pinExists(pinId)) {
			return Error.PIN_NOT_FOUND.response();
		}
		
		if(!(PinMode.ANALOG_INPUT.equals(GpioEnvironment.getInstance().getMode(pinId)))) {
			return Error.PIN_NO_ANALOG.response();
		}
		
		GpioEnvironment.getInstance().setValue(pinId, value);
		
		return Response.ok("OK").build();
	}
	
	private boolean pinExists(int pinId) {
		if(GpioEnvironment.getInstance().getPin(pinId) == null) {
			return false;
		}
		
		return true;
	}
}

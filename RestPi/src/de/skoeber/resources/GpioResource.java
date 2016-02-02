package de.skoeber.resources;

import java.util.ArrayList;
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
import de.skoeber.gpio.RestPiPin;
import de.skoeber.resources.exceptions.GpioException;
import de.skoeber.resources.exceptions.PinNotFoundException;
import de.skoeber.resources.exceptions.RestPiException;
import de.skoeber.resources.responses.Error;
import de.skoeber.util.Loggable;

@Path("/gpio")
public class GpioResource extends Loggable {
	
	@GET
	@Path("/pins")
	@Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
	public Response listConfiguration() {
		List<GpioPin> pins = GpioEnvironment.getInstance().getPins();
		List<RestPiPin> cPins = new ArrayList<>();
		for(GpioPin pin : pins) {
			String name = pin.getName();
			PinMode mode = pin.getMode();
			PinState state = GpioEnvironment.getInstance().getState(pin);
			RestPiPin cPin = new RestPiPin(name, mode.getName(), state.getValue());
			cPins.add(cPin);
		}
		
		return Response.ok(cPins).build();
	}

	@GET
	@Path("/pin/{n}/state")
	@Produces({MediaType.TEXT_PLAIN})
	public Response getState(@PathParam("n") int pinId) throws RestPiException {
		if(!pinExists(pinId)) {
			throw new PinNotFoundException(Error.PIN_NOT_FOUND);
		}
		
		PinState state = GpioEnvironment.getInstance().getState(pinId);
		
		if(state == null) {
			throw new GpioException(Error.PIN_NO_DIGITAL);
		}
		
		return Response.ok(state == PinState.HIGH ? 1 : 0).build();
	}
	
	@PUT
	@Path("/pin/{n}/state")
	@Produces({MediaType.TEXT_PLAIN})
	public Response setState(@PathParam("n") int pinId, @QueryParam("state") int state) throws RestPiException {
		if(!pinExists(pinId)) {
			throw new PinNotFoundException(Error.PIN_NOT_FOUND);
		}
		
		if(GpioEnvironment.getInstance().getState(pinId) == null) {
			throw new GpioException(Error.PIN_NO_DIGITAL);
		}
		
		GpioEnvironment.getInstance().setState(pinId, state);
		
		return Response.ok("OK").build();
	}
	
	@PUT
	@Path("/pin/{n}/switch")
	@Produces({MediaType.TEXT_PLAIN})
	public Response switchState(@PathParam("n") int pinId) throws RestPiException {
		if(!pinExists(pinId)) {
			throw new PinNotFoundException(Error.PIN_NOT_FOUND);
		}
		
		PinState state = GpioEnvironment.getInstance().getState(pinId);
		
		if(state == null) {
			throw new GpioException(Error.PIN_NO_DIGITAL);
		}
		
		state = PinState.getInverseState(state);
		GpioEnvironment.getInstance().setState(pinId, state.getValue());
		
		return Response.ok("OK").build();
	}
	
	@PUT
	@Path("/pin/{n}/interval")
	@Produces({MediaType.TEXT_PLAIN})
	public Response interval(@PathParam("n") int pinId, @QueryParam("frequency") int milliseconds, @QueryParam("repeats") int repeats) throws RestPiException {
		if(!pinExists(pinId)) {
			throw new PinNotFoundException(Error.PIN_NOT_FOUND);
		}
		
		PinState state = GpioEnvironment.getInstance().getState(pinId);
		
		if(state == null) {
			throw new GpioException(Error.PIN_NO_DIGITAL);
		}
		
		for(int i = 0; i < repeats; i++) {
			state = PinState.getInverseState(state);
			GpioEnvironment.getInstance().setState(pinId, state.getValue());
			try {
				Thread.sleep(milliseconds);
			} catch (InterruptedException e) {
				logError("Interval execution interrupted", e);
			}
		}
		
		return Response.ok("OK").build();
	}
	
	@GET
	@Path("/pin/{n}/value")
	@Produces({MediaType.TEXT_PLAIN})
	public Response getValue(@PathParam("n") int pinId) throws RestPiException {
		if(!pinExists(pinId)) {
			throw new PinNotFoundException(Error.PIN_NOT_FOUND);
		}
		
		if(!(PinMode.allAnalog().contains(GpioEnvironment.getInstance().getMode(pinId)))) {
			throw new GpioException(Error.PIN_NO_ANALOG);
		}
		
		Double value = GpioEnvironment.getInstance().getValue(pinId);
		
		return Response.ok(value).build();
	}
	
	@PUT
	@Path("/pin/{n}/value")
	@Produces({MediaType.TEXT_PLAIN})
	public Response getValue(@PathParam("n") int pinId, @QueryParam("value") double value) throws RestPiException {
		if(!pinExists(pinId)) {
			throw new PinNotFoundException(Error.PIN_NOT_FOUND);
		}
		
		if(!(PinMode.ANALOG_INPUT.equals(GpioEnvironment.getInstance().getMode(pinId)))) {
			throw new GpioException(Error.PIN_NO_ANALOG);
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

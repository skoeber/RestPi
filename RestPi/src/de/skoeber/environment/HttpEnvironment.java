package de.skoeber.environment;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.GrizzlyFuture;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import de.skoeber.environment.provider.GpioExceptionMapper;
import de.skoeber.environment.provider.PinNotFoundExceptionMapper;
import de.skoeber.environment.provider.RestPiExceptionMapper;
import de.skoeber.environment.provider.UnsatisfiedLinkErrorMapper;
import de.skoeber.util.Loggable;

/**
 * This class sets up and manages the http server.
 * @author skoeber
 *
 */
public class HttpEnvironment extends Loggable {
	
	// http server configuration
	private static final int port = 8080;
	private static final String host = "https://0.0.0.0/";
	
	private static HttpEnvironment INSTANCE;
	private static HttpServer webServer;
	
	private static final Object MUTEX = new Object();

	private HttpEnvironment() {
		logInfo("Starting RestPiServer");
		
		URI uri = UriBuilder.fromUri(host).port(port).build();
		ResourceConfig rc = new ResourceConfig();
		
		// register exception mapper
		rc.register(RestPiExceptionMapper.class);
		rc.register(GpioExceptionMapper.class);
		rc.register(PinNotFoundExceptionMapper.class);
		rc.register(UnsatisfiedLinkErrorMapper.class);
		
		// register media type support
		rc.register(JacksonFeature.class);
		
		// register ressource package
		rc.packages("de.skoeber.resources");
		
		webServer = GrizzlyHttpServerFactory.createHttpServer(uri, rc, false);
	}
	
	public static HttpEnvironment getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new HttpEnvironment();
		}
		return INSTANCE;
	}
	
	/**
	 * Start the http server
	 */
	public void startServer() {
		try {
			Runtime.getRuntime().addShutdownHook(new ShutdownHandler());
			webServer.start();
			Thread.currentThread().join();
		} catch (IOException | InterruptedException e) {
			logError(e.getMessage());
		}
	}
	
	/**
	 * Stop the http server
	 */
	public void stopServer() {
		if(webServer.isStarted()) {
			logInfo("The server is shutting down now");
			
			try {
				GpioEnvironment.getInstance().shutdown();
			} catch(UnsatisfiedLinkError e) {
				logError("GPIO was not running and thus cannot be shutdown properly.");
			}
			
			GrizzlyFuture<HttpServer> future = webServer.shutdown(10, TimeUnit.SECONDS);
			try {
				future.get();
			} catch (InterruptedException | ExecutionException e) {
				logError("Error on shutdown", e.getCause());
			} finally {
				logInfo("Server stopped");
				System.exit(0);
			}
		}
	}
	
	private static class ShutdownHandler extends Thread {

	    @Override
	    public void run() {
	        synchronized (MUTEX) {
	            HttpEnvironment.getInstance().stopServer();
	        }
	    }
	}
	
}
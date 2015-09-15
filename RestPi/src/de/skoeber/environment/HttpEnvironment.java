package de.skoeber.environment;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.GrizzlyFuture;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

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
			
			synchronized (MUTEX) {
	            try {
	                MUTEX.wait();
	            } catch (InterruptedException e) {
	                //
	            }
	        }
			
			//System.in.read();
			stopServer();
		} catch (IOException e) {
			logError(e.getMessage());
		}
	}
	
	/**
	 * Stop the http server
	 */
	public void stopServer() {
		if(webServer.isStarted()) {
			logInfo("The server is shutting down now");
			
			GpioEnvironment.getInstance().shutdown();
			GrizzlyFuture<HttpServer> future = webServer.shutdown();
			try {
				future.get(30, TimeUnit.SECONDS);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				logError("Error on shutdown", e.getCause());
			}
			
			logInfo("Server stopped");
		}
	}
	
	private static class ShutdownHandler extends Thread {

	    @Override
	    public void run() {
	        synchronized (MUTEX) {
	            MUTEX.notifyAll();
	        }
	    }
	}
	
}
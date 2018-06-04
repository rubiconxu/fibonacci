package com.rubicon.coding.fibonacci;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Application entrance
 *
 * This application uses Jersey and Java built-in HTTP server to provide RESTful web service for
 * simplicity.
 *
 * Typically, spring-boot is used as RESTful service solution.
 *
 * @author xulu 2018-06-03
 */
public class App {
  private static Logger logger = LogManager.getLogger(App.class);

  private static final String HOST = "http://localhost/";
  private static final int PORT = 8088;

  /**
   * configure and start RESTful server
   */
  public static void main(String[] args) {
    URI baseUri = UriBuilder.fromUri(HOST).port(PORT).build();
    ResourceConfig config = new ResourceConfig(FibonacciService.class);
    JdkHttpServerFactory.createHttpServer(baseUri, config);

    logger.info("Server started.");
  }
}

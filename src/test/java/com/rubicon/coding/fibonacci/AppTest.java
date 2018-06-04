package com.rubicon.coding.fibonacci;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.net.httpserver.HttpServer;

/**
 * Unit test for Fibonacci sequence service.
 */
public class AppTest {
  private static Logger logger = LogManager.getLogger(AppTest.class);

  /**
   * RESTful server
   */
  private static HttpServer server;

  /**
   * start RESTful service
   */
  @BeforeClass
  public static void startFibonacciService() {
    URI hostUri = UriBuilder.fromUri("http://localhost/").port(8088).build();
    ResourceConfig config = new ResourceConfig(FibonacciService.class);
    server = JdkHttpServerFactory.createHttpServer(hostUri, config);
  }

  /**
   * shutdown RESTful service
   */
  @AfterClass
  public static void shutDownFibonacciService() {
    server.stop(0);
  }

  /**
   * test by calling target method directly
   */
  @Test
  public void testFibonacci() {
    FibonacciService fiboService = new FibonacciService();

    assertEquals("[0]", fiboService.getFirstNFibonacci("1"));
    assertEquals("[0, 1]", fiboService.getFirstNFibonacci("2"));
    assertEquals("[0, 1, 1]", fiboService.getFirstNFibonacci("3"));
    assertTrue(fiboService.getFirstNFibonacci("100").endsWith(", 218922995834555169026]"));
    assertTrue(fiboService.getFirstNFibonacci("10000").endsWith("50294603536651238230626]"));

    assertTrue(fiboService.getFirstNFibonacci("abc").startsWith("error"));
    assertTrue(fiboService.getFirstNFibonacci("-").startsWith("error"));
    assertTrue(fiboService.getFirstNFibonacci("-12").startsWith("error"));
    assertTrue(fiboService.getFirstNFibonacci("0").startsWith("error"));
    assertTrue(fiboService.getFirstNFibonacci("10001").startsWith("error"));
  }

  /**
   * test by calling RESTful API
   */
  @Test
  public void testFibonacciRestApi() {
    String baseUri = "http://localhost:8088/v1/fibonacci/";

    assertEquals("[0]", callRestApi(baseUri + "1"));
    assertEquals("[0, 1]", callRestApi(baseUri + "2"));
    assertEquals("[0, 1, 1]", callRestApi(baseUri + "3"));
    assertTrue(callRestApi(baseUri + "100").endsWith(", 218922995834555169026]"));
    assertTrue(callRestApi(baseUri + "10000").endsWith("50294603536651238230626]"));

    assertTrue(callRestApi(baseUri + "abc").startsWith("error"));
    assertTrue(callRestApi(baseUri + "-").startsWith("error"));
    assertTrue(callRestApi(baseUri + "-12").startsWith("error"));
    assertTrue(callRestApi(baseUri + "0").startsWith("error"));
    assertTrue(callRestApi(baseUri + "10001").startsWith("error"));
  }

  /**
   * helper method to call RESTful API and get response
   *
   * @param endpoint
   *          RESTful API endpoint
   * @return RESTful API response
   */
  private String callRestApi(String endpoint) {
    StringBuilder response = new StringBuilder();

    // response stream reader
    BufferedReader bufin = null;
    try {
      URL url = new URL(endpoint);
      HttpURLConnection connction = (HttpURLConnection) url.openConnection();
      assertEquals(200, connction.getResponseCode());
      bufin = new BufferedReader(new InputStreamReader(connction.getInputStream()));
      String line;
      while ((line = bufin.readLine()) != null) {
        response.append(line);
      }
    } catch (IOException e) {
      logger.error("error calling RESTful API: " + e.getMessage());
    } finally {
      if (bufin != null) {
        try {
          bufin.close();
        } catch (IOException e) {
          logger.error("error closing response stream: " + e.getMessage());
        }
      }
    }
    return response.toString();
  }
}

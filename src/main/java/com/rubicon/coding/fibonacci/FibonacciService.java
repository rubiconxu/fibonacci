package com.rubicon.coding.fibonacci;

import java.math.BigInteger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Fibonacci sequence service implementation
 *
 * The service supports to handle arbitrary large number input in theory, but considering time/space
 * consumed and result size, we limit the input number below a predefined limit practically.
 *
 * The service calculates and returns result per each request, caching may be used for better
 * performance
 *
 * @author xulu 2018-06-03
 *
 */
@Path("v1/fibonacci")
public class FibonacciService {
  private static Logger logger = LogManager.getLogger(FibonacciService.class);

  // upper limit of input
  private static final int MAX_RANGE = 10000;

  // error messages returned to caller
  private static final String ERR_NOT_NUMBER = "error, must input a digit number.";
  private static final String ERR_NOT_IN_RANGE = "error, the number must be positive and no bigger than " + MAX_RANGE + ".";

  /**
   * get the first n Fibonacci sequence as a string
   *
   * @param fibo
   *          caller input string
   * @return a string of the first n Fibonacci sequence for valid input or an error message for
   *         invalid input
   */
  @GET
  @Path("{fibo}")
  @Produces(MediaType.TEXT_PLAIN)
  public String getFirstNFibonacci(@PathParam("fibo") String fibo) {
    String validateResult = validateInput(fibo);
    if (validateResult.length() > 0) {
      return validateResult;
    } else {
      return getFibonacci(Integer.valueOf(fibo));
    }
  }

  /**
   * validate caller input string
   *
   * @param fibo
   *          the string input
   * @return an empty string if the input is valid or an error message of invalid reason
   */
  private String validateInput(String fibo) {
    String result = "";
    if (!isDigits(fibo)) {
      result = ERR_NOT_NUMBER;
    } else if (!inRange(fibo)) {
      result = ERR_NOT_IN_RANGE;
    }
    return result;
  }

  /**
   * Determines whether the input string represents a decimal integer, the first character can be a
   * negative symbol
   *
   * @param s
   *          the input string to be evaluated
   * @return true if valid, false if otherwise
   */
  private boolean isDigits(String s) {
    if (s == null || s.length() == 0) {
      return false;
    }

    // if the first character is negative symbol, skip it
    int start = s.charAt(0) == '-' ? 1 : 0;

    // no digit character
    if (start == 1 && s.length() == 1) {
      return false;
    }

    for (int i = start; i < s.length(); i++) {
      char ch = s.charAt(i);
      if (!(ch >= '0' && ch <= '9')) {
        return false;
      }
    }
    return true;
  }

  /**
   * Determines whether the input number is in range.
   *
   * @param val
   *          the input digit number as a string
   * @return true if in range or false otherwise
   */
  private boolean inRange(String val) {
    try {
      int i = Integer.parseInt(val);
      return i > 0 && i <= MAX_RANGE;
    } catch (NumberFormatException e) {
      logger.warn("input " + val + " fails to be parsed as integer.");
    }
    return false;
  }

  /**
   * generates the first n Fibonacci numbers as a string
   */
  private String getFibonacci(int n) {
    if (n == 1) {
      return "[0]";
    } else if (n == 2) {
      return "[0, 1]";
    }

    BigInteger n1 = new BigInteger("0");
    BigInteger n2 = new BigInteger("1");
    BigInteger n3;

    StringBuilder sb = new StringBuilder("[0, 1");

    for (int i = 2; i < n; i++) {
      n3 = n1.add(n2);
      sb.append(", ").append(n3);
      n1 = n2;
      n2 = n3;
    }
    sb.append(']');
    return sb.toString();
  }
}

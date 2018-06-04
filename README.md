# Rubicon Coding Solution

By [Xuechao Lu](mailto:luxuechao@hotmail.com)

## Implementation

This application is implemented in Java.

This application uses Jersey and Java built-in HTTP server to provide RESTful web service for simplicity. Spring-boot is typically used in real production environments.

## deploy/run Instructions

1. Clone or download the source code;
2. Check that you have installed maven and JDK locally, the code has been tested with HotSpot JDK 8;
3. Run unit tests with command: mvn test;
4. Compile the application then run it with command: mvn exec:java, press Ctrl + C to kill it;
5. To call the application's RESTful service manually, use a web browser or curl. The endpoint is: http://localhost:8088/v1/fibonacci/, with input follows it.

Thanks!
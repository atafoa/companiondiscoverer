// Kevin Tung
// kxt9434
// CSE4344-001, Dr. Yonghe Liu

// https://medium.com/@ssaurel/create-a-simple-http-web-server-in-java-3fc12b29d5fd
// Used for general algorithm.

// https://alvinalexander.com/java/java-file-exists-directory-exists
// Used for checking for 404 errors.

// https://stackoverflow.com/questions/3571223/how-do-i-get-the-file-extension-of-a-file-in-java
// Used for determining MIME type.

// https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Complete_list_of_MIME_types
// Used for corresponding MIME types with extensions.

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class SocketServer { 
    private static final int DEFAULTPORT = 8000;    // Constant for default port of webserver.


    //===========================
    //  Program start point of execution
    //
    //  The program sets a port number, then opens a server socket on that port number.
    //  It then proceeds to wait for connections, and addresses HTTP requests with responses.
    //   
    public static void main(String argv[]) throws Exception {
        
        // SETTING PORT NUMBER
        Integer portNumber = null;

        if (argv.length > 0) {  // The user may choose to run the program with a port number.
            try {
                portNumber = Integer.parseInt(argv[0]);
                if (portNumber < 1024 || portNumber > 65535) {  // Avoids ports that shouldn't be used.
                    throw new Exception("Not a port!");         // Exception is thrown, making port default.
                }
            }
            catch (Exception e) {   // Ensures that we handle if the user enters something that is not a port number.
                timestamp("Unable to parse given port number, starting on port " + DEFAULTPORT + " instead.");
                portNumber = DEFAULTPORT;
            }
        }
        else {  // If no port is specified, we start on the constant DEFAULTPORT.
            timestamp("No port number specified, starting on port " + DEFAULTPORT + ".");
            portNumber = DEFAULTPORT;
        }

        // Create a new ServerSocket, on the port we just chose.
        ServerSocket publicSocket = new ServerSocket(portNumber);
        
        // Server has started, now forever wait for connections and handle them.
        while (true) {
            HTTPThread connection = new HTTPThread(publicSocket.accept());      // Accept a new connection and create an instance of HTTPThread with the socket.
            SocketServer.timestamp("New connection opened, starting thread.");  // Timestamp the occurence.
            Thread connectionThread = new Thread(connection);                   // Create a new thread of the instanced class.
            connectionThread.start();                                           // Start the thread from the run();
        }
    }

    // timestamp takes in a String to be printed with a timestamp, and returns nothing.
    public static void timestamp(String message) {
        System.out.print("[" + new Date() + "] ");  // Prints the current time at function invocation.
        System.out.println(message);                // Prints the messsage.
    }
}

class HTTPThread implements Runnable {      // implements Runnable to allow multi-threading.
    static final double HTTPVERSION = 1.0;  // The version of HTTP being used.
    static final String PATH = ".";         // The prefix for paths of requested files.

    // unsupportedMethods is an arraylist containing HTTP methods that are not supported.
    // This is used later to identify 501 errors.
    static ArrayList<String> unsupportedMethods = new ArrayList<>(Arrays.asList("HEAD", "POST", "PUT", "DELETE", "CONNECT", "OPTIONS", "TRACE", "PATCH"));
    
    private Socket connectionSocket;                    // Connection socket to client.
    private BufferedReader requestReader;               // Reader of the request message from client.
    private BufferedOutputStream responseDataStream;    // Buffer stream going out to client.

    //===========================
    // CONSTRUCTOR FOR HTTPThread
    //
    //  Constructor takes in a socket and stores it in connectionSocket
    //  It then proceeds to initialize the input stream and output streams of the socket.
    //
    public HTTPThread(Socket incomingSocket) {
        connectionSocket = incomingSocket;
        try {
            requestReader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));   // Sets up request stream.
            responseDataStream = new BufferedOutputStream(connectionSocket.getOutputStream());              // Sets up response stream.
        }
        catch (Exception e) {
            System.out.println(e);  // Notification for any strange exceptions.
        }
    }
    
    //===========================
    // HTTPThread's start point of execution
    //
    //  A new thread is created with a socket, and is started.
    //  The started thread begins at run().
    //  run() calls readRequest().
    //  readRequest() will then call buildResponse().
    //  buildResponse() will then close the connection.
    //
    @Override
    public void run() {
        try {
            readRequest();  // Function to read the request. Transitions into buildResponse.
        }
        catch (Exception e) {
            System.out.println(e); // Notification for any strange exceptions.
        }
    }

    //===========================
    // readRequest()
    //
    //  readRequest processes an HTTP request and delivers the result to buildResponse().
    //  It works by splitting the startline of the request into 3, and examining the method and target.
    //  After evaluating the request, the function passes a statusCode, statusText, the served file path,
    //  and the HTTP version to the response generator.
    //
    private void readRequest() throws IOException {
        Integer statusCode = null;  // HTTP Code to be passed to buildResponse();
        String statusText = null;   // HTTP Status text corresponding to code, also passed to buildResponse();
        Boolean moved = false;      // A flag on whether the resource has been moved; by default false.
        
        // Parse the request's start line, splitting it into 3.
        String line[] = requestReader.readLine().split(" ");
        if (line.length != 3) { // If there are less or more than 3 entries, it is a malformed request.
            // Set the code, text, and build a response with the error page instead of the original target.
            statusCode = 400;
            statusText = "Bad Request";
            buildResponse(HTTPVERSION, statusCode, statusText, PATH + "/error/400.html");
        }
        else if (line[0].equals("GET")) { // If the request isn't malformed, then if it is a GET request...
            if (line[1].equals("/")) {      // If the request target is for /
                line[1] = "/index.html";    // Adjust it to be /index.html
            }

            //======================
            // MOVED FILES
            // Moved files go below.
            // Requests that target a moved file are directed to the new target
            // and the moved flag is set accordingly.
            //
            if (line[1].equals("/soup.html")) { // /soup.html was moved into folder /html/soup.html
                line[1] = "/html/soup.html";    // Fix target.
                moved = true;                   // Set moved flag to true.
            }
            // END OF MOVED FILES
            //=======================

            // We continue with a valid GET request.

            File existChecker = new File(PATH + line[1]);   // Create a File to check to see if a file exists.
            if (!existChecker.exists()) {                   // If it doesn't exist
                // Set the code, text, and build a response with the error page instead of the original target.
                statusCode = 404;                     
                statusText = "File Not Found";
                buildResponse(HTTPVERSION, statusCode, statusText, PATH + "/error/404.html");
            }
            else {  // Otherwise, it exists!
                if (moved) {    // If it was moved, let the response know.
                    statusCode = 301;
                    statusText = "Moved Permanently";
                }
                else {  // Otherwise, everything was working normally.
                    statusCode = 200;
                    statusText = "OK";
                }
                // Now, build a response with the file we have worked so hard to GET.
                buildResponse(HTTPVERSION, statusCode, statusText, PATH + line[1]);
            }
        }
        else if (unsupportedMethods.contains(line[0])) {    // If the request had a unsupportedMethod (see above ArrayList)
            // Set the code, text, and build a response with the error page instead of the original target.
            statusCode = 501;
            statusText = "HTTP method not supported.";
            buildResponse(HTTPVERSION, statusCode, statusText, PATH + "/error/501.html");
        }
        else {  // If we reach this point, we must send some kind of response, but we haven't been able to detect it.
            // It is then appropriate to generate a generic error.
            // Set the code, text, and build a response with the error page instead of the original target.
            statusCode = 500;
            statusText = "Internal Server Error";
            buildResponse(HTTPVERSION, statusCode, statusText, PATH + "/error/500.html");
        }
        // At the end of this, log the request and the according response code.
        SocketServer.timestamp(line[0] + " request for " + line[1] + ", responded with code " + statusCode + ".");
    }
    
    //===========================
    //  buildResponse FUNCTION
    //
    //  Takes in protocolVersion, statusCode, statusText, and target which
    //  is all it needs to build a response.
    //  The first three variables are simply to create the startline.
    //  The last one (target) is used to find the file needing to be served.
    //  
    //  The function will create a byte array of the target's data.
    //  Then, it generates the appropriate headers for the response,
    //  followed by writing the header and byte array to the buffer before
    //  flushing it.
    //
    //  After the information has been flushed, the connection closes and the
    //  thread has reached the end of its life.
    //
    private void buildResponse(double protocolVersion, int statusCode, String statusText, String target) throws IOException {
        // Generates the byte array of the target to be served, and also stores the length of the target.
        File targetFile = new File(target);
        int targetLength = (int)targetFile.length();
        byte[] targetData = fileToBytes(targetFile, targetLength);
        String end = "\r\n";    // Carriage return

        // RESPONSE STRINGS - Self Explanatory
        // Each line also has the variable end added on, which signifies a carriage return and newline.
        String statusLine = "HTTP/" + protocolVersion + " " + statusCode + " " + statusText + end;    // Variables from function parameters.
		String serverLine = "Server: Java Webserver for CSE4344 by kxt9434" + end;
        String dateLine = "Date: " + new Date() + end;
        String contentTypeLine = "Content-type: " + selectType(target) + end; // Content type determined by function with appropriate MIME typing.
        String contentLengthLine = "Content-length: " + targetLength + end;   // Length taken from previous stored value above.

        // header is the concatenation of the response strings, along with two newlines to indicate
        // that the header has ended and the content is next.
        String header = statusLine + serverLine + dateLine + contentTypeLine + contentLengthLine + end;
        
        responseDataStream.write(header.getBytes(), 0, header.length());    // Write the header.
        responseDataStream.write(targetData, 0, targetLength);              // Write the target data.
        responseDataStream.flush();                                         // Send the data to the client.

        responseDataStream.close();                                         // Close the connection.
        connectionSocket.close();                                           // Close the socket completely.
    }

    //===========================
    //  selectType FUNCTION
    //
    //  Takes in a String filename.
    //  Uses the extension of the filename to determine
    //  an appropriate MIME type.
    //  Returns the correct type as a string.
    //
    private String selectType(String filename) {
        String targetExtension = null;  // Extension of filename.
        String type = null;             // MIME type.

        int i = filename.lastIndexOf('.');      // Find the last '.' in the filename.
        if (i > 0) {                // Avoid any weird errors.
            targetExtension = filename.substring(i+1);  // Set targetExtension to the extension, with no dot.
        }
        else {
            targetExtension = "";   // If we do have weird errors, avoid a null pointer.
        }

        // FOR EACH EXTENSION
        // IF IT IS THAT EXTENSION
        // SET THE APPROPRIATE MIME TYPE
        if (targetExtension.equals("html")) {
            type = "text/html"; 
        }
        else if (targetExtension.equals("png")) {
            type = "image/png";
        }
        else if (targetExtension.equals("ico")) {
            type = "image/vnd.microsoft.icon";
        }
        else {
            // THIS IS A DEFAULT MIME TYPE FOR UNKNOWN EXTENSIONS.
            type = "application/octet-stream";
        }

        return type;
    }

    //===========================
    //  fileToBytes FUNCTION
    //
    //  Takes in a File and the file's length.
    //  Returns a bytearray representation of the file.
    //
    private byte[] fileToBytes(File file, int length) throws IOException {
		byte[] fileData = new byte[length]; // Create the array of the file's size.

		try {
		    FileInputStream fileStream = new FileInputStream(file); // Make a stream out of the file.
			fileStream.read(fileData);                              // Read the stream to the byte array.
			fileStream.close();                                     // Close the stream.
		}
        catch (Exception e) {
            System.out.println(e);  // Notify of any exceptions occuring.
        }

		return fileData;
	}
}
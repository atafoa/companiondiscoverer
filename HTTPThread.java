import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class HTTPThread implements Runnable {      // implements Runnable to allow multi-threading.
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
    //  readRequest() will then call buildGETResponse().
    //  buildGETResponse() will then close the connection.
    //
    @Override
    public void run() {
        try {
            readRequest();  // Function to read the request. Transitions into buildGETResponse.
        }
        catch (Exception e) {
            System.out.println(e); // Notification for any strange exceptions.
        }
    }

    //===========================
    // readRequest()
    //
    //  readRequest processes an HTTP request and delivers the result to buildGETResponse().
    //  It works by splitting the startstartLine of the request into 3, and examining the method and target.
    //  After evaluating the request, the function passes a statusCode, statusText, the served file path,
    //  and the HTTP version to the response generator.
    //
    private void readRequest() throws IOException {
        Integer statusCode = null;  // HTTP Code to be passed to buildGETResponse();
        String statusText = null;   // HTTP Status text corresponding to code, also passed to buildGETResponse();
        JSONArray jsonArr = null;
        // Parse the request's start line, splitting it into 3.
        // startLine[0] : Request Method
        // startLine[1] : Request Target
        // startLine[2] : HTTP Version
        String startLine[] = requestReader.readLine().split(" ");
        String targetString[] = startLine[1].substring(1).split("/");

        // If there are less or more than 3 entries, it is a malformed request. Build response with error page.        
        if (startLine.length != 3) {
            buildGETResponse(400, "Bad Request", PATH + "/error/400.html");
        }
        else if (targetString[0].equals("api")) {
            //statusCode = WebAPI.handleQuery(startLine[0], startLine[1]);
            SocketServer.timestamp("API " + startLine[0] + " request for " + targetString[1] + ".");
            jsonArr = WebAPI.handleQuery(startLine[0], targetString[1]); // targetString[1] is the api request
            buildAPIResponse(jsonArr, "API OK", PATH + "/error/api.html");
        }

        else if (startLine[0].equals("GET")) { // If the request isn't malformed, then if it is a GET request...
            if (startLine[1].equals("/")) {      // If the request target is for /
                startLine[1] = "/index.html";    // Adjust it to be /index.html
            }

            File existChecker = new File(PATH + startLine[1]);   // Create a File to check to see if a file exists.
            if (!existChecker.exists()) {                   // If it doesn't exist
                // Set the code, text, and build a response with the error page instead of the original target.
                buildGETResponse(404, "File Not Found", PATH + "/error/404.html");
            }
            else {  // Otherwise, it exists!
                // Now, build a response with the file we have worked so hard to GET.
                buildGETResponse(200, "OK", PATH + startLine[1]);
            }
        }
        else if (unsupportedMethods.contains(startLine[0])) {    // If the request had a unsupportedMethod (see above ArrayList)
            // Set the code, text, and build a response with the error page instead of the original target.
            buildGETResponse(501, "HTTP method not supported.", PATH + "/error/501.html");
        }
        else {  // If we reach this point, we must send some kind of response, but we haven't been able to detect it.
            // It is then appropriate to generate a generic error.
            // Set the code, text, and build a response with the error page instead of the original target.
            buildGETResponse(500, "Internal Server Error", PATH + "/error/500.html");
        }
        // At the end of this, log the request and the according response code.
    }
    
    //===========================
    //  buildGETResponse FUNCTION
    //
    //  Takes in protocolVersion, statusCode, statusText, and target which
    //  is all it needs to build a response.
    //  The first three variables are simply to create the startstartLine.
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
    private void buildGETResponse(int statusCode, String statusText, String target) throws IOException {
        // Generates the byte array of the target to be served, and also stores the length of the target.
        File targetFile = new File(target);
        int targetLength = (int)targetFile.length();
        byte[] targetData = fileToBytes(targetFile, targetLength);
        String end = "\r\n";    // Carriage return

        // RESPONSE STRINGS - Self Explanatory
        // Each startLine also has the variable end added on, which signifies a carriage return and newstartLine.
        String statusLine = "HTTP/" + HTTPVERSION + " " + statusCode + " " + statusText + end;    // Variables from function parameters.
		String serverLine = "Server: Java Webserver for CSE4344 by kxt9434" + end;
        String dateLine = "Date: " + new Date() + end;
        String contentTypeLine = "Content-type: " + selectType(target) + end; // Content type determined by function with appropriate MIME typing.
        String contentLengthLine = "Content-length: " + targetLength + end;   // Length taken from previous stored value above.

        // header is the concatenation of the response strings, along with two newstartLines to indicate
        // that the header has ended and the content is next.
        String header = statusLine + serverLine + dateLine + contentTypeLine + contentLengthLine + end;
        
        responseDataStream.write(header.getBytes(), 0, header.length());    // Write the header.
        responseDataStream.write(targetData, 0, targetLength);              // Write the target data.
        responseDataStream.flush();                                         // Send the data to the client.

        responseDataStream.close();                                         // Close the connection.
        connectionSocket.close();                                           // Close the socket completely.
        SocketServer.timestamp("GET request for " + target + ", responded with code " + statusCode + ".");
    }

    private void buildAPIResponse(JSONArray jsonArr, String statusText, String target) throws IOException {
        // Generates the byte array of the target to be served, and also stores the length of the target.
        int statusCode = 200;
        System.out.println(jsonArr.toString());
        byte[] jsonByteArray = jsonArr.toString().getBytes();
        int targetLength = jsonByteArray.length;
        String end = "\r\n";    // Carriage return

        // RESPONSE STRINGS - Self Explanatory
        // Each startLine also has the variable end added on, which signifies a carriage return and newstartLine.
        String statusLine           = "HTTP/" + HTTPVERSION + " " + statusCode + " " + statusText + end;    // Variables from function parameters.
		String serverLine           = "Server: Java Webserver for CSE4344 by kxt9434" + end;
        String dateLine             = "Date: " + new Date() + end;
        String contentTypeLine      = "Content-type: " + selectType("itsa.json") + end; // Content type determined by function with appropriate MIME typing.
        String contentLengthLine    = "Content-length: " + targetLength + end;   // Length taken from previous stored value above.

        // header is the concatenation of the response strings, along with two newstartLines to indicate
        // that the header has ended and the content is next.
        String header = statusLine + serverLine + dateLine + contentTypeLine + contentLengthLine + end;
        
        responseDataStream.write(header.getBytes(), 0, header.length());    // Write the header.
        responseDataStream.write(jsonByteArray, 0, targetLength);              // Write the target data.
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
        else if (targetExtension.equals("json")) {
            type = "application/json";
        }
        else if (targetExtension.equals("js")) {
            type = "application/js";
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

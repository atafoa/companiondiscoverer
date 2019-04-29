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

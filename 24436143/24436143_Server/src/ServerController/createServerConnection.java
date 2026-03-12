/**
 * This class sets up the sockets needed to run the server
 * This will be a console application that will print messages in console
 */

package ServerController;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;



public class createServerConnection {
    // Defining server port number
    public static final int PORT = 2378;
    // Socket for server
    private static ServerSocket serverSocket = null;

    // Object to act as logical "brain" for the server. Handles requests made by client
    private static ServerController controller = new ServerController();


    // Main server console interface
    public static void main(String[] args) {
        System.out.println("Creating Server Connection....");


        // Attempt to create server socket
        try {
            serverSocket = new ServerSocket(PORT);
        } catch(IOException ex) {
            System.err.println("Could not create server socket!");
            System.exit(1);
        }


        // Making the server run + check for any exceptions
        // Allows server to handle multiple clients (One after the other)
        do {
            try {
                run();
            } catch(IOException ex) {
                System.err.println("Could not run Server Connection!");
                System.exit(1);
            }

        } while(true);


    }

    private static void run() throws IOException {

            Socket link = null;  // Socket which creates connection between server + client
            BufferedReader in = null;
            PrintWriter out = null;
            try
            {
                // Create link between server and client
                link = serverSocket.accept();

                // Set up input/output streams to get Client's String
                in = new BufferedReader(new InputStreamReader(link.getInputStream()));
                out = new PrintWriter(link.getOutputStream(),true);

                // Read in message from the client and check if null

                String clientMessage = in.readLine();


                // If not null try to process the client's message using server's controller
                // If null , do nothing
                if(clientMessage != null) {
                    System.out.println("SERVER: Received: " + clientMessage);
                    try {
                        String serverResponse = controller.processClientRequest(clientMessage);

                        // Output response to console using PrintWriter
                        out.println(serverResponse);

                    } catch (IncorrectActionException ex) {
                        // If the server can't process request throw our own custom exception
                        // Server catches the error and sends error message back to client
                        System.err.println("Incorrect message sent by client !" + ex.getMessage());
                        out.println("ERROR|" + ex.getMessage());
                    }
                }
            } catch(IOException ex) {
                // Catches/handles errors caused by errors with input/output streams
                ex.printStackTrace();
            }

            // Closes connection for period of inactivity etc

            finally {
                try {
                    // If the output stream isn't null...
                    if(out != null ) {
                        // Send message to client to process
                        out.println("SERVER_DISCONNECT|Connection closing...");
                        out.close(); // Close the PrintWriter
                    }

                    // These if statements prevent NullPointerExceptions on the client side
                    // If input stream isn't null close it
                    if(in != null) in.close();

                    // If link socket isn't null close it
                    if(link != null) link.close();

                } catch(IOException e)
                // Catches unknown errors when closing the link socket
                {
                    System.err.println("Unable to properly close client connection!");
                }
            }
        }
    }


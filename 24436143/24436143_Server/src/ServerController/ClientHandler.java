package ServerController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class has instrcutions for client Thread
 * Each client Thread is designed to simulate a client + allow the server to handle multiple clients
 */

public class ClientHandler implements Runnable {
    // Networking + Input/Output Stream variables
    private Socket link;
    private ServerController controller;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket link, ServerController controller) {
        this.link = link;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            // Setting up Client's input/output streams
            in = new BufferedReader(new InputStreamReader(link.getInputStream()));
            out = new PrintWriter(link.getOutputStream(), true);


            String clientMessage = " ";

            // If not null try to process the client's message using server's controller
            // If null , do nothing
            while ((clientMessage = in.readLine()) != null) {
                System.out.println("SERVER: Received: " + clientMessage);
                try {
                    String serverResponse = controller.processClientRequest(clientMessage);

                    // Output response to console using PrintWriter
                    out.println(serverResponse);


                    // If the client wants to quit, break the loop to close connection
                    if (clientMessage.equals("QUIT")) {
                        break;
                    }

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
                System.out.println("Client handler loop finished. Closing connection.");
                if(link != null) link.close();

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

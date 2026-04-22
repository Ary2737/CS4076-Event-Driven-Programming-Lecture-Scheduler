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
        } catch (IOException ex) {
            System.err.println("Could not create server socket!");
            System.exit(1);
        }


        System.out.println("Server is running and waiting for clients...");

        // Infinite loop to constantly accept new clients
        while (true) {
            try {
                // 1. Wait for a client to connect (blocks until someone connects)
                Socket link = serverSocket.accept();
                System.out.println("New client connected!");

                // 2. Create a new ClientHandler worker for this specific client
                ClientHandler handler = new ClientHandler(link, controller);

                // 3. Put the worker in a Thread and start it!
                Thread clientThread = new Thread(handler);
                clientThread.start();

            } catch (IOException ex) {
                System.err.println("Error accepting client connection!");
            }
        }
    }
}




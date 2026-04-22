package org.example.clientController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class clientNetwork {

    // TCP Network variables (Socket + Input/Output Streams)
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;

    /**
     * This method is only called once the client application starts
     * Called in the initialise() method in the MainMenuController
     * This method starts the client's connection to the server
     */

    public static void connectToServer(String IP, int port) {
        try {
            if(socket == null || socket.isClosed()) {
                // Create new Socket + Inout/output steams
                socket = new Socket(IP,port);
                out = new PrintWriter(socket.getOutputStream(),true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("Successfully connected to the server !");
            }
        } catch (IOException e) {
            System.err.println("Could not connect to the server.");
            e.printStackTrace();
        }
    }

    // Getter methods
    public static PrintWriter getOut() {return out;}
    public static BufferedReader getIn() {return in;}


    /**
     * This method closes the connection
     * Tt is called when the app is closed/"Stop Connection" is closed
     */

    public static void disconnect() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            System.out.println("Disconnected from server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

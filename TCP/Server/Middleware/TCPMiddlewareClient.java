package Middleware;

import java.io.*;
import java.net.*;

public class TCPMiddlewareClient {

    // Default values of host and port
    private static String serverHost = "localhost";
    private static int serverPort = 6007;

    private Socket clientSocket;
    private PrintWriter output;
    private BufferedReader input;
    private String host;
    private int port;

    public TCPMiddlewareClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.connect();
    }

    public void connect() {
        boolean first = true;
        try {
            while (true) {
                try {
                    clientSocket = new Socket(host, port);
                    output = new PrintWriter(clientSocket.getOutputStream(), true);
                    input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    System.out.println("Connected to host:" + this.host + " port:" + this.port);
                    break;
                } catch (Exception e) {
                    if (first) {
                        System.out.println("Waiting for host:" + this.host + " port:" + this.port);
                        first = false;
                    }
                }
                Thread.sleep(500);
            }
        }
        catch (Exception e) {
            System.err.println("An error occured while connecting to host " + this.host + " port:" + this.port);
            e.printStackTrace();
            System.exit(1);
        }

    }

    public String sendMessage(String message) throws IOException {
        output.println(message);
        String returnString = "";
        String inputMessage;
        
        // Getting the input from the server
        while ((inputMessage = input.readLine()) != null) {
            if (returnString.length() == 0)
                returnString += inputMessage;
            else
                returnString += "\n" + inputMessage;
        }
        connect();
        return returnString;
    }

    public void stopTCPClient() {
        try {
            input.close();
            output.close();
            clientSocket.close();
        } catch(Exception e) {
            System.err.println("An error occured while Stopping the client");
            e.printStackTrace();
        }
    }
}

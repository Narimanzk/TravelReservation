package Server.TCP;

import Server.Common.*;
import Middleware.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class TCPMiddleware extends Middleware {

    private static TCPMiddleware tcpMiddleware = null;

    private ServerSocket serverSocket;

    private static String flightIP = "localhost";
    private static int flightPort = 6007;

    private static String carIP = "localhost";
    private static int carPort = 6007;

    private static String roomIP = "localhost";
    private static int roomPort = 6007;

    public static void main(String[] args) {

        // 1. Getting the info of RM servers
        try {
            if(args.length != 3) {
				System.out.println("Exception: Incorrect number of arguments");
				throw new IllegalArgumentException();
			} else {
                String[] flight = args[0].split(",");
                String[] car = args[1].split(",");
                String[] room = args[2].split(",");

                flightIP = flight[0];
                flightPort = Integer.parseInt(flight[1]);

                carIP = car[0];
                carPort = Integer.parseInt(car[1]);

                roomIP = room[0];
                roomPort = Integer.parseInt(room[1]);
            }
        } catch (Exception e) {
            System.out.println("Exception encountered in starting the Middleware, exiting system.");
			e.printStackTrace();
            System.exit(1);
        }

        // 2. Create TCP middleware server instance
        try {
            tcpMiddleware = new TCPMiddleware("Middleware",flightIP,flightPort,carIP,carPort,roomIP,roomPort);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    tcpMiddleware.stopMiddleware();
                }
            });

            System.out.println("Starting the middleware server:");
            tcpMiddleware.startMiddleware(6007); //The default port for the middleware is 6007
        } catch(Exception e) {
            System.err.println("An error occured while starting the Middleware server");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public TCPMiddleware(String p_name, String flightIP, int flightPort, String carIP, int carPort, String roomIP, int roomPort)
    {
        super(p_name,flightIP,flightPort,carIP,carPort,roomIP,roomPort);
    }

    private void startMiddleware(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Middleware is now listening on port: " + port);
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            stopMiddleware();
        }
    }

    public void stopMiddleware() {
        try {
            serverSocket.close();
            System.out.println("Closed the Middleware server socket");
        }
        catch(IOException e) {
            System.err.println("An error occured while closing the Middleware server socket");
        }
    }

    // Listening for client requests
    private static class ClientHandler extends Thread{

        // The socket specifically for this client 
        private Socket clientSocket;

        // Input and Ouput streams 
        private BufferedReader input;
        private PrintWriter output;

        public ClientHandler(Socket socket) {
			this.clientSocket = socket;
        }
        
        // Running the client handler
        public void run() {
            try {
                output = new PrintWriter(clientSocket.getOutputStream(), true);
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // Gettin input from the client and parsing
                String inputCmd = input.readLine();

                Vector<String> parsedCmd = Parser.parse(inputCmd);

                // Input validation
                if (parsedCmd == null) {
                    output.println("");
                    input.close();
                    output.close();
                    clientSocket.close();
                    return;
                }
            
                String result = "";
                switch (parsedCmd.get(0).toLowerCase()) {
                    case "addflight": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        int flightNumber = Integer.parseInt(parsedCmd.get(2));
                        int num = Integer.parseInt(parsedCmd.get(3));
                        int price = Integer.parseInt(parsedCmd.get(4));
                        result = Boolean.toString(tcpMiddleware.addFlight(id, flightNumber, num, price));
			            break;
                    }
                    case "addcars": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        String location = parsedCmd.get(2);
                        int num = Integer.parseInt(parsedCmd.get(3));
                        int price = Integer.parseInt(parsedCmd.get(4));
                        result = Boolean.toString(tcpMiddleware.addCars(id, location, num, price));
			            break;
                    }
                    case "addrooms": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        String location = parsedCmd.get(2);
                        int num = Integer.parseInt(parsedCmd.get(3));
                        int price = Integer.parseInt(parsedCmd.get(4));
                        result = Boolean.toString(tcpMiddleware.addRooms(id, location, num, price));
			            break;
                    }
                    case "deleteflight": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        int flightNumber = Integer.parseInt(parsedCmd.get(2));
                        result = Boolean.toString(tcpMiddleware.deleteFlight(id, flightNumber));
			            break;
                    }
                    case "deletecars": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        String location = parsedCmd.get(2);
                        result = Boolean.toString(tcpMiddleware.deleteCars(id, location));
			            break;
                    }
                    case "deleterooms": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        String location = parsedCmd.get(2);
                        result = Boolean.toString(tcpMiddleware.deleteRooms(id, location));
			            break;
                    }
                    case "queryflight": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        int flightNum = Integer.parseInt(parsedCmd.get(2));
                        result = Integer.toString(tcpMiddleware.queryFlight(id, flightNum));
                        break;
                    }
                    case "querycars": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        String location = parsedCmd.get(2);
                        result = Integer.toString(tcpMiddleware.queryCars(id, location));
                        break;
                    }
                    case "queryrooms": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        String location = parsedCmd.get(2);
                        result = Integer.toString(tcpMiddleware.queryRooms(id, location));
                        break;
                    }
                    case "queryflightprice": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        int flightNum = Integer.parseInt(parsedCmd.get(2));
                        result = Integer.toString(tcpMiddleware.queryFlightPrice(id, flightNum));
                        break;
                    }
                    case "querycarsprice": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        String location = parsedCmd.get(2);
                        result = Integer.toString(tcpMiddleware.queryCarsPrice(id, location));
                        break;
                    }
                    case "queryroomsprice": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        String location = parsedCmd.get(2);
                        result = Integer.toString(tcpMiddleware.queryRoomsPrice(id, location));
                        break;
                    }
                    case "addcustomer": {
                        int xid = Integer.parseInt(parsedCmd.get(1));
                        result = Integer.toString(tcpMiddleware.newCustomer(xid));
                        break;
                    }
                    case "addcustomerid": {
                        int xid = Integer.parseInt(parsedCmd.get(1));
                        int id = Integer.parseInt(parsedCmd.get(2));
                        result = Boolean.toString(tcpMiddleware.newCustomer(xid, id));
                        break;
                    }
                    case "deletecustomer": {
                        int xid = Integer.parseInt(parsedCmd.get(1));
                        int customerID = Integer.parseInt(parsedCmd.get(2));
                        result = Boolean.toString(tcpMiddleware.deleteCustomer(xid, customerID));
                        break;
                    }
                    case "reserveflight": {
                        int xid = Integer.parseInt(parsedCmd.get(1));
                        int customerID = Integer.parseInt(parsedCmd.get(2));
                        int flightNum = Integer.parseInt(parsedCmd.get(3));
                        
                        result = Boolean.toString(tcpMiddleware.reserveFlight(xid, customerID, flightNum));
                        break;
                    }
                    case "reservecar": {
                        int xid = Integer.parseInt(parsedCmd.get(1));
                        int customerID = Integer.parseInt(parsedCmd.get(2));
                        String location = parsedCmd.get(3);
                        
                        result = Boolean.toString(tcpMiddleware.reserveCar(xid, customerID, location));
                        break;
                    }
                    case "reserveroom": {
                        int xid = Integer.parseInt(parsedCmd.get(1));
                        int customerID = Integer.parseInt(parsedCmd.get(2));
                        String location = parsedCmd.get(3);

                        result = Boolean.toString(tcpMiddleware.reserveRoom(xid, customerID, location));
                        break;
                    }
                    case "querycustomer": {
                        int xid = Integer.parseInt(parsedCmd.get(1));
                        int customerID = Integer.parseInt(parsedCmd.get(2));
    
                        result = tcpMiddleware.queryCustomerInfo(xid, customerID);
                        break;
                    }
                    case "itemsexist": {
                        int xid = Integer.parseInt(parsedCmd.get(1));
                        String key = parsedCmd.get(2);
                        int quantity = Integer.parseInt(parsedCmd.get(3));
    
                        result = Integer.toString(tcpMiddleware.ItemsExist(xid, key, quantity));
                        break;
                    }
                    case "bundle": {
                        int xid = Integer.parseInt(parsedCmd.get(1));
                        int customerID = Integer.parseInt(parsedCmd.get(2));
    
                        Vector<String> flightNumbers = new Vector<String>();
                        for (int i = 3; i < parsedCmd.size() - 3; ++i) {
                            flightNumbers.add(parsedCmd.elementAt(i));
                        }
                        String location = parsedCmd.get(parsedCmd.size() - 3);
                        boolean car = Boolean.parseBoolean(parsedCmd.get(parsedCmd.size() - 2));
                        boolean room = Boolean.parseBoolean(parsedCmd.get(parsedCmd.size() - 1));

                        System.out.println("Debug - TCPMiddleare calling " + flightNumbers.toString() + " " + car + " " + room);
    
                        result =  Boolean.toString(tcpMiddleware.bundle(xid, customerID, flightNumbers, location, car, room));
                        break;
                    }
                    case "removereservation": {
                        int xid = Integer.parseInt(parsedCmd.get(1));
                        int customerID = Integer.parseInt(parsedCmd.get(2));
                        String reserveditemKey = parsedCmd.get(3);
                        int reserveditemCount = Integer.parseInt(parsedCmd.get(4));
    
                        result = Boolean.toString(tcpMiddleware.removeReservation(xid, customerID, reserveditemKey, reserveditemCount));
                        break;
                    }
                }
                output.println(result);
                input.close();
                output.close();
                clientSocket.close();
            } catch (Exception e) {
                System.out.println("Exception encountered in Client Handler of Middleware.");
                e.printStackTrace();
            }
        }

    }
}
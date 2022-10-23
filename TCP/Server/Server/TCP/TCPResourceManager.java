package Server.TCP;

import Server.Common.*;
import Middleware.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class TCPResourceManager extends ResourceManager {
	
    private static TCPResourceManager manager = null;
	// default server name
    private static String name = "Server";
    private static int port = 6007;
    
	private ServerSocket serverSocket;

	public TCPResourceManager(String name)
	{
		super(name);
	}

	public static void main(String args[]) {

		try {
			// Getting the server name
			if (args.length == 1)
			{
                String[] serverInfo = args[0].split(",");

                name = serverInfo[0];
                port = Integer.parseInt(serverInfo[1]);
			}
			// Set the server manager 
            manager = new TCPResourceManager(name);
            
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    manager.stopServerSocket();
                }
            });

            // Starting the manager in port 3007
            System.out.println("Starting '" + name + ":" + port + "'");
			manager.startServerSocket(port);

		}
		catch(Exception e)
		{
			System.out.println("Exception encountered, exiting system.");
			e.printStackTrace();
			System.exit(1);
		}

	}

	private void startServerSocket(int port) {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Started the server socket on port " + port);

			// Start listening for the clients
			while(true) {
				new ClientHandler(serverSocket.accept(), name).start();
			}
		} catch (Exception e) {
            e.printStackTrace();
            stopServerSocket();
        }
        stopServerSocket();
    }
    
    public void stopServerSocket () {
        try {
            serverSocket.close();
            System.out.println("Server Socket closed of server");
        } catch (Exception e) {
            System.out.println("Exception occured in closing the server socket.");
            e.printStackTrace();
        }
    }
    
    // For each client request it will initiate a seperate socket to communicate with the client
    private static class ClientHandler extends Thread{

        // The socket specifically for this client 
        private Socket clientSocket;
        private String serverName;

        // Input and Ouput streams 
        private BufferedReader input;
        private PrintWriter output;

        public ClientHandler(Socket socket, String serverName) {
            this.clientSocket = socket;
            this.serverName = serverName;
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
                        int xid = Integer.parseInt(parsedCmd.get(1));
                        int flightNumber = Integer.parseInt(parsedCmd.get(2));
                        int num = Integer.parseInt(parsedCmd.get(3));
                        int price = Integer.parseInt(parsedCmd.get(4));
                        result = Boolean.toString(manager.addFlight(xid, flightNumber, num, price));
			            break;
                    }
                    case "addcars": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        String location = parsedCmd.get(2);
                        int num = Integer.parseInt(parsedCmd.get(3));
                        int price = Integer.parseInt(parsedCmd.get(4));
                        result = Boolean.toString(manager.addCars(id, location, num, price));
			            break;
                    }
                    case "addrooms": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        String location = parsedCmd.get(2);
                        int num = Integer.parseInt(parsedCmd.get(3));
                        int price = Integer.parseInt(parsedCmd.get(4));
                        result = Boolean.toString(manager.addRooms(id, location, num, price));
			            break;
                    }
                    case "deleteflight": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        int flightNumber = Integer.parseInt(parsedCmd.get(2));
                        result = Boolean.toString(manager.deleteFlight(id, flightNumber));
			            break;
                    }
                    case "deletecars": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        String location = parsedCmd.get(2);
                        result = Boolean.toString(manager.deleteCars(id, location));
			            break;
                    }
                    case "deleterooms": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        String location = parsedCmd.get(2);
                        result = Boolean.toString(manager.deleteRooms(id, location));
			            break;
                    }
                    case "queryflight": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        int flightNum = Integer.parseInt(parsedCmd.get(2));
                        result = Integer.toString(manager.queryFlight(id, flightNum));
                        break;
                    }
                    case "querycars": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        String location = parsedCmd.get(2);
                        result = Integer.toString(manager.queryCars(id, location));
                        break;
                    }
                    case "queryrooms": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        String location = parsedCmd.get(2);
                        result = Integer.toString(manager.queryRooms(id, location));
                        break;
                    }
                    case "queryflightprice": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        int flightNum = Integer.parseInt(parsedCmd.get(2));
                        result = Integer.toString(manager.queryFlightPrice(id, flightNum));
                        break;
                    }
                    case "querycarsprice": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        String location = parsedCmd.get(2);
                        result = Integer.toString(manager.queryCarsPrice(id, location));
                        break;
                    }
                    case "queryroomsprice": {
                        int id = Integer.parseInt(parsedCmd.get(1));
                        String location = parsedCmd.get(2);
                        result = Integer.toString(manager.queryRoomsPrice(id, location));
                        break;
                    }
                    case "addcustomer": {
                        int xid = Integer.parseInt(parsedCmd.get(1));
                        result = Integer.toString(manager.newCustomer(xid));
                        break;
                    }
                    case "addcustomerid": {
                        int xid = Integer.parseInt(parsedCmd.get(1));
                        int id = Integer.parseInt(parsedCmd.get(2));
                        result = Boolean.toString(manager.newCustomer(xid, id));
                        break;
                    }
                    case "deletecustomer": {
                        int xid = Integer.parseInt(parsedCmd.get(1));
                        int customerID = Integer.parseInt(parsedCmd.get(2));
                        result = Boolean.toString(manager.deleteCustomer(xid, customerID));
                        break;
                    }
                    case "reserveflight": {
                        int xid = Integer.parseInt(parsedCmd.get(1));
                        int customerID = Integer.parseInt(parsedCmd.get(2));
                        int flightNum = Integer.parseInt(parsedCmd.get(3));
                        result = Boolean.toString(manager.reserveFlight(xid, customerID, flightNum));
                        break;
                    }
                    case "reservecar": {
                        int xid = Integer.parseInt(parsedCmd.get(1));
                        int customerID = Integer.parseInt(parsedCmd.get(2));
                        String location = parsedCmd.get(3);
                        result = Boolean.toString(manager.reserveCar(xid, customerID, location));
                        break;
                    }
                    case "reserveroom": {
                        int xid = Integer.parseInt(parsedCmd.get(1));
                        int customerID = Integer.parseInt(parsedCmd.get(2));
                        String location = parsedCmd.get(3);
                        result = Boolean.toString(manager.reserveRoom(xid, customerID, location));
                        break;
                    }
                    case "itemsexist": {
                        int xid = Integer.parseInt(parsedCmd.get(1));
                        String key = parsedCmd.get(2);
                        int quantity = Integer.parseInt(parsedCmd.get(3));
    
                        result = Integer.toString(manager.ItemsExist(xid, key, quantity));
                        break;
                    }
                    case "bundle": {;
                        int xid = Integer.parseInt(parsedCmd.get(1));
                        int customerID = Integer.parseInt(parsedCmd.get(2));
    
                        Vector<String> flightNumbers = new Vector<String>();
                        for (int i = 3; i < parsedCmd.size() - 3; ++i) {
                            flightNumbers.add(parsedCmd.elementAt(i));
                        }
                        String location = parsedCmd.get(parsedCmd.size() - 3);
                        boolean car = Boolean.parseBoolean(parsedCmd.get(parsedCmd.size() - 2));
                        boolean room = Boolean.parseBoolean(parsedCmd.get(parsedCmd.size() - 1));
    
                        System.out.println("Debug - in TCPResourcemanage command sending to flightmanager ");
                        result =  Boolean.toString(manager.bundle(xid, customerID, flightNumbers, location, car, room));
                        System.out.println("Debug - in TCPResourcemanage result is " + result);
                        break;
                    }
                    case "removereservation": {
                        int xid = Integer.parseInt(parsedCmd.get(1));
                        int customerID = Integer.parseInt(parsedCmd.get(2));
                        String reserveditemKey = parsedCmd.get(3);
                        int reserveditemCount = Integer.parseInt(parsedCmd.get(4));
    
                        result = Boolean.toString(manager.removeReservation(xid, customerID, reserveditemKey, reserveditemCount));
                        break;
                    }
                }

                output.println(result);
                input.close();
                output.close();
                clientSocket.close();
            } catch (Exception e) {
                System.out.println("Exception encountered in Client Handler of " + this.serverName);
                e.printStackTrace();
            }
        }

    }

}

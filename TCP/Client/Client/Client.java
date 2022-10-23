package Client;

import java.util.*;
import java.io.*;
import java.rmi.ConnectException;
import java.rmi.ServerException;
import java.rmi.UnmarshalException;

public class Client {

	private TCPClient connectionClient = null;

	public Client(TCPClient tcpClient)
	{
		this.connectionClient = tcpClient;
	}

	public void start()
	{
		// Getting user commands
		System.out.println();
		System.out.println("Enter \"help\" for the list of supported commands");

		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			// Read the next command
			String command = "";
			Vector<String> arguments = new Vector<String>();
			try {
				System.out.print((char)27 + "[32;1m\n>] " + (char)27 + "[0m");
				command = stdin.readLine().trim();
			}
			catch (IOException io) {
				System.err.println((char)27 + "[31;1mClient exception: " + (char)27 + "[0m" + io.getLocalizedMessage());
				io.printStackTrace();
				System.exit(1);
			}

			try {
				arguments = parse(command);
				Command cmd = Command.fromString((String)arguments.elementAt(0));
				try {
					execute(cmd, arguments);
				}
				catch (ConnectException e) {
					connectionClient.connect();
					execute(cmd, arguments);
				}
			}
			catch (IllegalArgumentException|ServerException e) {
				System.err.println((char)27 + "[31;1mCommand exception: " + (char)27 + "[0m" + e.getLocalizedMessage());
			}
			catch (ConnectException|UnmarshalException e) {
				System.err.println((char)27 + "[31;1mCommand exception: " + (char)27 + "[0mConnection to server lost");
			}
			catch (Exception e) {
				System.err.println((char)27 + "[31;1mCommand exception: " + (char)27 + "[0mUncaught exception");
				e.printStackTrace();
			}
		}
	}

	public void execute(Command cmd, Vector<String> arguments) throws NumberFormatException,IOException
	{
		switch (cmd)
		{
			case Help:
			{
				if (arguments.size() == 1) {
					System.out.println(Command.description());
				} else if (arguments.size() == 2) {
					Command l_cmd = Command.fromString((String)arguments.elementAt(1));
					System.out.println(l_cmd.toString());
				} else {
					System.err.println((char)27 + "[31;1mCommand exception: " + (char)27 + "[0mImproper use of help command. Location \"help\" or \"help,<CommandName>\"");
				}
				break;
			}
			case AddFlight: {
				checkArgumentsCount(5, arguments.size());

				System.out.println("Adding a new flight [xid=" + arguments.elementAt(1) + "]");
				System.out.println("-Flight Number: " + arguments.elementAt(2));
				System.out.println("-Flight Seats: " + arguments.elementAt(3));
				System.out.println("-Flight Price: " + arguments.elementAt(4));

				// Sending the command to TCP client of the client
				send(connectionClient, arguments.toString(), "FlightAdded", "Flight could not be added", 'B');
				break;
			}
			case AddCars: {
				checkArgumentsCount(5, arguments.size());

				System.out.println("Adding new cars [xid=" + arguments.elementAt(1) + "]");
				System.out.println("-Car Location: " + arguments.elementAt(2));
				System.out.println("-Number of Cars: " + arguments.elementAt(3));
				System.out.println("-Car Price: " + arguments.elementAt(4));

				send(connectionClient, arguments.toString(), "Cars added", "Cars could not be added", 'B');
				break;
			}
			case AddRooms: {
				checkArgumentsCount(5, arguments.size());

				System.out.println("Adding new rooms [xid=" + arguments.elementAt(1) + "]");
				System.out.println("-Room Location: " + arguments.elementAt(2));
				System.out.println("-Number of Rooms: " + arguments.elementAt(3));
				System.out.println("-Room Price: " + arguments.elementAt(4));

				send(connectionClient, arguments.toString(), "Rooms added", "Rooms could not be added", 'B');
				break;
			}
			case DeleteFlight: {
				checkArgumentsCount(3, arguments.size());

				System.out.println("Deleting a flight [xid=" + arguments.elementAt(1) + "]");
				System.out.println("-Flight Number: " + arguments.elementAt(2));

				send(connectionClient, arguments.toString(), "Flight Deleted", "Flight could not be deleted", 'B');
				break;
			}
			case DeleteCars: {
				checkArgumentsCount(3, arguments.size());

				System.out.println("Deleting all cars at a particular location [xid=" + arguments.elementAt(1) + "]");
				System.out.println("-Car Location: " + arguments.elementAt(2));

				send(connectionClient, arguments.toString(), "Cars Deleted", "Cars could not be deleted", 'B');
				break;
			}
			case DeleteRooms: {
				checkArgumentsCount(3, arguments.size());

				System.out.println("Deleting all rooms at a particular location [xid=" + arguments.elementAt(1) + "]");
				System.out.println("-Car Location: " + arguments.elementAt(2));

				send(connectionClient, arguments.toString(), "Rooms Deleted", "Rooms could not be deleted", 'B');
				break;
			}
			case QueryFlight: {
				checkArgumentsCount(3, arguments.size());

				System.out.println("Querying a flight [xid=" + arguments.elementAt(1) + "]");
				System.out.println("-Flight Number: " + arguments.elementAt(2));

				send(connectionClient, arguments.toString(), "Number of seats available: ", "Could not query number of seats", 'I');
				break;
			}
			case QueryCars: {
				checkArgumentsCount(3, arguments.size());

				System.out.println("Querying cars location [xid=" + arguments.elementAt(1) + "]");
				System.out.println("-Car Location: " + arguments.elementAt(2));

				send(connectionClient, arguments.toString(), "Number of cars at this location: ", "Could not query number of cars", 'I');
				break;
			}
			case QueryRooms: {
				checkArgumentsCount(3, arguments.size());

				System.out.println("Querying rooms location [xid=" + arguments.elementAt(1) + "]");
				System.out.println("-Room Location: " + arguments.elementAt(2));

				send(connectionClient, arguments.toString(), "Number of rooms at this location: ", "Could not query number of rooms", 'I');
				break;
			}
			case QueryFlightPrice: {
				checkArgumentsCount(3, arguments.size());
				
				System.out.println("Querying a flight price [xid=" + arguments.elementAt(1) + "]");
				System.out.println("-Flight Number: " + arguments.elementAt(2));

				send(connectionClient, arguments.toString(), "Price of a seat: ", "Could not query price of seat", 'I');
				break;
			}
			case QueryCarsPrice: {
				checkArgumentsCount(3, arguments.size());

				System.out.println("Querying cars price [xid=" + arguments.elementAt(1) + "]");
				System.out.println("-Car Location: " + arguments.elementAt(2));

				send(connectionClient, arguments.toString(), "Price of cars at this location: ", "Could not query price of cars", 'I');
				break;
			}
			case QueryRoomsPrice: {
				checkArgumentsCount(3, arguments.size());

				System.out.println("Querying rooms price [xid=" + arguments.elementAt(1) + "]");
				System.out.println("-Room Location: " + arguments.elementAt(2));

				send(connectionClient, arguments.toString(), "Price of rooms at this location: ", "Could not query price of rooms", 'I');
				break;
			}
			case AddCustomer: {
				checkArgumentsCount(2, arguments.size());

				System.out.println("Adding a new customer [xid=" + arguments.elementAt(1) + "]");

				send(connectionClient, arguments.toString(), "Add Customer ID: ", "Customer could not be added", 'I');
				break;
			}
			case AddCustomerID: {
				checkArgumentsCount(3, arguments.size());

				System.out.println("Adding a new customer [xid=" + arguments.elementAt(1) + "]");
				System.out.println("-Customer ID: " + arguments.elementAt(2));

				int customerId = toInt(arguments.elementAt(2));

				send(connectionClient, arguments.toString(), "Add Customer ID: " + customerId, "Customer could not be added", 'B');
				break;
			}
			case DeleteCustomer: {
				checkArgumentsCount(3, arguments.size());

				System.out.println("Deleting a customer from the database [xid=" + arguments.elementAt(1) + "]");
				System.out.println("-Customer ID: " + arguments.elementAt(2));

				int customerId = toInt(arguments.elementAt(2));

				send(connectionClient, arguments.toString(), "Deleted customer with customerID: " + customerId, "Customer could not be added", 'B');
				break;
			}
			case QueryCustomer: {
				checkArgumentsCount(3, arguments.size());

				System.out.println("Querying customer information [xid=" + arguments.elementAt(1) + "]");
				System.out.println("-Customer ID: " + arguments.elementAt(2));

				int customerId = toInt(arguments.elementAt(2));

				send(connectionClient, arguments.toString(), "" + customerId, "Customer could not be queried", 'S');
				break;
			}
			case ReserveFlight: {
				checkArgumentsCount(4, arguments.size());

				System.out.println("Reserving seat in a flight [xid=" + arguments.elementAt(1) + "]");
				System.out.println("-Customer ID: " + arguments.elementAt(2));
				System.out.println("-Flight Number: " + arguments.elementAt(3));

				int customerId = toInt(arguments.elementAt(2));

				send(connectionClient, arguments.toString(), "Flight Reserved" + customerId, "Flight could not be reserved", 'B');
				break;
			}
			case ReserveCar: {
				checkArgumentsCount(4, arguments.size());

				System.out.println("Reserving a car at a location [xid=" + arguments.elementAt(1) + "]");
				System.out.println("-Customer ID: " + arguments.elementAt(2));
				System.out.println("-Car Location: " + arguments.elementAt(3));

				send(connectionClient, arguments.toString(), "Car Reserved", "Car could not be reserved", 'B');
				break;
			}
			case ReserveRoom: {
				checkArgumentsCount(4, arguments.size());

				System.out.println("Reserving a room at a location [xid=" + arguments.elementAt(1) + "]");
				System.out.println("-Customer ID: " + arguments.elementAt(2));
				System.out.println("-Room Location: " + arguments.elementAt(3));

				send(connectionClient, arguments.toString(), "Room Reserved", "Room could not be reserved", 'B');
				break;
			}
			case Bundle: {
				if (arguments.size() < 7) {
					System.err.println((char)27 + "[31;1mCommand exception: " + (char)27 + "[0mBundle command needs at least 7 arguments. Use \"help\" or \"help,<CommandName>\"");
					break;
				}

				int xid = Integer.parseInt(arguments.elementAt(1));
				int customerID = Integer.parseInt(arguments.elementAt(2));
				String location = arguments.elementAt(arguments.size()-3);

				System.out.println("Reserving a bundle [xid=" + xid + "]");
				System.out.println("-Customer ID: " + customerID);

				for (int i = 3; i < arguments.size() - 3; i ++) {
					System.out.println("-Flight Number: " + arguments.elementAt(i));
				} 

				System.out.println("-Location for room and car : " + location);

				StringBuilder s = new StringBuilder();
				s.append("[Bundle,");
				s.append(xid + ",");
				s.append(customerID + ",");

				for (int i = 3; i < arguments.size() - 3; i ++)
				{
					s.append(Integer.parseInt(arguments.elementAt(i)) + ",");
				}

				s.append(location + ",");

				// Booleans showing reserving car or room
				s.append(toBooleanFromInt(arguments.elementAt(arguments.size()-2)) + ",");
				s.append(toBooleanFromInt(arguments.elementAt(arguments.size()-1)) + "]");

				send(connectionClient, s.toString(), "Bundle Reserved", "Bundle could not be reserved", 'B');
				break;
			}
			case Quit:
				checkArgumentsCount(1, arguments.size());

				System.out.println("Quitting client");
				System.exit(0);
		}
	}

	// Send command to Middleware using the TCP client
	public static void send(TCPClient connection, String args, String success, String failure, char responseType) throws IOException {
		String response = connection.sendMessage(args);
	
		if (response.equals("")) {
			connection.connect();
			response = connection.sendMessage(args);
		}

		if (response != null) {
			try {
				if (responseType == 'B' && toBoolean(response)) {
					System.out.println(success);
					return;
				}
				else if (responseType == 'I') {
					System.out.println(success + toInt(response));
					return;
				}
				else if (responseType == 'S') {
					System.out.println(success + response);
					return;
				}
			}catch(Exception e) {
				System.err.println((char)27 + "[31;1mClient exception: " + (char)27 + "[0m" + e.getLocalizedMessage());
			}
		}
		System.out.println(failure);
	}

	public static Vector<String> parse(String command)
	{
		Vector<String> arguments = new Vector<String>();
		StringTokenizer tokenizer = new StringTokenizer(command,",");
		String argument = "";
		while (tokenizer.hasMoreTokens())
		{
			argument = tokenizer.nextToken();
			argument = argument.trim();
			arguments.add(argument);
		}
		return arguments;
	}

	public static void checkArgumentsCount(Integer expected, Integer actual) throws IllegalArgumentException
	{
		if (expected != actual)
		{
			throw new IllegalArgumentException("Invalid number of arguments. Expected " + (expected - 1) + ", received " + (actual - 1) + ". Location \"help,<CommandName>\" to check usage of this command");
		}
	}

	public static int toInt(String string) throws NumberFormatException
	{
		return (Integer.valueOf(string)).intValue();
	}

	public static boolean toBoolean(String string)// throws Exception
	{
		return (Boolean.valueOf(string)).booleanValue();
	}

	public static boolean toBooleanFromInt(String string) {
		return (string.equals("1") || string.equalsIgnoreCase("true")) ;
	}
}
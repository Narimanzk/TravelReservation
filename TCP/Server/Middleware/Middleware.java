 package Middleware;

import Server.Common.*;
import java.io.*;
import java.util.HashMap;
import java.util.Vector;

public class Middleware extends ResourceManager {

    protected TCPMiddlewareClient flightResourceManager = null;
    protected TCPMiddlewareClient carResourceManager = null;
    protected TCPMiddlewareClient roomResourceManager = null;

    public Middleware(String p_name, String flightIP, int flightPort, String carIP, int carPort, String roomIP, int roomPort)
    {
        super(p_name);
        flightResourceManager = new TCPMiddlewareClient(flightIP,flightPort);
        carResourceManager = new TCPMiddlewareClient(carIP,carPort);
        roomResourceManager = new TCPMiddlewareClient(roomIP,roomPort);
    }

    // Disconnecting all the RM clients from their servers
    public void close() {
        flightResourceManager.stopTCPClient();
        carResourceManager.stopTCPClient();
        roomResourceManager.stopTCPClient();
    }

    public boolean addFlight(int xid, int flightNum, int seats, int price){
        Trace.info("addFlight in Middleware - Redirect to Flight Resource Manager");
        String command = String.format("AddFlight,%d,%d,%d,%d",xid,flightNum,seats,price);
        try {
	    return Boolean.parseBoolean(send(flightResourceManager,'B',command));
        } catch (Exception e) {
            System.out.println("An error occured while getting response from the resource manager server: ");
            e.printStackTrace();
            return false;
        }
    }

    public boolean addCars(int xid, String location, int count, int price)
	{
		Trace.info("addCars in Middleware - Redirect to Car Resource Manager");
        String command = String.format("AddCars,%d,%s,%d,%d",xid,location,count,price);
        try {
	        return Boolean.parseBoolean(send(carResourceManager,'B',command));
        } catch (Exception e) {
            System.out.println("An error occured while getting response from the resource manager server");
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean addRooms(int xid, String location, int count, int price)
	{
		Trace.info("addRooms in Middleware - Redirect to Room Resource Manager");
        String command = String.format("AddRooms,%d,%s,%d,%d",xid,location,count,price);
        try {
	    return Boolean.parseBoolean(send(roomResourceManager,'B',command));
        } catch (Exception e) {
            System.out.println("An error occured while getting response from the resource manager server");
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteFlight(int xid, int flightNum){
        Trace.info("deleteFlight in Middleware - Redirect to Flight Resource Manager");
        String command = String.format("DeleteFlight,%d,%d",xid,flightNum);
        try {
	    return Boolean.parseBoolean(send(flightResourceManager,'B',command));
        } catch (Exception e) {
            System.out.println("An error occured while getting response from the resource manager server: ");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCars(int xid, String location)
	{
		Trace.info("deleteCars in Middleware - Redirect to Car Resource Manager");
        String command = String.format("DeleteCars,%d,%s",xid,location);
        try {
	    return Boolean.parseBoolean(send(carResourceManager,'B',command));
        } catch (Exception e) {
            System.out.println("An error occured while getting response from the resource manager server");
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteRooms(int xid, String location)
	{
		Trace.info("deleteRooms in Middleware - Redirect to Room Resource Manager");
        String command = String.format("DeleteRooms,%d,%s",xid,location);
        try {
	    return Boolean.parseBoolean(send(roomResourceManager,'B',command));
        } catch (Exception e) {
            System.out.println("An error occured while getting response from the resource manager server");
            e.printStackTrace();
            return false;
        }
	}

    public int queryFlight(int xid, int flightNum){
        Trace.info("queryFlight in Middleware - Redirect to Flight Resource Manager");
        String command = String.format("QueryFlight,%d,%d",xid,flightNum);
        try {
	    return Integer.parseInt(send(flightResourceManager,'I',command));
        } catch (Exception e) {
            System.out.println("An error occured while getting response from the resource manager server: ");
            e.printStackTrace();
            return -1;
        }
    }

    public int queryCars(int xid, String location){
        Trace.info("queryCars in Middleware - Redirect to Car Resource Manager");
        String command = String.format("QueryCars,%d,%s",xid,location);
        try {
	    return Integer.parseInt(send(carResourceManager,'I',command));
        } catch (Exception e) {
            System.out.println("An error occured while getting response from the resource manager server: ");
            e.printStackTrace();
            return -1;
        }
    }
    
    public int queryRooms(int xid, String location){
        Trace.info("queryRooms in Middleware - Redirect to Room Resource Manager");
        String command = String.format("QueryRooms,%d,%s",xid,location);
        try {
	    return Integer.parseInt(send(roomResourceManager,'I',command));
        } catch (Exception e) {
            System.out.println("An error occured while getting response from the resource manager server: ");
            e.printStackTrace();
            return -1;
        }
    }

    public int queryFlightPrice(int xid, int flightNum){
        Trace.info("queryFlightPrice in Middleware - Redirect to Flight Resource Manager");
        String command = String.format("QueryFlightPrice,%d,%d",xid,flightNum);
        try {
	    return Integer.parseInt(send(flightResourceManager,'I',command));
        } catch (Exception e) {
            System.out.println("An error occured while getting response from the resource manager server: ");
            e.printStackTrace();
            return -1;
        }
    }

    public int queryCarsPrice(int xid, String location){
        Trace.info("queryCarsPrice in Middleware - Redirect to Car Resource Manager");
        String command = String.format("QueryCarsPrice,%d,%s",xid,location);
        try {
	    return Integer.parseInt(send(carResourceManager,'I',command));
        } catch (Exception e) {
            System.out.println("An error occured while getting response from the resource manager server: ");
            e.printStackTrace();
            return -1;
        }
    }
    
    public int queryRoomsPrice(int xid, String location){
        Trace.info("queryRoomsPrice in Middleware - Redirect to Room Resource Manager");
        String command = String.format("QueryRoomsPrice,%d,%s",xid,location);
        try {
	    return Integer.parseInt(send(roomResourceManager,'I',command));
        } catch (Exception e) {
            System.out.println("An error occured while getting response from the resource manager server: ");
            e.printStackTrace();
            return -1;
        }
    }

    public boolean reserveFlight(int xid, int customerID, int flightNumber) {
        String key = Flight.getKey(flightNumber);

        Trace.info("RM::reserveFlight(" + xid + ", customer=" + customerID + ", " + key + ") called" );

        // Check customer exists
        Customer customer = (Customer)readData(xid, Customer.getKey(customerID));
        if (customer == null)
        {
            Trace.warn("RM::reserveItem(" + xid + ", " + customerID + ", " + flightNumber + ")  failed as customer doesn't exist");
            return false;
        }
        synchronized (customer) {
            synchronized (flightResourceManager) {
                // Checking if flight is avialable and getting the price of it
                int price = -1;
                try {
                    String command = String.format("ItemsExist,%d,%s,%d", xid, key, 1);
                    // If anything goes wrong, price would be -1
                    price = Integer.parseInt(flightResourceManager.sendMessage(command));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (price < 0) {
                    Trace.warn("RM::reserveItem(" + xid + ", " + customerID + ", " + flightNumber + ")  failed--item unavailable");
                    return false;
                }

                boolean reserved = false;

                try {
                    String command = String.format("ReserveFlight,%d,%d,%d", xid, customerID, flightNumber);
                    reserved = Boolean.parseBoolean(flightResourceManager.sendMessage(command));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Updating customer with the new reservation
                if (reserved) {
                    customer.reserve(key, String.valueOf(flightNumber), price);
                    writeData(xid, customer.getKey(), customer);
                    return true;
                }
                Trace.warn("RM::reserveItem(" + xid + ", " + customerID + ", " + flightNumber + ")  failed--Could not reserve item");
                return false;
            }
        }
    }
    
    public boolean reserveCar(int xid, int customerID, String location) {
        String key = Car.getKey(location);

        Trace.info("RM::reserveCar(" + xid + ", customer=" + customerID + ", " + key + ") called" );

        // Check customer exists
        Customer customer = (Customer)readData(xid, Customer.getKey(customerID));
        if (customer == null)
        {
            Trace.warn("RM::reserveItem(" + xid + ", " + customerID + ", " + location + ")  failed as customer doesn't exist");
            return false;
        }
        synchronized (customer) {
            synchronized (carResourceManager) {
                // Checking if car is avialable and getting the price of it
                int price = -1;
                try {
                    String command = String.format("ItemsExist,%d,%s,%d", xid, key, 1);
                    // If anything goes wrong, price would be -1
                    price = Integer.parseInt(carResourceManager.sendMessage(command));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (price < 0) {
                    Trace.warn("RM::reserveItem(" + xid + ", " + customerID + ", " + location + ")  failed--item unavailable");
                    return false;
                }

                boolean reserved = false;

                try {
                    String command = String.format("ReserveCar,%d,%d,%s", xid, customerID, location);
                    reserved = Boolean.parseBoolean(carResourceManager.sendMessage(command));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Updating customer with the new reservation
                if (reserved) {
                    customer.reserve(key, location, price);
                    writeData(xid, customer.getKey(), customer);
                    return true;
                }
                Trace.warn("RM::reserveItem(" + xid + ", " + customerID + ", " + location + ")  failed--Could not reserve item");
                return false;
            }
        }
    }
    
    public boolean reserveRoom(int xid, int customerID, String location) {
        String key = Room.getKey(location);

        Trace.info("RM::reserveRoom(" + xid + ", customer=" + customerID + ", " + key + ") called" );

        // Check customer exists
        Customer customer = (Customer)readData(xid, Customer.getKey(customerID));
        if (customer == null)
        {
            Trace.warn("RM::reserveItem(" + xid + ", " + customerID + ", " + location + ")  failed as customer doesn't exist");
            return false;
        }
        synchronized (customer) {
            synchronized (roomResourceManager) {
                // Checking if room is avialable and getting the price of it
                int price = -1;
                try {
                    String command = String.format("ItemsExist,%d,%s,%d", xid, key, 1);
                    // If anything goes wrong, price would be -1
                    price = Integer.parseInt(roomResourceManager.sendMessage(command));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (price < 0) {
                    Trace.warn("RM::reserveItem(" + xid + ", " + customerID + ", " + location + ")  failed--item unavailable");
                    return false;
                }

                boolean reserved = false;

                try {
                    String command = String.format("ReserveRoom,%d,%d,%s", xid, customerID, location);
                    reserved = Boolean.parseBoolean(roomResourceManager.sendMessage(command));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Updating customer with the new reservation
                if (reserved) {
                    customer.reserve(key, location, price);
                    writeData(xid, customer.getKey(), customer);
                    return true;
                }
                Trace.warn("RM::reserveItem(" + xid + ", " + customerID + ", " + location + ")  failed--Could not reserve item");
                return false;
            }
        }
    }

    public boolean deleteCustomer(int xid, int customerID) {
        Trace.info("RM::deleteCustomer(" + xid + ", " + customerID + ") called");
        
        // Checking if the customer exists
        Customer customer = (Customer)readData(xid, Customer.getKey(customerID));
        if (customer == null) {
            Trace.warn("RM::deleteCustomer(" + xid + ", " + customerID + ") failed as customer doesn't exist");
            return false;
        }
        else { // Updating all the reservations that the customer made
            synchronized (customer) {
                RMHashMap reservations = customer.getReservations();

                for (String reservedKey : reservations.keySet()) {
                    String resourceType = reservedKey.split("-")[0];
                    ReservedItem reserveditem = customer.getReservedItem(reservedKey);
                    String command = String.format("RemoveReservation,%d,%d,%s,%d", xid, customerID, reserveditem.getKey(), reserveditem.getCount());

                    if (resourceType.equals("flight")) {
                        synchronized (flightResourceManager) {
                            try {
                                flightResourceManager.sendMessage(command);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (resourceType.equals("car")) {
                        synchronized (carResourceManager) {
                            try {
                                carResourceManager.sendMessage(command);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (resourceType.equals("room")) {
                        synchronized (roomResourceManager) {
                            try {
                                roomResourceManager.sendMessage(command);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else
                        Trace.error("RM::deleteCustomer(" + xid + ", " + customerID + ") failed--reservedKey (" + reservedKey + ") wasn't of expected type.");
                }
                // Remove the customer from the storage
                removeData(xid, customer.getKey());
                Trace.info("RM::deleteCustomer(" + xid + ", " + customerID + ") succeeded");
            }
            return true;
        }

    }
    
    public boolean bundle(int xid, int customerID, Vector<String> flightNumbers, String location, boolean car, boolean room) {
        
        Trace.info("RM::bundle(" + xid + ", customer=" + customerID + ", " + flightNumbers.toString() + ", " + location + ") called" );
        System.out.println("Debug - in Middleare car is " + car + " room is " + room);
        Customer customer = (Customer)readData(xid, Customer.getKey(customerID));
        if (customer == null)
        {
            Trace.warn("RM:bundle(" + xid + ", customer=" + customerID + ", " + flightNumbers.toString() + ", " + location + ")  failed--customer doesn't exist");
            return false;
        }
        synchronized (customer) {
            HashMap<String, Integer> flighCount = new HashMap<String, Integer>();

            for (String flightNumber : flightNumbers) {
                if (flighCount.containsKey(flightNumber))
                flighCount.put(flightNumber, flighCount.get(flightNumber) + 1);
                else
                flighCount.put(flightNumber, 1);
            }
            HashMap<Integer, Integer> flightPrice = new HashMap<Integer, Integer>();
            int carPrice = -1;
            int roomPrice = -1;

            // We always check for the avialability of all items before reserving any of them
            synchronized (flightResourceManager) {
                if (car && room) {
                    synchronized (carResourceManager) {
                        synchronized (roomResourceManager) {
                            for (String key : flighCount.keySet()) {
                                int keyInt; // flight number
                                try {
                                    keyInt = Integer.parseInt(key);
                                } catch (Exception e) {
                                    Trace.warn("RM:bundle(" + xid + ", customer=" + customerID + ", " + flightNumbers.toString() + ", " + location + ")  failed--could not parse flightNumber");
                                    return false;
                                }
                                int price = -1;
                                try {
                                    String command = String.format("ItemsExist,%d,%s,%d", xid, Flight.getKey(keyInt), flighCount.get(key));
                                    System.out.println("Debug - in Middleare command sending to flightmanager " + command);
                                    price = Integer.parseInt(flightResourceManager.sendMessage(command));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (price < 0) {
                                    Trace.warn("RM:bundle(" + xid + ", customer=" + customerID + ", " + flightNumbers.toString() + ", " + location + ")  failed--flight-" + key + " doesn't have enough spots");
                                    return false;
                                } else {
                                    flightPrice.put(keyInt, price);
                                }
                            }
                            carPrice = bundleHelperReserveCar(xid, customerID, location);
                            if (carPrice < 0) {
                                return false;
                            } else {
                                customer.reserve(Car.getKey(location), location, carPrice);
                                writeData(xid, customer.getKey(), customer);
                            }
                            roomPrice = bundleHelperReserveRoom(xid, customerID, location);
                            if (roomPrice < 0) {
                                return false;
                            } else {
                                customer.reserve(Room.getKey(location), location, roomPrice);
                                writeData(xid, customer.getKey(), customer);
                            }
                        }
                    }
                } else if (car) {
                    synchronized (carResourceManager) {
                        carPrice = bundleHelperReserveCar(xid, customerID, location);
                        for (String key : flighCount.keySet()) {
                            int keyInt; // flight number
                            try {
                                keyInt = Integer.parseInt(key);
                            } catch (Exception e) {
                                Trace.warn("RM:bundle(" + xid + ", customer=" + customerID + ", " + flightNumbers.toString() + ", " + location + ")  failed--could not parse flightNumber");
                                return false;
                            }
                            int price = -1;
                            try {
                                String command = String.format("ItemsExist,%d,%s,%d", xid, Flight.getKey(keyInt), flighCount.get(key));
                                price = Integer.parseInt(flightResourceManager.sendMessage(command));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (price < 0) {
                                Trace.warn("RM:bundle(" + xid + ", customer=" + customerID + ", " + flightNumbers.toString() + ", " + location + ")  failed--flight-" + key + " doesn't have enough spots");
                                return false;
                            } else {
                                flightPrice.put(keyInt, price);
                            }
                        }
                        if (carPrice < 0) {
                            return false;
                        } else {
                            customer.reserve(Car.getKey(location), location, carPrice);
                            writeData(xid, customer.getKey(), customer);
                        }
                    }
                } else if (room) {
                    synchronized (roomResourceManager) {
                        for (String key : flighCount.keySet()) {
                            int keyInt; // flight number
                            try {
                                keyInt = Integer.parseInt(key);
                            } catch (Exception e) {
                                Trace.warn("RM:bundle(" + xid + ", customer=" + customerID + ", " + flightNumbers.toString() + ", " + location + ")  failed--could not parse flightNumber");
                                return false;
                            }
                            int price = -1;
                            try {
                                String command = String.format("ItemsExist,%d,%s,%d", xid, Flight.getKey(keyInt), flighCount.get(key));
                                price = Integer.parseInt(flightResourceManager.sendMessage(command));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (price < 0) {
                                Trace.warn("RM:bundle(" + xid + ", customer=" + customerID + ", " + flightNumbers.toString() + ", " + location + ")  failed--flight-" + key + " doesn't have enough spots");
                                return false;
                            } else {
                                flightPrice.put(keyInt, price);
                            }
                        }
                        roomPrice = bundleHelperReserveRoom(xid, customerID, location);
                        if (roomPrice < 0) {
                            return false;
                        } else {
                            customer.reserve(Room.getKey(location), location, roomPrice);
                            writeData(xid, customer.getKey(), customer);
                        }
                    }
                }
                else {
                    for (String key : flighCount.keySet()) {
                        int keyInt; // flight number
                        try {
                            keyInt = Integer.parseInt(key);
                        } catch (Exception e) {
                            Trace.warn("RM:bundle(" + xid + ", customer=" + customerID + ", " + flightNumbers.toString() + ", " + location + ")  failed--could not parse flightNumber");
                            return false;
                        }
                        int price = -1;
                        try {
                            String command = String.format("ItemsExist,%d,%s,%d", xid, Flight.getKey(keyInt), flighCount.get(key));
                            System.out.println("Debug - in Middleare command sending to flightmanager " + command);
                            price = Integer.parseInt(flightResourceManager.sendMessage(command));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (price < 0) {
                            Trace.warn("RM:bundle(" + xid + ", customer=" + customerID + ", " + flightNumbers.toString() + ", " + location + ")  failed--flight-" + key + " doesn't have enough spots");
                            return false;
                        } else {
                            flightPrice.put(keyInt, price);
                        }
                    }
                }
                
                // If everything up until here was fine, we can reserve the flights
                for (Integer key : flightPrice.keySet()) {
                    for (int i = 0; i < flighCount.get(String.valueOf(key)); i++) {
                        int price = flightPrice.get(key);

                        //m_flightResourceManager.reserveFlight(xid, customerID, key);
                        try {
                            Boolean.parseBoolean(flightResourceManager.sendMessage(String.format("ReserveFlight,%d,%d,%d", xid, customerID, key)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        customer.reserve(Flight.getKey(key), String.valueOf(key), price);
                        writeData(xid, customer.getKey(), customer);
                    }
                }
            Trace.info("RM:bundle() -- succeeded");
            return true;
        }
    }
    }

    private int bundleHelperReserveCar(int xid, int customerID, String location) {
        int price = -1;
        try {
            String command = String.format("ItemsExist,%d,%s,%d", xid, Car.getKey(location), 1);
            price = Integer.parseInt(carResourceManager.sendMessage(command));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (price < 0) {
            Trace.warn("RM:bundle(" + xid + ", customer=" + customerID  + ", " + location + ")  failed--car-" + location + " is not available");
            return -1;
        }
        try {
            Boolean.parseBoolean(carResourceManager.sendMessage(String.format("ReserveCar,%d,%d,%s", xid, customerID, location)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }

    private int bundleHelperReserveRoom(int xid, int customerID, String location) {
        int price = -1;
        try {
            String command = String.format("ItemsExist,%d,%s,%d", xid, Room.getKey(location), 1);
            price = Integer.parseInt(roomResourceManager.sendMessage(command));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (price < 0) {
            Trace.warn("RM:bundle(" + xid + ", customer=" + customerID  + ", " + location + ")  failed--room-" + location + " is not available");
            return -1;
        }
        try {
            Boolean.parseBoolean(roomResourceManager.sendMessage(String.format("ReserveRoom,%d,%d,%s", xid, customerID, location)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }
    
    // Middleware sending a message to RM clients
    // The send is async => 
    // Middleware will not block when it is waiting for the ResourceManagers to execute a request.
    private String send(TCPMiddlewareClient rm, char returnType, String command) {
        String res;
        try {
            res = rm.sendMessage(command);
            if (res.equals(""))
                throw new IOException();
            return res;
        } catch (IOException e) {
            // Connecting the resource managers to their servers 
            rm.connect();
            try {
                return rm.sendMessage(command);
            } catch (IOException e1) {
                System.err.println("An error occured while sending the command from middleware");
                e1.printStackTrace();
                return "";
            }
        }
    }

    public String getName() {
        return m_name;
    }
}
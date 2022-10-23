package Middleware;

import Server.Common.*;
import Server.Common.ResourceManager;
import Server.Common.Trace;
import Server.Interface.IResourceManager;

import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

import java.util.Vector;
import java.util.*;

public class Middleware extends ResourceManager{

    protected IResourceManager flightResourceManager = null;
    protected IResourceManager roomResourceManager = null;
    protected IResourceManager carResourceManager = null;

    private static String s_rmiPrefix = "group_07_";

    public Middleware(String p_name) {
        super(p_name);
    }


    public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice) throws RemoteException {
        synchronized (flightResourceManager) {
            try {
                // TODO : Maybe we can add another attempt to connect to the fight server before throwing the error
                Trace.info("addFlight in Middleware - Invoking AddFlight in FlightResourceManager");
                return flightResourceManager.addFlight(id, flightNum, flightSeats, flightPrice);
            } catch (RemoteException e) {
                Trace.error(e.toString());
                return false;
            }
        }
    }

    public boolean addCars(int id, String location, int numCars, int price) throws RemoteException {
        synchronized (carResourceManager) {
            try {
                // TODO : Maybe we can add another attempt to connect to the fight server before throwing the error
                Trace.info("addCars in Middleware - Invoking addCars in CarResourceManager");
                return carResourceManager.addCars(id, location, numCars, price);
            } catch (RemoteException e) {
                Trace.error(e.toString());
                return false;
            }
        }
    }

    public boolean addRooms(int id, String location, int numRooms, int price) throws RemoteException {
        synchronized (roomResourceManager) {
            try {
                // TODO : Maybe we can add another attempt to connect to the fight server before throwing the error
                Trace.info("addRooms in Middleware - Invoking addRooms in roomResourceManager");
                return roomResourceManager.addRooms(id, location, numRooms, price);
            } catch (RemoteException e) {
                Trace.error(e.toString());
                return false;
            }
        }
    }

    public boolean deleteFlight(int id, int flightNum) throws RemoteException {
        synchronized (flightResourceManager) {
            try {
                // TODO : Maybe we can add another attempt to connect to the fight server before throwing the error
                Trace.info("deleteFlight in Middleware - Invoking deleteFlight in flightResourceManager");
                return flightResourceManager.deleteFlight(id, flightNum);
            } catch (RemoteException e) {
                Trace.error(e.toString());
                return false;
            }
        }
    }

    public boolean deleteCars(int id, String location) throws RemoteException {
        synchronized (carResourceManager) {
            try {
                // TODO : Maybe we can add another attempt to connect to the fight server before throwing the error
                Trace.info("deleteCars in Middleware - Invoking deleteCars in carResourceManager");
                return carResourceManager.deleteCars(id, location);
            } catch (RemoteException e) {
                Trace.error(e.toString());
                return false;
            }
        }
    }

    public boolean deleteRooms(int id, String location) throws RemoteException {
        synchronized (roomResourceManager) {
            try {
                // TODO : Maybe we can add another attempt to connect to the fight server before throwing the error
                Trace.info("deleteRooms in Middleware - Invoking deleteRooms in roomResourceManager");
                return roomResourceManager.deleteRooms(id, location);
            } catch (RemoteException e) {
                Trace.error(e.toString());
                return false;
            }
        }
    }

    public boolean deleteCustomer(int id, int customerID) throws RemoteException {
        Trace.info("RM::deleteCustomer(" + id + ", " + customerID + ") called");
        Customer customer = (Customer)readData(id, Customer.getKey(customerID));
        if (Objects.isNull(customer))
        {
            Trace.warn("RM::deleteCustomer(" + id + ", " + customerID + ") failed: customer cannot be found");
            return false;
        }
        else {
            synchronized (customer) {
            RMHashMap reservations = customer.getReservations();
            for (String key : reservations.keySet()) {
                String type = key.split("-")[0];
                ReservedItem reserveditem = customer.getReservedItem(key);
                switch(type){
                    case "flight":
                        synchronized (flightResourceManager) {
                        flightResourceManager.cancelReservation(id, customerID, reserveditem.getKey(), reserveditem.getCount());
                        }
                        break;
                    case "car":
                        synchronized (carResourceManager) {
                        carResourceManager.cancelReservation(id, customerID, reserveditem.getKey(), reserveditem.getCount());
                        }
                        break;
                    case "room":
                        synchronized (roomResourceManager) {
                        roomResourceManager.cancelReservation(id, customerID, reserveditem.getKey(), reserveditem.getCount());
                        }
                        break;
                }
            }
            removeData(id, customer.getKey());
            Trace.info("RM::deleteCustomer(" + id + ", " + customerID + ") succeessfully");
            return true;
            }
        }
    }

    public int queryFlight(int id, int flightNumber) throws RemoteException {
        synchronized (flightResourceManager) {
            try {
                // TODO : Maybe we can add another attempt to connect to the fight server before throwing the error
                Trace.info("queryFlight in Middleware - Invoking queryFlight in flightResourceManager");
                return flightResourceManager.queryFlight(id, flightNumber);
            } catch (RemoteException e) {
                Trace.error(e.toString());
                return -1;
            }
        }
    }

    public int queryCars(int id, String location) throws RemoteException {
        synchronized (carResourceManager) {
            try {
                // TODO : Maybe we can add another attempt to connect to the fight server before throwing the error
                Trace.info("queryCars in Middleware - Invoking queryCars in carResourceManager");
                return carResourceManager.queryCars(id, location);
            } catch (RemoteException e) {
                Trace.error(e.toString());
                return -1;
            }
        }
    }

    public int queryRooms(int id, String location) throws RemoteException {
        synchronized (roomResourceManager) {
            try {
                // TODO : Maybe we can add another attempt to connect to the fight server before throwing the error
                Trace.info("queryRooms in Middleware - Invoking queryRooms in roomResourceManager");
                return roomResourceManager.queryRooms(id, location);
            } catch (RemoteException e) {
                Trace.error(e.toString());
                return -1;
            }
        }
    }

    public int queryFlightPrice(int id, int flightNumber) throws RemoteException {
        synchronized (flightResourceManager) {
            try {
                // TODO : Maybe we can add another attempt to connect to the fight server before throwing the error
                Trace.info("queryFlightPrice in Middleware - Invoking queryFlightPrice in flightResourceManager");
                return flightResourceManager.queryFlightPrice(id, flightNumber);
            } catch (RemoteException e) {
                Trace.error(e.toString());
                return -1;
            }
        }
    }

    public int queryCarsPrice(int id, String location) throws RemoteException {
        synchronized (carResourceManager) {
            try {
                // TODO : Maybe we can add another attempt to connect to the fight server before throwing the error
                Trace.info("queryCarsPrice in Middleware - Invoking queryCarsPrice in carResourceManager");
                return carResourceManager.queryCarsPrice(id, location);
            } catch (RemoteException e) {
                Trace.error(e.toString());
                return -1;
            }
        }
    }

    public int queryRoomsPrice(int id, String location) throws RemoteException {
        synchronized (roomResourceManager) {
            try {
                // TODO : Maybe we can add another attempt to connect to the fight server before throwing the error
                Trace.info("queryRoomsPrice in Middleware - Invoking queryRoomsPrice in roomResourceManager");
                return roomResourceManager.queryRoomsPrice(id, location);
            } catch (RemoteException e) {
                Trace.error(e.toString());
                return -1;
            }
        }
    }

    public boolean reserveFlight(int id, int customerID, int flightNumber) throws RemoteException {

        Trace.info("RM::reserveFlight(" + id + ", " + customerID + ", " + flightNumber + ") called" );
 
        String flight = Flight.getKey(flightNumber);
        Customer customer = (Customer)readData(id, Customer.getKey(customerID));
        if (Objects.isNull(customer))
        {
            Trace.warn("RM::reserveFlight(" + id + ", " + customerID + ", " + flightNumber + ")  failed--customer not found");
            return false;
        }
        synchronized (flightResourceManager) {
            int price = flightResourceManager.itemsAvailable(id, flight, 1);

            if (price < 0) 
            {
                Trace.warn("RM::reserveFlight(" + id + ", " + customerID + ", " + flightNumber + ")  failed--flight is not available");
                return false;
            }
            synchronized (customer) {
                if (flightResourceManager.reserveFlight(id, customerID, flightNumber)) {
                    customer.reserve(flight, String.valueOf(flightNumber), price);
                    writeData(id, customer.getKey(), customer);
                    return true;
                }
                Trace.warn("RM::reserveFlight(" + id + ", " + customerID + ", " + flightNumber + ")  failed--flight cannot be reserved");
                return false;
            }
        }
    }

    public boolean reserveCar(int id, int customerID, String location) throws RemoteException {

        Trace.info("RM::reserveCar(" + id + ", " + customerID + ", " + location + ") called" );
        
        String car = Car.getKey(location);
        Customer customer = (Customer)readData(id, Customer.getKey(customerID));
        if (Objects.isNull(customer))
        {
            Trace.warn("RM::reserveCar" + id + ", " + customerID + ", " + location + ")  failed--customer not found");
            return false;
        }
        synchronized (carResourceManager) {
            int price = carResourceManager.itemsAvailable(id, car, 1);

            if (price < 0) {
                Trace.warn("RM::reserveCar(" + id + ", " + customerID + ", " + location + ")  failed--car is not available");
                return false;
            }
            synchronized (customer) {
                if (carResourceManager.reserveCar(id, customerID, location)) {
                    customer.reserve(car, location, price);
                    writeData(id, customer.getKey(), customer);
                    return true;
                }
                Trace.warn("RM::reserveCar(" + id + ", " + customerID + ", " + location + ")  failed--car cannot be reserved");
                return false;
            }
        }
    }

    public boolean reserveRoom(int id, int customerID, String location) throws RemoteException {

        Trace.info("RM::reserveRoom(" + id + ", " + customerID + ", " + location + ") called" );
  
        String room = Room.getKey(location);
        Customer customer = (Customer)readData(id, Customer.getKey(customerID));
        if (Objects.isNull(customer))
        {
            Trace.warn("RM::reserveRoom(" + id + ", " + customerID + ", " + location + ")  failed--customer not found");
            return false;
        }
        synchronized (roomResourceManager) {
            int price = roomResourceManager.itemsAvailable(id, room, 1);

            if (price < 0) {
                Trace.warn("RM::reserveRoom(" + id + ", " + customerID + ", " + location + ")  failed--room is not available");
                return false;
            }
            synchronized (customer) {
                if (roomResourceManager.reserveRoom(id, customerID, location)) {
                    customer.reserve(room, location, price);
                    writeData(id, customer.getKey(), customer);
                    return true;
                }
                Trace.warn("RM::reserveRoom(" + id + ", " + customerID + ", " + location + ")  failed--room cannot be reserved");
                return false;
            }
        }
    }

    public boolean bundle(int id, int customerID, Vector<String> flightNumbers, String location, boolean car, boolean room) throws RemoteException {

        Trace.info("RM::bundle(" + id + ", " + customerID + ", " + flightNumbers.toString() + ", " + location + ", " + "car= " + car + ", " + "room= " + room + ") called" );
        Customer customer = (Customer)readData(id, Customer.getKey(customerID));
        if (Objects.isNull(customer))
        {
            Trace.warn("RM::bundle(" + id + ", " + customerID + ", " + flightNumbers.toString() + ", " + location + ", " + "car= " + car + ", " + "room= " + room + ")  failed--customer not found");
            return false;
        }
        synchronized (customer) {

            synchronized (flightResourceManager) {
                HashMap<String, Integer> countMap = countFlights(flightNumbers);
                HashMap<Integer, Integer> flightPrice = new HashMap<Integer, Integer>();
                int roomPrice, carPrice;

                if (car && room) {
                    synchronized (carResourceManager) {
                        synchronized (roomResourceManager) {

                            for (String key : countMap.keySet()) {
                                int keyInt;
                                try {
                                    keyInt = Integer.parseInt(key);
                                } catch (Exception e) {
                                    Trace.warn("RM::bundle(" + id + ", " + customerID + ", " + flightNumbers.toString() + ", " + location + ", " + "car= " + car + ", " + "room= " + room + ")  failed--flight number is not int");
                                    return false;
                                }

                                int price = flightResourceManager.itemsAvailable(id, Flight.getKey(keyInt), countMap.get(key));

                                if (price < 0) {
                                    Trace.warn("RM::bundle(" + id + ", " + customerID + ", " + flightNumbers.toString() + ", " + location + ", " + "car= " + car + ", " + "room= " + room + ")  failed--flight-" + key + " is not available");
                                    return false;
                                } else {
                                    flightPrice.put(keyInt, price);
                                }
                            }

                            carPrice = carResourceManager.itemsAvailable(id, Car.getKey(location), 1);

                            if (carPrice < 0) {
                                Trace.warn("RM::bundle(" + id + ", " + customerID + ", " + flightNumbers.toString() + ", " + location + ", " + "car= " + car + ", " + "room= " + room + ")  failed--car-" + location + " is not available");
                                return false;
                            }


                            roomPrice = roomResourceManager.itemsAvailable(id, Room.getKey(location), 1);

                            if (roomPrice < 0) {
                                Trace.warn("RM::bundle(" + id + ", " + customerID + ", " + flightNumbers.toString() + ", " + location + ", " + "car= " + car + ", " + "room= " + room + ")  failed--room-" + location + " is not available");
                                return false;
                            }

                            roomResourceManager.reserveRoom(id, customerID, location);
                            customer.reserve(Room.getKey(location), location, roomPrice);
                            writeData(id, customer.getKey(), customer);


                            carResourceManager.reserveCar(id, customerID, location);
                            customer.reserve(Car.getKey(location), location, carPrice);
                            writeData(id, customer.getKey(), customer);

                        }
                    }

                } else if (car) {
                    synchronized (carResourceManager) {

                        for (String key : countMap.keySet()) {
                            int keyInt;
                            try {
                                keyInt = Integer.parseInt(key);
                            } catch (Exception e) {
                                Trace.warn("RM::bundle(" + id + ", " + customerID + ", " + flightNumbers.toString() + ", " + location + ", " + "car= " + car + ", " + "room= " + room + ")  failed--flight number is not int");
                                return false;
                            }

                            int price = flightResourceManager.itemsAvailable(id, Flight.getKey(keyInt), countMap.get(key));

                            if (price < 0) {
                                Trace.warn("RM::bundle(" + id + ", " + customerID + ", " + flightNumbers.toString() + ", " + location + ", " + "car= " + car + ", " + "room= " + room + ")  failed--flight-" + key + " is not available");
                                return false;
                            } else {
                                flightPrice.put(keyInt, price);
                            }
                        }

                        carPrice = carResourceManager.itemsAvailable(id, Car.getKey(location), 1);

                        if (carPrice < 0) {
                            Trace.warn("RM::bundle(" + id + ", " + customerID + ", " + flightNumbers.toString() + ", " + location + ", " + "car= " + car + ", " + "room= " + room + ")  failed--car-" + location + " is not available");
                            return false;
                        }
                        carResourceManager.reserveCar(id, customerID, location);
                        customer.reserve(Car.getKey(location), location, carPrice);
                        writeData(id, customer.getKey(), customer);

                    }
                } else if (room) {
                    synchronized (roomResourceManager) {
                        for (String key : countMap.keySet()) {
                            int keyInt;
                            try {
                                keyInt = Integer.parseInt(key);
                            } catch (Exception e) {
                                Trace.warn("RM::bundle(" + id + ", " + customerID + ", " + flightNumbers.toString() + ", " + location + ", " + "car= " + car + ", " + "room= " + room + ")  failed--flight number is not int");
                                return false;
                            }

                            int price = flightResourceManager.itemsAvailable(id, Flight.getKey(keyInt), countMap.get(key));

                            if (price < 0) {
                                Trace.warn("RM::bundle(" + id + ", " + customerID + ", " + flightNumbers.toString() + ", " + location + ", " + "car= " + car + ", " + "room= " + room + ")  failed--flight-" + key + " is not available");
                                return false;
                            } else {
                                flightPrice.put(keyInt, price);
                            }
                        }

                        roomPrice = roomResourceManager.itemsAvailable(id, Room.getKey(location), 1);

                        if (roomPrice < 0) {
                            Trace.warn("RM::bundle(" + id + ", " + customerID + ", " + flightNumbers.toString() + ", " + location + ", " + "car= " + car + ", " + "room= " + room + ")  failed--room-" + location + " is not available");
                            return false;
                        }

                        roomResourceManager.reserveRoom(id, customerID, location);
                        customer.reserve(Room.getKey(location), location, roomPrice);
                        writeData(id, customer.getKey(), customer);
                    }
                }
                else{
                    for (String key : countMap.keySet()) {
                        int keyInt;
                        try {
                            keyInt = Integer.parseInt(key);
                        } catch (Exception e) {
                            Trace.warn("RM::bundle(" + id + ", " + customerID + ", " + flightNumbers.toString() + ", " + location + ", " + "car= " + car + ", " + "room= " + room + ")  failed--flight number is not int");
                            return false;
                        }

                        int price = flightResourceManager.itemsAvailable(id, Flight.getKey(keyInt), countMap.get(key));

                        if (price < 0) {
                            Trace.warn("RM::bundle(" + id + ", " + customerID + ", " + flightNumbers.toString() + ", " + location + ", " + "car= " + car + ", " + "room= " + room + ")  failed--flight-" + key + " is not available");
                            return false;
                        } else {
                            flightPrice.put(keyInt, price);
                        }
                    }
                }

                for (Integer key : flightPrice.keySet()) {
                    for (int i = 0; i < countMap.get(String.valueOf(key)); i++) {
                        int price = flightPrice.get(key);

                        flightResourceManager.reserveFlight(id, customerID, key);
                        customer.reserve(Flight.getKey(key), String.valueOf(key), price);
                        writeData(id, customer.getKey(), customer);
                    }
                }

            }
            Trace.info("RM::bundle(" + id + ", " + customerID + ", " + flightNumbers.toString() + ", " + location + ", " + "car= " + car + ", " + "room= " + room + ") -- succeeded");
            return true;
        }
    }

    public String Analytics(int xid, int threshhold)
    {
        Trace.info("RM::analytics(" + xid + ", " + threshhold + ") called");
        String results = "";
        results += "Flights with low quantity:\n";
        try {
            results += flightResourceManager.Analytics(xid, threshhold);
        } catch (Exception e) {
            Trace.warn("RM::analytics(" + xid + ", " + threshhold + ") failed--flight quantity problem");
        }

        results += "Cars with low quantity:\n";
        try {
            results += carResourceManager.Analytics(xid, threshhold);
        } catch (Exception e) {
            Trace.warn("RM::analytics(" + xid + ", " + threshhold + ") failed--car quantity problem");
        }

        results += "Rooms with low quantity:\n";
        try {
            results += roomResourceManager.Analytics(xid, threshhold);
        } catch (Exception e) {
            Trace.warn("RM::analytics(" + xid + ", " + threshhold + ") failed--room quantity problem");
        }

        Trace.info("RM::analytics(" + xid + ", " + threshhold + ") succeeded");
        return results;
    }

    public String getName() throws RemoteException {
        return m_name;
    }

    protected HashMap<String, Integer> countFlights(Vector<String> flightNumbers) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        for (String flightNumber : flightNumbers) {
            if (map.containsKey(flightNumber))
                map.put(flightNumber, map.get(flightNumber) + 1);
            else
                map.put(flightNumber, 1);
        }
        return map;
    }

    

}

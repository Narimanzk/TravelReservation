package Server.RMI;

import Server.Interface.*;
import Server.Common.*;
import Middleware.Middleware;

import java.rmi.NotBoundException;
import java.util.*;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIMiddleware extends Middleware 
{
	public RMIMiddleware(String name)
{
	super(name);
}
    private static String s_serverName = "Middleware";
	private static String s_rmiPrefix = "group_07_";
	protected Registry registry = null;

	public static void main(String args[])
	{       
		// Create the RMI server entry
		try {
			//ensure user gave resource manager server information
			if(args.length != 3)
			{
				System.out.println("Exception: Incorrect number of arguments");
				throw new IllegalArgumentException();
			}
            //Create RMI middleware server instance
            RMIMiddleware middlewareServer = new RMIMiddleware(s_serverName);

			// start or connect to registry running on port 3007 of middleware server
			System.out.println("Step 1: Connect to registry where we expose middleware remote object");
			middlewareServer.connectRegistry();

			// Dynamically generate the stub (client proxy)
			System.out.println("Step 2: Add remote object stub to RMI registry");
			IResourceManager middlewareStub = (IResourceManager)UnicastRemoteObject.exportObject(middlewareServer, 0);

			// Add middleware stub to middleware's registry instance (make it available to clients)
			middlewareServer.bindToRegistry(middlewareStub);

			// connect to resource manager servers -> instantiate resourceManager object for each (flights, cars, rooms)
			System.out.println("Step 3: Connect to RM servers for flights, cars and rooms");
			middlewareServer.connectServers(args);
			
            //set security manager
            if (System.getSecurityManager() == null)
            {
                System.setSecurityManager(new SecurityManager());
            }
        }
		catch(IllegalArgumentException iae)
		{
			System.out.println("Please restart server and try again");
			System.exit(1);

		}
        catch(Exception e)
        {
            System.out.println("Exception encountered, exiting system.");
			e.printStackTrace();
            System.exit(1);
        }
    }

	// Set registry for this instance of RMImiddleware
	public void connectRegistry()
	{
		try{
			Registry l_registry;
			try {
				l_registry = LocateRegistry.createRegistry(3007);
			} catch (RemoteException e) {
				l_registry = LocateRegistry.getRegistry(3007);
			}
			registry = l_registry;
		}
		catch (Exception e) {
			System.err.println((char)27 + "[31;1mServer exception: " + (char)27 + "[0mUncaught exception");
			e.printStackTrace();
			System.exit(1);
		}
		
	}

    public void connectServers(String[] input)
    {
        for(int i=0; i<3; i++)
        {
            connectRMServer(input[i], i);
        }
        
    }

	public void connectRMServer(String hostname, int index)
    {
        //String rm_info is of format: server,port,name (seperated by commas) -> ex: lab7-2,1099,flights
		//each server runs the registry on 1099, so it should always be used as the port number
        String[] rm_info = hostname.split(",");
        connectServer(rm_info[0], Integer.parseInt(rm_info[1]), rm_info[2]);
    }

	// Connect to given server by looking up server stub in server's registry
	// This is done for Flights, Cars, Rooms, Customers servers
	public void connectServer(String server, int port, String name)
    {
		System.out.println("Connecting "+name+"on "+server);
        try {
			boolean first = true;
			while (true) {
				try {
					Registry remoteRegistry = LocateRegistry.getRegistry(server, port);
                    switch(name)
                    {
                        case "Flights": {
							//use registry we connected to above and find flightRM stub 
                        flightResourceManager = (IResourceManager)remoteRegistry.lookup(s_rmiPrefix + name);
                        break;
                        }
                        case "Cars": {
                        carResourceManager = (IResourceManager)remoteRegistry.lookup(s_rmiPrefix + name);
                        break;
                        }
                        case "Rooms": {
                        roomResourceManager = (IResourceManager)remoteRegistry.lookup(s_rmiPrefix + name);
                        break;
                        }                                            
                    }

					System.out.println("Connected to '" + name + "' server [" + server + ":" + port + "/" + s_rmiPrefix + name + "]");
					break;

				}
				catch (NotBoundException|RemoteException e) {
					if (first) {
						System.out.println("Waiting for '" + name + "' server [" + server + ":" + port + "/" + s_rmiPrefix + name + "]");
						first = false;
					}
				}
				Thread.sleep(500);
			}
		}
		catch (Exception e) {
			System.err.println((char)27 + "[31;1mServer exception: " + (char)27 + "[0mUncaught exception");
			e.printStackTrace();
			System.exit(1);
		}
    }

	// creates remote object stub and adds it to RMI registry
	public void bindToRegistry(IResourceManager stub)
	{
		// Create the RMI middleware entry in RMI registry
		try {
			
			// Bind the remote object's stub in the registry
			registry.rebind(s_rmiPrefix + s_serverName, stub);

			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						registry.unbind(s_rmiPrefix + s_serverName);
						System.out.println("'" + s_serverName + "' unbound");
					}
					catch(Exception e) {
						System.err.println((char)27 + "[31;1mServer exception: " + (char)27 + "[0mUncaught exception");
						e.printStackTrace();
					}
				}
			});                                       
			System.out.println("'" + s_serverName + "' server ready and bound to '" + s_rmiPrefix + s_serverName + "'");
		}
		catch (Exception e) {
			System.err.println((char)27 + "[31;1mServer exception: " + (char)27 + "[0mUncaught exception");
			e.printStackTrace();
			System.exit(1);
		}
	}


}
// -------------------------------
// adapted from Kevin T. Manley
// CSE 593
// -------------------------------

package Server.RMI;

import Server.Interface.*;
import Server.Common.*;

import java.rmi.NotBoundException;
import java.util.*;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIResourceManager extends ResourceManager 
{
	// default server name
	private static String s_serverName = "Server";
	// Ensures hostnames, etc. are unique
	private static String s_rmiPrefix = "group_07_";
	// local registry instance where we add server stub 
	protected Registry registry = null;

	public RMIResourceManager(String name)
	{
		super(name);
	}
	public static void main(String args[])
	{

		try {
			if (args.length > 0)
			{
				s_serverName = args[0];
			}
			// Create a new Server object
			RMIResourceManager server = new RMIResourceManager(s_serverName);
			// start or connect to registry running on port 3007 of server
			server.connectRegistry();
			// Dynamically generate the server stub (client proxy)
			IResourceManager serverStub = (IResourceManager)UnicastRemoteObject.exportObject(server, 0);
			// Add server stub to server's registry instance (make it available to clients)
            server.bindToRegistry(serverStub);

			// Create and install a security manager
			if (System.getSecurityManager() == null)
			{
				System.setSecurityManager(new SecurityManager());
			}
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

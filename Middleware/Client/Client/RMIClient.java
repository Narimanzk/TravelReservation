package Client;

import Server.Interface.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

import java.util.*;
import java.io.*;

public class RMIClient extends Client
{
	// Parameters used if following GettingStarted.PDF
	private static String s_serverHost = "localhost";
	private static int s_serverPort = 1099;
	private static String s_serverName = "Server";

	// Parameters used if connecting client to middleware
	private static String m_serverHost = "lab2-8";
	private static int m_serverPort = 3007;
	private static String m_serverName = "Middleware";

	// Ensures hostnames, etc. are unique
	private static String s_rmiPrefix = "group_07_";

	// Registry instance, gets set to the middleware registry running on middleware server
	protected Registry registry = null;

	public static void main(String args[])
	{	
		// args[0]: computer running middleware -> default: lab2-8
		if (args.length > 0)
		{
			m_serverHost = args[0];
		}
		// args[1]: server name -> default: Middleware 
		if (args.length > 1)
		{
			m_serverName = args[1];
		}

		if (args.length > 2)
		{
			System.err.println((char)27 + "[31;1mClient exception: " + (char)27 + "[0mUsage: java client.RMIClient [server_hostname [server_rmiobject]]");
			System.exit(1);
		}

		// Set the security policy
		if (System.getSecurityManager() == null)
		{
			System.setSecurityManager(new SecurityManager());
		}

		try {
			// new RMIclient instance for client
			RMIClient client = new RMIClient();
			//connect to registry instance running on middleware server
			client.connectRegistry();
			// connect server: get the middleware stub from registry instance by lookup
			client.connectServer();
			//start CLI
			client.start();
		} 
		catch (Exception e) {    
			System.err.println((char)27 + "[31;1mClient exception: " + (char)27 + "[0mUncaught exception");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public RMIClient()
	{
		super();
	}

	public void connectServer()
	{
		connectServer(m_serverHost, m_serverPort, m_serverName);
	}

	// Connect to registry instance running on port 3007 of middleware server
	public void connectRegistry()
	{
		try{
			Registry l_registry;

			l_registry = LocateRegistry.getRegistry(m_serverHost, 3007);

			registry = l_registry;
		}
		catch (RemoteException e) {
			System.err.println((char)27 + "[31;1mRemote Exception when connecting to registry: " + (char)27 + "[0mUncaught exception");
			e.printStackTrace();
			System.exit(1);
		}
		
	}

	// Connect to middleware server by looking up middleware stub in registry
	public void connectServer(String server, int port, String name)
	{
		try {
			boolean first = true;
			while (true) {
				try {
					m_resourceManager = (IResourceManager)registry.lookup(s_rmiPrefix + name);
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
}


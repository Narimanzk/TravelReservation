# comp512-project

Update: Oct 4 - Nariman
- default port for registry is set to 3007 for our group (NOT 1099)
- in example below the setup is as follows: 
	- Client runs on lab2-7
	- Middleware runs on lab2-8, server_name is 'Middleware'
	- Flights RM runs on lab2-9, server_name is 'Flights'
	- Cars RM runs on lab2-10, server)name is 'Cars'
	- Rooms RM runs on lab2-11, server_name is 'Rooms'

Step 1: Run the RMI resource manager for each server: The rmi_name parameter should be capitalized, so either Flights, Cars or Rooms(one run resource manager server for each) 

```
cd Server/
./run_server.sh [<rmi_name>] # starts a single ResourceManager
./run_servers.sh # convenience script for starting multiple resource managers
```

Step 2: Run the middleware server: Run middleware and pass the hostname where server is running, port (default 3007 for our group), server name for each resource manager we want to connect to (Flights, Cars, Rooms)
'''
cd Server/
./run_middleware.sh [<server_hostname>,<port>,<server name> ...]
ex: ./run_middleware.sh lab2-9,3007,Flights lab2-10,3007,Cars lab2-11,3007,Rooms
'''

Step 3: Run the RMI client: Run the client and pass in the hostname of host running middleware server, and pass name of middleware server (Middleware)

```
cd Client
./run_client.sh [<server_hostname> [<middleware_server_name>]]
```

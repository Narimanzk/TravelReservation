## Run Servers:

General syntax: ./run_server.sh <Servername>,<port>
  
```./run_server.sh Flights,6007``` in lab2-9
  
```./run_server.sh Cars,6007``` in lab2-10
  
```./run_server.sh Rooms,6007``` in lab2-11
  
## Run Middleware:

General syntax: ./run_middleware.sh <flights_hostname>,<flights_port> <cars_hostname>,<cars_port> <rooms_hostname>,<rooms_port> 
  
```./run_middleware.sh lab2-9,6007 lab2-10,6007 lab2-11,6007``` in lab2-8
  
## Run Client:

General syntax: ./run_client.sh <middleware_host> <middleware_port>
  
```./run_client.sh lab2-8 6007``` in lab2-7

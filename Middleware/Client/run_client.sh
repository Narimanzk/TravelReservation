# Usage: ./run_client.sh [<server_hostname> [<server_rmiobject>]]

#RMI Middleware parameters:
#server_hostname: name of server running middleware (ex: lab8-2)
#server rmi_object: "Middleware"

java -Djava.security.policy=java.policy -cp ../Server/RMIInterface.jar:. Client.RMIClient $1 $2

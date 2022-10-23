./run_rmi.sh > /dev/null

echo "To run middleware, please specify the following info for each resource manager server: <server>,<port>,<hostname>"
echo '  $1 - hostname of Flights'
echo '  $2 - hostname of Cars'
echo '  $3 - hostname of Rooms'

java -Djava.security.policy=java.policy -Djava.rmi.server.codebase=file:$(pwd)/ Server.RMI.RMIMiddleware $1 $2 $3

# Usage: ./run_client.sh [<server_hostname> [<server_rmiobject>]]

echo "java -Djava.security.policy=java.policy Client.TCPClient $1 $2"
java -Djava.security.policy=java.policy Client.TCPClient $1 $2
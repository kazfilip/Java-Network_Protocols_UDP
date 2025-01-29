# Java-Network_Protocols_UDP
The DAS (Distributed Average System) is a application designed for distributed computation over UDP.
The system operates in two modes: Master and Slave.
It utilizes UDP broadcasts to coordinate and compute the average of numbers across nodes in a network.

Key Features:
Master Mode: Listens on a specified port, receives numbers, calculates their average, and broadcasts results to the network.
Slave Mode: Sends a number to a master node and listens for results using dynamically allocated ports.
Broadcast Communication: Employs UDP broadcast to facilitate lightweight and efficient communication between nodes.
Fault Tolerance: Dynamically generates new ports in case of port conflicts.

Usage:
Run the application with the appropriate arguments:

Master Mode: java DAS <port> <starting_number>
Slave Mode: Fallback mode if the specified port is unavailable. Sends the number to the master node.
This project is a practical demonstration of distributed systems principles, offering insights into UDP communication, broadcasting, and distributed computation.

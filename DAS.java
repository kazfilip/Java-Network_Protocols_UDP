import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

public class DAS {

    static DatagramSocket socket;
    static DatagramPacket packet;
    static int port;
    static ArrayList<Integer> numbers = new ArrayList<Integer>();

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Please provide two arguments: <port> <number>");
        }
        try{
            port = Integer.parseInt(args[0]);
        }catch (NumberFormatException e){
            System.out.println("Please provide a number");
            System.exit(1);
        }
        try{
            socket = new DatagramSocket(port);
            socket.setBroadcast(true);
        }catch(SocketException e){
            slaveMode(args);
            System.exit(0);
        }

        byte[] buffer = new byte[1024];
        packet = new DatagramPacket(buffer, buffer.length);
        System.out.println("Master mode started.");

        int startingNumber;

        try{
            startingNumber = Integer.parseInt(args[1]);
            numbers.add(startingNumber);
        }catch (NumberFormatException e){
            System.out.println("Please enter a number as your second argument");
            System.exit(0);
        }

        while(true) {
            socket.receive(packet);
            String message = new String(packet.getData(), 0, packet.getLength());
            int receivedNumber;

            try{
                receivedNumber = Integer.parseInt(message);
            }catch (NumberFormatException e){
                continue;
            }

            if(receivedNumber != 0 && receivedNumber != -1 ) {
                System.out.println("Received number: " + receivedNumber);
                numbers.add(receivedNumber);

            }else if(receivedNumber == 0){
                int average = calculateAverage(numbers);
                System.out.println("Average of numbers: " + average);
                broadcast(socket,60000, String.valueOf(average));

            }else if (receivedNumber == -1){
                System.out.println("Received number: " + receivedNumber);
                broadcast(socket,60000, String.valueOf(receivedNumber));
                socket.close();
                break;
            }
        }

    }
    private static int calculateAverage(ArrayList<Integer> numbers) {
        double average = 0;
        for (int number : numbers) {
            average += number;
        }
        return (int) Math.floor(average);
    }
    private static void broadcast(DatagramSocket socket,int port, String message) throws IOException {
        String broadcastIP = "255.255.255.255";
        byte[] data = message.getBytes();
        InetAddress address = InetAddress.getByName(broadcastIP);
        System.out.println(address);
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        socket.send(packet);
    }
    private static void slaveMode(String[] args) throws IOException {
        int randomPort = generateRandomPort();
        int destinationPort = 0;

        int numberToSend =0;
        try{
            destinationPort = Integer.parseInt(args[0]);
            numberToSend = Integer.parseInt(args[1]);
        }catch (NumberFormatException e){
            System.out.println("Please provide a number value as both of your arguments");
            System.exit(0);
        }
        boolean connected = false;

        while(!connected){
            try{
                DatagramSocket socket = new DatagramSocket(randomPort);
                socket.setBroadcast(true);
                broadcast(socket,destinationPort,String.valueOf(numberToSend));
                connected = true;
            }catch (SocketException e){
                randomPort = generateRandomPort();
            }
        }
    }
    private static int generateRandomPort(){
        int minPort = 1024;
        int maxPort = 49151;
        Random random = new Random();
        int randomPort = random.nextInt((maxPort-minPort) + 1) + minPort;
        while(randomPort == 12340) {
            randomPort = random.nextInt((maxPort-minPort) + 1) + minPort;
        }
        return randomPort;
    }
}

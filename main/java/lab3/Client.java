package lab3;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    private Socket connectedSocket;
    private BufferedReader in;
    private PrintWriter out;

    public static void main(String[] args) {
        try {
            Client client1 = new Client("0.0.0.0", 8080);

            Client client3 = new Client("0.0.0.0", 8080);

            System.out.println("Client1 sent: " + client1.sendMessage("c1: Helo"));

            System.out.println("Client3 sent: " + client3.sendMessage("c3: Helllo"));
            System.out.println("Client1 sent: " + client1.sendMessage("c1: World"));
            Client client2 = new Client("0.0.0.0", 8080);
            System.out.println("Client2 sent: " + client2.sendMessage("c2: Dev"));
            client2.sendMessage("quit");
            client2.quit();

            System.out.println("Client3 sent: " + client3.sendMessage("c3: my"));
            System.out.println("Client3 sent: " + client3.sendMessage("c3: Server"));
            client3.sendMessage("quit");
            client3.quit();

            System.out.println("Client1 sent: " + client1.sendMessage("c1: Friend"));
            client1.sendMessage("quit");
            client1.quit();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Client finished");
    }

    public Client(String ip, int port) throws IOException {
        System.out.println("Connecting to server on " + ip + ":" + port);
        connectedSocket = new Socket(ip, port);
        System.out.println("Connected to server on " + ip + ":" + port);
        out = new PrintWriter(connectedSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(connectedSocket.getInputStream()));
        System.out.println("Streams connected to server");
    }

    public String sendMessage(String message) throws IOException {
        out.println(message);
        return in.readLine();
    }

    private void quit() throws IOException {
        in.close();
        out.close();
        connectedSocket.close();
    }

}

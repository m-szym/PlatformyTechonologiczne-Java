package lab3;

import java.io.*;
import java.net.Socket;


public class Client {
    private Socket connectedSocket;
    private BufferedReader in;
    private PrintWriter out;

    public static void main(String[] args) {
        try {
            Client client = new Client("0.0.0.0", 8080);
            System.out.println(client.sendMessage("Hello Server!"));
            System.out.println(client.sendMessage(", are you there?"));
            System.out.println("Client finished - shutting down");
            client.quit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Client finished");
    }

    public Client(String ip, int port) throws IOException {
        System.out.println("Connecting to server on " + ip + ":" + port);
        this.connectedSocket = new Socket(ip, port);
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

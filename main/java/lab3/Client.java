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
            Client client = new Client("0.0.0.0", 8080);

            Scanner scanner = new Scanner(System.in);
            String message = "";
            while (message != null && !message.equals("quit")) {
                System.out.println("Enter message to send to server");
                message = scanner.nextLine();
                System.out.println("Server responded: " + client.sendMessage(message));
            }

            client.quit();
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

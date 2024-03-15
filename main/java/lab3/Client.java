package lab3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    private static final Logger clientLogger = LogManager.getLogger(Client.class);
    private Socket connectedSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public static void main(String[] args) {
        try {
            Client client1 = new Client("0.0.0.0", 8080);

            Message[] messages = new Message[3];
            messages[0] = new Message("Alpha");
            messages[1] = new Message("Beta");
            messages[2] = new Message("Gamma");

            Message[] messages2 = new Message[2];
            messages2[0] = new Message("Delta");
            messages2[1] = new Message("Epsilon");
            Client client2 = new Client("0.0.0.0", 8080);

            try {
                client1.transfer(messages);
                client2.transfer(messages2);
            } catch(IOException | ClassNotFoundException e) {
                clientLogger.error("Transmission failed", e);
                throw new RuntimeException(e);
            }

            client2.quit();
            client1.quit();

        } catch (IOException e) {
            clientLogger.error("Client failed", e);
            throw new RuntimeException(e);
        }
        System.out.println("Client finished");
    }

    public Client(String ip, int port) throws IOException {
        clientLogger.info("Client started");
        connectedSocket = new Socket(ip, port);
        clientLogger.debug("Client connected to server on ip " + connectedSocket.getInetAddress() + " and port " + connectedSocket.getPort());
        out = new ObjectOutputStream(connectedSocket.getOutputStream());
        in = new ObjectInputStream(connectedSocket.getInputStream());
        clientLogger.debug("Client streams created");
        clientLogger.info("Client connected to server");
    }

    public void transfer(Message[] messages) throws IOException, ClassNotFoundException {
        clientLogger.debug("Attempting to transfer " + messages.length + " messages");
        String serverResponse = (String) in.readObject();
        if (serverResponse.equals(Server.SERVER_READY)) {
            clientLogger.debug("Server accepted connection and is ready to receive messages");
            out.writeObject(messages.length);
            out.flush();
            serverResponse = (String) in.readObject();
            if (serverResponse.equals(Server.TRANSFER_READY)) {
                clientLogger.debug("Server waiting to receive messages. Sending " + messages.length + " messages.");
                for (Message message : messages) {
                    out.writeObject(message);
                    out.flush();
                }
                serverResponse = (String) in.readObject();
                if (serverResponse.equals(Server.TRANSFER_FINISHED)) {
                    clientLogger.debug("Server confirmed receiving transmission");
                    clientLogger.info("Transmission complete. Messages sent: " + messages.length);
                } else {
                    clientLogger.error("Server failed to confirm fining transmission");
                }
            }
        }
    }

    public String sendMessage(String message) throws IOException {
        //out.println(message);
        return in.readLine();
    }

    private void quit() throws IOException {
        in.close();
        out.close();
        connectedSocket.close();
    }

}

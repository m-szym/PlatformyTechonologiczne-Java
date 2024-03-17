package lab3;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Client {
    private static final Logger clientLogger = LogManager.getLogger(Client.class);
    private Socket connectedSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public static void main(String[] args) {
        //long_test();
        // short_test();
        multi_test();
    }

    private static void long_test() {
        try {
            Client client = new Client();
            client.connectToServer("0.0.0.0", 8080);
            client.runCLient();
            client.closeConnection();
        } catch (IOException e) {
            clientLogger.error("Client failed", e);
            throw new RuntimeException(e);
        }
    }

    private static void short_test() {
        try {
            Client client = new Client();
            client.connectToServer("0.0.0.0", 8080);
            ArrayList<Message> messages = new ArrayList<>();
            messages.add(new Message("Hello World"));
            messages.add(new Message("Goodbye World"));

            client.transfer(messages);

            client.closeConnection();
        } catch (IOException | ClassNotFoundException e) {
            clientLogger.error("Client failed", e);
            throw new RuntimeException(e);
        }
    }

    private static void multi_test() {
        try {
            Client greekClient = new Client();
            Client latinClient = new Client();
            ArrayList<Message> messages = new ArrayList<>();


            greekClient.connectToServer("0.0.0.0", 8080);

            messages.add(new Message("Alpha"));
            messages.add(new Message("Beta"));

            greekClient.transfer(messages);
            messages.clear();

            latinClient.connectToServer("0.0.0.0", 8080);

            messages.add(new Message("A"));
            messages.add(new Message("B"));
            messages.add(new Message("C"));
            latinClient.transfer(messages);
            messages.clear();

            latinClient.closeConnection();

            messages.add(new Message("Gamma"));
            messages.add(new Message("Delta"));
            greekClient.transfer(messages);

            greekClient.closeConnection();
        } catch (IOException | ClassNotFoundException e) {
            clientLogger.error("Client failed", e);
            throw new RuntimeException(e);
        }
    }

//    public Client(String ip, int port) throws IOException {
//        clientLogger.info("Client started");
//        connectedSocket = new Socket(ip, port);
//        clientLogger.debug("Client connected to server on ip " + connectedSocket.getInetAddress() + " and port " + connectedSocket.getPort());
//        out = new ObjectOutputStream(connectedSocket.getOutputStream());
//        in = new ObjectInputStream(connectedSocket.getInputStream());
//        clientLogger.debug("Client streams created");
//        clientLogger.info("Client connected to server");
//    }

    public Client() {
        clientLogger.info("Client started");
        connectedSocket = null;
        out = null;
        in = null;
    }

    public void connectToServer(String ip, int port) throws IOException {
        if (connectedSocket != null) {
            closeConnection();
        }

        connectedSocket = new Socket(ip, port);
        clientLogger.debug("Client connected to server on ip " + connectedSocket.getInetAddress() + " and port " + connectedSocket.getPort());
        out = new ObjectOutputStream(connectedSocket.getOutputStream());
        in = new ObjectInputStream(connectedSocket.getInputStream());
        clientLogger.debug("Client streams created");
        clientLogger.info("Client connected to server");
    }

    public void transfer(ArrayList<Message> messages) throws IOException, ClassNotFoundException {
        clientLogger.debug("Attempting to transfer " + messages.size() + " messages");
        String serverResponse = (String) in.readObject();
        if (serverResponse.equals(Server.SERVER_READY)) {
            clientLogger.debug("Server accepted connection and is ready to receive messages");
            out.writeObject(messages.size());
            out.flush();
            serverResponse = (String) in.readObject();
            if (serverResponse.equals(Server.TRANSFER_READY)) {
                clientLogger.debug("Server waiting to receive messages. Sending " + messages.size() + " messages.");
                for (Message message : messages) {
                    out.writeObject(message);
                    out.flush();
                }
                serverResponse = (String) in.readObject();
                if (serverResponse.equals(Server.TRANSFER_FINISHED)) {
                    clientLogger.debug("Server confirmed receiving transmission");
                    clientLogger.info("Transmission complete. Messages sent: " + messages.size());
                } else {
                    clientLogger.error("Server failed to confirm fining transmission");
                }
            }
        }
        clientLogger.debug("Transmission done");
    }

    private void closeConnection() throws IOException {
        out.writeObject(0);
//        in.close();
//        out.close();
//        connectedSocket.close();
    }

    public void runCLient() throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        ArrayList<Message> messages = new ArrayList<>();

        while (running) {
            System.out.print("\nEnter command or message: ");
            if (scanner.hasNextLine()) {

                String message = scanner.nextLine();

                switch (message) {
                    case "exit" -> {
                        running = false;

                    }
                    case "ping" -> {
                        try {
                            Message ping = new Message("ping");
                            ArrayList<Message> pingList = new ArrayList<>();
                            pingList.add(ping);
                            transfer(pingList);
                        } catch (IOException | ClassNotFoundException e) {
                            clientLogger.error("Failed to send message", e);
                        }
                    }
                    case "test" -> {
                        try {
                            ArrayList<Message> testMessages = new ArrayList<>();
                            testMessages.add(new Message("Hello World"));
                            testMessages.add(new Message("Goodbye World"));
                            testMessages.add(new Message("Hello Again"));
                            transfer(testMessages);
                        } catch (IOException | ClassNotFoundException e) {
                            clientLogger.error("Failed to send message", e);
                        }
                    }
                    case "send" -> {
                        try {
                            transfer(messages);
                            messages.clear();
                        } catch (IOException | ClassNotFoundException e) {
                            clientLogger.error("Failed to send message", e);
                        }
                    }
                    default -> messages.add(new Message(message));
                }
            }
        }
    }
}

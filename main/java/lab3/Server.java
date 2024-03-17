package lab3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.Integer.parseInt;

public class Server {
    public static final String SERVER_READY = "ready";
    public static final String TRANSFER_READY = "ready for messages";
    public static final String TRANSFER_FINISHED = "finished";

    private static final Logger serverLogger = LogManager.getLogger(Server.class);
    private final ServerSocket socket;
    private boolean running = true;
    private ArrayList<Message> receivedMessages;
    private ArrayList<Thread> clientThreads;

    public static void main(String[] args) {
        try {
            Server server = new Server("8080");
            server.runServer();
        } catch (IOException e) {
            serverLogger.error("Server failed to start", e);
        }
    }

    public Server(String port) throws IOException{
        socket = new ServerSocket(parseInt(port));
        receivedMessages = new ArrayList<>();
        clientThreads = new ArrayList<>();

        serverLogger.info("Server on ip " + socket.getInetAddress() + " and port " + socket.getLocalPort());
    }

    public void runServer() throws IOException {
        serverLogger.debug("Server started");
        Scanner cli = new Scanner(System.in);

        try {
            while(running) {
                Socket client = socket.accept();
                serverLogger.debug("Client connected");
                ClientHandler clientHandler = new ClientHandler(client);
                try {
                    clientHandler.connectToClient();
                    Thread clientThread = new Thread(clientHandler);
                    clientThreads.add(clientThread);
                    clientThread.start();
                } catch (IOException e) {
                    serverLogger.error("ClientHandler failed to connect to client", e);
                    throw new RuntimeException(e);
                }
            }
        }
        catch (IOException e) {
            serverLogger.error("Server failed: ", e);
        }
        finally {
            socket.close();
        }
    }




    private class ClientHandler implements Runnable {
        private final Socket client;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        private final ArrayList<Message> messagesBuffer;

        public ClientHandler(Socket clientSocket) {
            serverLogger.info("New ClientHandler created");
            client = clientSocket;
            in = null;
            out = null;
            messagesBuffer = new ArrayList<>();

        }

        public void connectToClient() throws IOException {
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());
            serverLogger.debug("Handler connected to client");
        }

        public void closeConnection() throws IOException {
            in.close();
            out.close();
            client.close();
        }


        private void protocol() throws IOException, ClassNotFoundException {
            out.writeObject(SERVER_READY);
            serverLogger.debug("ClientHandler ready to start transmission");
            out.flush();
            Integer messgesToRead = (Integer) in.readObject();
            out.writeObject(TRANSFER_READY);
            serverLogger.debug("ClientHandler ready to receive " + messgesToRead + " messages");
            out.flush();
            for (int i = 0; i < messgesToRead; i++) {
                Message message = (Message) in.readObject();
                messagesBuffer.add(message);
            }
            out.writeObject(TRANSFER_FINISHED);
            serverLogger.debug("ClientHandler finished receiving " + messagesBuffer.size() + " messages");
            out.flush();
            serverLogger.info("Transmission complete. Messages received: " + messagesBuffer.size());

        }

        @Override
        public void run() {
            boolean handlerRunning = true;
            while (handlerRunning) {
                try {
                    //protocol();
                    out.writeObject(SERVER_READY);
                    serverLogger.debug("ClientHandler ready to start transmission");
                    out.flush();

                    Integer messagesToRead = (Integer) in.readObject();
                    if (messagesToRead > 0) {
                        out.writeObject(TRANSFER_READY);
                        serverLogger.debug("ClientHandler ready to receive " + messagesToRead + " messages");
                        out.flush();
                        for (int i = 0; i < messagesToRead; i++) {
                            Message message = (Message) in.readObject();
                            messagesBuffer.add(message);
                        }
                        out.writeObject(TRANSFER_FINISHED);
                        serverLogger.debug("ClientHandler finished receiving " + messagesToRead + " messages");
                        out.flush();
                        serverLogger.info("Transmission complete. Messages received: " + messagesBuffer.size());
                    } else {
                        out.writeObject(TRANSFER_FINISHED);
                        serverLogger.debug("ClientHandler received total of " + messagesBuffer.size() + " messages");
                        out.flush();
                        serverLogger.info("Transmission complete. Disconnecting client.");
                        handlerRunning = false;
                    }

                } catch (IOException | ClassNotFoundException e) {
                    serverLogger.error("ClientHandler failed: ", e);
                    throw new RuntimeException(e);

                }
            }
            serverLogger.info("ClientHandler finished");
            try {
                closeConnection();
            } catch (IOException e) {
                serverLogger.error("ClientHandler failed to close connection", e);
                throw new RuntimeException(e);
            }
        }
    }
}

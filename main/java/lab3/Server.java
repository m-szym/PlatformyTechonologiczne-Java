package lab3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class Server {
    public static final String SERVER_READY = "ready";
    public static final String TRANSFER_READY = "ready for messages";
    public static final String TRANSFER_FINISHED = "finished";


    private ServerSocket socket;
    private boolean running = true;
    private ArrayList<Message> receivedMessages;
    private ArrayList<Thread> clientThreads;

    public static void main(String[] args) {
        try {
            Server server = new Server("8080");

            try {
                server.run();
            } catch (IOException e) {
                System.out.println("Server failed");
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("Server failed to start on port " + args[0]);
            e.printStackTrace();
        }
    }

    public Server(String port) throws IOException{
        socket = new ServerSocket(parseInt(port));
        receivedMessages = new ArrayList<>();
        clientThreads = new ArrayList<>();

        System.out.println("Server started on port " + port);
        System.out.println("Server on ip " + socket.getInetAddress() + " and port " + socket.getLocalPort());
    }

    public void run() throws IOException {
        System.out.println("Server running");

        try {
            while(running) {
                Socket client = socket.accept();
                System.out.println("Client connected");
                ClientHandler clientHandler = new ClientHandler(this, client);
                Thread clientThread = new Thread(clientHandler);
                clientThreads.add(clientThread);
                clientThread.start();
            }
        }
        catch (IOException e) {
            System.out.println("Server failed");
            e.printStackTrace();
        }
        finally {
            socket.close();
        }
    }




    private class ClientHandler implements Runnable {
        private Server server;
        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private ArrayList<Message> messagesBuffer;

        public ClientHandler(Server _server, Socket _client) throws IOException {
            System.out.println("ClientHandler created");
            server = _server;
            client = _client;

            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            System.out.println("Streams connected to client");

            messagesBuffer = new ArrayList<>();

        }


        @Override
        public void run() {
            try {
                boolean clientOnline = true;
                while(clientOnline) {
                    String message = in.readLine();
                    if (message == null || message.equals("quit")) {
                        System.out.println("Client disconnected");
                        clientOnline = false;
                    } else {
                        System.out.println("Received message: " + message);
                        out.println("Received message: " + message);
                    }
                }

                System.out.println("Closing client connection");

                in.close();
                out.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);

            }
            System.out.println("ClientHandler finished");
        }
    }
}

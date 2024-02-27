package lab3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

        Thread newClientThread = null;
        //while (running) {
            try {
                Socket client = socket.accept();
                System.out.println("Accepted client");

                newClientThread = new Thread(new ClientHandler(this, client));
                clientThreads.add(newClientThread);
                System.out.println("Starting client thread");
                newClientThread.start();

            } catch (IOException e) {
                System.out.println("Failed to accept client");
                e.printStackTrace();
            }
        //}

        quit();
    }

    private void quit() throws IOException {
        for (Thread thread : clientThreads) {
            thread.interrupt();
        }
        socket.close();
    }

    public void addMessage(Message message) {
        receivedMessages.add(message);
        System.out.println("Received message: " + message);
    }


    private class ClientHandler implements Runnable {
        private Server server;
        private Socket client;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        private ArrayList<Message> messagesBuffer;

        public ClientHandler(Server _server, Socket _client) throws IOException {
            System.out.println("ClientHandler created");
            server = _server;
            client = _client;

            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());

            messagesBuffer = new ArrayList<>();

        }

        private void stop() throws IOException{
            in.close();
            out.close();
            //client.close();
            System.out.println("ClientHandler stopped");
        }

        private void transfer() throws IOException, ClassNotFoundException {
//            out.writeObject(TRANSFER_READY);
//            System.out.println("ClientHandler transfer ready");
//            int messageCount = in.readInt();
//            System.out.println("ClientHandler received message count: " + messageCount);
//            for (int i = 0; i < messageCount; i++) {
//                Message message = (Message) in.readObject();
//                messagesBuffer.add(message);
//            }
//            System.out.println("ClientHandler received messages");
//            for (Message message : messagesBuffer) {
//                server.addMessage(message);
//            }
//            out.writeObject(TRANSFER_FINISHED);
//
            try {
                out.writeObject(TRANSFER_READY);
                try {
                    int messageCount = (int) in.readObject();
                    try {
                        out.writeObject(TRANSFER_READY);
                        try {
                            for (int i = 0; i < messageCount; i++) {
                                Message message = (Message) in.readObject();
                                messagesBuffer.add(message);
                            }
                            for (Message message : messagesBuffer) {
                                server.addMessage(message);
                            }
                            try {
                                out.writeObject(TRANSFER_FINISHED);
                            }
                            catch (IOException e) {
                                System.out.println("Server failed to send finished message");
                            }
                        } catch (IOException e) {
                            System.out.println("Server failed to read message");
                        }
                    } catch (IOException e) {
                        System.out.println("Server not ready to receive messages");
                    }
                }
                catch (IOException e) {
                    System.out.println("Server dint get count");
                }
            }
            catch (IOException e) {
                System.out.println("Server not ready");
            }

        }

        @Override
        public void run() {
            System.out.println("ClientHandler running");
            //while (true) {
                try {
                    transfer();
                    System.out.println("ClientHandler transfer complete");
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Failed to read message from client");
                   // break;
                }
            //}

            try {
                stop();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}

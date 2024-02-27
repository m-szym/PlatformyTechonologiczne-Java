package lab3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Client {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public static void main(String[] args) {
        try {
            Client client = new Client("0.0.0.0", 8080);

            Message[] messages = new Message[3];
            messages[0] = new Message("Hello World");
            messages[1] = new Message("Lorem Ipsum");
            messages[2] = new Message("Hello World");

            client.sendMessages(messages);

        } catch (IOException e) {
            System.out.println("Failed to connect to server");
            e.printStackTrace();
        }
    }

    public Client(String ip, int port) throws IOException {
        System.out.println("Connecting to server on " + ip + ":" + port);
        Socket socket = new Socket(ip, port);
        System.out.println("Connected to server on " + ip + ":" + port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());



        System.out.println("Connected to server on " + ip + ":" + port);
    }



    private void quit() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    private void transfer(Message[] messages) {
//        try {
//            String serverResponse = (String) in.readObject();
//            if (!serverResponse.equals(Server.TRANSFER_READY)) {
//                out.flush();
//                System.out.println("Server not ready");
//            }
//
//            out.writeObject(messages.length);
//            serverResponse = (String) in.readObject();
//            if (!serverResponse.equals(Server.TRANSFER_READY)) {
//                out.flush();
//                System.out.println("Server not ready to receive messages");
//            }
//
//            for (Message message : messages) {
//                out.writeObject(message);
//            }
//
//            serverResponse = (String) in.readObject();
//            if (!serverResponse.equals(Server.TRANSFER_FINISHED)) {
//                out.flush();
//                System.out.println("Transfer couldn't be completed");
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            System.out.println("Failed to transfer messages");
//        }
        try {
            String serverResponse = (String) in.readObject();
            try {
                out.writeObject(messages.length);
                try {
                    serverResponse = (String) in.readObject();
                    try {
                        for (Message message : messages) {
                            out.writeObject(message);
                        }
                        try {
                            serverResponse = (String) in.readObject();
                        }
                        catch (ClassNotFoundException e) {
                            System.out.println("Failed to get final server response");
                        }
                    }
                    catch (IOException e) {
                        System.out.println("Failed to send messages");
                    }
                }
                catch (IOException e) {
                    System.out.println("Failed get server response");
                }
            }
            catch (IOException e) {
                System.out.println("Failed to send message count");
            }
        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed on ready message");
        }
    }

    public void sendMessages(Message[] messages) {
        transfer(messages);
        System.out.println("Messages sent");
        try {
            quit();
        } catch (IOException e) {
            System.out.println("Failed to quit client");
            e.printStackTrace();
        }
    }

}

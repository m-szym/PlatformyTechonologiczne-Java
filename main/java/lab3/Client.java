package lab3;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {
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

            client1.transfer(messages);

            client1.quit();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Client finished");
    }

    public Client(String ip, int port) throws IOException {
        System.out.println("Connecting to server on " + ip + ":" + port);
        connectedSocket = new Socket(ip, port);
        System.out.println("Connected to server on " + ip + ":" + port);
        out = new ObjectOutputStream(connectedSocket.getOutputStream());
        in = new ObjectInputStream(connectedSocket.getInputStream());
        System.out.println("Streams connected to server");
    }

    public void transfer(Message[] messages) throws IOException, ClassNotFoundException {
        String serverResponse = (String) in.readObject();
        if (serverResponse.equals(Server.SERVER_READY)) {
            out.writeObject(messages.length);
            out.flush();
            serverResponse = (String) in.readObject();
            if (serverResponse.equals(Server.TRANSFER_READY)) {
                for (Message message : messages) {
                    out.writeObject(message);
                    out.flush();
                }
                serverResponse = (String) in.readObject();
                if (serverResponse.equals(Server.TRANSFER_FINISHED)) {
                    System.out.println("Transfer finished");
                } else {
                    System.out.println("Transfer failed");
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

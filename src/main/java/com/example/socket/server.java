package com.example.socket;

import java.io.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class server {
    private static final int PORT = 7422;
    private static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running....");

            //accepting incomimg ccnnections
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New Client Connected " + clientSocket);

                //clienthandler to handle that client
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //Brodcast message to everyone except sender
    public static void brodcast(String message, ClientHandler sender){
        for (ClientHandler client : clients){
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    //To handle client connections
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String Username;

        //constructor
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;

            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void run() {
                try {
                    //input and output stream
                    Username = getUsername();
                    System.out.println("User "+ Username + " connected");

                    out.println("Welcome to chat, " + Username + " !");

                    out.println("Type your message!!");
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("["+Username+"]: " + inputLine);

                        brodcast("[" + Username + "] " + inputLine, this);
                    }
                    clients.remove(this);

                    in.close();
                    out.close();
                    clientSocket.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

        private String getUsername() throws IOException {
            out.println("Enter your username : ");
            return in.readLine();
        }

        public void sendMessage(String message){
            out.println(message);
            out.println("Type your message");
        }
    }
}

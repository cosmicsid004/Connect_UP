package com.example.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class client {
    private static final String SERVER_ADDRESS = "localhost"; //final keyword tells that it cannot be changed during the programe execution
    private static final int SERVER_PORT = 7422;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to chat server....");

            //Input and output stream
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //a thread to handle incoming messages
            new Thread(() -> {
                try {
                    String serverResponce;
                    while ((serverResponce = in.readLine()) != null) {
                        System.out.println(serverResponce);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            //message to send to server
            Scanner scanner = new Scanner(System.in);
            String userInput;
            while (true) {
                userInput = scanner.nextLine();
                out.println(userInput);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

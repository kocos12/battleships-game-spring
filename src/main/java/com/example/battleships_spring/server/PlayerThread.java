package com.example.battleships_spring.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class PlayerThread extends Thread {
    private Socket socket;
    private BattleshipsSpringApplication server;
    private PrintWriter writer;

    public PlayerThread(Socket socket, BattleshipsSpringApplication server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            printUsers();

            String userName = bufferedReader.readLine();
            server.addUserName(userName);

            server.broadcast("New user connected: " + userName, this);

            String message;

            do {
                message = bufferedReader.readLine();
                server.broadcast(userName + ": " + message, this);
            } while (!message.equals("bye"));

            server.removeUser(userName, this);
            socket.close();

            message = userName + " has quitted.";
            server.broadcast(message, this);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    void printUsers() {
        if (server.hasUsers())
            writer.println("Connected users: " + server.getUserNameSet());
        else
            writer.println("No other users connected");
    }

    void sendMessage(String message) {
        writer.println(message);
    }
}

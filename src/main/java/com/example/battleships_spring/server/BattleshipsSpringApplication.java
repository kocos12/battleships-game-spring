package com.example.battleships_spring.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class BattleshipsSpringApplication {
    final public int port = 9000;
    private Set<String> userNameSet = new HashSet<>();
    private Set<PlayerThread> userThreadSet = new HashSet<>();

    public void runServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port: " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("User connected");

                PlayerThread user = new PlayerThread(socket, this);
                userThreadSet.add(user);
                user.start();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BattleshipsSpringApplication.class, args);
        DatabaseOperator databaseOperator = context.getBean(DatabaseOperator.class);

        try {
            BattleshipsSpringApplication server = new BattleshipsSpringApplication();
            server.runServer();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
//        databaseOperator.deleteAll();
//        databaseOperator.saveSamplePlayer();
//        databaseOperator.saveSampleGameHistory();
//        databaseOperator.showPlayers();
//        databaseOperator.updatePlayerScore();
//        databaseOperator.updatePlayerScore();
//        databaseOperator.showPlayers();
    }

    void broadcast(String message, PlayerThread author)
    {
        for (PlayerThread user : userThreadSet)
        {
            if (user != author)
                user.sendMessage(message);
        }
    }
    boolean hasUsers() {
        return !userThreadSet.isEmpty();
    }
    Set<String> getUserNameSet()
    {
        return userNameSet;
    }
    void addUserName(String userName)
    {
        userNameSet.add(userName);
    }

    void removeUser(String userName, PlayerThread playerThread)
    {
        if(userNameSet.remove(userName))
        {
            userThreadSet.remove(playerThread);
            System.out.println("User: " + userName + " exited chat");
        }
    }
}

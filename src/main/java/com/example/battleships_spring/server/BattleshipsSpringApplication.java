package com.example.battleships_spring.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class BattleshipsSpringApplication {
    private final int port = 9000;
    private Set<String> userNameSet = new HashSet<>();
    private Set<PlayerThread> connectedUsers = new HashSet<>();
    private Set<PlayerThread> matchMaking = new HashSet<>();


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BattleshipsSpringApplication.class, args);
        PlayerService playerService = context.getBean(PlayerService.class);
        try {
            BattleshipsSpringApplication server = new BattleshipsSpringApplication();
            server.runServer(playerService);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public void runServer(PlayerService databaseOperator) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port: " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("User connected");

                PlayerThread user = new PlayerThread(socket, this, databaseOperator);
                connectedUsers.add(user);
                user.start();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    void addUserName(String userName) {
        userNameSet.add(userName);
    }

    void removeUser(String userName, PlayerThread playerThread) {
        if (userNameSet.remove(userName)) {
            connectedUsers.remove(playerThread);
            matchMaking.remove(playerThread);
            System.out.println("User: " + userName + " exited chat");
        }
    }

    void addPlayerToMatchmaking(PlayerThread playerThread) {
        matchMaking.add(playerThread);
    }

    boolean tryMatchmaking(PlayerThread author) {
        if (matchMaking.size() % 2 == 0 && matchMaking.size() > 0) {
            System.out.println("Mozna utowrzyc gre - mam 2 graczy!");

            List<PlayerThread> matchmakingArray = new ArrayList<>(matchMaking);
            int setSize = matchMaking.size();

            //przypisanie ref wątków
            matchmakingArray.get(setSize - 1).setOpponentPlayer(matchmakingArray.get(setSize - 2));
            matchmakingArray.get(setSize - 2).setOpponentPlayer(matchmakingArray.get(setSize - 1));

            //przypisanie info o przeciwniku do gameHistory
            matchmakingArray.get(setSize - 1).getGameHistory()
                    .setPlayer2_id(matchmakingArray.get(setSize - 2).getGameHistory().getPlayer1_id());
            matchmakingArray.get(setSize - 2).getGameHistory()
                    .setPlayer2_id(matchmakingArray.get(setSize - 1).getGameHistory().getPlayer1_id());

            //gdy zaczynaja grac znikaja z lobby
            matchMaking.remove(author);
            matchMaking.remove(author.getOpponentPlayer());

            //rozesłanie powiadomien kto z kim gra
            //author.getOpponentPlayer().sendMessage("Twoim przeciwnikiem jest " + author.getMyPlayerNickname());
            author.getOpponentPlayer().getOpponentPlayer().sendMessage("Twoim przeciwnikiem jest " +
                    author.getOpponentPlayer().getMyPlayerNickname());

            //daje znak przeciiwnkowi
            author.getOpponentPlayer().setStartGame(true);
            //zaczyna ten drugi
            author.setMyTurn(true);
            return true;
        } else {
            System.out.println("Matchmaking nie udany - brak pary");
            author.sendMessage("Brak graczy w lobby. Zaczekaj az dolaczy wiecej graczy.");
            return false;
        }
    }
}

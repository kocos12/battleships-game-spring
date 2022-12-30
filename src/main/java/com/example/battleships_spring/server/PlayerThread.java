package com.example.battleships_spring.server;

import com.example.battleships_spring.model.GameHistory;
import com.example.battleships_spring.model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class PlayerThread extends Thread {
    private Socket socket;
    private BattleshipsSpringApplication server;
    private PrintWriter writer;
    private DatabaseOperator databaseOperator;
    private PlayerThread opponentPlayer;
    private GameHistory gameHistory;


    public PlayerThread(Socket socket, BattleshipsSpringApplication server, DatabaseOperator databaseOperator) {
        this.socket = socket;
        this.server = server;
        this.databaseOperator = databaseOperator;
        this.gameHistory = new GameHistory();
    }

    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            printUsers();

            String userLogin = bufferedReader.readLine();

            String userPasswd = bufferedReader.readLine();

            while(!databaseOperator.tryLoggingInPlayer(userLogin,userPasswd)){
                System.out.println("Logowanie nie udane brak gracza w bazie");
                System.out.println("Nie ma kogos takiego jak " + userLogin);
                this.sendMessage("Nie poprawne dane logowania! Spróbuj ponownie");
                userLogin = bufferedReader.readLine();
                userPasswd = bufferedReader.readLine();
//                server.removeUser(userLogin, this);
//                socket.close();
            }

            System.out.println("zalogowano: " + userLogin);
            server.addUserName(userLogin);
            gameHistory.setPlayer1_id(databaseOperator.findPlayerByLogin(userLogin).getId());
            server.addPlayerToMatchmaking(this);
            server.broadcastToLobby(userLogin + " dolaczyl do lobby", this);

            if(server.tryMatchmaking(this)){
                //no to gramy!
            }

            //server.broadcast("New user connected: " + userLogin, this);

            String message;

            do {
                message = bufferedReader.readLine();
                //server.broadcast(userLogin + ": " + message, this);
            } while (!message.equals("bye"));

            server.removeUser(userLogin, this);
            socket.close();

            message = userLogin + " has quited.";
            server.broadcastToLobby(message, this);
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

    String getMyPlayerNickname(){
        Player temp = databaseOperator.findPlayerById(this.gameHistory.getPlayer1_id());
        return  temp.getNickname();
    }

    public void setOpponentPlayer(PlayerThread opponentPlayer) {
        this.opponentPlayer = opponentPlayer;
    }

    public PlayerThread getOpponentPlayer() {
        return opponentPlayer;
    }

    public GameHistory getGameHistory() {
        return gameHistory;
    }
}

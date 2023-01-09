package com.example.battleships_spring.server;

import com.example.battleships_spring.model.GameHistory;
import com.example.battleships_spring.model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;


public class PlayerThread extends Thread {
    private Socket socket;
    private BattleshipsSpringApplication server;
    private PrintWriter writer;
    private BufferedReader bufferedReader;
    private DatabaseOperator databaseOperator;
    private PlayerThread opponentPlayer;
    private GameHistory gameHistory;
    private boolean startGame;


    public PlayerThread(Socket socket, BattleshipsSpringApplication server, DatabaseOperator databaseOperator) {
        this.socket = socket;
        this.server = server;
        this.databaseOperator = databaseOperator;
        this.gameHistory = new GameHistory();
    }

    public void run() {
        String userLogin = "";
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            userLogin = bufferedReader.readLine();
            String userPasswd = bufferedReader.readLine();

            while(!databaseOperator.tryLoggingInPlayer(userLogin,userPasswd)){
                System.out.println("Logowanie nie udane, brak gracza w bazie");
                System.out.println("Nie ma kogos takiego jak " + userLogin);
                this.sendMessage("Bledne dane logowania. Sprobuj ponownie.");
                userLogin = bufferedReader.readLine();
                userPasswd = bufferedReader.readLine();
            }

            System.out.println("Zalogowano: " + userLogin);
            this.sendMessage("Zalogowano poprawnie.");
            server.addUserName(userLogin);
            gameHistory.setPlayer1_id(databaseOperator.findPlayerByLogin(userLogin).getId());
            server.addPlayerToMatchmaking(this);
            server.broadcastToLobby(userLogin + " dolaczyl do lobby", this);

            String message;
            if(server.tryMatchmaking(this)){
                //no to gramy!
                placeShips();

            }else{
                while(!startGame){}
                placeShips();
            }

            server.removeUser(userLogin, this);
            socket.close();

            message = userLogin + " has quited.";
            server.broadcastToLobby(message, this);
        } catch (SocketException socketException){
            //System.out.println("Player ragequited, perhaps. I mean for sure hah");
            server.removeUser(userLogin,this);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void placeShips() throws IOException {
        gameHistory.prepareBattleground();
        String operationType;
        String coord;
        do {
            operationType = bufferedReader.readLine();
            coord = bufferedReader.readLine();

            if(operationType.equals("add")){
                gameHistory.setShipOnCoord(coord);
            }
            if(operationType.equals("remove")){
                gameHistory.removeShipOnCoord(coord);
            }

        } while (!operationType.equals("Ready"));
        opponentPlayer.getGameHistory().setBattleground2(gameHistory.getBattleground1());
        opponentPlayer.sendMessage("Przeciwnik ustawil statki");
        System.out.println(gameHistory);
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public String getMyPlayerNickname(){
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

    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
    }
}

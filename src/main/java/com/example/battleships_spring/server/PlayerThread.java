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
    private final BattleshipsSpringApplication server;
    private PrintWriter writer;
    private BufferedReader bufferedReader;
    private final PlayerService playerService;
    private PlayerThread opponentPlayer;
    private GameHistory gameHistory;
    private boolean startGame;
    private boolean isReadyToBattle;
    private volatile boolean isMyTurn;
    private boolean opponentWon;


    public PlayerThread(Socket socket, BattleshipsSpringApplication server, PlayerService playerService) {
        this.socket = socket;
        this.server = server;
        this.playerService = playerService;
        this.gameHistory = new GameHistory();
    }

    public void run() {
        String userLogin = "";
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            userLogin = bufferedReader.readLine();
            String userPasswd = bufferedReader.readLine();

            while (!playerService.tryLoggingInPlayer(userLogin, userPasswd)) {
                System.out.println("Logowanie nie udane, brak gracza w bazie");
                System.out.println("Nie ma kogos takiego jak " + userLogin);
                this.sendMessage("Bledne dane logowania. Sprobuj ponownie.");
                this.sendMessage("");
                userLogin = bufferedReader.readLine();
                userPasswd = bufferedReader.readLine();
            }

            System.out.println("Zalogowano: " + userLogin);
            this.sendMessage("Zalogowano poprawnie.");
            server.addUserName(userLogin);
            gameHistory.setPlayer1_id(playerService.findPlayerByLogin(userLogin).getId());
            server.addPlayerToMatchmaking(this);

            if (server.tryMatchmaking(this)) {
                placeShips();
                battle();
            } else {
                while (!startGame) {}
                placeShips();
                battle();
            }

            server.removeUser(userLogin, this);
            socket.close();

        } catch (SocketException socketException) {
            server.removeUser(userLogin, this);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void battle() throws IOException {
        while (!getOpponentPlayer().isReadyToBattle()) {}
        System.out.println("Bitwa");
        if(isMyTurn){
            sendMessage("Twoj ruch");
        }else{
            sendMessage("Ruch przeciwnika");
        }
        while (gameHistory.checkVictory() && isOpponentLost()) {
            while (!isMyTurn) {
                Thread.onSpinWait();
            }
            if(isOpponentLost()) {
                String shotCoord = bufferedReader.readLine();
                Boolean hit = gameHistory.checkShot(shotCoord);
                if (hit) {
                    sendMessage("X");
                } else {
                    sendMessage("");
                }
                //getOpponentPlayer().sendMessage("Twoj ruch");

                setMyTurn(false);
                getOpponentPlayer().setMyTurn(true);
            }
        }
        if(!opponentWon){
            getOpponentPlayer().setOpponentWon(true);
            playerService.addPointToPlayer(gameHistory.getPlayer1_id());
            System.out.println("Dodano punkt za zwyciestwo");
        }
    }

    private void placeShips() throws IOException {
        gameHistory.prepareBattleground();
        String operationType;
        String coord;
        do {
            operationType = bufferedReader.readLine();
            coord = bufferedReader.readLine();

            if (operationType.equals("add")) {
                gameHistory.setShipOnCoord(coord);
            }
            if (operationType.equals("remove")) {
                gameHistory.removeShipOnCoord(coord);
            }

        } while (!operationType.equals("Ready"));
        opponentPlayer.getGameHistory().setBattleground2(gameHistory.getBattleground1());
        isReadyToBattle = true;
        System.out.println("Ustawiono statki");
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public String getMyPlayerNickname() {
        Player temp = playerService.findPlayerById(this.gameHistory.getPlayer1_id());
        return temp.getNickname();
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

    public boolean isReadyToBattle() {
        return isReadyToBattle;
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    public boolean isOpponentLost() {
        return !opponentWon;
    }

    public void setOpponentWon(boolean opponentWon) {
        this.opponentWon = opponentWon;
    }
}

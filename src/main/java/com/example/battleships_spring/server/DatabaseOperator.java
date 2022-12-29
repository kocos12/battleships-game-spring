package com.example.battleships_spring.server;

import com.example.battleships_spring.model.GameHistory;
import com.example.battleships_spring.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DatabaseOperator {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameHistoryRepository gameHistoryRepository;

    public void saveSamplePlayer(){

        playerRepository.save(new Player("jaca","jacek","tajne"));
        playerRepository.save (new Player("jacek", "jaca2000", "1234"));
        playerRepository.save (new Player("gabore", "g*bor", "4321"));
    }
    public void saveSampleGameHistory() {
        ArrayList <Player> playerList = (ArrayList<Player>) playerRepository.findAll();
        GameHistory gameHistory = new GameHistory(playerList.get(0).getId(), playerList.get(1).getId());
        gameHistoryRepository.save(gameHistory);
    }
    public void deleteAll(){
        playerRepository.deleteAll();
        gameHistoryRepository.deleteAll();
    }

    public void showPlayers(){
        for (Player player: playerRepository.findAll()) {
            System.out.println(player);
        }
    }

    public void updatePlayerScore() {
        Player playerJacek = playerRepository.findByLogin("jacek");
        playerJacek.setScore(100);
        playerRepository.save(playerJacek);
    }

    public boolean tryLoggingInPlayer (String login, String passwd){
        for (Player player: playerRepository.findAll()) {
            if(player.getLogin().equals(login) && player.getPassword().equals(passwd)){
                return true;
            }
        }
        return false;
    }
    public Player findPlayerByLogin(String login){
        return playerRepository.findByLogin(login);
    }
}

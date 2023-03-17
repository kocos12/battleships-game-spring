package com.example.battleships_spring.server;

import com.example.battleships_spring.model.Player;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    private PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void saveSamplePlayer(){
        playerRepository.save(new Player("jaca","jacek","tajne"));
        playerRepository.save (new Player("jacek", "jaca2000", "1234"));
        playerRepository.save (new Player("gabore", "g*bor", "4321"));
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
    public Player findPlayerById(String id){
        return playerRepository.findPlayerById(id);
    }
    public void addPointToPlayer(String id){
       Player player = findPlayerById(id);
       player.setScore(player.getScore()+1);
       playerRepository.save(player);
    }
}

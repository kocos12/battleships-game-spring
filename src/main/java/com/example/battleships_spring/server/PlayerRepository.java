package com.example.battleships_spring.server;

import com.example.battleships_spring.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PlayerRepository extends MongoRepository<Player, String> {
    Player findByLogin(String login);
    Player findPlayerById(String id);
}

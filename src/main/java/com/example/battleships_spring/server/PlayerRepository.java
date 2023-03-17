package com.example.battleships_spring.server;

import com.example.battleships_spring.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {
    Player findByLogin(String login);
    Player findPlayerById(String id);
}

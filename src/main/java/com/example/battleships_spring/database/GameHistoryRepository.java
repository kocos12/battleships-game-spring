package com.example.battleships_spring.database;

import com.example.battleships_spring.model.GameHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameHistoryRepository extends MongoRepository<GameHistory, String> {
    //GamesHistory findGamesHistoryById (String id);
}

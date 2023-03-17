package com.example.battleships_spring.server;

import com.example.battleships_spring.model.GameHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameHistoryRepository extends MongoRepository<GameHistory, String> {
}

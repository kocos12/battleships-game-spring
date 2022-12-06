package com.example.battleships_spring.database;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BattleshipsSpringApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BattleshipsSpringApplication.class, args);
        DatabaseOperator databaseOperator = context.getBean(DatabaseOperator.class);

        databaseOperator.deleteAll();
        databaseOperator.saveSamplePlayer();
        databaseOperator.saveSampleGameHistory();
        databaseOperator.showPlayers();
        databaseOperator.updatePlayerScore();
        databaseOperator.updatePlayerScore();
        databaseOperator.showPlayers();
    }

}

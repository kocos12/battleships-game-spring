package com.example.battleships_spring.model;


public class Game {
    private Player player1;

    private Player player2;

    private GameResultEnum gameResultEnum;

    public Game(Player player1, Player player2, GameResultEnum gameResultEnum) {
        this.player1 = player1;
        this.player2 = player2;
        this.gameResultEnum = gameResultEnum;
    }

    @Override
    public String toString() {
        return "Game{" +
                "player1=" + player1 +
                ", player2=" + player2 +
                ", gameResultEnum=" + gameResultEnum +
                '}';
    }
}

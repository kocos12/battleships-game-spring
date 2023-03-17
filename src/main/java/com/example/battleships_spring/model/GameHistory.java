package com.example.battleships_spring.model;


import org.springframework.data.annotation.Id;

import java.util.HashMap;


public class GameHistory {
    @Id
    private String id;
    private HashMap<String, BattlegroundChunk> battleground1 = new HashMap<>(); //my battleground
    private HashMap<String, BattlegroundChunk> battleground2 = new HashMap<>(); //opponent's battleground
    private String player1_id;
    private String player2_id;

    public GameHistory() {}

    public void prepareBattleground() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                String pair = String.valueOf(i) + j;
                BattlegroundChunk battlegroundChunk = new BattlegroundChunk(false, false);
                this.battleground1.put(pair, battlegroundChunk);
            }
        }
    }

    public Boolean checkShot(String coord) {
        if (this.battleground2.get(coord).isShip()) {
            this.battleground2.get(coord).setHit(true);
            return true;
        } else {
            this.battleground2.get(coord).setHit(true);
            return false;
        }
    }

    public Boolean checkVictory() {
        for (BattlegroundChunk bgch : battleground2.values()) {
            if (bgch.isShip() && (!bgch.isHit())) {
                return true;
            }
        }
        return false;
    }

    public HashMap<String, BattlegroundChunk> getBattleground1() {
        return battleground1;
    }

    public void setBattleground2(HashMap<String, BattlegroundChunk> battleground2) {
        this.battleground2 = battleground2;
    }

    public void setShipOnCoord(String coord) {
        battleground1.get(coord).setShip(true);
    }

    public void removeShipOnCoord(String coord) {
        battleground1.get(coord).setShip(false);
    }

    public String getPlayer1_id() {
        return player1_id;
    }

    public void setPlayer1_id(String player1_id) {
        this.player1_id = player1_id;
    }

    public String getPlayer2_id() {
        return player2_id;
    }

    public void setPlayer2_id(String player2_id) {
        this.player2_id = player2_id;
    }

    @Override
    public String toString() {
        return "GameHistory{" +
                "id='" + id + '\'' +
                ", battleground1=" + battleground1 +
                ", battleground2=" + battleground2 +
                ", player1_id='" + player1_id + '\'' +
                ", player2_id='" + player2_id + '\'' +
                '}';
    }
}

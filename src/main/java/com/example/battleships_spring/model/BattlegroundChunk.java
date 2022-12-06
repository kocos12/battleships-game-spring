package com.example.battleships_spring.model;



public class BattlegroundChunk {
    private boolean isShip;
    private boolean isHit;

    public BattlegroundChunk(boolean isShip, boolean isHit) {
        this.isShip = isShip;
        this.isHit = isHit;
    }

    @Override
    public String toString() {
        return "BattlegroundChunk{" +
                "isShip=" + isShip +
                ", isHit=" + isHit +
                '}';
    }
}


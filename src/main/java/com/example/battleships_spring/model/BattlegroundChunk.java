package com.example.battleships_spring.model;



public class BattlegroundChunk {
    private boolean isShip;
    private boolean isHit;

    public BattlegroundChunk(boolean isShip, boolean isHit) {
        this.isShip = isShip;
        this.isHit = isHit;
    }

    public boolean isShip() {
        return isShip;
    }

    public void setShip(boolean ship) {
        isShip = ship;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    @Override
    public String toString() {
        return "BattlegroundChunk{" +
                "isShip=" + isShip +
                ", isHit=" + isHit +
                '}';
    }
}


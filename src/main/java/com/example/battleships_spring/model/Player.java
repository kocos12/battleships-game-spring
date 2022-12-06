package com.example.battleships_spring.model;


import org.springframework.data.annotation.Id;

import java.util.Objects;

public class Player {
    @Id
    private String id;
    private String nickname;
    private String login;
    private String password;
    private int score;

    public Player(String nickname, String login, String password) {

        this.nickname = nickname;
        this.login = login;
        this.password = password;
        this.score = 0;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", score=" + score +
                '}';
    }
}

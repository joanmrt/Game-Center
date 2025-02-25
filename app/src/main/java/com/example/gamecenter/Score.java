package com.example.gamecenter;

public class Score {
    private String name;
    private int points;
    private String game;

    public Score(String name, String game, int points) {
        this.game = game;
        this.name = name;
        this.points = points;
    }

    public String getGame() {
        return game;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }
}

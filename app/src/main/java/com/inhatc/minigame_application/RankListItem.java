package com.inhatc.minigame_application;

public class RankListItem {
    private String name;
    private int rank;
    private int score;

    public RankListItem(String name, int rank, int score){
        this.name=name;
        this.rank=rank;
        this.score=score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

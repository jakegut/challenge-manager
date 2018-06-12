package com.borgdude.challenge.objects;

import java.util.ArrayList;
import java.util.UUID;

public class ChallengeSet {

    private ArrayList<Challenge> challenges;
    private UUID uuid;
    private String title;

    public ChallengeSet(String title, ArrayList<Challenge> challenges, UUID uuid){
        this.title = title;
        this.challenges = challenges;
        this.uuid = uuid;
    }

    public Challenge createChallenge(String description){
        Challenge ch = new Challenge(description, UUID.randomUUID());
        this.challenges.add(ch);
        return ch;
    }

    public int getChallengeIndex(Challenge ch){
        return this.challenges.indexOf(ch) + 1;
    }

    public ArrayList<Challenge> getChallenges() {
        return challenges;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }


}

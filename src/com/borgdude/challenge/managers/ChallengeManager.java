package com.borgdude.challenge.managers;

import com.borgdude.challenge.objects.ChallengeSet;

import java.util.ArrayList;
import java.util.UUID;

public class ChallengeManager {

    private ArrayList<ChallengeSet> challengeSet;

    public ChallengeManager(ArrayList<ChallengeSet> challengeSet){
        this.challengeSet = challengeSet;
    }

    public ArrayList<ChallengeSet> getChallenges(){
        return this.challengeSet;
    }

    public ChallengeSet createChallengeSet(String title){
        ChallengeSet cs = new ChallengeSet(title, new ArrayList<>(), UUID.randomUUID());
        this.challengeSet.add(cs);
        return cs;
    }

    public ChallengeSet getChallengeSet(String title){
       for(ChallengeSet cs : challengeSet){
           if(cs.getTitle().equalsIgnoreCase(title))
               return cs;
       }
        return null;
    }
}

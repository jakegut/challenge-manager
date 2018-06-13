package com.borgdude.challenge.managers;

import com.borgdude.challenge.objects.Challenge;
import com.borgdude.challenge.objects.ChallengeSet;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ChallengeManager {

    private ArrayList<ChallengeSet> challengeSet;

    public HashMap<Player, ChallengeSet> getAssignedChallengeSets() {
        return assignedChallengeSets;
    }

    public void setAssignedChallengeSets(HashMap<Player, ChallengeSet> assignedChallengeSets) {
        this.assignedChallengeSets = assignedChallengeSets;
    }

    private HashMap<Player, ChallengeSet> assignedChallengeSets;
    private HashMap<Player, Challenge> completedChallenges;

    public ChallengeManager(ArrayList<ChallengeSet> challengeSet){
        this.challengeSet = challengeSet;
        this.assignedChallengeSets = new HashMap<>();
        this.completedChallenges = new HashMap<>();
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

    public int addPlayerToSet(Player player, ChallengeSet cs){
        if(this.assignedChallengeSets.containsKey(player)){
            if(this.assignedChallengeSets.get(player).equals(cs)){
                return -1; // Already assigned to cs
            } else {
                this.assignedChallengeSets.replace(player, cs);
                return 2; //Assigned to a new challenge set
            }
        }

        this.assignedChallengeSets.put(player, cs);
        return 1;


    }
}

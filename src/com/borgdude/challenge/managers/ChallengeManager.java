package com.borgdude.challenge.managers;

import com.borgdude.challenge.events.ChallengeCompletedEvent;
import com.borgdude.challenge.events.ChallengeSetCompletedEvent;
import com.borgdude.challenge.objects.Challenge;
import com.borgdude.challenge.objects.ChallengeSet;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChallengeManager {

    public ChallengeManager(ArrayList<ChallengeSet> challengeSet){
        this.challengeSet = challengeSet;
        this.assignedChallengeSets = new HashMap<>();
        this.completedChallenges = new HashMap<>();
    }

    private ArrayList<ChallengeSet> challengeSet;

    public HashMap<Player, ChallengeSet> getAssignedChallengeSets() {
        return assignedChallengeSets;
    }

    public void setAssignedChallengeSets(HashMap<Player, ChallengeSet> assignedChallengeSets) {
        this.assignedChallengeSets = assignedChallengeSets;
    }

    private HashMap<Player, ChallengeSet> assignedChallengeSets;

    public void removePlayer(Player player){
        if(this.getAssignedChallengeSets().containsKey(player)){
            Bukkit.getConsoleSender().sendMessage(player.getName() + " has been removed from their set.");
            this.getAssignedChallengeSets().remove(player);
        }

        if(this.getCompletedChallenges().containsKey(player)){
            this.getCompletedChallenges().remove(player);
        }

    }

    public int completedChallenge(Player player){
        if(this.completedChallenges.containsKey(player)){
            Challenge currentChallenge = this.completedChallenges.get(player);
            ChallengeSet cs = this.getChallengeSetById(currentChallenge.getCsUUID());
            if(cs.getChallenges().contains(currentChallenge)){
                int index = cs.getChallengeIndex(currentChallenge);
                if((index + 1) == cs.getChallenges().size()){
                    //ChallengeCompletedEvent challengeCompletedEvent = new ChallengeCompletedEvent(player, cs.getChallenges().get(index));
                    ChallengeSetCompletedEvent challengeSetCompletedEvent = new ChallengeSetCompletedEvent(cs, player);
                    Bukkit.getPluginManager().callEvent(challengeSetCompletedEvent);
                    //Bukkit.getPluginManager().callEvent(challengeCompletedEvent);
                    return 3; // Completed all challenges in set
                }
                this.completedChallenges.replace(player, cs.getChallenges().get(index));
                ChallengeCompletedEvent challengeCompletedEvent = new ChallengeCompletedEvent(player, cs.getChallenges().get(index));
                Bukkit.getPluginManager().callEvent(challengeCompletedEvent);
                return 1; //Successfully completed next challenge
            } else{
                return -2; //How tf did this happen
            }
        } else {
            if(!this.assignedChallengeSets.containsKey(player))
                return -1;

            Challenge ch = this.assignedChallengeSets.get(player).getChallenges().get(0);

            this.completedChallenges.put(player, ch);

            ChallengeCompletedEvent challengeCompletedEvent = new ChallengeCompletedEvent(player, ch);
            Bukkit.getPluginManager().callEvent(challengeCompletedEvent);

            return 2; //Successfully completed first challenge in set
        }
    }

    public Challenge getCurrentChallenge(Player player){
        if(this.completedChallenges.containsKey(player)){
            Challenge ch = this.getCompletedChallenges().get(player);
            UUID csUUID = ch.getCsUUID();
            ChallengeSet cs = this.getChallengeSetById(csUUID);
            int num = cs.getChallengeIndex(ch);
            return cs.getChallenges().get(num);
        }
        else if(this.assignedChallengeSets.containsKey(player))
            return this.assignedChallengeSets.get(player).getChallenges().get(0);
        else
            return null;
    }

    public HashMap<Player, Challenge> getCompletedChallenges() {
        return completedChallenges;
    }

    public void setCompletedChallenges(HashMap<Player, Challenge> completedChallenges) {
        this.completedChallenges = completedChallenges;
    }

    private HashMap<Player, Challenge> completedChallenges;

    public ArrayList<ChallengeSet> getChallenges(){
        return this.challengeSet;
    }

    public ChallengeSet createChallengeSet(String title){
        ChallengeSet cs = new ChallengeSet(title, new ArrayList<>(), UUID.randomUUID());
        this.challengeSet.add(cs);
        return cs;
    }

    public ChallengeSet getChallengeSetByString(String title){
       for(ChallengeSet cs : challengeSet){
           if(cs.getTitle().equalsIgnoreCase(title))
               return cs;
       }
        return null;
    }

    public ChallengeSet getChallengeSetById(UUID uuid){
        for(ChallengeSet cs : challengeSet){
            if(cs.getUuid().equals(uuid))
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

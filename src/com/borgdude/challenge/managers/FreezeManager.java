package com.borgdude.challenge.managers;

import java.util.ArrayList;

public class FreezeManager {

    private ArrayList<String> playersFrozen;

    public FreezeManager(){
        this.playersFrozen = new ArrayList<>();
    }

    public ArrayList<String> getPlayersFrozen() {
        return playersFrozen;
    }

    public void setPlayersFrozen(ArrayList<String> playersFrozen) {
        this.playersFrozen = playersFrozen;
    }

    public boolean isFrozen(String player){
        if(playersFrozen.indexOf(player) == -1)
            return false;

        return true;
    }

    public void addPlayer(String player){
        if(!isFrozen(player))
            playersFrozen.add(player);
    }

    public void removePlayer(String player){
        if(isFrozen(player)){
            playersFrozen.remove(player);
        }
    }
}

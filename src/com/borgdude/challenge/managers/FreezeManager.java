package com.borgdude.challenge.managers;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
        if(!isFrozen(player)){
            Player p = Bukkit.getPlayer(player);
            p.sendMessage(ChatColor.BLUE + "You have been frozen!");
            playersFrozen.add(player);
        }

    }

    public void removePlayer(String player){
        if(isFrozen(player)){
            Player p = Bukkit.getPlayer(player);
            p.sendMessage(ChatColor.GREEN + "You are now unfrozen!");
            playersFrozen.remove(player);
        }
    }
}

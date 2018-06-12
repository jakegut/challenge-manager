package com.borgdude.challenge.events;

import com.borgdude.challenge.Main;
import com.borgdude.challenge.managers.FreezeManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class EventClass implements Listener {

    private FreezeManager freezeManager = Main.freezeManager;

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Action action = event.getAction();
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if(action.equals(Action.LEFT_CLICK_BLOCK)){
            if(block.getType().equals(Material.EMERALD_BLOCK)){
                player.sendMessage(ChatColor.GREEN + "You have been healed");
                player.setHealth(20);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(freezeManager.isFrozen(player.getName())){
            event.setTo(event.getFrom());
        }

    }


}



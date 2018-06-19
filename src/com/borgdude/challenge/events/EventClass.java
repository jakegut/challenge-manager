package com.borgdude.challenge.events;

import com.borgdude.challenge.Main;
import com.borgdude.challenge.managers.ChallengeBookManager;
import com.borgdude.challenge.managers.ChallengeManager;
import com.borgdude.challenge.managers.FreezeManager;
import com.borgdude.challenge.objects.ChallengeSet;
import com.borgdude.challenge.util.SetBookUtil;
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
    private ChallengeManager challengeManager = Main.challengeManager;
    private ChallengeBookManager challengeBookManager = Main.challengeBookManager;
    private Main plugin = Main.plugin;

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

    @EventHandler
    public void onAssignedPlayer(AssignedPlayerEvent event){
        Player player = event.getPlayer();

        SetBookUtil.updateSetBook(player, this.challengeManager.getAssignedChallengeSets().get(player).getChallenges().get(0));

    }

    @EventHandler
    public void onChallengeCompleted(ChallengeCompletedEvent event){
        Player player = (Player) event.getPlayer();
        this.challengeBookManager.removePlayer(player);
        ChallengeSet cs = challengeManager.getAssignedChallengeSets().get(player);
        SetBookUtil.updateSetBook(player, cs.getChallenges().get(cs.getChallengeIndex(event.getChallengeCompleted())));
    }

    @EventHandler
    public void onChallengeSetCompleted(ChallengeSetCompletedEvent event){
        Player player = (Player) event.getPlayer();
        this.challengeBookManager.removePlayer(player);
        SetBookUtil.removeSetBook(player, event.getChallengeSetCompleted());
    }
}



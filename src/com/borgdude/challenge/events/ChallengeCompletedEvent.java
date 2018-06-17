package com.borgdude.challenge.events;

import com.borgdude.challenge.objects.Challenge;
import com.borgdude.challenge.objects.ChallengeSet;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChallengeCompletedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Challenge getChallengeCompleted() {
        return challengeCompleted;
    }

    public void setChallengeCompleted(Challenge challengeCompleted) {
        this.challengeCompleted = challengeCompleted;
    }

    private Challenge challengeCompleted;

    public ChallengeCompletedEvent(Player player, Challenge challengeCompleted) {
        this.player = player;
        this.challengeCompleted = challengeCompleted;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

package com.borgdude.challenge.events;

import com.borgdude.challenge.objects.ChallengeSet;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChallengeSetCompletedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public ChallengeSet getChallengeSetCompleted() {
        return challengeSetCompleted;
    }

    public void setChallengeSetCompleted(ChallengeSet challengeSetCompleted) {
        this.challengeSetCompleted = challengeSetCompleted;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private ChallengeSet challengeSetCompleted;
    private Player player;

    public ChallengeSetCompletedEvent(ChallengeSet challengeSetCompleted, Player player) {
        this.challengeSetCompleted = challengeSetCompleted;
        this.player = player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

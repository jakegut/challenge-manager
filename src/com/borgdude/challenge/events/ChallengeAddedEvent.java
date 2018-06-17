package com.borgdude.challenge.events;

import com.borgdude.challenge.objects.Challenge;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChallengeAddedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Challenge challenge;

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public ChallengeAddedEvent(Challenge challenge) {

        this.challenge = challenge;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

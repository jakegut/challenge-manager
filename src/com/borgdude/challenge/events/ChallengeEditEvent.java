package com.borgdude.challenge.events;

import com.borgdude.challenge.objects.Challenge;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChallengeEditEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Challenge previous;
    private Challenge current;

    public ChallengeEditEvent(Challenge previous, Challenge current) {
        this.previous = previous;
        this.current = current;
    }


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Challenge getPreviousChallenge() {
        return previous;
    }

    public void setPreviousChallenge(Challenge previous) {
        this.previous = previous;
    }

    public Challenge getCurrentChallenge() {
        return current;
    }

    public void setCurrentChallenge(Challenge current) {
        this.current = current;
    }
}

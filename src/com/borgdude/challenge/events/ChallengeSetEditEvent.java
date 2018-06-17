package com.borgdude.challenge.events;

import com.borgdude.challenge.objects.ChallengeSet;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChallengeSetEditEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private ChallengeSet previous;

    public ChallengeSet getPreviousChallengeSet() {
        return previous;
    }

    public void setPreviousChallengeSet(ChallengeSet previous) {
        this.previous = previous;
    }

    public ChallengeSet getCurrentChallengeSet() {
        return current;
    }

    public void setCurrentChallengeSet(ChallengeSet current) {
        this.current = current;
    }

    private ChallengeSet current;

    public ChallengeSetEditEvent(ChallengeSet prev, ChallengeSet curr) {
        previous = prev;
        current = curr;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
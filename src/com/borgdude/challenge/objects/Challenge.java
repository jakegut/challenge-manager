package com.borgdude.challenge.objects;

import java.util.UUID;

public class Challenge {

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    private String description;
    private UUID uuid;

    public Challenge(String desc, UUID uuid){
        this.description = desc;
        this.uuid = uuid;
    }

}

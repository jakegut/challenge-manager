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


    public UUID getCsUUID() {
        return csUUID;
    }

    public void setCsUUID(UUID csUUID) {
        this.csUUID = csUUID;
    }

    private UUID csUUID;

    public Challenge(String desc, UUID uuid, UUID csUUID){
        this.description = desc;
        this.uuid = uuid;
        this.csUUID = csUUID;
    }

}

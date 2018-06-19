package com.borgdude.challenge;


import com.borgdude.challenge.commands.ChallengeCommand;
import com.borgdude.challenge.commands.FreezeCommand;
import com.borgdude.challenge.commands.OnTabComplete;
import com.borgdude.challenge.commands.UnfreezeCommand;
import com.borgdude.challenge.events.EventClass;
import com.borgdude.challenge.managers.ChallengeBookManager;
import com.borgdude.challenge.managers.ChallengeManager;
import com.borgdude.challenge.managers.FreezeManager;
import com.borgdude.challenge.objects.Challenge;
import com.borgdude.challenge.objects.ChallengeSet;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public class Main extends JavaPlugin {

    public static FreezeManager freezeManager;
    public static ChallengeManager challengeManager;
    public static ChallengeBookManager challengeBookManager;
    public static Main plugin;

    @Override
    public void onEnable(){
        plugin = this;
        freezeManager = new FreezeManager();
        challengeBookManager = new ChallengeBookManager();
        getConfig().options().copyDefaults(true);
        saveConfig();
        loadChallengeSets();
        getCommand("freeze").setExecutor(new FreezeCommand());
        getCommand("unfreeze").setExecutor(new UnfreezeCommand());
        getCommand("challenge").setExecutor(new ChallengeCommand());
        getCommand("challenge").setTabCompleter(new OnTabComplete());
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Challenges has been enabled");
        getServer().getPluginManager().registerEvents(new EventClass(), this);
    }

    @Override
    public void onDisable(){
        for(ChallengeSet cs : this.challengeManager.getChallenges()){
            UUID csID = cs.getUuid();
            getConfig().set("challenges." + csID + ".title", cs.getTitle());
            for(Challenge ch : cs.getChallenges()){
                UUID chID = ch.getUuid();
                getConfig().set("challenges." + csID + "." + chID + ".description",
                        ch.getDescription());
                getConfig().set("challenges." + csID + "." + chID + ".index",
                        cs.getChallengeIndex(ch));
            }
        }
        saveConfig();
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "Challenges has been disabled");
    }

    public void loadChallengeSets(){
        ArrayList<ChallengeSet> challengeSets = new ArrayList<>();
        ConfigurationSection section = getConfig().getConfigurationSection("challenges");
        if(section != null){
            for(String csID : section.getKeys(false)){
                UUID uuid = UUID.fromString(csID);
                String title = getConfig().getString("challenges." + uuid + ".title");
                ArrayList<Challenge> challenges = new ArrayList<>();
                for(String chID : getConfig().getConfigurationSection("challenges." + csID).getKeys(false)){
                    try{
                        UUID chUUID = UUID.fromString(chID);
                        String desc = getConfig().getString("challenges." + csID + "." + chID + ".description");
                        int index = getConfig().getInt("challenges." + csID + "." + chID + ".index");
                        challenges.add(index - 1, new Challenge(desc, chUUID, uuid));
                    } catch (IllegalArgumentException exception){
                        continue;
                    }
                }
                challengeSets.add(new ChallengeSet(title, challenges, uuid));
            }
        }
        challengeManager = new ChallengeManager(challengeSets);
    }
}

package com.borgdude.challenge.commands;

import com.borgdude.challenge.Main;
import com.borgdude.challenge.objects.Challenge;
import com.borgdude.challenge.objects.ChallengeSet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OnTabComplete implements TabCompleter {

    private Main plugin = Main.plugin;

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if(command.getName().equalsIgnoreCase("challenge")){
            if(args.length == 1){
                return Arrays.asList("set", "list", "complete", "current");
            }

            if(args.length == 2){
                if(args[0].equalsIgnoreCase("list")){
                    ArrayList<String> r = this.getChallengeSet(args[1]);
                    if(("list").startsWith(args[1]))
                        r.add("list");
                    return r;
                }
                if(args[0].equalsIgnoreCase("set")){
                    return this.getChallengeSet(args[1]);
                }

                if(args[0].equalsIgnoreCase("edit")){
                    return this.getChallengeSet(args[1]);
                }
            }
        }
        return null;
    }

    public ArrayList<String> getChallengeSet(String start){
        ArrayList<String> r = new ArrayList<>();

        if(!start.equals("")){
            for(ChallengeSet cs : plugin.challengeManager.getChallenges()){
                if(cs.getTitle().startsWith(start))
                    r.add(cs.getTitle());
            }
        } else{
            for(ChallengeSet cs : plugin.challengeManager.getChallenges()){
                r.add(cs.getTitle());
            }
        }

        Collections.sort(r);

        return r;
    }
}

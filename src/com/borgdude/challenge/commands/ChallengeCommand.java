package com.borgdude.challenge.commands;

import com.borgdude.challenge.Main;
import com.borgdude.challenge.managers.ChallengeManager;
import com.borgdude.challenge.objects.Challenge;
import com.borgdude.challenge.objects.ChallengeSet;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class ChallengeCommand implements CommandExecutor {

    private ChallengeManager challengeManager = Main.challengeManager;
    private Main plugin = Main.plugin;
    private HashMap<Player, ChallengeSet> currentlyEditing;

    public ChallengeCommand(){
        this.currentlyEditing = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("You must be a player!");
            return true;
        }
        if(command.getName().equalsIgnoreCase("challenge")){
            Player player = (Player) sender;

            if(args.length == 0){
                player.sendMessage(ChatColor.GREEN + "Available commands for: " +ChatColor.BOLD + ChatColor.AQUA +
                        "Challenges");
                if(player.hasPermission("challenges.admin")){
                    player.sendMessage(ChatColor.GREEN + "/challenge create <title> " + ChatColor.RESET +
                            "Create a new set of challenges with a title.");
                }
                player.sendMessage(ChatColor.GREEN + "/challenge list " + ChatColor.RESET +
                        "List all of the sets of challenges.");
                return true;
            }

            if(player.hasPermission("challenges.admin")){
                if(args[0].equalsIgnoreCase("create")){

                    if(args.length < 2){
                        player.sendMessage(ChatColor.RED + "Usage: /challenge create <title>");
                        return true;
                    }

                    ChallengeSet cs = challengeManager.createChallengeSet(args[1]);
                    player.sendMessage(ChatColor.GREEN + "Challenge: " + ChatColor.AQUA + args[1] + ChatColor.GREEN +
                            " added successfully.");
                    setCurrentlyEditing(player, cs);
                    return true;
                } else if (args[0].equalsIgnoreCase("edit")){

                    if(args.length < 2){
                        player.sendMessage(ChatColor.RED + "Usage: /challenge edit <title of challenge>");
                        return true;
                    }

                    ChallengeSet cs = challengeManager.getChallengeSet(args[1]);
                    if(cs != null){
                        setCurrentlyEditing(player, cs);
                    } else {
                        player.sendMessage(ChatColor.RED + "Couldn't find challenge set named: " +
                                ChatColor.GOLD + args[1]);
                        return true;
                    }

                    return true;
                } else if (args[0].equalsIgnoreCase("change")){
                    if(args[1].equalsIgnoreCase("title")){
                        String title;
                        try{
                            title = args[2];
                        } catch(ArrayIndexOutOfBoundsException e){
                            player.sendMessage(ChatColor.RED + "Usage: /challenge change title <title>");
                            return true;
                        }

                        String previousTitle = this.currentlyEditing.get(player).getTitle();
                        this.currentlyEditing.get(player).setTitle(title);
                        player.sendMessage(ChatColor.GREEN + "Changed challenge named: " + ChatColor.BLUE +
                                previousTitle + ChatColor.GREEN + " to: " + ChatColor.BLUE + title);
                        return true;
                    } else {
                        try{
                            int index = Integer.parseInt(args[1]);
                            if(index > this.currentlyEditing.get(player).getChallenges().size() || index < 1){
                                player.sendMessage(ChatColor.RED + "That challenge doesn't exist in" + ChatColor.GOLD
                                        + this.currentlyEditing.get(player).getTitle());
                                return true;
                            }

                            String desc = "";

                            for(int i = 2; i < args.length; i++){
                                if(i != 2)
                                    desc += " ";

                                desc += args[i];
                            }

                            String previousDesc = this.currentlyEditing.get(player).getChallenges().get(index - 1)
                                    .getDescription();
                            this.currentlyEditing.get(player).getChallenges().get(index - 1).setDescription(desc);

                            player.sendMessage(ChatColor.GREEN + "Changed challenge number " + ChatColor.AQUA +
                                    String.valueOf(index) + ChatColor.GREEN +  " from " + ChatColor.AQUA + previousDesc
                                    + " " + ChatColor.GREEN +  " to " + ChatColor.AQUA + desc);

                        } catch (IllegalArgumentException exception){
                            return true;
                        }
                }
                } else if (args[0].equalsIgnoreCase("add")){

                    if(this.currentlyEditing.get(player) == null){
                        player.sendMessage(ChatColor.RED +
                                "You need to be editing a challenge. Use: /challenge edit <challenge set>");
                        return true;
                    }

                    if (args.length < 1){
                        player.sendMessage(ChatColor.RED +
                                "Usage: /challenge add <description of challenge> (Adds a challenge to: " +
                                ChatColor.AQUA + this.currentlyEditing.get(player).toString());
                        return true;
                    }

                    String desc = "";

                    for(int i = 1; i < args.length; i++){
                        if(i != 1)
                            desc += " ";

                        desc += args[i];
                    }

                    ChallengeSet cs = this.currentlyEditing.get(player);
                    Challenge ch = cs.createChallenge(desc);
                    player.sendMessage(ChatColor.GREEN + "Added Challenge: " + ChatColor.AQUA + desc +
                            ChatColor.GREEN + " to " + ChatColor.AQUA + cs.getTitle() + ChatColor.GREEN +
                            " with index: " + ChatColor.AQUA + cs.getChallengeIndex(ch));
                    return true;
                }
            }

            if(args[0].equalsIgnoreCase("list")){
                if(args.length > 1) {
                    ChallengeSet cs = this.challengeManager.getChallengeSet(args[1]);
                    if (this.challengeManager.getChallengeSet(args[1]) == null) {
                        player.sendMessage(ChatColor.RED + "Couldn't find challenge set named: " + ChatColor.GOLD +
                        args[1]);
                        return true;
                    }

                    if(cs.getChallenges().isEmpty()){
                        player.sendMessage(ChatColor.RED + "The Challenge set " + ChatColor.GOLD + cs.getTitle() +
                        ChatColor.RED + " has no challenges. Use: /challenge add <desc> to add one.");
                        return true;
                    }

                    player.sendMessage(ChatColor.AQUA + "Challenges for: " + ChatColor.GREEN + ChatColor.BOLD +
                            cs.getTitle());

                    for(Challenge ch : cs.getChallenges()){
                        player.sendMessage(ChatColor.YELLOW + String.valueOf(cs.getChallengeIndex(ch)) + ". " +
                        ChatColor.AQUA + ch.getDescription());
                    }

                    return true;

                } else {
                    ArrayList<ChallengeSet> challengeSet = challengeManager.getChallenges();
                    if (challengeSet.isEmpty()) {
                        player.sendMessage(ChatColor.RED + "No challenges!");
                        return true;
                    }

                    int i = 1;

                    for (ChallengeSet cs : challengeSet) {
                        player.sendMessage(ChatColor.YELLOW + String.valueOf(i) + ". " + ChatColor.AQUA + cs.getTitle());
                        i++;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void setCurrentlyEditing(Player player, ChallengeSet cs){
        if(currentlyEditing.containsKey(player))
            currentlyEditing.replace(player, cs);
        else
            currentlyEditing.put(player, cs);

        player.sendMessage(ChatColor.GREEN + "Now currently editing the Challenge Set: " + ChatColor.BLUE + cs.getTitle());
    }
}

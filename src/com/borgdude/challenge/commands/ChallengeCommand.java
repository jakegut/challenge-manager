package com.borgdude.challenge.commands;

import com.borgdude.challenge.Main;
import com.borgdude.challenge.managers.ChallengeManager;
import com.borgdude.challenge.objects.Challenge;
import com.borgdude.challenge.objects.ChallengeSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

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
                player.sendMessage(ChatColor.GREEN + "Available commands for: " + ChatColor.BOLD + ChatColor.AQUA +
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

                    ChallengeSet cs = challengeManager.getChallengeSetByString(args[1]);
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
                } else if (args[0].equalsIgnoreCase("set")){

                    if(args.length < 1){
                        player.sendMessage(ChatColor.RED + "Usage: /challenge set <ChallengeSet|list> player <players>");
                        return true;
                    }

                    if(args[1].equalsIgnoreCase("list")){
                        HashMap<Player, ChallengeSet> challengeSets = this.challengeManager.getAssignedChallengeSets();
                        if(challengeSets.isEmpty()){
                            player.sendMessage(ChatColor.RED + "No challenges have been assigned yet!");
                            return true;
                        }

                        for(Player challenger : challengeSets.keySet()){
                            player.sendMessage(ChatColor.AQUA + challenger.getName() + ChatColor.BLUE + " : " +
                                    challengeSets.get(challenger).getTitle());
                        }
                        return true;
                    }

                    ChallengeSet cs = this.challengeManager.getChallengeSetByString(args[1]);
                    if(cs == null){
                        player.sendMessage(ChatColor.RED + "The challenge set: " + ChatColor.GOLD + args[1] +
                        ChatColor.RED + " doesn't exist.");
                        return true;
                    }

                    if(cs.getChallenges().isEmpty()){
                        player.sendMessage(ChatColor.RED + "The Challenge set " + ChatColor.GOLD + cs.getTitle() +
                                ChatColor.RED + " has no challenges. Use: /challenge add <desc> to add one.");
                        return true;
                    }

                    if(args.length < 4 || !(args[2].equalsIgnoreCase("player"))){
                        player.sendMessage(ChatColor.RED + "Usage: /challenge set <ChallengeSet> player <players>");
                        return true;
                    }

                    for(int i = 3; i < args.length; i++){
                        Player challenger = plugin.getServer().getPlayer(args[i]);

                        if(challenger == null){
                            player.sendMessage(ChatColor.RED + "Player " + ChatColor.GOLD + args[i] +
                                    ChatColor.RED + " not found");
                            continue;
                        }


                        int status = this.challengeManager.addPlayerToSet(challenger, cs);

                        if(status == -1){ // -1: Challenge already set to that player
                            player.sendMessage(ChatColor.RED + "Player " + ChatColor.GOLD + args[i] +
                                    ChatColor.RED + " already added to the set.");
                        } else if(status == 2){
                            challenger.sendMessage(ChatColor.GREEN + "You have been changed to the challenge set: " +
                                    ChatColor.AQUA + cs.getTitle());
                            player.sendMessage(ChatColor.GREEN + "Player " + ChatColor.AQUA + args[i] + ChatColor.GREEN +
                                    " changed to the challenge set: " + ChatColor.AQUA + cs.getTitle());
                        } else if(status == 1){
                            challenger.sendMessage(ChatColor.GREEN + "You have been added to the challenge set: " +
                                    ChatColor.AQUA + cs.getTitle());
                            player.sendMessage(ChatColor.GREEN + "Player " + ChatColor.AQUA + args[i] + ChatColor.GREEN +
                                    " added to the challenge set: " + ChatColor.AQUA + cs.getTitle());
                        }


                    }

                    return true;

                } else if (args[0].equalsIgnoreCase("complete")){
                    if(args.length < 1){
                        player.sendMessage(ChatColor.RED + "Usage: /challenge complete <player(s)>");
                        return true;
                    }

                    for(int i = 1; i < args.length; i++){
                        Player challenger = Bukkit.getPlayer(args[i]);

                        if(challenger == null){
                            player.sendMessage(ChatColor.RED + "Player " + ChatColor.GOLD + args[i] +
                                    ChatColor.RED + " not found");
                            continue;
                        }

                        int status = this.challengeManager.completedChallenge(challenger);

                        if(status == -2){
                            player.sendMessage(ChatColor.RED + "Something went terribly wrong.");
                        } else if (status == -1){
                            player.sendMessage(ChatColor.RED + "Player " + ChatColor.GOLD + args[i] +
                                    ChatColor.RED + " has not been assigned a challenge set.");
                        } else if (status == 1 || status == 2) {

                            Challenge ch = this.challengeManager.getCompletedChallenges().get(challenger);
                            UUID csUUID = ch.getCsUUID();
                            ChallengeSet cs = this.challengeManager.getChallengeSetById(csUUID);
                            int num = cs.getChallengeIndex(ch);

                            Bukkit.getServer().broadcastMessage(ChatColor.AQUA + challenger.getName() + ChatColor.GREEN +
                                    " has successfully completed challenge "  + ChatColor.AQUA + num +
                                    ChatColor.GREEN + " in " + ChatColor.AQUA + cs.getTitle());
                            launchFirework(challenger);

                        } else if (status == 3) {

                            Challenge ch = this.challengeManager.getCompletedChallenges().get(challenger);
                            UUID csUUID = ch.getCsUUID();
                            ChallengeSet cs = this.challengeManager.getChallengeSetById(csUUID);

                            Bukkit.getServer().broadcastMessage(ChatColor.AQUA + challenger.getName() + ChatColor.GREEN +
                                    " has successfully completed all the challenges for " + ChatColor.AQUA + cs.getTitle());
                            launchFirework(challenger);

                            this.challengeManager.removePlayer(challenger);
                        }
                    }

                    return true;
                }
            }

            if(args[0].equalsIgnoreCase("list")){
                if(args.length > 1) {
                    ChallengeSet cs = this.challengeManager.getChallengeSetByString(args[1]);
                    if (this.challengeManager.getChallengeSetByString(args[1]) == null) {
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
            } else if (args[0].equalsIgnoreCase("current")){
                Challenge currentChallenge = this.challengeManager.getCurrentChallenge(player);

                if(currentChallenge == null){
                    player.sendMessage(ChatColor.RED + "You're not in a challenge set!");
                    return true;
                }

                UUID csUUID = currentChallenge.getCsUUID();
                ChallengeSet cs = this.challengeManager.getChallengeSetById(csUUID);
                int num = cs.getChallengeIndex(currentChallenge);

                player.sendMessage(ChatColor.BLUE + "Current challenge: " + ChatColor.AQUA + num + ". " +
                        currentChallenge.getDescription());

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

    public void launchFirework(Player player){
        Firework f = player.getWorld().spawn(player.getLocation(), Firework.class);
        FireworkMeta fm = f.getFireworkMeta();
        fm.addEffect(FireworkEffect.builder()
                .flicker(false)
                .trail(true)
                .with(FireworkEffect.Type.STAR)
                .withColor(Color.AQUA)
                .withFade(Color.BLUE)
                .build());
        fm.setPower(2);
        f.setFireworkMeta(fm);
    }
}

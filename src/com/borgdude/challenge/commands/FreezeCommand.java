package com.borgdude.challenge.commands;

import com.borgdude.challenge.Main;
import com.borgdude.challenge.managers.FreezeManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class FreezeCommand implements CommandExecutor {

    private FreezeManager freezeManager = Main.freezeManager;
    private Main plugin = Main.plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args){
        if(sender instanceof Player){
            Player player = (Player) sender;

            if(command.getName().equalsIgnoreCase("freeze")){
                if(!player.hasPermission("challenges.freeze")){
                    player.sendMessage(ChatColor.RED + "You don't have permission to use this command");
                    return true;
                }
                if(args.length == 0){
                    player.sendMessage(ChatColor.RED + "/freeze <players|all>");
                } else{
                    System.out.print(args[0]);
                    if(args[0].equalsIgnoreCase("all")){
                        for(Player p: plugin.getServer().getOnlinePlayers()) {
                            freezeManager.addPlayer(p.getName());
                            player.sendMessage(ChatColor.GREEN + "Froze: " + ChatColor.BLUE + p.getName());
                        }
                    } else {
                        for(int i = 0; i < args.length; i++){
                            freezeManager.addPlayer(args[i]);
                            player.sendMessage(ChatColor.GREEN + "Froze: " + ChatColor.BLUE + args[i]);
                        }
                    }
                }
            }
        }
        return true;
    }
}

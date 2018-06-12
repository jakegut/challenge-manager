package com.borgdude.challenge.commands;

import com.borgdude.challenge.Main;
import com.borgdude.challenge.managers.FreezeManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnfreezeCommand implements CommandExecutor {

    private FreezeManager freezeManager = Main.freezeManager;
    private Main plugin = Main.plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;

            if(command.getName().equalsIgnoreCase("unfreeze")){
                if(!player.hasPermission("challenges.unfreeze")){
                    player.sendMessage(ChatColor.RED + "You don't have permission to use this command");
                    return true;
                }
                if(args.length == 0){
                    player.sendMessage(ChatColor.RED + "/unfreeze <player|all>");
                } else{
                    if(args[0].equalsIgnoreCase("all")){
                        for(Player p: plugin.getServer().getOnlinePlayers()) {
                            freezeManager.removePlayer(p.getName());
                            player.sendMessage(ChatColor.GREEN + "Unfroze: " + ChatColor.BLUE + p.getName());
                            p.sendMessage(ChatColor.BLUE + "You're now unfrozen.");
                        }
                    } else {
                        for(int i = 0; i < args.length; i++){
                            freezeManager.removePlayer(args[i]);
                            player.sendMessage(ChatColor.GREEN + "Unfroze: " + ChatColor.BLUE + args[i]);
                            Bukkit.getPlayer(args[i]).sendMessage(ChatColor.BLUE + "You're now unfrozen.");
                        }
                    }
                }
            }
        }
        return true;
    }
}

package com.borgdude.challenge.managers;

import com.borgdude.challenge.Main;
import com.borgdude.challenge.objects.Challenge;
import com.borgdude.challenge.objects.ChallengeSet;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChallengeBookManager {
    public ItemStack getBook() {
        return book;
    }

    public void setBook(ItemStack book) {
        this.book = book;
    }

    private ItemStack book;
    private BookMeta bm;
    private List<IChatBaseComponent> pages;

    public ArrayList<Player> getChallengeQueue() {
        return challengeQueue;
    }

    public void setChallengeQueue(ArrayList<Player> challengeQueue) {
        this.challengeQueue = challengeQueue;
    }

    public void addPlayer(Player player){
        if(!challengeQueue.contains(player)){
            challengeQueue.add(player);
            player.sendMessage("The admins have been notified.");
            for(Player p : Bukkit.getServer().getOnlinePlayers()){
                if(p.hasPermission("challenges.admin"))
                    p.sendMessage(ChatColor.AQUA + "Your challenge book has been updated.");
            }
            updateBook();
        }
    }

    public void removePlayer(Player player){
        if(challengeQueue.contains(player)){
            challengeQueue.remove(player);
            updateBook();
        }
    }

    private ArrayList<Player> challengeQueue;

    private ChallengeManager challengeManager = Main.challengeManager;

    public ChallengeBookManager() {
        challengeQueue = new ArrayList<>();

        book = new ItemStack(Material.WRITTEN_BOOK);
        bm = (BookMeta) book.getItemMeta();

        try{
            pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bm);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return;
        }

        //set the title and author of this book
        bm.setTitle("Challenge Book");
        bm.setAuthor("Borgdude");
        bm.setPages("Nothing here yet");

        //update the ItemStack with this new meta
        book.setItemMeta(bm);
    }

    public ItemStack makeBook(){
        String nl = "\n";
        ItemStack newBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bm = (BookMeta) newBook.getItemMeta();
        List<IChatBaseComponent> pages;
        bm.setTitle("Challenge Book");
        bm.setAuthor("FUNGAdmins");

        try{
            pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bm);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return null;
        }

        TextComponent text = new TextComponent();

        TextComponent sep = new TextComponent(" | ");
        sep.setUnderlined(false);
        sep.setColor(ChatColor.RESET);

        for(Player player : challengeQueue){
            TextComponent p = new TextComponent(player.getName());
            p.setColor(ChatColor.BLUE);
            p.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
                challengeManager.getCurrentChallenge(player).getDescription()
            ).create() ) );

            TextComponent tp = new TextComponent("TP");
            tp.setUnderlined(true);
            tp.setColor(ChatColor.DARK_AQUA);
            tp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + player.getName()));
            tp.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
                    "TP to " + player.getName()
            ).create() ) );

            TextComponent complete = new TextComponent("V");
            complete.setUnderlined(true);
            complete.setColor(ChatColor.DARK_GREEN);
            complete.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/challenge complete " + player.getName()));
            complete.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
                    "Set " + player.getName() + "'s challenge completed"
            ).create() ) );

            TextComponent deny = new TextComponent("X");
            deny.setColor(ChatColor.DARK_RED);
            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/challenge deny " + player.getName()));
            deny.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
                    "Deny " + player.getName() + "'s challenge"
            ).create() ) );

            text.addExtra(p);
            text.addExtra(": ");
            text.addExtra(tp);
            text.addExtra(sep);
            text.addExtra(complete);
            text.addExtra(sep);
            text.addExtra(deny);
            text.addExtra(nl);
            Bukkit.getConsoleSender().sendMessage("Added a player to the book");
        }

        //get an IChatBaseComponent object which represents this json string
        IChatBaseComponent page = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(text));
        pages.add(page);

        book.setItemMeta(bm);
        return book;
    }

    private void updateBook(){
        ItemStack newBook = makeBook();
        for(Player player: Bukkit.getServer().getOnlinePlayers()){
            if(player.hasPermission("challenges.admin")){
                for(ItemStack is : player.getInventory()){
                    if (is != null) {
                        BookMeta bmbm;
                        if(is.getItemMeta() instanceof BookMeta)
                            bmbm = (BookMeta) is.getItemMeta();
                        else
                            continue;

                        if (bmbm.getTitle().equals("Challenge Book"))
                            player.getInventory().remove(is);
                    }
                }
                player.getInventory().addItem(newBook);
                player.updateInventory();
            }
        }

        book = newBook;
    }

    public void addBook(Player player){
        player.getInventory().addItem(book);
    }
}

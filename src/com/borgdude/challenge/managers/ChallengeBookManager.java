package com.borgdude.challenge.managers;

import com.borgdude.challenge.objects.Challenge;
import com.borgdude.challenge.objects.ChallengeSet;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
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
        bm.setAuthor("Borgdude");

        try{
            pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bm);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return null;
        }

        TextComponent text = new TextComponent();
        for(Player player : challengeQueue){
            TextComponent p = new TextComponent(player.getName());
            p.setColor(ChatColor.BLUE);
            TextComponent tp = new TextComponent("TP");
            tp.setUnderlined(true);
            tp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + player.getName()));
            TextComponent complete = new TextComponent("COMP");
            complete.setUnderlined(true);
            complete.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/challenge complete " + player.getName()));
            text.addExtra(p);
            text.addExtra(": ");
            text.addExtra(tp);
            text.addExtra(" | ");
            text.addExtra(complete);
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
            if(player.hasPermission("challenges.admin"))
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

        book = newBook;
    }

    public void addBook(Player player){
        player.getInventory().addItem(book);
    }
}

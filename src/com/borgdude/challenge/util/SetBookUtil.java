package com.borgdude.challenge.util;

import com.borgdude.challenge.Main;
import com.borgdude.challenge.managers.ChallengeManager;
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

import java.util.Arrays;
import java.util.List;

public class SetBookUtil {

    private static ChallengeManager challengeManager = Main.challengeManager;

    public static ItemStack generateBook(Player player, Challenge challenge){

        String nl = "\n";
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bm = (BookMeta) book.getItemMeta();
        List<IChatBaseComponent> pages;
        bm.setAuthor(ChatColor.AQUA + "The Challenge God");

        TextComponent text = new TextComponent();

        try{
            pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bm);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return null;
        }

        ChallengeSet cs = challengeManager.getAssignedChallengeSets().get(player);
        bm.setTitle(ChatColor.AQUA + cs.getTitle() + " Challenges");
        bm.setLore(Arrays.asList((ChatColor.BLUE + "The " + cs.getChallenges().size() + " Challenges")));
        // Challenge currentChallenge = cs.getChallenges().get(0);

        boolean swap = false;

        for(Challenge ch : cs.getChallenges()){

            TextComponent comp = new TextComponent();
            if(ch.equals(challenge)){
                comp.setColor(ChatColor.DARK_GREEN);
                comp.setUnderlined(true);
                comp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/challenge completed"));
                swap = true;
            }
            if(!swap) {
                comp.setStrikethrough(true);
            }
            comp.setText(cs.getChallengeIndex(ch) + ". " + ch.getDescription());
            text.addExtra(comp);
            text.addExtra(nl);
        }

        //get an IChatBaseComponent object which represents this json string
        IChatBaseComponent page = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(text));
        pages.add(page);

        book.setItemMeta(bm);
        return book;
    }

    public static void updateSetBook(Player player, Challenge challenge){
        ItemStack newBook = generateBook(player, challenge);
        ChallengeSet cs = challengeManager.getChallengeSetById(challenge.getCsUUID());
        removeSetBook(player, cs);
        player.getInventory().addItem(newBook);
    }

    public static void removeSetBook(Player player, ChallengeSet cs){
        for(ItemStack is : player.getInventory()){
            if(is != null){
                BookMeta bm;
                if(is.getItemMeta() instanceof BookMeta)
                    bm = (BookMeta) is.getItemMeta();
                else
                    continue;

                if(bm.getTitle().equals(ChatColor.AQUA + cs.getTitle() + " Challenges")){
                    player.getInventory().remove(is);
                }
            }
        }
    }
}

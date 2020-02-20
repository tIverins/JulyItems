package github.july_summer.julyitems.utils;

import github.july_summer.julyitems.item.ItemManager;
import github.july_summer.julyitems.item.JItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryUtil {

    public static List<ItemStack> getMainHandAndArmorContents(Player p){
        List<ItemStack> itemList = new ArrayList<>();
        itemList.add(p.getInventory().getItemInMainHand());
        Arrays.asList(p.getInventory().getArmorContents()).forEach(itemList::add);
        return itemList;
    }

    public static JItem getJitemMainInHand(Player p){
        ItemStack item = p.getInventory().getItemInMainHand();
        if(item != null && item.getType() != Material.AIR && item.hasItemMeta()
                && item.getItemMeta().hasDisplayName() && ItemManager.isJItem(item)){
            return ItemManager.getItem(ItemManager.getItemId(item));
        }
        return null;
    }

    public static List<JItem> getJItemArmorContents(Player p){
        List<JItem> itemList = new ArrayList<>();
        for(ItemStack item : p.getInventory().getArmorContents()){
            if(item != null && item.getType() != Material.AIR && item.hasItemMeta()
                    && item.getItemMeta().hasDisplayName() && ItemManager.isJItem(item)){
                itemList.add(ItemManager.getItem(ItemManager.getItemId(item)));
            }
        }
        return itemList;
    }


    public static Integer[] getMainHandAndArmorContentsSlots(Player p) {
        return new Integer[]{p.getInventory().getHeldItemSlot(), 36, 37, 38, 39};
    }
}

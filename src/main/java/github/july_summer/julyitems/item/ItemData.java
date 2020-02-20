package github.july_summer.julyitems.item;

import github.july_summer.julyitems.utils.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Matcher;

public class ItemData {

    public int durability = 0;
    public int maxDurability = 0;

    public ItemData(int durability, int maxDurability){
        this.durability = durability;
        this.maxDurability = maxDurability;
    }

    public void setDurability(int durability){
        this.durability = durability;
    }

    public int getDurability(){
        return durability;
    }

    public int getMaxDurability(){
        return maxDurability;
    }

    public void setItem(ItemStack item){
        ItemUtil.editLine(item, ItemManager.toDurabilityDisplayLore(durability, maxDurability), ItemManager.durabilityLorePattern);
        double displayDurability = (double)item.getType().getMaxDurability() * (double)(1.0 / (double)(maxDurability / durability));
        item.setDurability((short) (item.getType().getMaxDurability() - displayDurability));
    }

    public static ItemData toItemData(ItemStack item){
        if(ItemManager.isJItem(item)){
            JItem jitem = ItemManager.getItem(ItemManager.getItemId(item));
            String lore = ChatColor.stripColor(ItemUtil.findLore(ItemUtil.getLore(item), ItemManager.durabilityLorePattern));
            if(lore != null){
                Matcher matcher = ItemManager.DURABILITY_VALUE_PATTERN.matcher(lore);
                if(matcher.find()){
                    String[] split = matcher.group().split("/");
                    int durability = Integer.parseInt(split[0]);
                    int maxDurability = Integer.parseInt(split[1]);
                    return new ItemData(durability, maxDurability);
                }
            }
        }
        return null;
    }

}

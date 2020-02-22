package github.july_summer.julyitems.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemUtil {

    /**
     * 添加Flag
     * @param itemStack
     * @param flags
     */
    public static void setFlag(ItemStack itemStack, List<ItemFlag> flags){
        ItemMeta itemMeta = itemStack.getItemMeta();
        for(ItemFlag flag : getFlagList(itemStack)){
            itemMeta.removeItemFlags(flag);
        }
        ItemFlag[] itemFlags = new ItemFlag[flags.size()];
        flags.toArray(itemFlags);
        itemMeta.addItemFlags(itemFlags);
        itemStack.setItemMeta(itemMeta);
    }

    /**
     * 取flag
     * @param item
     * @return
     */
    public static List<ItemFlag> getFlagList(ItemStack item){
        if (item != null && item.getType() != Material.AIR
                && item.hasItemMeta()
                && item.getItemMeta().getItemFlags().size() != 0){
            return new ArrayList<>(item.getItemMeta().getItemFlags());
        }
        return new ArrayList<>();
    }

    /**
     * 编辑Lore
     * @param item
     * @param lore
     * @param pattern
     */
    public static void editLine(ItemStack item, String lore, Pattern pattern){
        List<String> loreList = getLore(item);
        for(int i = 0; i < loreList.size(); i++){
            String line = loreList.get(i);
            if(pattern.matcher(line).find()){
                loreList.set(i, lore);
                setLore(item, loreList);
                return;
            }
        }
    }

    public static void addLore(ItemStack item, String lore){
        List<String> loreList = getLore(item);
        loreList.add(lore);
        setLore(item, loreList);
    }

    /**
     * 查找Lore
     * @param lore
     * @param pattern
     * @return
     */
    public static String findLore(List<String> lore, Pattern pattern){
        for(String line : lore){
            Matcher matcher = pattern.matcher(line);
            if(matcher.find()){
                return matcher.group();
            }
        }
        return null;
    }

    /**
     * lore是否相等
     * @param lore1
     * @param lore2
     * @param patterns
     * @return
     */
    public static boolean equalsLore(List<String> lore1, List<String> lore2, List<Pattern> patterns){
        if(lore1.size() != lore2.size()){
            return false;
        }
        for(int i = 0; i < lore1.size(); i++){
            String lore1Str = lore1.get(i);
            String lore2Str = lore2.get(i);
            for(Pattern pattern : patterns){
                if(pattern.matcher(lore1Str).find() && pattern.matcher(lore2Str).find()){
                    continue;
                }
                if(!lore1Str.equals(lore2Str)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 设置是否不可破坏
     * @param item
     * @param isUnbreakable
     */
    public static void setUnbreakable(ItemStack item, boolean isUnbreakable){
        ItemMeta im = item.getItemMeta();
        im.setUnbreakable(isUnbreakable);
        item.setItemMeta(im);
    }

    /**
     * 设置附魔
     * @param item
     * @param maps
     */
    public static void setEnchantments(ItemStack item, Map<Enchantment,Integer> maps){
        ItemMeta im = item.getItemMeta();
        for(Enchantment enchantment : ItemUtil.getEnchantments(item).keySet()){
            im.removeEnchant(enchantment);
        }
        for(Map.Entry<Enchantment,Integer> map : maps.entrySet()){
            im.addEnchant(map.getKey(), map.getValue(), true);
        }
        item.setItemMeta(im);
    }

    /**
     * 设置name
     * @param item
     * @param displayName
     */
    public static void setDisplayName(ItemStack item, String displayName){
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(displayName);
        item.setItemMeta(im);
    }

    /**
     * 设置Lore
     * @param item
     * @param loreList
     */
    public static void setLore(ItemStack item, List<String> loreList){
        ItemMeta im = item.getItemMeta();
        im.setLore(loreList);
        item.setItemMeta(im);
    }

    /**
     * 取附魔
     * @param item
     * @return
     */
    public static Map<Enchantment,Integer> getEnchantments(ItemStack item){
        if (item != null && item.getType() != Material.AIR
                && item.hasItemMeta()
                && item.getItemMeta().hasEnchants()) {
            return item.getItemMeta().getEnchants();
        }
        return new HashMap<Enchantment, Integer>();
    }

    /**
     * 是否不可破坏
     * @param item
     * @return
     */
    public static boolean isUnbreakable(ItemStack item){
        if (item != null && item.getType() != Material.AIR
                && item.hasItemMeta()) {
            return item.getItemMeta().isUnbreakable();
        }
        return false;
    }

    /**
     * 取lore
     * @param item
     * @return
     */
    public static List<String> getLore(ItemStack item){
        if (item != null && item.getType() != Material.AIR
                && item.hasItemMeta()
                && item.getItemMeta().hasLore()) {
            return item.getItemMeta().getLore();
        }
        return new ArrayList<>();
    }

    /**
     * 取显示名
     * @param item
     * @return
     */
    public static String getDisplayName(ItemStack item){
        if (item != null && item.getType() != Material.AIR
                && item.hasItemMeta()
                && item.getItemMeta().hasDisplayName()) {
            return item.getItemMeta().getDisplayName();
        }
        return new String();
    }

}

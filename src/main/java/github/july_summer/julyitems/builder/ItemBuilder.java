package github.july_summer.julyitems.builder;

import github.july_summer.julyitems.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemBuilder {

    public Material type = Material.AIR;
    public String name = null;
    public List<String> loreList = new ArrayList<>();

    public ItemBuilder(){}

    public static ItemBuilder builder(){ return new ItemBuilder(); }

    public ItemBuilder type(Material type){
        this.type = type;
        return this;
    }

    public ItemBuilder name(String name){
        this.name = name;
        return this;
    }

    public ItemBuilder lore(List<String> loreList){
        this.loreList = loreList;
        return this;
    }

    public ItemStack build(){
        ItemStack item = new ItemStack(type);
        if(name != null){
            ItemUtil.setDisplayName(item, name);
        }
        if(loreList != null){
            ItemUtil.setLore(item, loreList);
        }
        return item;
    }

}

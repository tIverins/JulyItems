package github.july_summer.julyitems.listener;

import github.july_summer.julyitems.drop.DropManager;
import github.july_summer.julyitems.item.ItemManager;
import github.july_summer.julyitems.utils.Util;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class DropListener implements Listener{

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        EntityType entityType = event.getEntityType();
        if(DropManager.hasEntityDrop(entityType)){
            for(Map.Entry<String, Integer> dropMap : DropManager.dropMap.get(entityType).entrySet()){
                if(Util.isChance(dropMap.getValue())){
                    ItemStack dropItem = ItemManager.getItem(dropMap.getKey()).buildItem();
                    event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), dropItem);
                }
            }
        }
    }

}

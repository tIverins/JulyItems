package github.july_summer.julyitems.listener;

import github.july_summer.julyitems.item.ItemData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class DurabilityListener implements Listener {

    @EventHandler
    public void onDurability(PlayerItemDamageEvent event){
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        ItemData data = ItemData.toItemData(item);
        if(data == null){
            return;
        }
        data.setDurability(data.getDurability() - event.getDamage());
        data.setItem(item);
        event.setCancelled(true);
    }

}

package github.july_summer.julyitems.listener;

import github.july_summer.julyitems.item.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class ItemUpDateLlistener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(event.getWhoClicked() instanceof Player){
            Player p = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();
            if(ItemManager.isJItem(item) && ItemManager.checkItem(item, ItemManager.getItemId(item))){
                p.sendMessage("§a背包有一件物品更新");
        }
        }
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent event){
            Player p = (Player) event.getPlayer();
            ItemStack item = p.getInventory().getItem(event.getNewSlot());
            if(ItemManager.isJItem(item) && ItemManager.checkItem(item, ItemManager.getItemId(item))){
                p.sendMessage("§a背包有一件物品更新");
            }
    }

}

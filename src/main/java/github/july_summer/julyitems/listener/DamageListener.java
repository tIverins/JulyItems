package github.july_summer.julyitems.listener;

import github.july_summer.julyitems.item.JItem;
import github.july_summer.julyitems.utils.InventoryUtil;
import github.july_summer.julyitems.utils.Util;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        Entity damager = event.getDamager();
        if(damager instanceof Projectile){
            Projectile projectile = ((Projectile)event.getDamager());
            if(projectile.getShooter() instanceof LivingEntity){
                damager = (Entity)projectile.getShooter();
            }
        }
        Entity entity = event.getEntity();
        if(damager instanceof Player){
            Player p = (Player)damager;
            JItem jItem = InventoryUtil.getJitemMainInHand(p);
            if(jItem != null){
                event.setDamage(event.getDamage() + Util.getRandom(jItem.getMinDamage(), jItem.getMaxDamage()));
            }
        }
        if(entity instanceof Player){
            Player p = (Player)entity;
            double armor = 0.0;
            for(JItem jItem : InventoryUtil.getJItemArmorContents(p)){
                armor += jItem.armor;
            }
            event.setDamage(event.getDamage() - (event.getDamage() *(armor / 100)));
        }
    }

}

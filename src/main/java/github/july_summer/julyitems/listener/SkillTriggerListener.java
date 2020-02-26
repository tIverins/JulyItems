package github.july_summer.julyitems.listener;

import github.july_summer.julyitems.skills.SkillManager;
import github.july_summer.julyitems.skills.SkillTrigger;
import github.july_summer.julyitems.utils.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class SkillTriggerListener implements Listener {

    public static void lastHeldTask(){
        for(Player p : Bukkit.getOnlinePlayers()){
            SkillManager.triggerItem(p, p.getInventory().getHeldItemSlot(), SkillTrigger.LAST_HELD, null, p);
        }
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent event){
        Player p = event.getPlayer();
        SkillManager.triggerItem(p, event.getNewSlot(), SkillTrigger.FIRST_HELD, event, p);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Player p = event.getPlayer();
        SkillManager.triggerItem(p, p.getInventory().getHeldItemSlot(), SkillTrigger.BREAK_BLOCK, event, p);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event){
        Player p = event.getPlayer();
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            SkillManager.triggerItem(p, p.getInventory().getHeldItemSlot(), SkillTrigger.RIGHT_CLICK, event, p);
        }

        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK){
            SkillManager.triggerItem(p, p.getInventory().getHeldItemSlot(), SkillTrigger.LEFT_CLICK, event, p);
        }
    }

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
            for(int slot : InventoryUtil.getMainHandAndArmorContentsSlots(p)){
                SkillManager.triggerItem(p, slot, SkillTrigger.ATTACK, event, entity);
                if(entity instanceof Player){
                    SkillManager.triggerItem(p, slot, SkillTrigger.ATTACK_PLAYER, event, entity);
                    return;
                }
                if(entity instanceof LivingEntity){
                    SkillManager.triggerItem(p, slot, SkillTrigger.ATTACK_ENTITY, event, entity);
                }
            }
        }
        if(entity instanceof Player){
            Player p = (Player)entity;
            for(int slot : InventoryUtil.getMainHandAndArmorContentsSlots(p)){
                SkillManager.triggerItem(p, slot, SkillTrigger.GET_DAMAGE, event, entity);
                if(damager instanceof Player){
                    SkillManager.triggerItem(p, slot, SkillTrigger.GET_PLAYER_DAMAGE, event, damager);
                    return;
                }
                if(damager instanceof LivingEntity){
                    SkillManager.triggerItem(p, slot, SkillTrigger.GET_ENTITY_DAMAGE, event, damager);
                }
            }
        }
    }


}

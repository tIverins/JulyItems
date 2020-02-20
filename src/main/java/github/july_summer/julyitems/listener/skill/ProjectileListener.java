package github.july_summer.julyitems.listener.skill;

import github.july_summer.julyitems.skills.customskills.FireArrowSkill;
import github.july_summer.julyitems.skills.customskills.FireballSkill;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ProjectileListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        int entityId = event.getDamager().getEntityId();
        if(FireballSkill.entityIdMap.containsKey(entityId)){
            event.setDamage(event.getDamage() + FireballSkill.entityIdMap.get(entityId));
            FireballSkill.entityIdMap.remove(entityId);
        }
        if(FireArrowSkill.entityIdMap.containsKey(entityId)){
            event.setDamage(event.getDamage() + FireArrowSkill.entityIdMap.get(entityId));
            FireArrowSkill.entityIdMap.remove(entityId);
        }
    }

}

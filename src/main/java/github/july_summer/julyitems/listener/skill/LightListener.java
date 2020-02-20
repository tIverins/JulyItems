package github.july_summer.julyitems.listener.skill;

import github.july_summer.julyitems.skills.customskills.LightSkill;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class LightListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if(event.getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING)){
            if(LightSkill.entityIdMap.containsKey(event.getDamager().getEntityId())){
                event.setDamage(event.getDamage() + LightSkill.entityIdMap.get(event.getDamager().getEntityId()));
            }
        }
    }

}

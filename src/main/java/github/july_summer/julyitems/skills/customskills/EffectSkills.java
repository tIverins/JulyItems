package github.july_summer.julyitems.skills.customskills;

import github.july_summer.julyitems.effects.EffectGroup;
import github.july_summer.julyitems.effects.EffectManager;
import github.july_summer.julyitems.skills.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;

public class EffectSkills implements SkillExecute, SkillCustomLore {

    public EffectSkills(){
        SkillManager.registerCommand(EffectSkills.class);
        SkillManager.registerCustomLore("effect", this);
    }

    @Override
    public boolean isDisplayLore() {
        return false;
    }

    @Override
    public String getSkillDisplayLore(SkillData data) {
        return null;
    }

    @Override
    public void exec(Player p, int triggerItemSlot, SkillTrigger trigger, SkillData data, Event event, Entity eventEntity) {
        Location location = p.getLocation();
        TargetEntity targetEntity = TargetEntity.valueOf(data.getData(0).toString());
        List<Entity> entities = TargetEntity.getTargetEntity(p, targetEntity, eventEntity);
        entities.forEach(entity -> {
            EffectManager.getGroup(data.getData(1).toString()).exec(p.getLocation(), entity.getLocation());
        });
    }
    
}

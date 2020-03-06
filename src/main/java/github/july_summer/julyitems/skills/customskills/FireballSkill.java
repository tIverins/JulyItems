package github.july_summer.julyitems.skills.customskills;

import com.google.common.collect.Maps;
import github.july_summer.julyitems.ConfigManager;
import github.july_summer.julyitems.command.SubCommand;
import github.july_summer.julyitems.item.ItemManager;
import github.july_summer.julyitems.item.JItem;
import github.july_summer.julyitems.skills.*;
import github.july_summer.julyitems.utils.Util;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashMap;
import java.util.List;

public class FireballSkill extends SkillCooldown implements SkillExecute {

    public static HashMap<Integer, Integer> entityIdMap = Maps.newHashMap();

    public FireballSkill(){
        super("fireball");
        SkillManager.registerCommand(this.getClass());
    }

    @Override
    public int getCooldown(SkillData data){
        return Util.objectToInteger(data.getData(2));
    }

    @Override
    public void exec(Player p, int triggerItemSlot, SkillTrigger trigger, SkillData data, Event event, Entity eventEntity) {
        Location eyeLocation = p.getEyeLocation();
        int damage = Util.objectToInteger(data.getData(1));
        int liveTick = Integer.parseInt(String.valueOf(ConfigManager.getValue("skills.fireball.liveTick")));
        TargetEntity triggerEntity1 = TargetEntity.valueOf(data.getData(0).toString());
        List<Entity> entities =  TargetEntity.getTargetEntity(p, triggerEntity1, eventEntity);
        entities.forEach(entity -> {
            Fireball fireball = (Fireball) entity.getLocation().getWorld().spawnEntity(eyeLocation.add(eyeLocation.getDirection().multiply(1.5)), EntityType.FIREBALL);
            fireball.setShooter((ProjectileSource) entity);
            fireball.setTicksLived(liveTick);
            entityIdMap.put(fireball.getEntityId(), damage);
        });
    }

    @SubCommand(cmd = "addSkill fireball <触发方式> <触发目标> <伤害> <冷却>", msg = "添加火球技能", checkArgs1 = 2, checkArgs2 = 1)
    public static void fireballSkill(CommandSender sender, String[] args){
        if(!SkillTrigger.contains(args[3])){
            sender.sendMessage("§c未知的触发方式");
            return;
        }
        if(!TargetEntity.contians(args[4])){
            sender.sendMessage("§c未知触发目标");
            return;
        }
        if(!Util.isNumber(args[5]) || !Util.isNumber(args[6])){
            sender.sendMessage("§c伤害和冷却必须是整数");
            return;
        }
        JItem jitem = ItemManager.getItem(args[0]);
        jitem.addSkill(args[2], SkillTrigger.valueOf(args[3]), new SkillData(new Object[]{args[4], args[5], args[6]}));
        sender.sendMessage("§a技能 " + SkillManager.skillDisplayNameMap.get("fireball") + " 添加成功");
    }

}

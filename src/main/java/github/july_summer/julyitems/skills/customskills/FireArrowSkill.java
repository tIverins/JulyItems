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
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashMap;
import java.util.List;

public class FireArrowSkill extends SkillCooldown implements SkillExecute {

    public static HashMap<Integer, Integer> entityIdMap = Maps.newHashMap();

    public FireArrowSkill() {
        super("firearrow");
        SkillManager.registerCommand(FireArrowSkill.class);
    }

    @Override
    public int getCooldown(SkillData data){
        return Util.objectToInteger(data.getData(2));
    }

    @Override
    public void exec(Player p, int triggerItemSlot, SkillTrigger trigger, SkillData data, Event event, Entity eventEntity) {
        Location eyeLocation = p.getEyeLocation();
        int liveTick = Integer.parseInt(String.valueOf(ConfigManager.getValue("skills.firearrow.liveTick")));
        int damage = Util.objectToInteger(data.getData(1));
        TargetEntity triggerEntity1 = TargetEntity.valueOf(data.getData(0).toString());
        List<Entity> entities =  TargetEntity.getTargetEntity(p, triggerEntity1, eventEntity);
        entities.forEach(entity -> {
            if(entities instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;
                Arrow arrow = (Arrow) livingEntity.launchProjectile(Arrow.class);
                arrow.setFireTicks(liveTick);
                arrow.setShooter((ProjectileSource) livingEntity);
                arrow.setTicksLived(liveTick);
                arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
                entityIdMap.put(arrow.getEntityId(), damage);
            }
        });
    }

    @SubCommand(cmd = "addSkill firearrow <触发方式> <触发目标> <伤害> <冷却>", msg = "添加火箭技能", checkArgs1 = 2, checkArgs2 = 1)
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
        sender.sendMessage("§a技能 " + SkillManager.skillDisplayNameMap.get("firearrow") + " 添加成功");
    }


}

package github.july_summer.julyitems.skills.customskills;

import github.july_summer.julyitems.ConfigManager;
import github.july_summer.julyitems.command.SubCommand;
import github.july_summer.julyitems.item.ItemManager;
import github.july_summer.julyitems.item.JItem;
import github.july_summer.julyitems.skills.*;
import github.july_summer.julyitems.utils.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;

public class InstantKillSkill implements SkillExecute, SkillCustomLore{

    public InstantKillSkill(){
        SkillManager.registerCustomLore("instantkill", this);
        SkillManager.registerCommand(InstantKillSkill.class);
    }

    @Override
    public boolean isDisplayLore() {
        return true;
    }

    @Override
    public String getSkillDisplayLore(SkillData data) {
        return ConfigManager.getValue("defaultDisplayLore.skill.customLore.chanceDisplayLore").toString().replace("{chance}", data.getData(1).toString());
    }

    @Override
    public void exec(Player p, int triggerItemSlot, SkillTrigger trigger, SkillData data, Event event, Entity eventEntity) {
        boolean isChance = Util.isChance(Util.objectToInteger(data.getData(1)));
        if (isChance) {
            TargetEntity triggerEntity1 = TargetEntity.valueOf(data.getData(0).toString());
            List<Entity> entities = TargetEntity.getTargetEntity(p, triggerEntity1, eventEntity);
            entities.forEach(entity -> {
                if (entity instanceof Damageable) {
                    Damageable damageable = (Damageable) entity;
                    damageable.setHealth(0.0);
                    if (entity instanceof Player) {
                        ((Player) entity).sendTitle((String) ConfigManager.getValue("skills.instantkill.entityTitle"), "");
                    }
                }
            });
            p.sendTitle((String) ConfigManager.getValue("skills.instantkill.damagerTitle"), "");
        }
    }

    @SubCommand(cmd = "addSkill <触发方式> <触发目标> instantkill <几率>", msg = "添加瞬杀技能", checkArgs1 = 2, checkArgs2 = 1)
    public static void blindingSkill(CommandSender sender, String[] args){
        if(!SkillTrigger.contains(args[3])){
            sender.sendMessage("§c未知触发方式");
            return;
        }
        if(!TargetEntity.contians(args[4])){
            sender.sendMessage("§c未知触发目标");
            return;
        }
        if(!Util.isNumber(args[5])){
            sender.sendMessage("§c几率请填写整数");
            return;
        }
        JItem jitem = ItemManager.getItem(args[0]);
        jitem.addSkill(args[2], SkillTrigger.valueOf(args[3]), new SkillData(new Object[]{args[4], args[5]}));
        sender.sendMessage("§a技能 " + SkillManager.skillDisplayNameMap.get("instantkill") + " 添加成功");
    }


}

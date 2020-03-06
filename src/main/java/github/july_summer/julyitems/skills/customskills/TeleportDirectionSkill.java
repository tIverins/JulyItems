package github.july_summer.julyitems.skills.customskills;

import github.july_summer.julyitems.command.SubCommand;
import github.july_summer.julyitems.item.ItemManager;
import github.july_summer.julyitems.item.JItem;
import github.july_summer.julyitems.skills.*;
import github.july_summer.julyitems.utils.Util;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;

public class TeleportDirectionSkill extends SkillCooldown implements SkillExecute {

    public TeleportDirectionSkill(){
        super("teleport");
        SkillManager.registerCommand(this.getClass());
    }

    @Override
    public int getCooldown(SkillData data){
        return Util.objectToInteger(data.getData(2));
    }

    @Override
    public void exec(Player p, int triggerItemSlot, SkillTrigger trigger, SkillData data, Event event, Entity eventEntity) {
        int range = Integer.parseInt(String.valueOf(data.getData(1)));
        TargetEntity triggerEntity1 = TargetEntity.valueOf(data.getData(0).toString());
        List<Entity> entities = TargetEntity.getTargetEntity(p, triggerEntity1, eventEntity);
        entities.forEach(entity -> {
            if(entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                Block block = livingEntity.getTargetBlock(null, range);
                livingEntity.teleport(block.getLocation());
            }
        });
    }

    @SubCommand(cmd = "addSkill teleport <触发方式> <触发目标> <传送距离> <冷却>", msg = "添加传送技能", checkArgs1 = 2, checkArgs2 = 1)
    public static void teleportSkill(CommandSender sender, String[] args){
        if(!SkillTrigger.contains(args[3])){
            sender.sendMessage("§c未知的触发方式");
            return;
        }
        if(!TargetEntity.contians(args[4])){
            sender.sendMessage("§c未知触发目标");
            return;
        }
        if(!Util.isNumber(args[5]) || !Util.isNumber(args[6])){
            sender.sendMessage("§c冷却和距离必须是整数");
            return;
        }
        JItem jitem = ItemManager.getItem(args[0]);
        jitem.addSkill(args[2], SkillTrigger.valueOf(args[3]), new SkillData(new Object[]{args[4], args[5], args[6]}));
        sender.sendMessage("§a技能 " + SkillManager.skillDisplayNameMap.get("teleport") + " 添加成功");
    }

}

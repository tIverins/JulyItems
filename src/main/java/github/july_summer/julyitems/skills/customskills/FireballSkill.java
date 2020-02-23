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

import java.util.HashMap;

public class FireballSkill extends SkillCooldown implements SkillExecute {

    public static HashMap<Integer, Integer> entityIdMap = Maps.newHashMap();

    public FireballSkill(){
        super("fireball");
        SkillManager.registerCommand(this.getClass());
    }

    @Override
    public int getCooldown(SkillData data){
        return Util.objectToInteger(data.getData(1));
    }

    @Override
    public void exec(Player p, int triggerItemSlot, SkillTrigger trigger, SkillData data, Event event, Entity triggerEntity) {
        Location eyeLocation = p.getEyeLocation();
        int liveTick = Integer.parseInt(String.valueOf(ConfigManager.getValue("skills.fireball.liveTick")));
        Fireball fireball = (Fireball) p.getLocation().getWorld().spawnEntity(eyeLocation.add(eyeLocation.getDirection().multiply(1.5)), EntityType.FIREBALL);
        fireball.setShooter(p);
        fireball.setTicksLived(liveTick);
        entityIdMap.put(fireball.getEntityId(), Util.objectToInteger(data.getData(0)));
    }

    @SubCommand(cmd = "addSkill fireball <触发方式> <伤害> <冷却>", msg = "添加火球技能", checkArgs1 = 2, checkArgs2 = 1)
    public static void fireballSkill(CommandSender sender, String[] args){
        if(!SkillTrigger.contains(args[3])){
            sender.sendMessage("§c未知的触发方式");
            return;
        }
        if(!Util.isNumber(args[4]) || !Util.isNumber(args[5])){
            sender.sendMessage("§c伤害和冷却必须是整数");
            return;
        }
        JItem jitem = ItemManager.getItem(args[0]);
        jitem.addSkill(args[2], SkillTrigger.valueOf(args[3]), new SkillData(new Object[]{args[4], args[5]}));
        sender.sendMessage("§a技能 " + SkillManager.skillDisplayNameMap.get("fireball") + " 添加成功");
    }

}

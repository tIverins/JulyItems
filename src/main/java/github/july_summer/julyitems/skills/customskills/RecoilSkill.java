package github.july_summer.julyitems.skills.customskills;

import github.july_summer.julyitems.ConfigManager;
import github.july_summer.julyitems.command.SubCommand;
import github.july_summer.julyitems.item.ItemManager;
import github.july_summer.julyitems.item.JItem;
import github.july_summer.julyitems.skills.*;
import github.july_summer.julyitems.utils.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

public class RecoilSkill extends SkillCooldown implements SkillExecute, SkillCustomLore {

    public RecoilSkill(){
        super("recoil");
        SkillManager.registerCommand(RecoilSkill.class);
        SkillManager.registerCustomLore("recoil", this);
    }

    @Override
    public int getCooldown(SkillData data){
        return Util.objectToInteger(data.getData(1));
    }

    @Override
    public boolean isDisplayLore(){
        return true;
    }

    @Override
    public String getSkillDisplayLore(SkillData data) {
        return ConfigManager.getValue("defaultDisplayLore.skill.defaultLore").toString();
    }

    @Override
    public void exec(Player p, int triggerItemSlot, SkillTrigger trigger, SkillData data, Event event, Entity triggerEntity) {
        Vector vector = p.getLocation().getDirection();
        p.setVelocity(vector.multiply((-Util.objectToInteger(data.getData(0))) / 10.0));
    }

    @SubCommand(cmd = "addSkill recoil <触发方式> <退后倍数> <冷却>", msg = "添加退后技能(用作后坐力或弹跳)", checkArgs1 = 2, checkArgs2 = 1)
    public static void recoilSkill(CommandSender sender, String[] args){
        if(!SkillTrigger.contains(args[3])){
            sender.sendMessage("§c未知的触发方式");
            return;
        }
        if(!Util.isNumber(args[4]) || !Util.isNumber(args[5])){
            sender.sendMessage("§c距离/冷却 请填写整数");
            return;
        }
        JItem jitem = ItemManager.getItem(args[0]);
        jitem.addSkill(args[2], SkillTrigger.valueOf(args[3]), new SkillData(new Object[]{args[4], args[5]}));
        sender.sendMessage("§a技能 " + SkillManager.skillDisplayNameMap.get("recoil") + " 添加成功");
    }





}

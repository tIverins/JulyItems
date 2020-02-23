package github.july_summer.julyitems.skills.customskills;

import github.july_summer.julyitems.ConfigManager;
import github.july_summer.julyitems.command.SubCommand;
import github.july_summer.julyitems.item.ItemManager;
import github.july_summer.julyitems.item.JItem;
import github.july_summer.julyitems.potion.PotionManager;
import github.july_summer.julyitems.skills.*;
import github.july_summer.julyitems.utils.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

public class BlindingSkill implements SkillExecute, SkillCustomLore{

    public BlindingSkill(){
        SkillManager.registerCommand(BlindingSkill.class);
        SkillManager.registerCustomLore("blinding", this);
    }

    @Override
    public boolean isDisplayLore() {
        return true;
    }

    @Override
    public String getSkillDisplayLore(SkillData data) {
        return ConfigManager.getValue("defaultDisplayLore.skill.customLore.chanceDisplayLore").toString().replace("{chance}", data.getData(0).toString());
    }

    @Override
    public void exec(Player p, int triggerItemSlot, SkillTrigger trigger, SkillData data, Event event, Entity triggerEntity) {
        if(event instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
            Entity entity = damageByEntityEvent.getEntity();
            if(entity instanceof LivingEntity){
                LivingEntity livingEntity = (LivingEntity)entity;
                if(Util.isChance(Util.objectToInteger(data.getData(0)))) {
                    PotionManager.givePotion(livingEntity, PotionEffectType.BLINDNESS
                            , new Integer[]{Util.objectToInteger(data.getData(1))
                                    ,Util.objectToInteger(data.getData(2))});

                    if(entity instanceof Player){
                        ((Player)entity).sendTitle((String) ConfigManager.getValue("skills.blinding.entityTitle"), "");
                    }
                    p.sendTitle((String) ConfigManager.getValue("skills.blinding.damagerTitle"), "");
                }
            }
        }
    }

    @SubCommand(cmd = "addSkill blinding <几率> <持续时间> <持续等级>", msg = "添加致盲技能", checkArgs1 = 2, checkArgs2 = 1)
    public static void blindingSkill(CommandSender sender, String[] args){
        if(!Util.isNumber(args[3]) || !Util.isNumber(args[4]) || !Util.isNumber(args[5])){
            sender.sendMessage("§c几率/等级/持续时间 请填写整数");
            return;
        }
        JItem jitem = ItemManager.getItem(args[0]);
        jitem.addSkill(args[2], SkillTrigger.ATTACK_ENTITY, new SkillData(new Object[]{args[3], args[4], args[5]}));
        sender.sendMessage("§a技能 " + SkillManager.skillDisplayNameMap.get("blinding") + " 添加成功");
    }

}

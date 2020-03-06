package github.july_summer.julyitems.skills;

import com.google.common.collect.Maps;
import github.july_summer.julyitems.ConfigManager;
import github.july_summer.julyitems.item.ItemManager;
import github.july_summer.julyitems.item.JItem;
import github.july_summer.julyitems.potion.PotionManager;
import github.july_summer.julyitems.skills.customskills.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillManager {

    public static HashMap<String, SkillExecute> skillMap = Maps.newHashMap();
    public static HashMap<String, SkillCooldown> cooldownClassMap = Maps.newHashMap();
    public static HashMap<String, String> skillDisplayNameMap = Maps.newHashMap();
    public static HashMap<String, SkillCustomLore> skillCustomLoreMap = Maps.newHashMap();
    public static List<Class> commandClassList = new ArrayList<>();

    public static void loadDefaultSkills(){
        registerSkill("fireball", new FireballSkill(), "火球");
        registerSkill("teleport", new TeleportDirectionSkill(), "传送");
        registerSkill("blinding", new BlindingSkill(), "致盲");
        registerSkill("firearrow", new FireArrowSkill(), "火箭");
        registerSkill("instantkill", new InstantKillSkill(), "瞬杀");
        registerSkill("cmd", new CmdSkill(), "命令");
        registerSkill("consume", new ConsumeSkill(), "消耗");
        registerSkill("light", new LightSkill(), "闪电");
        registerSkill("ignite", new IgniteSkill(), "点燃");
        registerSkill("recoil", new RecoilSkill(), "反跳");
        registerSkill("chain", new ChainSkill(), "连锁挖矿");
        registerSkill("effect", new EffectSkills(), "粒子组");
    }

    /**
     * 执行技能
     * @param skillName
     * @param p
     * @param data
     * @param event
     */
    public static void execSkill(String skillName, Player p, int triggerItemSlot, SkillTrigger trigger, SkillData data, Event event, Entity triggerEntity) {
        JItem jitem = ItemManager.getItem(ItemManager.getItemId(p.getInventory().getItem(triggerItemSlot)));
        if(cooldownClassMap.containsKey(skillName)){
            SkillCooldown skillCooldown = cooldownClassMap.get(skillName);
            skillCooldown.setTrigger(trigger);
            if(skillCooldown.getCooldown(p.getName()) > 0){
                if(jitem.isDisplayCooldown()) {
                    p.sendMessage(ConfigManager.getValue("cooldown.cooldownMessage").toString()
                            .replace("{cooldown}", String.valueOf(skillCooldown.getCooldown(p.getName())))
                            .replace("{skillDisplayName}", skillDisplayNameMap.get(skillName).toString()));
                }
                return;
            }
            skillMap.get(skillName).exec(p, triggerItemSlot, trigger, data, event, triggerEntity);
            skillCooldown.setCooldown(p.getName(), skillCooldown.getCooldown(data));
            return;
        }
        skillMap.get(skillName).exec(p, triggerItemSlot, trigger, data, event, triggerEntity);
    }

    /**
     * 触发该物品
     * @param p
     * @param triggerItemSlot
     * @param trigger
     * @param event
     */
    public static void triggerItem(Player p, int triggerItemSlot, SkillTrigger trigger, Event event, Entity triggerEntity){
        ItemStack item = p.getInventory().getItem(triggerItemSlot);
        if(ItemManager.isJItem(item)){
            JItem jitem = ItemManager.getItem(ItemManager.getItemId(item));
            Map<String, SkillData> skillMap = jitem.getTriggerSkillMap(trigger);
            for(Map.Entry<String, SkillData> map : skillMap.entrySet()) {
                SkillManager.execSkill(map.getKey(), p, triggerItemSlot, trigger, map.getValue(), event, triggerEntity);
            }
            List<PotionEffectType> potionList = jitem.getTriggerPotionName(trigger);
            for(PotionEffectType potion : potionList){
                PotionManager.givePotion(p, potion, jitem.potionMap.get(potion));
            }
        }
    }

    public static String getSkillDisplayName(String skillName){
        return skillDisplayNameMap.containsKey(skillName) ? skillDisplayNameMap.get(skillName) : new String();
    }

    /**
     * 技能是否存在
     * @param skillName
     * @return
     */
    public static boolean hasSkill(String skillName){
        return skillMap.containsKey(skillName);
    }

    public static void registerCustomLore(String skillName, SkillCustomLore loreClass){
        skillCustomLoreMap.put(skillName, loreClass);
    }

    public static void registerCommand(Class skillExecute){
        commandClassList.add(skillExecute);
    }

    /**
     * 注册技能
     * @param skillName
     * @param skillExecute
     */
    public static void registerSkill(String skillName, SkillExecute skillExecute, String displayName){
        skillMap.put(skillName, skillExecute);
        skillDisplayNameMap.put(skillName, displayName == null ? skillName : displayName);
    }
}

package github.july_summer.julyitems.api;

import github.july_summer.julyitems.item.ItemData;
import github.july_summer.julyitems.item.ItemManager;
import github.july_summer.julyitems.item.JItem;
import github.july_summer.julyitems.skills.SkillCooldown;
import github.july_summer.julyitems.skills.SkillExecute;
import github.july_summer.julyitems.skills.SkillManager;
import github.july_summer.julyitems.skills.SkillTrigger;
import org.bukkit.inventory.ItemStack;

public class JItemAPI {

    private static JItemAPI API = null;

    static {
        API = new JItemAPI();
    }

    public static JItemAPI getInstance(){
        return API;
    }

    /**
     * 注册技能
     * @param skillName 技能名
     * @param execute 技能实现类
     * @param displayName 技能显示名
     * @return 是否注册成功
     */
    public boolean registerSkill(String skillName, SkillExecute execute, String displayName){
        if(SkillManager.hasSkill(skillName)){
            return false;
        }
        SkillManager.registerSkill(skillName, execute, displayName);
        return true;
    }

    /**
     * 取玩家指定技能指定触发方式冷却
     * @param playerName
     * @param skillName
     * @param trigger
     * @return 冷却 或 -1
     */
    public int getCooldown(String playerName, String skillName, SkillTrigger trigger){
        if(SkillManager.cooldownClassMap.containsKey(skillName)) {
            SkillCooldown skillCooldown = SkillManager.cooldownClassMap.get(skillName);
            skillCooldown.setTrigger(trigger);
            return skillCooldown.getCooldown(playerName);
        }
        return -1;
    }

    /**
     *注: 修改数据后为即时保存
     * @param itemStack
     * @return Jitem 或 null
     */
    public JItem toJItem(ItemStack itemStack){
        if(ItemManager.isJItem(itemStack)){
            return ItemManager.getItem(ItemManager.getItemId(itemStack));
        }
        return null;
    }

    /**
     * 取Item当前耐久 (如果有)
     * @param item
     * @return 当前耐久
     */
    public float getJItemDurability(ItemStack item){
        ItemData itemData = ItemData.toItemData(item);
        if(itemData != null){
            return itemData.getDurability();
        }
        return 0;
    }

    /**
     * 设置Item耐久
     * @param item
     * @return
     */
    public boolean setJItemDurability(ItemStack item, int durability){
        ItemData itemData = ItemData.toItemData(item);
        if(itemData != null){
            itemData.setDurability(durability);
            itemData.setItem(item);
            return true;
        }
        return false;
    }

}

package github.july_summer.julyitems.item;

import com.google.common.collect.Maps;
import github.july_summer.julyitems.ConfigManager;
import github.july_summer.julyitems.drop.DropManager;
import github.july_summer.julyitems.potion.PotionManager;
import github.july_summer.julyitems.recipe.RecipeData;
import github.july_summer.julyitems.recipe.RecipeManager;
import github.july_summer.julyitems.skills.SkillCustomLore;
import github.july_summer.julyitems.skills.SkillData;
import github.july_summer.julyitems.skills.SkillManager;
import github.july_summer.julyitems.skills.SkillTrigger;
import github.july_summer.julyitems.utils.Util;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class JItem{

    //编号
    public String itemId;
    //显示名
    public String displayName = new String("New Item");
    //材质
    public Material type = Material.IRON_SWORD;
    //描述
    public List<String> loreList = new ArrayList<>();
    //耐久
    public short durability = 250;
    //耐久lore
    public String durabilityLore;
    //是否检查lore
    public boolean isCheckLore = true;
    //是否检查附魔
    public boolean isCheckEnchantment = true;
    //是否无限耐久
    public boolean unbreakable = false;
    //是否显示技能Lore
    public boolean isDisplayDefaultLore = true;
    //是否提示冷却
    public boolean isDisplayCooldown = true;
    //最大伤害值
    public int maxDamage = 0;
    //最小伤害值
    public int minDamage = 0;
    //护甲
    public int armor = 0;
    //技能
    public HashMap<SkillTrigger, LinkedHashMap<String,SkillData>>skillMap = Maps.newHashMap();
    //技能loreList
    public List<String> skillLoreList = new ArrayList<>();
    //药水
    public HashMap<PotionEffectType, Integer[]> potionMap = Maps.newHashMap();
    //药水触发
    public HashMap<PotionEffectType, SkillTrigger> potionTriggerMap = Maps.newHashMap();
    //附魔
    public HashMap<Enchantment,Integer> enchantmentMap = Maps.newHashMap();
    //生物掉落
    public HashMap<EntityType, Integer> dropMap = Maps.newHashMap();
    //ItemFlag
    public List<ItemFlag> flagList = new ArrayList<>();
    //合成配方
    public RecipeData recipeData = null;

    public JItem(String itemId){
        for(SkillTrigger trigger : SkillTrigger.values()){
            skillMap.put(trigger, Maps.newLinkedHashMap());
        }
        this.itemId = itemId;
    }

    public Material getType(){
        return type;
    }

    public String getDisplayName(){
        return displayName;
    }

    public String getItemId(){
        return itemId;
    }

    public boolean isUnbreakable(){
        return unbreakable;
    }

    public boolean isCheckEnchantment(){
        return isCheckEnchantment;
    }

    public boolean isCheckLore() {
        return isCheckLore;
    }

    public boolean isDisplayDefaultLore(){
        return isDisplayDefaultLore;
    }

    public boolean isDisplayCooldown(){
        return isDisplayCooldown;
    }
    public int getArmor(){
        return armor;
    }

    public int getMaxDamage(){
        return maxDamage;
    }

    public int getMinDamage() { return minDamage; }

    public short getDurability(){
        return durability;
    }

    public int getEnchantmentLevel(Enchantment enchantment){
        return enchantmentMap.get(enchantment);
    }

    public HashMap<Enchantment,Integer> getEnchantmentMap(){
        return enchantmentMap;
    }

    public HashMap<EntityType,Integer> getDropMap(){
        return dropMap;
    }

    public List<String> getLoreList() {
        return loreList;
    }

    public List<String> getSkillLoreList(){
        List<String> list = new ArrayList<>();
        skillLoreList.forEach(list::add);
        loreList.forEach(list::add);
        if(durabilityLore != null && durability != 0) {
            list.add(durabilityLore);
        }
        return list;
    }

    public void setRecipeChance(int recipeChance){
        if(recipeData != null){
            recipeData.setRecipeChance(recipeChance);
        }
    }

    public void addFlag(ItemFlag itemFlag){
        flagList.add(itemFlag);
    }

    public void removeFlag(ItemFlag itemFlag){
        flagList.remove(itemFlag);
    }

    public boolean hasFlag(ItemFlag itemFlag){
        return flagList.contains(itemFlag);
    }

    public void setRecipe(RecipeData data){
        this.recipeData = data;
        if(data != null && data.getRecipeChance() > 0){
            RecipeManager.addRecipe(recipeData);
        }
        if(data == null){
            RecipeManager.removeRecipe(itemId);
        }
        ItemManager.saveItem(this);
    }

    public RecipeData getRecipeData() {
        return recipeData;
    }

    public void addDrop(EntityType type, int chance){
        dropMap.put(type, chance);
        DropManager.addDrop(type, chance, itemId);
        ItemManager.saveItem(this);
    }

    public void removeDrop(EntityType type){
        dropMap.remove(type);
        DropManager.removeDrop(type, itemId);
        ItemManager.saveItem(this);
    }

    public boolean hasEntityDrop(EntityType type){
        return dropMap.containsKey(type);
    }

    public void setUnbreakable(boolean unbreakable){
        this.unbreakable = unbreakable;
        ItemManager.saveItem(this);
    }

    public void setCheckEnchantment(boolean isCheckEnchantment){
        this.isCheckEnchantment = isCheckEnchantment;
        ItemManager.saveItem(this);
    }

    public void setCheckLore(boolean isCheckLore){
        this.isCheckLore = isCheckLore;
        ItemManager.saveItem(this);
    }

    public void setDisplayDefaultLore(boolean isDisplayDefaultLore){
        this.isDisplayDefaultLore = isDisplayDefaultLore;
        this.updateDefaultLore();
        ItemManager.saveItem(this);
    }

    public void setDisplayCooldown(boolean isDisplayCooldown){
        this.isDisplayCooldown = isDisplayCooldown;
        ItemManager.saveItem(this);
    }

    public void setArmor(int armor){
        this.armor = armor;
        updateDefaultLore();
        ItemManager.saveItem(this);
    }

    public void setMaxDamage(int maxDamage){
        this.maxDamage = maxDamage;
        updateDefaultLore();
        ItemManager.saveItem(this);
    }

    public void setMinDamage(int minDamage){
        this.minDamage = minDamage;
        updateDefaultLore();
        ItemManager.saveItem(this);
    }

    public void setType(Material type){
        this.type = type;
        this.durability = type.getMaxDurability();
        ItemManager.saveItem(this);
    }

    public void setDisplayName(String displayName){
        this.displayName = displayName;
        ItemManager.saveItem(this);
    }

    public void setDurability(short durability){
        this.durability = durability;
        upDateDurabilityLore();
        ItemManager.saveItem(this);
    }

    public void removeEnchantment(Enchantment enchantment){
        this.enchantmentMap.remove(enchantment);
        ItemManager.saveItem(this);
    }

    public void addEnchantment(Enchantment enchantment, int level){
        this.enchantmentMap.put(enchantment, level);
        ItemManager.saveItem(this);
    }

    public boolean hasEnchantment(Enchantment enchantment){
        return enchantmentMap.containsKey(enchantment);
    }

    public boolean removeLine(int line){
        if(loreList.size() - 1 < line){
            return false;
        }
        loreList.remove(line);
        ItemManager.saveItem(this);
        return true;
    }

    public boolean editLine(int line, String lore){
        if(loreList.size() - 1 < line){
            return false;
        }
        loreList.set(line, lore);
        ItemManager.saveItem(this);
        return true;
    }

    public void addLore(String lore){
        loreList.add(lore);
        ItemManager.saveItem(this);
    }

    public void setLore(List<String> loreList){
        this.loreList = loreList;
        ItemManager.saveItem(this);
    }

    public void addPoint(PotionEffectType type, SkillTrigger trigger, int time, int level){
        potionMap.put(type, new Integer[]{time, level});
        potionTriggerMap.put(type, trigger);
        updateDefaultLore();
        ItemManager.saveItem(this);
    }

    public boolean hasPotion(PotionEffectType type){
        return potionMap.containsKey(type);
    }

    public void removePoint(PotionEffectType type){
        potionMap.remove(type);
        potionTriggerMap.remove(type);
        updateDefaultLore();
        ItemManager.saveItem(this);
    }

    public List<PotionEffectType> getTriggerPotionName(SkillTrigger trigger){
        List<PotionEffectType> potionList = new ArrayList<>();
        for(Map.Entry<PotionEffectType, SkillTrigger> map : potionTriggerMap.entrySet()){
            if(map.getValue().equals(trigger)){
                potionList.add(map.getKey());
            }
        }
        return potionList;
    }

    public Map<String, SkillData> getTriggerSkillMap(SkillTrigger trigger){
        return skillMap.get(trigger);
    }

    public void updateDefaultLore(){
        if(isDisplayDefaultLore) {
            skillLoreList.clear();
            if(minDamage < maxDamage && maxDamage != 0) {
                skillLoreList.add(ConfigManager.getValue("defaultDisplayLore.damageLore").toString()
                        .replace("{minDamage}", String.valueOf(this.minDamage))
                        .replace("{maxDamage}", String.valueOf(this.maxDamage)));
            }
            if(armor != 0) {
                skillLoreList.add(ConfigManager.getValue("defaultDisplayLore.armorLore").toString()
                        .replace("{armor}", String.valueOf(this.armor)));
            }
            for (Map.Entry<SkillTrigger, LinkedHashMap<String, SkillData>> skillTriggerMap : skillMap.entrySet()) {
                for (Map.Entry<String, SkillData> dataMap : skillTriggerMap.getValue().entrySet()) {
                    String skillLore = null;
                    if(SkillManager.skillCustomLoreMap.containsKey(dataMap.getKey())) {
                        SkillCustomLore customLore = SkillManager.skillCustomLoreMap.get(dataMap.getKey());
                        if (!customLore.isDisplayLore()) {
                            continue;
                        }
                        skillLore = SkillManager.skillCustomLoreMap.get(dataMap.getKey()).getSkillDisplayLore(dataMap.getValue());
                    }else {
                        skillLore = ConfigManager.getValue("defaultDisplayLore.skill.defaultLore").toString();
                    }
                     skillLore = skillLore.replace("{triggerDisplayName}", skillTriggerMap.getKey().getDisplayName())
                            .replace("{skillDisplayName}", SkillManager.getSkillDisplayName(dataMap.getKey()));
                    skillLoreList.add(skillLore);
                }
            }
            for(Map.Entry<PotionEffectType, Integer[]> map : potionMap.entrySet()){
                String potionLore = ConfigManager.getValue("defaultDisplayLore.potion.defaultLore").toString()
                        .replace("{triggerDisplayName}", potionTriggerMap.get(map.getKey()).getDisplayName())
                        .replace("{time}", String.valueOf(map.getValue()[0]))
                        .replace("{level}", String.valueOf(map.getValue()[1]))
                        .replace("{potionDisplayName}", PotionManager.getDisplayName(map.getKey()));
                skillLoreList.add(potionLore);
            }
            //根据字符串长度进行排序
            skillLoreList.sort(Comparator.comparingInt(str -> str.length()));
        } else {
            skillLoreList.clear();
        }
    }

    public void upDateDurabilityLore(){
        if(durability != 0) {
            this.durabilityLore = ItemManager.toDurabilityDisplayLore(durability, durability);
        }
    }

    public void addSkill(String skillName, SkillTrigger trigger, SkillData data) {
        LinkedHashMap<String, SkillData> map = skillMap.get(trigger);
        map.put(skillName, data);
        skillMap.put(trigger, map);
        updateDefaultLore();
        ItemManager.saveItem(this);
    }

    public boolean hasSkill(SkillTrigger trigger, String skillName) {
        Map<String, SkillData> map = skillMap.get(trigger);
        return map.containsKey(skillName);
    }

    public void removeSkill(SkillTrigger trigger, String skillName){
        LinkedHashMap<String, SkillData> map = skillMap.get(trigger);
        map.remove(skillName);
        skillMap.put(trigger, map);
        updateDefaultLore();
        ItemManager.saveItem(this);
    }

    public ItemStack buildItem(){
        ItemStack item = new ItemStack(type);
        updateDefaultLore();
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(displayName + ItemManager.ITEMID_COLOR + Util.addColor(Util.toBinaryString(itemId)));
        im.setLocalizedName(displayName);
        im.setLore(getSkillLoreList());
        im.setUnbreakable(unbreakable);
        for(Map.Entry<Enchantment, Integer> map : enchantmentMap.entrySet()){
            im.addEnchant(map.getKey(), map.getValue(), true);
        }
        ItemFlag[] itemFlags = new ItemFlag[flagList.size()];
        flagList.toArray(itemFlags);
        im.addItemFlags(itemFlags);
        item.setItemMeta(im);
        return item;
    }

}
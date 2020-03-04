package github.july_summer.julyitems.item;

import com.google.common.collect.Maps;
import github.july_summer.julyitems.ConfigManager;
import github.july_summer.julyitems.JulyItems;
import github.july_summer.julyitems.drop.DropManager;
import github.july_summer.julyitems.potion.PotionManager;
import github.july_summer.julyitems.recipe.RecipeData;
import github.july_summer.julyitems.recipe.RecipeManager;
import github.july_summer.julyitems.skills.SkillData;
import github.july_summer.julyitems.skills.SkillTrigger;
import github.july_summer.julyitems.utils.ItemUtil;
import github.july_summer.julyitems.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.regex.Pattern;

public class ItemManager {

    public static JulyItems instance;
    public static final String ITEMID_COLOR = new String("§d§d§d");
    public static YamlConfiguration itemData;
    public static List<Pattern> patterns = new ArrayList<>();
    public static Pattern durabilityLorePattern;
    public static final Pattern DURABILITY_VALUE_PATTERN = Pattern.compile("[0-9]+/[0-9]+");
    public static HashMap<String, JItem> itemMap = Maps.newHashMap();
    public static boolean isDisplayDefaultLore = true;
    public static boolean isDisplayCooldown = true;

    public static void init(YamlConfiguration itemData) {
        ItemManager.itemData = itemData;
        ItemManager.instance = JulyItems.getInstance();
        ItemManager.isDisplayDefaultLore = Util.objectToBoolean(ConfigManager.getValue("defaultDisplayLore.displayLore"));
        ItemManager.isDisplayCooldown = Util.objectToBoolean(ConfigManager.getValue("cooldown.messageEnable"));
        durabilityLorePattern = Pattern.compile(ConfigManager.getValue("durability.displayLore").toString()
                .replace("%durability%", "[0-9]+")
                .replace("%maxDurability%", "[0-9]+"));
        patterns.add(durabilityLorePattern);
        ItemManager.loadItems();
    }

    /**
     * 加载Item
     */
    public static void loadItems() {
        for (String itemId : itemData.getConfigurationSection("").getKeys(false)) {
            itemMap.put(itemId, ItemManager.loadItem(itemId));
        }
        Bukkit.getConsoleSender().sendMessage("[JulyItems] loaded " + itemMap.size() + " Items Success!");
    }

    /**
     * 加载Item
     *
     * @param itemId
     */
    public static JItem loadItem(String itemId) {
        JItem item = new JItem(itemId);
        item.displayName = itemData.getString(itemId + ".displayName");
        item.type = Material.valueOf(itemData.getString(itemId + ".type"));
        item.durability = Short.parseShort(itemData.getString(itemId + ".durability"));
        item.maxDamage = itemData.getInt(itemId + ".maxDamage");
        item.minDamage = itemData.getInt(itemId + ".minDamage");
        item.armor = itemData.getInt(itemId + ".armor");
        item.loreList = itemData.getStringList(itemId + ".lore");
        item.unbreakable = itemData.getBoolean(itemId + ".unbreakable");
        item.isCheckLore = itemData.getBoolean(itemId + ".isCheckLore");
        item.isCheckEnchantment = itemData.getBoolean(itemId + ".isCheckEnchantment");
        item.isDisplayDefaultLore = isDisplayDefaultLore ? itemData.getBoolean(itemId + ".isDisplayDefaultLore") : false;
        item.isDisplayCooldown = isDisplayCooldown ? itemData.getBoolean(itemId + ".isDisplayCooldown") : false;
        if(itemData.contains(itemId + ".skills")){
            List<String> skillsLlist = itemData.getStringList(itemId + ".skills");
            skillsLlist.forEach(skill -> {
                String[] splitSkill = skill.split(",", 3);
                SkillTrigger trigger = SkillTrigger.valueOf(splitSkill[1]);
                LinkedHashMap<String, SkillData> map = item.skillMap.get(trigger);
                String[] data = splitSkill[2].split(",");
                for(int i = 0; i < data.length; i++){
                    data[i] = data[i].replace("[comma]", ",");
                }
                map.put(splitSkill[0], new SkillData(data));
                item.skillMap.put(trigger, map);
            });
        }
        if(itemData.contains(itemId + ".potions")){
            List<String> potionsList = itemData.getStringList(itemId + ".potions");
            potionsList.forEach(potion -> {
                String[] splitPotion = potion.split(" ", 4);
                if(splitPotion != null && splitPotion.length == 4){
                PotionEffectType potionEffectType = PotionManager.getByNamePotion(splitPotion[0]);
                Integer[] data = new Integer[]{Integer.parseInt(splitPotion[2]), Integer.parseInt(splitPotion[3])};
                item.potionMap.put(potionEffectType, data);
                item.potionTriggerMap.put(potionEffectType, SkillTrigger.valueOf(splitPotion[1]));
            }});
        }
        if(itemData.contains(itemId + ".enchantments")){
            for(String enchantment : itemData.getConfigurationSection(itemId + ".enchantments").getKeys(false)){
                item.enchantmentMap.put(Enchantment.getByName(enchantment), itemData.getInt(itemId + ".enchantments." + enchantment));
            }
        }
        if(itemData.contains(itemId + ".drops")){
            for(String entityType : itemData.getConfigurationSection(itemId + ".drops").getKeys(false)){
                item.dropMap.put(EntityType.valueOf(entityType), itemData.getInt(itemId + ".drops." + entityType));
            }
        }
        DropManager.loadDropItem(item);
        if(itemData.contains(itemId + ".flags")){
            item.flagList = (List<ItemFlag>) itemData.getList(itemId + ".flags");
        }

        if(itemData.contains(itemId + ".recipe")){
            RecipeData recipeData = new RecipeData(itemId);
            recipeData.setRecipeChance(itemData.getInt(itemId + ".recipe.chance"));
            for(String key : itemData.getConfigurationSection(itemId + ".recipe.itemStacks").getKeys(false)){
                recipeData.addItemStack(Integer.parseInt(key), itemData.getItemStack(itemId + ".recipe.itemStacks." + key));
            }
            item.setRecipe(recipeData);
        }
        RecipeManager.loadShapeRecipe(item);

        item.upDateDurabilityLore();
        item.updateDefaultLore();
        return item;
    }

    /**
     * 是否含有物品编号
     *
     * @param itemId
     * @return
     */
    public static boolean hasItemId(String itemId) {
        return itemData.contains(itemId);
    }

    /**
     * 更新全部Jitem的DefaultLore
     */
    public static void upDataAllJItemDefaultLore() {
        for(JItem jItem : itemMap.values()){
            jItem.updateDefaultLore();
        }
    }

    /**
     * 更新全服玩家的指定物品
     * @param itemId
     */
    public static void upDateOnlinePlayerItem(String itemId){
        for(Player p : Bukkit.getOnlinePlayers()){
            for(ItemStack item : p.getInventory().getContents()){
                if(ItemManager.isJItem(item)) {
                    ItemManager.checkItem(item, itemId);
                    p.sendMessage("§a背包有一件物品更新");
                }
            }
        }
    }

    /**
     * 取显示名
     *
     * @param item
     * @return
     */
    public static String[] getDisplayName(ItemStack item) {
        String name = ItemUtil.getDisplayName(item);
        return name.contains(ITEMID_COLOR) ? name.split(ITEMID_COLOR) : null;
    }

    /**
     * 取物品编号
     *
     * @param item
     * @return
     */
    public static String getItemId(ItemStack item) {
        String[] name = ItemManager.getDisplayName(item);
        if (name == null) {
            return null;
        }
        for (String str : name) {
            String end = Util.binaryToString(Util.removeColor(str));
            if (ItemManager.hasItemId(end)) {
                return end;
            }
        }
        return null;
    }


    /**
     * 是否是JulyItems的物品
     * @param item
     * @return
     */
    public static boolean isJItem(ItemStack item){
        return ItemManager.getItemId(item) != null;
    }

    /**
     * 检查lore与type name是否一致
     */
    public static boolean checkItem(ItemStack item, String itemId) {
        boolean isChange = false;
        JItem jitem = ItemManager.getItem(itemId);
        String displayName = ItemUtil.getDisplayName(item).replace(ITEMID_COLOR + Util.addColor(Util.toBinaryString(itemId)), "");
        if(!displayName.equals(jitem.getDisplayName())){
            ItemUtil.setDisplayName(item, jitem.getDisplayName() + ITEMID_COLOR + Util.addColor(Util.toBinaryString(itemId)));
            isChange = true;
        }
        if (jitem.isCheckLore()) {
            List<String> lore = ItemUtil.getLore(item);
            if(!ItemUtil.equalsLore(lore, jitem.getSkillLoreList(), patterns)){
                ItemUtil.setLore(item, jitem.getSkillLoreList());
                isChange = true;
            }
        }
        if(jitem.isCheckEnchantment()) {
            Map<Enchantment, Integer> maps = ItemUtil.getEnchantments(item);
                for (Map.Entry<Enchantment, Integer> map : jitem.getEnchantmentMap().entrySet()) {
                    if (!maps.containsKey(map.getKey()) || map.getValue() != maps.get(map.getKey())) {
                        ItemUtil.setEnchantments(item, jitem.getEnchantmentMap());
                        isChange = true;
                    }
            }
        }
        List<ItemFlag> flagList = ItemUtil.getFlagList(item);
        if(jitem.flagList.size() != flagList.size() || !jitem.flagList.equals(flagList)){
            ItemUtil.setFlag(item, jitem.flagList);
            isChange = true;
        }

        if(ItemUtil.isUnbreakable(item) != jitem.isUnbreakable()){
            ItemUtil.setUnbreakable(item, jitem.isUnbreakable());
            isChange = true;
        }
        if (item.getType() != jitem.getType()) {
            item.setType(jitem.getType());
            isChange = true;
        }

        return isChange;
    }

    /**
     * 取物品编号列表
     * @return
     */
    public static List<String> getJItemIdList(){
        List<String> list = new ArrayList<>();
        itemMap.keySet().forEach(list::add);
        return list;
    }

    /**
     * 保存物品
     * @param item
     */
    public static void saveItem(JItem item) {
        itemData.set(item.getItemId() + ".type", item.getType().toString());
        itemData.set(item.getItemId() + ".displayName", item.getDisplayName());
        itemData.set(item.getItemId() + ".lore", item.getLoreList());
        itemData.set(item.getItemId() + ".maxDamage", item.getMaxDamage());
        itemData.set(item.getItemId() + ".minDamage", item.getMinDamage());
        itemData.set(item.getItemId() + ".armor", item.getArmor());
        itemData.set(item.getItemId() + ".durability", item.getDurability());
        itemData.set(item.getItemId() + ".unbreakable", item.isUnbreakable());
        itemData.set(item.getItemId() + ".isCheckLore", item.isCheckLore());
        itemData.set(item.getItemId() + ".isCheckEnchantment", item.isCheckEnchantment());
        itemData.set(item.getItemId() + ".isDisplayDefaultLore", item.isDisplayDefaultLore());
        itemData.set(item.getItemId() + ".isDisplayCooldown", item.isDisplayCooldown());
        List<String> skillsList = new ArrayList<>();
        for (Map.Entry<SkillTrigger, LinkedHashMap<String, SkillData>> map : item.skillMap.entrySet()) {
            for (Map.Entry<String, SkillData> dataMap : map.getValue().entrySet()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(dataMap.getKey()).append(",");
                stringBuilder.append(map.getKey().toString()).append(",");
                for (Object data : dataMap.getValue().data) {
                    stringBuilder.append(data.toString().replace(",", "[comma]")).append(",");
                }
                skillsList.add(stringBuilder.substring(0, stringBuilder.length() - 1));
            }
        }
        itemData.set(item.getItemId() + ".skills", skillsList);
        List<String> potionsList = new ArrayList<>();
        for (Map.Entry<PotionEffectType, Integer[]> map : item.potionMap.entrySet()) {
            potionsList.add(map.getKey().getName()
                    + " " + item.potionTriggerMap.get(map.getKey()).toString()
                    + " " + map.getValue()[0]
                    + " " + map.getValue()[1]);
        }
        itemData.set(item.getItemId() + ".potions", potionsList);
        for (Map.Entry<Enchantment, Integer> map : item.enchantmentMap.entrySet()) {
            itemData.set(item.getItemId() + ".enchantments." + map.getKey().getName(), map.getValue());
        }
        for (Map.Entry<EntityType, Integer> dropMap : item.dropMap.entrySet()) {
            itemData.set(item.getItemId() + ".drops." + dropMap.getKey().toString(), dropMap.getValue());
        }
        if(!item.flagList.isEmpty()){
            itemData.set(item.getItemId() + ".flags", item.flagList);
        }

        RecipeData recipeData = item.getRecipeData();
        if (recipeData != null) {
            itemData.set(item.getItemId() + ".recipe.chance", recipeData.getRecipeChance());
            ItemStack[] itemStacks = recipeData.getItemStacks();
            for (int i = 0; i < 9; i++) {
                ItemStack itemStack = itemStacks[i];
                if (itemStack != null) {
                    itemData.set(item.getItemId() + ".recipe.itemStacks." + i, itemStack);
                }
            }
        }
        itemMap.put(item.getItemId(), item);
        ConfigManager.saveItemData(itemData);
    }

    public static void removeItem(String itemId){
        itemData.set(itemId, null);
        ConfigManager.saveItemData(itemData);
    }

    /**
     * 获取Jitem
     * @param itemId
     * @return
     */
   public static JItem getItem(String itemId){
       return itemMap.get(itemId);
   }

   public static String toDurabilityDisplayLore(int durability, int maxDurability){
       return ConfigManager.getValue("durability.displayLore").toString()
               .replace("%durability%", String.valueOf(durability))
               .replace("%maxDurability%", String.valueOf(maxDurability));
   }



}

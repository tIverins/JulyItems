package github.july_summer.julyitems.command;

import github.july_summer.julyitems.JulyItems;
import github.july_summer.julyitems.item.ItemManager;
import github.july_summer.julyitems.item.JItem;
import github.july_summer.julyitems.potion.PotionManager;
import github.july_summer.julyitems.recipe.RecipeManager;
import github.july_summer.julyitems.skills.SkillTrigger;
import github.july_summer.julyitems.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class EditItemCommands {

    public static EditItemCommands instance;

    static {
        EditItemCommands.instance = new EditItemCommands();
    }

    public static EditItemCommands getInstance(){
        return instance;
    }

    public static List<Method> getMethods(){
        List<Method> methods = new ArrayList<>();
        Arrays.asList(EditItemCommands.class.getMethods()).forEach(method -> {
                    if (method.getAnnotation(Sort.class) != null && method.getAnnotation(SubCommand.class) != null) {
                        methods.add(method);
                    }
                }
        );
        //默认获取的Methods是乱序的
        methods.sort(Comparator.comparingInt(method -> method.getAnnotation(Sort.class).order()));
        return methods;
    }

    @Sort(order = 23)
    @SubCommand(cmd = "setRecipe <合成几率>", msg = "设置该物品的合成表, 如果需要删除请在合成几率输入任意字符", isPlayer = true)
    public void setRecipe(Player p, String[] args) {
        JItem jitem = ItemManager.getItem(args[0]);
        if (!Util.isNumber(args[2])) {
            p.sendMessage("§a该物品的合成表已移除");
            jitem.setRecipe(null);
            return;
        }
        int chance = Integer.parseInt(args[2]);
        if (chance < 0) {
            p.sendMessage("§a请输入正整数");
            return;
        }
        p.sendMessage("§a请在即将打开的合成表里, 放入合成材料");
        Bukkit.getScheduler().runTaskLater(JulyItems.getInstance(), () -> {
            if (!RecipeManager.openCraftingInventory(args[0], chance, p)) {
                p.sendMessage("§c打开失败: 有其他玩家在编辑合成表");
                return;
            }
        }, 40);
    }

    @Sort(order =  22)
    @SubCommand(cmd = "removeFlag <ItemFlag>", msg = "移除物品的ItemFlag")
    public void removeFlag(CommandSender sender, String[] args){
        ItemFlag itemFlag = null;
        try {
            itemFlag = ItemFlag.valueOf(args[2]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§c未知Flag");
            return;
        }

        JItem jItem = ItemManager.getItem(args[0]);
        if(!jItem.hasFlag(itemFlag)){
            sender.sendMessage("§c未知Flag");
            return;
        }
        jItem.removeFlag(itemFlag);
        sender.sendMessage("§aItemFlag已移除");
        return;
    }

    @Sort(order =  21)
    @SubCommand(cmd = "addFlag <ItemFlag>", msg = "为物品添加ItemFlag")
    public void addFlag(CommandSender sender, String[] args){
        ItemFlag itemFlag = null;
        try {
            itemFlag = ItemFlag.valueOf(args[2]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§c未知Flag");
            return;
        }
        ItemManager.getItem(args[0]).addFlag(itemFlag);
        sender.sendMessage("§aItemFlag已添加");
        return;
    }

    @Sort(order = 20)
    @SubCommand(cmd = "removeDrop <生物种类>", msg = "移除掉落")
    public void removeDrop(CommandSender sender, String[] args){
        EntityType entityType = EntityType.valueOf(args[2]);
        if(entityType == null){
            sender.sendMessage("§c未知生物");
            return;
        }
        ItemManager.getItem(args[0]).removeDrop(entityType);
        sender.sendMessage("§a掉落已移除");
    }

    @Sort(order = 19)
    @SubCommand(cmd = "addDrop <生物种类> <几率>", msg = "添加掉落")
    public void addDrop(CommandSender sender, String[] args){
        EntityType entityType = EntityType.valueOf(args[2]);
        if(entityType == null){
            sender.sendMessage("§c未知生物");
            return;
        }
        if(!Util.isNumber(args[3])){
            sender.sendMessage("§c几率必须为整数");
            return;
        }
        ItemManager.getItem(args[0]).addDrop(entityType, Integer.parseInt(args[3]));
        sender.sendMessage("§a掉落已添加");
    }

    @Sort(order = 18)
    @SubCommand(cmd = "removePotion <药水> ", msg = "移除物品的药水效果")
    public void removePotion(CommandSender sender, String[] args){
        PotionEffectType potion = PotionManager.getByNamePotion(args[2]);
        if(potion == null){
            sender.sendMessage("§c未知药水");
            return;
        }
        JItem jitem = ItemManager.getItem(args[0]);
        if(!jitem.hasPotion(potion)){
            sender.sendMessage("物品上没有 " +args[2] + " 药水效果");
            return;
        }
        jitem.removePoint(potion);
        sender.sendMessage("§a已移除 " + args[2] + " 药水效果");
    }

    @Sort(order = 17)
    @SubCommand(cmd = "addPotion <药水> <触发方式> <持续时间> <等级>", msg = "为物品添加药水效果")
    public void addPotion(CommandSender sender, String[] args){
        if(PotionManager.getByNamePotion(args[2]) == null){
            sender.sendMessage("§c未知药水");
            return;
        }
        if(!SkillTrigger.contains(args[3])){
            sender.sendMessage("§c未知的触发方式");
            return;
        }
        if(!Util.isNumber(args[4]) || !Util.isNumber(args[5])){
            sender.sendMessage("§c时间和等级必须是整数");
            return;
        }
        JItem jitem = ItemManager.getItem(args[0]);
        jitem.addPoint(PotionManager.getByNamePotion(args[2]), SkillTrigger.valueOf(args[3])
                , Integer.parseInt(args[4])
                , Integer.parseInt(args[5]));
        sender.sendMessage("§a药水添加成功");
    }

    @Sort(order = 16)
    @SubCommand(cmd = "setArmor <整数>", msg = "设置物品的护甲(注: 只在装备栏生效)")
    public void setArmor(CommandSender sender, String[] args){
        if(Util.isNumber(args[2])) {
            ItemManager.getItem(args[0]).setArmor(Integer.parseInt(args[2]));
            sender.sendMessage("§a将护甲设置为: §f" +args[2]);
            return;
        }
        sender.sendMessage("§c请输入整数");
    }

    @Sort(order = 15)
    @SubCommand(cmd = "setMaxDamage <整数>", msg = "设置物品的最大伤害(注: 手持生效)")
    public void setMaxDamage(CommandSender sender, String[] args){
        if(Util.isNumber(args[2])) {
            ItemManager.getItem(args[0]).setMaxDamage(Integer.parseInt(args[2]));
            sender.sendMessage("§a将最大伤害设置为: §f" +args[2]);
            return;
        }
        sender.sendMessage("§c请输入整数");
    }

    @Sort(order = 14)
    @SubCommand(cmd = "setMinDamage <整数>", msg = "设置物品的最小伤害(注: 手持生效)")
    public void setMinDamage(CommandSender sender, String[] args) {
        if (Util.isNumber(args[2])) {
            ItemManager.getItem(args[0]).setMinDamage(Integer.parseInt(args[2]));
            sender.sendMessage("§a将最小伤害设置为: §f" + args[2]);
            return;
        }
        sender.sendMessage("§c请输入整数");
    }

    @Sort(order = 13)
    @SubCommand(cmd = "setLore <true/false>", msg = "设置是否允许Lore变动 true为是 false为否")
    public void setCheckLore(CommandSender sender, String[] args){
        if(args[2].equals("true") || args[2].equals("false")) {
            ItemManager.getItem(args[0]).setCheckLore(Boolean.parseBoolean(args[2]));
            sender.sendMessage("§a将是否允许LORE变动设置为: §f" +args[2]);
            return;
        }
        sender.sendMessage("§c请填写true或者false");
    }

    @Sort(order = 12)
    @SubCommand(cmd = "setDisplayCooldown <true/false>", msg = "设置是否显示冷却true为是 false为否")
    public void setDisplayCooldown(CommandSender sender, String[] args){
        if(args[2].equals("true") || args[2].equals("false")) {
            ItemManager.getItem(args[0]).setDisplayCooldown(Boolean.parseBoolean(args[2]));
            sender.sendMessage("§a将是否显示冷却设置为: §f" +args[2]);
            return;
        }
        sender.sendMessage("§c请填写true或者false");
    }

    @Sort(order = 12)
    @SubCommand(cmd = "setDisplayLore <true/false>", msg = "设置是否显示默认Lore true为是 false为否")
    public void setDisplayLore(CommandSender sender, String[] args){
        if(args[2].equals("true") || args[2].equals("false")) {
            ItemManager.getItem(args[0]).setDisplayDefaultLore(Boolean.parseBoolean(args[2]));
            sender.sendMessage("§a将是否显示默认Lore设置为: §f" +args[2]);
            return;
        }
        sender.sendMessage("§c请填写true或者false");
    }

    @Sort(order = 11)
    @SubCommand(cmd = "setCheckEnchantment <true/false>", msg = "设置是否允许附魔变动 true为是 false为否")
    public void setCheckEnchantment(CommandSender sender, String[] args){
        if(args[2].equals("true") || args[2].equals("false")) {
            ItemManager.getItem(args[0]).setCheckEnchantment(Boolean.parseBoolean(args[2]));
            sender.sendMessage("§a将是否允许附魔变动设置为: §f" +args[2]);
            return;
        }
        sender.sendMessage("§c请填写true或者false");
    }

    @Sort(order = 10)
    @SubCommand(cmd = "setUnbreakable <true/false>", msg = "设置物品为无限耐久 true为是 false为否")
    public void setUnbreakable(CommandSender sender, String[] args){
        if(args[2].equals("true") || args[2].equals("false")) {
            ItemManager.getItem(args[0]).setUnbreakable(Boolean.parseBoolean(args[2]));
            sender.sendMessage("§a将无限耐久设置为: §f" +args[2]);
            return;
        }
        sender.sendMessage("§c请填写true或者false");
    }

    @Sort(order = 9)
    @SubCommand(cmd = "removeEnchantment <附魔>", msg = "移除附魔")
    public void removeEnchantment(CommandSender sender, String[] args){
        if(Enchantment.getByName(args[2]) == null){
            sender.sendMessage("§c未知附魔");
            return;
        }
        JItem jitem = ItemManager.getItem(args[0]);
        Enchantment enchantment = Enchantment.getByName(args[2]);
        if(jitem.hasEnchantment(enchantment)){
            sender.sendMessage("§c物品上并没有该附魔");
            return;
        }
        jitem.removeEnchantment(enchantment);
        sender.sendMessage("§a附魔已移除");
    }

    @Sort(order = 8)
    @SubCommand(cmd = "addEnchantment <附魔> <等级>", msg = "添加附魔")
    public void addEnchantment(CommandSender sender, String[] args){
        if(Enchantment.getByName(args[2]) == null){
            sender.sendMessage("§c未知附魔");
            return;
        }
        if(!Util.isNumber(args[3])){
            sender.sendMessage("§c等级请输入整数");
            return;
        }
        ItemManager.getItem(args[0]).addEnchantment(Enchantment.getByName(args[2]), Integer.parseInt(args[3]));
        sender.sendMessage("§a附魔已添加");
    }

    @Sort(order = 7)
    @SubCommand(cmd = "removeLore <行数>", msg = "删除一行描述")
    public void removeLore(CommandSender sender, String[] args){
        if(!Util.isNumber(args[2])){
            sender.sendMessage("§c行数请输入整数");
            return;
        }
        if(ItemManager.getItem(args[0]).removeLine(Integer.parseInt(args[2]) - 1)){
            sender.sendMessage("§a描述删除成功");
        } else {
            sender.sendMessage("§c无法找到该行");
        }

    }

    @Sort(order = 6)
    @SubCommand(cmd = "editLore <行数> <描述>", msg = "编辑一行描述", checkLength = false)
    public void editLine(CommandSender sender, String[] args){
        if(Util.argsToString(args).contains(" editLore ")) {
            String lore = Util.argsToString(args).split(" editLore " + args[2] + " ")[1].replace("&", "§");
            if (!Util.isNumber(args[2])) {
                sender.sendMessage("§c行数请输入整数");
                return;
            }
            if (ItemManager.getItem(args[0]).editLine(Integer.parseInt(args[2]) - 1, lore)) {
                sender.sendMessage("§a描述编辑成功");
            } else {
                sender.sendMessage("§c无法找到该行");
            }
        }
    }

    @Sort(order = 5)
    @SubCommand(cmd = "addLore <描述>", msg = "增加一行描述", checkLength = false)
    public void addLore(CommandSender sender, String[] args){
        if(Util.argsToString(args).contains(" addLore ")) {
            String lore = Util.argsToString(args).split(" addLore ", 2)[1].replace("&", "§");
            ItemManager.getItem(args[0]).addLore(lore);
            sender.sendMessage("§a描述添加成功");
        }
    }

    @Sort(order = 4)
    @SubCommand(cmd = "setDisplayName <display>", msg = "设置物品显示名", checkLength = false)
    public void setDisplayName(CommandSender sender, String[] args){
        args = Util.argsToString(args).split(" ", 3);
        String colorName = args[2].replace("&", "§");
        ItemManager.getItem(args[0]).setDisplayName(colorName);
        sender.sendMessage("§a设置物品显示名为: " + colorName);
    }

    @Sort(order = 3)
    @SubCommand(cmd = "setDurability <最大耐久度>", msg = "设置物品的最大耐久 (如果物品有耐久)")
    public void setDurability(CommandSender sender, String[] args){
        if(!Util.isNumber(args[2])){
            sender.sendMessage("§c耐久请输入整数");
            return;
        }
        int durability = Integer.parseInt(args[2]);
        if(durability <= 0){
            sender.sendMessage("耐久请输入正整数");
            return;
        }
        ItemManager.getItem(args[0]).setDurability((short) durability);
        sender.sendMessage("§a设置物品最大耐久度为: §f" + durability);
    }

    @Sort(order = 2)
    @SubCommand(cmd = "setType", msg = "设置物品材质为手中物品", isPlayer = true)
    public void setType(Player p, String[] args){
        ItemStack mainHand = p.getInventory().getItemInMainHand();
        if(mainHand == null || mainHand.getType() == Material.AIR){
            p.sendMessage("请将物品拿在手中");
            return;
        }
        ItemManager.getItem(args[0]).setType(mainHand.getType());
        p.sendMessage("§a材质设置为 §f" + mainHand.getType().toString());
    }

    @Sort(order = 1)
    @SubCommand(cmd = "create ", msg = "新建一个物品", existId = false)
    public void create(CommandSender sender, String[] args) {
        ItemManager.saveItem(new JItem(args[0]));
        sender.sendMessage(args[0] + " §a创建成功");
    }

    @Sort
    @SubCommand(cmd = "remove ", msg = "删除一个物品")
    public void remove(CommandSender sender, String[] args){
        ItemManager.removeItem(args[0]);
        sender.sendMessage(args[0] + " §a已被删除");
    }


}

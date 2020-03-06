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
import org.bukkit.inventory.ItemStack;

public class ConsumeSkill extends SkillCooldown implements SkillExecute, SkillCustomLore {

    public ConsumeSkill(){
        super("consume");
        SkillManager.registerCommand(ConsumeSkill.class);
        SkillManager.registerCustomLore("consume", this);
    }

    @Override
    public boolean isDisplayLore() {
        return true;
    }

    @Override
    public String getSkillDisplayLore(SkillData data) {
        return ConfigManager.getValue("skills.consume.displayLore").toString();
    }

    @Override
    public int getCooldown(SkillData data){
        return Util.objectToInteger(data.getData(0));
    }

    @Override
    public void exec(Player p, int triggerItemSlot, SkillTrigger trigger, SkillData data, Event event, Entity eventEntity) {
        ItemStack triggerItem = p.getInventory().getItem(triggerItemSlot);
        int amount = triggerItem.getAmount() - 1 <= 0 ? 0 : triggerItem.getAmount() - 1;
        triggerItem.setAmount(amount);
        p.sendMessage(ConfigManager.getValue("skills.consume.message").toString());
    }

    @SubCommand(
            cmd = "addSkill consume <触发方式> <冷却>",
            msg = "使该物品消耗",
            checkArgs1 = 2,
            checkArgs2 = 1
    )
    public static void consumekill(CommandSender sender, String[] args){
        if(!SkillTrigger.contains(args[3])){
            sender.sendMessage("§c未知的触发方式");
            return;
        }
        if(!Util.isNumber(args[4])){
            sender.sendMessage("§c冷却请输入整数");
            return;
        }
        JItem jitem = ItemManager.getItem(args[0]);
        jitem.addSkill(args[2], SkillTrigger.valueOf(args[3]), new SkillData(new Object[]{args[4]}));
        sender.sendMessage("§a技能 " + SkillManager.skillDisplayNameMap.get("consume") + " 添加成功");
    }

}

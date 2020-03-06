package github.july_summer.julyitems.skills.customskills;

import github.july_summer.julyitems.ConfigManager;
import github.july_summer.julyitems.command.SubCommand;
import github.july_summer.julyitems.item.ItemManager;
import github.july_summer.julyitems.item.JItem;
import github.july_summer.julyitems.skills.*;
import github.july_summer.julyitems.utils.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;

public class ChainSkill implements SkillExecute {

    public List<Material> oreMaterials = new ArrayList<>();

    public ChainSkill(){
        List<Material> list = (List<Material>) ConfigManager.getValue("skills.chain.blockList");
        for(Object type : list){
            oreMaterials.add(Material.valueOf(type.toString()));
        }
        SkillManager.registerCommand(ChainSkill.class);
    }

    public List<Block> getChainBlock(Block block, int range){
        Location location = block.getLocation();
        List<Block> blocks = new ArrayList<>();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        for(int i = 0; i < range; i++){
            Block[] blocks1 = new Block[range * 6];
            blocks1[i] = location.getWorld().getBlockAt(x + i + 1, y, z);
            blocks1[i + 1] = location.getWorld().getBlockAt(x - (i + 1), y, z);
            blocks1[i + 2] = location.getWorld().getBlockAt(x, y + i + 1, z);
            blocks1[i + 3] = location.getWorld().getBlockAt(x, y - (i + 1), z);
            blocks1[i + 4] = location.getWorld().getBlockAt(x, y, z + i +1);
            blocks1[i + 5] = location.getWorld().getBlockAt(x, y, z - (i +1));
            for(Block block1 : blocks1) {
                if (block1 != null && oreMaterials.contains(block1.getType())) {
                    blocks.add(block1);
                }
            }
        }
        return blocks;
    }

    @Override
    public void exec(Player p, int triggerItemSlot, SkillTrigger trigger, SkillData data, Event event, Entity eventEntity) {
        if(event instanceof BlockBreakEvent){
            BlockBreakEvent blockBreakEvent = (BlockBreakEvent)event;
            if(blockBreakEvent.isCancelled()){
                return;
            }
            List<Block> blockList = getChainBlock(blockBreakEvent.getBlock(), Util.objectToInteger(data.getData(0)));
            for(Block block : blockList){
                block.breakNaturally();
            }
        }
    }

    @SubCommand(cmd = "addSkill chain <直线距离>", msg = "添加连锁挖矿技能", checkArgs1 = 2, checkArgs2 = 1)
    public static void chain(CommandSender sender, String[] args){
        if(!Util.isNumber(args[3])){
            sender.sendMessage("§c范围必须是整数");
            return;
        }
        JItem jitem = ItemManager.getItem(args[0]);
        jitem.addSkill(args[2], SkillTrigger.BREAK_BLOCK, new SkillData(new Object[]{args[3]}));
        sender.sendMessage("§a技能 " + SkillManager.skillDisplayNameMap.get("chain") + " 添加成功");
    }
}

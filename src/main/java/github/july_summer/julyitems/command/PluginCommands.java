package github.july_summer.julyitems.command;

import github.july_summer.julyitems.ConfigManager;
import github.july_summer.julyitems.item.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PluginCommands {

    public static PluginCommands instance;

    static {
        PluginCommands.instance = new PluginCommands();
    }

    public static PluginCommands getInstance(){
        return instance;
    }

    public static List<Method> getMethods(){
        List<Method> methods = new ArrayList<>();
        Arrays.asList(PluginCommands.class.getMethods()).forEach(method -> {
                    if (method.getAnnotation(Sort.class) != null && method.getAnnotation(SubCommand.class) != null) {
                        methods.add(method);
                    }
                }
        );
        //默认获取的Methods是乱序的
        methods.sort(Comparator.comparingInt(method -> method.getAnnotation(Sort.class).order()));
        return methods;
    }

    @Sort(order = 4)
    @SubCommand(cmd = "reload ", msg = "重载插件", checkId = false)
    public void reload_Plugin_File(CommandSender sender, String[] args){
        ConfigManager.load();
        ItemManager.init(YamlConfiguration.loadConfiguration(ConfigManager.items));
        sender.sendMessage("§a插件已重载");
    }

    @Sort(order = 3)
    @SubCommand(cmd = "list ", msg = "列出所有物品", checkId = false)
    public void list(CommandSender sender, String[] args){
        List<String> list = ItemManager.getJItemIdList();
        sender.sendMessage("§6列表: ");
        list.forEach(itemId -> {
            sender.sendMessage("§7- §f" + itemId + " §a: §f" + ItemManager.getItem(itemId).getDisplayName());
        });
    }

    @Sort(order = 2)
    @SubCommand(cmd = "giveItem <Player>", msg = "发送物品")
    public void giveItem_Build(CommandSender sender, String[] args){
        Player receivePlayer = Bukkit.getPlayer(args[2]);
        if(receivePlayer == null){
            sender.sendMessage("§c玩家不在线");
            return;
        }
        receivePlayer.getInventory().addItem(ItemManager.getItem(args[0]).buildItem());
        sender.sendMessage("§a物品已发送到 §f" + args[2] + " §a的背包");
        receivePlayer.sendMessage("§a你收到了 " + ItemManager.getItem(args[0]).getDisplayName());
    }

    @Sort(order = 1)
    @SubCommand(cmd = "getItem ", msg = "获取物品", isPlayer = true)
    public void getItem_Build(Player p, String[] args){
        p.getInventory().addItem(ItemManager.getItem(args[0]).buildItem());
        p.sendMessage("§a物品已发送到你的背包");
    }

    @Sort
    @SubCommand(cmd = "update ", msg = "立即更新当前物品")
    public void update(CommandSender sender, String[] args){
        ItemManager.upDateOnlinePlayerItem(args[0]);
        sender.sendMessage("§a已更新");
    }


}

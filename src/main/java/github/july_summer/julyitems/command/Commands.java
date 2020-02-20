package github.july_summer.julyitems.command;

import com.google.common.collect.Maps;
import github.july_summer.julyitems.item.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


public class Commands implements CommandExecutor {

    public LinkedHashMap<Object, List<Method>> methodMap = Maps.newLinkedHashMap();
    public List<String> playerHelpMessage = new ArrayList<>();
    public List<String> opHelpMessage = new ArrayList<>();

    public Commands() {
        methodMap.put(EditItemCommands.getInstance(), EditItemCommands.getMethods());
        for (Map.Entry<Class, Method> map : SkillCommands.getSkillMethods().entrySet()) {
            methodMap.put(map.getKey(), Arrays.asList(map.getValue()));
        }
        methodMap.put(SkillCommands.getInstance(), SkillCommands.getMethods());
        methodMap.put(PluginCommands.getInstance(), PluginCommands.getMethods());
        for (List<Method> methodList : methodMap.values()) {
            for (Method method : methodList) {
                SubCommand annotation = method.getAnnotation(SubCommand.class);
                String helpMessage = "§a/julyitem ";
                if (annotation.checkId()) {
                    helpMessage += "<itemID> ";
                }
                helpMessage += annotation.cmd() + "  " + annotation.msg();
                this.opHelpMessage.add(helpMessage);
                if (!annotation.isOp()) {
                    this.playerHelpMessage.add(helpMessage);
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!label.equalsIgnoreCase("julyitem"))
            return true;
        //send Help
        if(args.length == 0){
            if(sender.isOp()) {
                opHelpMessage.forEach(sender::sendMessage);
            } else {
                playerHelpMessage.forEach(sender::sendMessage);
            }
            return true;
        }
        //SubCommand
        for(Map.Entry<Object, List<Method>> methodEntry : methodMap.entrySet()) {
            for (Method method : methodEntry.getValue()) {
                SubCommand annotation = method.getAnnotation(SubCommand.class);
                String[] cmd = annotation.cmd().split(" ");
                if (annotation.checkId()) {
                    if (annotation.checkLength() && args.length - 1 != cmd.length) {
                        continue;
                    } else if (args.length >= 2) {
                        if (args[1].equalsIgnoreCase(cmd[0])) {
                            //Itemid exits
                            boolean exitsId = ItemManager.hasItemId(args[0]);
                            if (!annotation.existId() && exitsId) {
                                sender.sendMessage("§c编号已存在");
                                return true;
                            }
                            //ItemId not exits
                            if (annotation.existId() && !exitsId) {
                                sender.sendMessage("§c编号不存在");
                                return true;
                            }
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                } else {
                    if (args.length != cmd.length || !args[0].equalsIgnoreCase(cmd[0])) {
                        continue;
                    }
                }
                if(annotation.checkArgs1() != 0 && annotation.checkArgs2() != 0){
                    if(args.length > annotation.checkArgs1() && cmd.length > annotation.checkArgs2()){
                        if(!args[annotation.checkArgs1()].equals(cmd[annotation.checkArgs2()])){
                            continue;
                        }
                    }
                }
                //isPlayer
                if (annotation.isPlayer() && !(sender instanceof Player)) {
                    sender.sendMessage("§c非玩家无法使用该命令");
                    return true;
                }
                //isOp
                if (annotation.isOp() && !sender.isOp()) {
                    sender.sendMessage("§c你没有权限使用该命令");
                    return true;
                }
                Object objSender = annotation.isPlayer() && sender instanceof Player ? (Player)sender : sender;
                try {
                    method.invoke(methodEntry.getKey(), objSender, args);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        sender.sendMessage("§6指令错误, 输入 §f/julyitem §6查看帮助");
        return true;
    }
}

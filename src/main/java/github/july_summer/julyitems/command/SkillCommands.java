package github.july_summer.julyitems.command;

import com.google.common.collect.Maps;
import github.july_summer.julyitems.item.ItemManager;
import github.july_summer.julyitems.item.JItem;
import github.july_summer.julyitems.skills.SkillManager;
import github.july_summer.julyitems.skills.SkillTrigger;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.*;

public class SkillCommands {

    public static SkillCommands instance;

    static {
        SkillCommands.instance = new SkillCommands();
    }

    public static SkillCommands getInstance(){
        return instance;
    }

    public static HashMap<Class, Method> getSkillMethods(){
        HashMap<Class,Method> methodHashMap = Maps.newHashMap();
        SkillManager.commandClassList.forEach(skillExecute -> {
            for (Method method : skillExecute.getMethods()) {
                if (method.getAnnotation(SubCommand.class) != null) {
                    methodHashMap.put(skillExecute, method);
                }
            }
        });
        return methodHashMap;
    }

    public static List<Method> getMethods(){
        List<Method> commandMethods = new ArrayList<>();
        Arrays.asList(SkillCommands.class.getMethods()).forEach(method -> {
                    if (method.getAnnotation(Sort.class) != null && method.getAnnotation(SubCommand.class) != null) {
                        commandMethods.add(method);
                    }
                }
        );
        //默认获取的Methods是乱序的
        commandMethods.sort(Comparator.comparingInt(method -> method.getAnnotation(Sort.class).order()));
        return commandMethods;
    }

    @Sort
    @SubCommand(cmd = "removeSkill <触发方式> <技能> ", msg = "移除物品的技能")
    public void removeSkill(CommandSender sender, String[] args){
        JItem jitem = ItemManager.getItem(args[0]);
        if(!SkillTrigger.contains(args[2])){
            sender.sendMessage("§c未知触发方式");
            return;
        }
        SkillTrigger trigger = SkillTrigger.valueOf(args[2]);
        if(!jitem.hasSkill(trigger, args[3])){
            sender.sendMessage("§c无法找到 " + args[3] + " 技能");
            return;
        }
        jitem.removeSkill(trigger, args[3]);
        sender.sendMessage("§a已移除 " + args[3] + " 技能");
    }

}

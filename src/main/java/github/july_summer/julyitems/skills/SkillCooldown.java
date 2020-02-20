package github.july_summer.julyitems.skills;

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillCooldown{

    public HashMap<String,Map<SkillTrigger,Integer>> cooldownMap;
    public SkillTrigger trigger;

    public static List<HashMap<String,Map<SkillTrigger,Integer>>> mapList = new ArrayList<>();

    public static void runTask(){
        for(HashMap<String,Map<SkillTrigger,Integer>> list : mapList){
            for(Map.Entry<String,Map<SkillTrigger,Integer>> cooldownMap : list.entrySet()){
                for(Map.Entry<SkillTrigger,Integer> triggerMap : cooldownMap.getValue().entrySet()){
                    if (triggerMap.getValue() > 0) {
                        triggerMap.setValue(triggerMap.getValue() - 1);
                    }
                }
            }
        }
    }

    public SkillCooldown(String skillName){
        this.cooldownMap = Maps.newHashMap();
        mapList.add(cooldownMap);
        SkillManager.cooldownClassMap.put(skillName, this);
    }

    public void setTrigger(SkillTrigger trigger){
        this.trigger = trigger;
    }

    public void setCooldown(String playerName, int cooldown){
        Map<SkillTrigger, Integer> map = cooldownMap.containsKey(playerName) ? cooldownMap.get(playerName) : new HashMap<SkillTrigger, Integer>();
        map.put(trigger, cooldown);
        cooldownMap.put(playerName, map);
    }

    public int getCooldown(String playerName){
        Map<SkillTrigger, Integer> map = cooldownMap.containsKey(playerName) ? cooldownMap.get(playerName) : new HashMap<SkillTrigger, Integer>();
        return map.containsKey(trigger) ? map.get(trigger) : 0;
    }

    public int getCooldown(SkillData data){
        return 0;
    }

}

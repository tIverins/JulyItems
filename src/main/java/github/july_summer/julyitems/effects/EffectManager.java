package github.july_summer.julyitems.effects;

import com.google.common.collect.Maps;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.List;

public class EffectManager {

    public static HashMap<String, EffectExecute> effects = Maps.newHashMap();

    /**
     * 初始化粒子组
     * @param effectData
     */
    public static void init(YamlConfiguration effectData){
        effects.clear();
        for(String effectNode : effectData.getConfigurationSection("").getKeys(false)){
            List<String> effectList = effectData.getStringList(effectNode);
            effectList.forEach(line -> {

            });
        }
    }

}

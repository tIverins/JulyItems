package github.july_summer.julyitems;

import com.google.common.collect.Maps;
import github.july_summer.julyitems.item.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class ConfigManager {

    public static JulyItems instance;
    public static File items;
    public static File effects;
    public static HashMap<Object, Object> valueMap = Maps.newHashMap();

    public static void initFile(){
        instance = JulyItems.getInstance();
        items = new File(instance.getDataFolder(), "items.yml");
        effects = new File(instance.getDataFolder(), "effects.yml");
        ConfigManager.load();
    }

    public static void load(){
        if(!instance.getDataFolder().exists()) {
            instance.getDataFolder().mkdir();
        }
        //config.yml
        instance.saveResource("config.yml", false);
        instance.reloadConfig();
        FileConfiguration config = instance.getConfig();
        //check config default node
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(instance.getResource("config.yml"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(reader);
        for(String node : defaultConfig.getConfigurationSection("").getKeys(true)){
            if(!config.getConfigurationSection("").getKeys(true).contains(node)){
                config.set(node, defaultConfig.get(node));
                Bukkit.getConsoleSender().sendMessage("[JulyItems] create node " + node);
            }
        }
        instance.saveConfig();
        for(String node : config.getConfigurationSection("").getKeys(true)){
            Object value = config.get(node);
            if(value instanceof String){
                value = value.toString().replace("&", "§");
            }
            valueMap.put(node, value);
        }
        instance.saveResource("items.yml", false);
        instance.saveResource("effects.yml", false);
    }

    public static Object getValue(String node){
        return valueMap.get(node);
    }

    public static void saveItemData(YamlConfiguration yaml){
        try {
            yaml.save(items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

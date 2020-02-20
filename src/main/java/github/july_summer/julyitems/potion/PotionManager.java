package github.july_summer.julyitems.potion;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import github.july_summer.julyitems.JulyItems;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class PotionManager {

    public static HashMap<PotionEffectType,String> potionDisplayNameMap = Maps.newHashMap();

    public static void initDefaultDisplayName(JulyItems instance) {
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(instance.getResource("potion_chinese.json"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObject nameJson = new Gson().fromJson(reader, JsonObject.class);
        for(PotionEffectType potionEffectType :PotionEffectType.values()){
            if(potionEffectType != null){
                if(!nameJson.has(potionEffectType.getName().toLowerCase())){
                    nameJson.addProperty(potionEffectType.getName().toLowerCase(), potionEffectType.getName().toLowerCase());
                }
            }
        }
        for(PotionEffectType type : PotionEffectType.values()) {
            if (type != null) {
                JsonElement data = nameJson.get(type.getName().toLowerCase());
                if(data != null){
                    potionDisplayNameMap.put(type, data.getAsString());
                }
            }
        }
    }

    public static PotionEffectType getByNamePotion(String potion) {
        for(PotionEffectType type :PotionEffectType.values()){
            if(type != null) {
                if (potion.equalsIgnoreCase(type.getName())) {
                    return type;
                }
            }
        }
        return null;
    }

    public static void givePotion(LivingEntity livingEntity, PotionEffectType potionType, Integer[] data) {
        new PotionEffect(potionType, data[0] * 20, data[1] - 1).apply(livingEntity);
    }

    public static String getDisplayName(PotionEffectType key) {
        return potionDisplayNameMap.get(key);
    }

}

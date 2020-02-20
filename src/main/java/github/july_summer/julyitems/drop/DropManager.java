package github.july_summer.julyitems.drop;

import com.google.common.collect.Maps;
import github.july_summer.julyitems.item.ItemManager;
import github.july_summer.julyitems.item.JItem;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class DropManager {

    public static HashMap<EntityType, Map<String, Integer>> dropMap = Maps.newHashMap();

    public static void initEntityDrop(){
        for(EntityType type : EntityType.values()){
            dropMap.put(type, Maps.newHashMap());
        }
    }

    public static void loadDropItem(JItem jitem){
        for(Map.Entry<EntityType,Integer> dropItemMap : jitem.dropMap.entrySet()) {
            Map<String, Integer> dropDataMap = dropMap.get(dropItemMap.getKey());
            dropDataMap.put(jitem.getItemId(), dropItemMap.getValue());
        }
    }

    public static void addDrop(EntityType type, Integer chance, String dropItemId){
        dropMap.get(type).put(dropItemId, chance);
    }

    public static void removeDrop(EntityType type, String itemId){
        dropMap.get(type).remove(itemId);
    }

    public static boolean hasEntityDrop(EntityType entityType){
        return dropMap.containsKey(entityType);
    }


}

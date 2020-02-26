package github.july_summer.julyitems.skills;

import github.july_summer.julyitems.ConfigManager;
import github.july_summer.julyitems.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;

public enum TriggerEntity {

    PLAYER,
    EVENT_ENTITY,
    RANDOM_SERVER_PLAYER,
    RANDOM_WORLD_PLAYER,
    RANDOM_NEARBY_ENTITY,
    RANDOM_NEARBY_PLAYER,
    NEARBY_ENTITY,
    NEARBY_PLAYER;

    public static int range = 20;

    public static void load(){
        range = Util.objectToInteger(ConfigManager.getValue("nearbyRange"));
    }


    /**
     * 是否包含触发
     * @param triggerEntity
     * @return
     */
    public static boolean contians(String triggerEntity){
        for(TriggerEntity t : TriggerEntity.values()){
            if(t.toString().equals(triggerEntity)){
                return true;
            }
        }
        return false;
    }

    /**
     * 取触发生物列表
     * @param p
     * @param triggerEntity
     * @param eventEntity
     * @return
     */
    public static List<Entity> getTriggerEntity(Player p, TriggerEntity triggerEntity, Entity eventEntity){
        List<Entity> entities = new ArrayList<>();
        if(triggerEntity == null){
            return entities;
        }
        if(triggerEntity.equals(TriggerEntity.PLAYER)){
            entities.add(p);
            return entities;
        }
        if(triggerEntity.equals(TriggerEntity.EVENT_ENTITY)){
            entities.add(eventEntity);
            return entities;
        }
        if(triggerEntity.equals(TriggerEntity.NEARBY_ENTITY)){
            entities = nearByEntity(p, range);
            return entities;
        }
        if(triggerEntity.equals(TriggerEntity.NEARBY_PLAYER)){
            entities = nearByPlayer(p, range);
            return entities;
        }
        if(triggerEntity.equals(TriggerEntity.RANDOM_NEARBY_ENTITY)){
            entities.add(randomNearByEntity(p, range));
            return entities;
        }
        if(triggerEntity.equals(TriggerEntity.RANDOM_NEARBY_PLAYER)){
            entities.add(randomNearByPlayer(p, range));
            return entities;
        }
        if(triggerEntity.equals(TriggerEntity.RANDOM_SERVER_PLAYER)){
            entities.add(randomServerPlayer());
            return entities;
        }
        if(triggerEntity.equals(TriggerEntity.RANDOM_WORLD_PLAYER)){
            entities.add(randomWorldPlayer(p.getLocation().getWorld()));
            return entities;
        }
        return entities;
    }

    /**
     * 取附近所有玩家
     * @param p
     * @param range
     * @return
     */
    public static List<Entity> nearByPlayer(Player p, int range){
        Location location = p.getLocation();
        List<Entity> entities = nearByEntity(p, range);
        entities.removeIf(entity -> !(entities instanceof Player));
        entities.remove(p);
        return entities;
    }

    /**
     * 取附近所有生物
     * @param p
     * @param range
     * @return
     */
    public static List<Entity> nearByEntity(Player p, int range){
        Location location = p.getLocation();
        List<Entity> entities = new ArrayList<Entity>(location.getWorld().getNearbyEntities(location, range, range, range));
        entities.remove(p);
        return entities;
    }

    /**
     * 取附近随机生物
     * @param p
     * @param range
     * @return
     */
    public static Entity randomNearByEntity(Player p, int range){
        Iterator<Entity> nearByEntity = nearByEntity(p, range).iterator();
        List<LivingEntity> entities = new ArrayList<>();
        while(nearByEntity.hasNext()){
            Entity entity = nearByEntity.next();
            if(entity instanceof LivingEntity){
                entities.add((LivingEntity)entity);
            }
        }
        entities.remove(p);
        return entities.get(new Random().nextInt(entities.size()));
    }

    /**
     * 取附近随机玩家
     * @param p
     * @param range
     * @return
     */
    public static Player randomNearByPlayer(Player p, int range){
        Iterator<Entity> nearByEntity = nearByEntity(p, range).iterator();
        List<Player> players = new ArrayList<>();
        while(nearByEntity.hasNext()){
            Entity entity = nearByEntity.next();
            if(entity instanceof Player){
                players.add((Player)entity);
            }
        }
        players.remove(p);
        return players.get(new Random().nextInt(players.size()));
    }

    /**
     * 取服务器内随机玩家
     * @return
     */
    public static Player randomServerPlayer(){
        Collection<? extends Player> collection = Bukkit.getOnlinePlayers();
        Player[] players = new Player[collection.size()];
        int random = new Random().nextInt(players.length - 1);
        return players[random];
    }

    /**
     * 取世界内随机玩家
     * @param world
     * @return
     */
    public static Player randomWorldPlayer(World world){
        List<Player> players = world.getPlayers();
        return players.get(new Random().nextInt(players.size() - 1));
    }

}

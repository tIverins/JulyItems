package github.july_summer.julyitems.skills;

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

    /**
     * 取附近所有玩家
     * @param p
     * @param range
     * @return
     */
    public static List<Player> nearByPlayer(Player p, int range){
        Location location = p.getLocation();
        List<Entity> entities = nearByEntity(p, range);
        Player[] players = new Player[entities.size()];
        entities.toArray(players);
        return Arrays.asList(players);
    }

    /**
     * 取附近所有生物
     * @param p
     * @param range
     * @return
     */
    public static List<Entity> nearByEntity(Player p, int range){
        Location location = p.getLocation();
        return new ArrayList<Entity>(location.getWorld().getNearbyEntities(location, range, range, range));
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

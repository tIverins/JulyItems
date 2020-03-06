package github.july_summer.julyitems.effects;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public interface EffectExecute {

    /**
     * 执行粒子
     */
    abstract void exec(Particle particle, Location location1, Location location2);


}

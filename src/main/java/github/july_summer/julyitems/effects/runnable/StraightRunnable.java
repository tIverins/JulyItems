package github.july_summer.julyitems.effects.runnable;

import github.july_summer.julyitems.effects.EffectExecute;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class StraightRunnable implements EffectExecute {

    private final double distance = 0.1;

    @Override
    public void exec(Particle particle, Location location1, Location location2) {
        World world = location1.getWorld();
        if(particle == null || location1 == null || location2 == null || !location1.getWorld().equals(location2.getWorld()))
            return;

        Vector vector = location2.clone().subtract(location1).toVector();
        for(double i = 0; i < vector.length(); i += distance){
            Location location = location1.clone().add(vector.multiply(i));
            world.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), 1);
        }
    }

}

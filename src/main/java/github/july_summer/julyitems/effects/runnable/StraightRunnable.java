package github.july_summer.julyitems.effects.runnable;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class StraightRunnable extends BukkitRunnable {

    private final double distance = 0.1;

    private Particle particle = null;
    private Location location1 = null;
    private Location location2 = null;
    private World world = null;

    public StraightRunnable value(Particle particle, Location location1, Location location2){
        this.location1 = location1;
        this.location2 = location2;
        this.world = location1.getWorld();
        return this;
    }

    @Override
    public void run() {
        if(particle == null || location1 == null || location2 == null || !location1.getWorld().equals(location2.getWorld()))
            return;

        Vector vector = location2.clone().subtract(location1).toVector();
        for(double i = 0; i < vector.length(); i += distance){
            Location location = location1.clone().add(vector.multiply(i));
            world.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), 1);
        }
    }

}

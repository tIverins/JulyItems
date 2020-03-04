package github.july_summer.julyitems.effects;

import com.google.common.collect.Maps;
import github.july_summer.julyitems.effects.runnable.StraightRunnable;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EffectExecute {

    public static HashMap<String, BukkitRunnable> effectRunnables = Maps.newHashMap();

    static {
        effectRunnables.put("straight", new StraightRunnable());
    }

    private List<String> effectList = new ArrayList<>();

    public EffectExecute(List<String> effect) {
        this.effectList = effect;
    }

    public void exec(Location triggerLocation, Location targetLocation){

    }

}

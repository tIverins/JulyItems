package github.july_summer.julyitems.effects;

import com.google.common.collect.Maps;
import github.july_summer.julyitems.effects.runnable.StraightRunnable;
import org.bukkit.Location;
import org.bukkit.Particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EffectGroup {

    public static HashMap<String, EffectExecute> effectExecutes = Maps.newHashMap();

    static {
        effectExecutes.put("straight", new StraightRunnable());
    }

    private List<String> effectList = new ArrayList<>();

    public EffectGroup(List<String> effect) {
        this.effectList = effect;
    }

    public void exec(Location triggerLocation, Location targetLocation){
        effectList.forEach(effect -> {
            if(effectExecutes.containsKey(effect)){
                String[] data = effect.split(" ");
                if(data.length >= 2){
                    String effectName = data[0];
                    Particle particle = Particle.valueOf(data[1]);
                    effectExecutes.get(effectName).exec(particle, triggerLocation, targetLocation);
                }
            }
        });
    }

}

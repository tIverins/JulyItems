package github.july_summer.julyitems.effects;

import com.google.common.collect.Maps;
import github.july_summer.julyitems.effects.runnable.StraightEffect;
import github.july_summer.julyitems.utils.LocationUtil;
import github.july_summer.julyitems.utils.Util;
import org.bukkit.Location;
import org.bukkit.Particle;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EffectGroup {

    public static HashMap<String, EffectExecute> effectExecutes = Maps.newHashMap();
    public static ScriptEngine js = new ScriptEngineManager().getEngineByName("js");

    static {
        effectExecutes.put("straight", new StraightEffect());
    }

    private List<String> effectList = new ArrayList<>();

    public EffectGroup(List<String> effect) {
        this.effectList = effect;
    }

    public void exec(Location triggerLocation, Location targetLocation){
        Location finalTriggerLocation = triggerLocation.clone();
        Location finalTargetLocation = targetLocation.clone();
        effectList.forEach(effect -> {
                String[] data = effect.split(" ");
                if(data.length >= 4){
                    String effectName = data[0];
                    Particle particle = Particle.valueOf(data[1]);
                    conversion(finalTriggerLocation, data[2]);
                    conversion(finalTargetLocation, data[3]);
                    effectExecutes.get(effectName).exec(particle, finalTriggerLocation, finalTargetLocation);
                }
        });
    }

    void conversion(Location location, String text){
       String[] formula = replace(location, text).split(",");
       location.setX(operation(formula[0]));
       location.setY(operation(formula[1]));
       location.setZ(operation(formula[2]));
    }

    String replace(Location location, String text){
        return LocationUtil.replace(location, text, "{x}", "{y}", "{z}");
    }

    double operation(String formula){
        try {
            return (double)js.eval(formula);
        } catch (ScriptException e) {
            //e.printStackTrace();
            return 0.0;
        }
    }

}

package github.july_summer.julyitems;

import github.july_summer.julyitems.command.Commands;
import github.july_summer.julyitems.drop.DropManager;
import github.july_summer.julyitems.effects.EffectManager;
import github.july_summer.julyitems.item.ItemManager;
import github.july_summer.julyitems.listener.*;
import github.july_summer.julyitems.listener.skill.LightListener;
import github.july_summer.julyitems.listener.skill.ProjectileListener;
import github.july_summer.julyitems.potion.PotionManager;
import github.july_summer.julyitems.recipe.RecipeManager;
import github.july_summer.julyitems.skills.SkillCooldown;
import github.july_summer.julyitems.skills.SkillManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;


public class JulyItems extends JavaPlugin {

    public static JulyItems instance;

    public static JulyItems getInstance(){
        return instance;
    }

    @Override
    public void onEnable() {
        init();
        registerListeners();
        getCommand("julyitem").setExecutor(new Commands());
    }

    @Override
    public void onDisable(){
        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll();
    }

    void init(){
        instance = this;
        ConfigManager.initFile();
        PotionManager.initDefaultDisplayName(this);
        SkillManager.loadDefaultSkills();
        DropManager.initEntityDrop();
        RecipeManager.initCrafting();
        ItemManager.init(YamlConfiguration.loadConfiguration(ConfigManager.items));
        EffectManager.init(YamlConfiguration.loadConfiguration(ConfigManager.effects));
        Bukkit.getScheduler().runTaskTimer(this, () -> SkillCooldown.runTask(), 20 , 20);
        Bukkit.getScheduler().runTaskTimer(this, () -> SkillTriggerListener.lastHeldTask(), 20 , 20);
    }

    void registerListeners(){
        getServer().getPluginManager().registerEvents(new ItemUpDateLlistener(), this);
        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getServer().getPluginManager().registerEvents(new SkillTriggerListener(), this);
        getServer().getPluginManager().registerEvents(new DurabilityListener(), this);
        getServer().getPluginManager().registerEvents(new DropListener(), this);
        getServer().getPluginManager().registerEvents(new RecipeListener(), this);
        getServer().getPluginManager().registerEvents(new ProjectileListener(), this);
        getServer().getPluginManager().registerEvents(new LightListener(), this);
    }

}

package github.july_summer.julyitems.recipe;

import com.google.common.collect.Maps;
import github.july_summer.julyitems.JulyItems;
import github.july_summer.julyitems.builder.ItemBuilder;
import github.july_summer.julyitems.item.ItemManager;
import github.july_summer.julyitems.item.JItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RecipeManager {

    public static ArrayList<RecipeData> recipeList = new ArrayList<RecipeData>();
    public static CraftingInventoryHolder inventoryHolder = null;

    public static void initCrafting(){
        recipeList.clear();
        inventoryHolder = new CraftingInventoryHolder();
    }

    public static void loadShapeRecipe(JItem jitem) {
        RecipeData recipeData = jitem.getRecipeData();
        if (recipeData != null) {
            addRecipe(recipeData);
        }
    }

    public static boolean openCraftingInventory(String itemId, Integer chance, Player p) {
        if (p.getOpenInventory() != null) {
            p.closeInventory();
        }
        if (inventoryHolder.isOpen()) {
            return false;
        }
        inventoryHolder.itemId(itemId).chance(chance).open(p);
        return true;
    }

    public static void removeRecipe(String itemId) {
        for (int i = 0; i < recipeList.size(); i++) {
            if (recipeList.get(i).getResultJitem().equals(itemId)) {
                recipeList.remove(i);
            }
        }
    }

    public static boolean containsRecipe(String itemId) {
        for (int i = 0; i < recipeList.size(); i++) {
            if (recipeList.get(i).getResultJitem().equals(itemId)) {
                return true;
            }
        }
        return false;
    }

    public static void addRecipe(RecipeData recipeData) {
        if (containsRecipe(recipeData.getResultJitem())) {
            removeRecipe(recipeData.getResultJitem());
        }
        recipeList.add(recipeData);
    }

}



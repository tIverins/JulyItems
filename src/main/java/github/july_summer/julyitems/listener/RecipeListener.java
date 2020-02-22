package github.july_summer.julyitems.listener;

import github.july_summer.julyitems.JulyItems;
import github.july_summer.julyitems.item.ItemManager;
import github.july_summer.julyitems.item.JItem;
import github.july_summer.julyitems.recipe.CraftingInventoryHolder;
import github.july_summer.julyitems.recipe.RecipeData;
import github.july_summer.julyitems.recipe.RecipeManager;
import github.july_summer.julyitems.utils.ItemUtil;
import github.july_summer.julyitems.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;


public class RecipeListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        Player p = (Player) event.getPlayer();
        InventoryHolder holder = event.getInventory().getHolder();
        if(holder != null && holder instanceof CraftingInventoryHolder){
            CraftingInventoryHolder inventoryHolder = (CraftingInventoryHolder) holder;
            if(inventoryHolder.isEmpty()){
                p.sendMessage("§c你取消了本次设置");
                inventoryHolder.over();
                return;
            }
            String itemId = inventoryHolder.getItemId();
            RecipeData recipeData = new RecipeData(itemId);
            int chance = inventoryHolder.getChance();
            ItemStack[] itemStacks = inventoryHolder.getMatrix();
            if(chance != -1){
                recipeData.setRecipeChance(chance);
            }
            for(int i = 0; i < 9; i++){
                ItemStack item = itemStacks[i];
                if(item != null && !item.getType().equals(Material.AIR)){
                    recipeData.addItemStack(i, item);
                }
            }
            ItemManager.getItem(itemId).setRecipe(recipeData);
            p.sendMessage("§a合成表设置成功");
            inventoryHolder.over();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(InventoryClickEvent event){
        Inventory clickInventory = event.getClickedInventory();
        if(clickInventory == null){
            return;
        }
        InventoryHolder holder = clickInventory.getHolder();
        if (holder != null && holder instanceof CraftingInventoryHolder) {
            return;
        }
        Player p = (Player)event.getWhoClicked();
        if (clickInventory.getType().equals(InventoryType.WORKBENCH)) {
            CraftingInventory craftingInventory = (CraftingInventory) clickInventory;
            if(event.getSlot() == 0){
                ItemStack result = craftingInventory.getResult();
                if(result != null && result.getType() != Material.AIR && ItemManager.isJItem(result)){
                    RecipeData recipeData = ItemManager.getItem(ItemManager.getItemId(result)).getRecipeData();
                    if(recipeData != null){
                        if(!Util.isChance(recipeData.getRecipeChance())){
                            craftingInventory.setMatrix(new ItemStack[9]);
                            p.closeInventory();
                            p.sendTitle("§4合成失败!", "§4你损失了所有材料!");
                            event.setCancelled(true);
                            return;
                        }
                        craftingInventory.setMatrix(new ItemStack[9]);
                        p.closeInventory();
                        p.getInventory().addItem(result);
                        p.sendTitle("§a合成成功", "§a物品已发送到你的背包!");
                        return;
                    }
                }
            }
            if(event.getClick() == ClickType.LEFT || event.getClick() == ClickType.RIGHT){
                if(craftingInventory.getResult() != null){
                    if(event.getCurrentItem() != null && event.getCursor() != null){
                        if(event.getCurrentItem().getType().equals(event.getCursor().getType())){
                            if(ItemManager.isJItem(craftingInventory.getResult())){
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public void addCraftItem(PrepareItemCraftEvent event) {
        Inventory clickInventory = event.getInventory();
        if(clickInventory == null){
            return;
        }
        InventoryHolder holder = clickInventory.getHolder();
        if (holder != null && holder instanceof CraftingInventoryHolder) {
            return;
        }
        if (clickInventory.getType().equals(InventoryType.WORKBENCH)) {
            CraftingInventory craftingInventory = (CraftingInventory) clickInventory;
            for (RecipeData recipeData : RecipeManager.recipeList) {
                if (!recipeData.isSmilar(craftingInventory.getMatrix())) {
                    continue;
                }
                ItemStack resultItem = ItemManager.getItem(recipeData.getResultJitem()).buildItem();
                ItemUtil.addLore(resultItem, "§6§l§m-------------------------");
                ItemUtil.addLore(resultItem, "§a§l合成成功几率为: §f" + recipeData.getRecipeChance() + "%");
                craftingInventory.setResult(resultItem);
            }
        }
    }
}


package github.july_summer.julyitems.recipe;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;


public class CraftingInventoryHolder implements InventoryHolder {

    private Inventory inv;
    private String itemId;
    private String playerName;
    private int chance = -1;
    private boolean isOpen = false;

    public CraftingInventoryHolder(){
        inv = Bukkit.createInventory(this, InventoryType.WORKBENCH);
    }

    public CraftingInventoryHolder itemId(String itemId){
        this.itemId = itemId;
        return this;
    }

    public CraftingInventoryHolder chance(int chance){
        this.chance = chance;
        return this;
    }

    public void open(Player p){
        this.playerName = p.getName();
        this.isOpen = true;
        p.openInventory(inv);
    }

    public void over(){
        this.isOpen = false;
    }

    public boolean isOpen(){
        return isOpen;
    }

    public boolean isEmpty(){
        for(ItemStack item : getMatrix()){
            if(item != null && !item.getType().equals(Material.AIR)){
                return false;
            }
        }
        return true;
    }

    public String getItemId(){
        return itemId;
    }

    public int getChance() {
        return chance;
    }

    public String getPlayerName() {
        return playerName;
    }

    public ItemStack[] getMatrix(){
        ItemStack[] itemStacks = new ItemStack[9];
        for(int i = 0; i < 9; i++){
            itemStacks[i] = inv.getItem(i + 1);
        }
        return itemStacks;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}

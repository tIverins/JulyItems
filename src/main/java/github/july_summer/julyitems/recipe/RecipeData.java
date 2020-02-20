package github.july_summer.julyitems.recipe;

import org.bukkit.inventory.ItemStack;

public class RecipeData {

    private String resultJitem;
    private int recipeChance = 100;
    private ItemStack[] itemStacks;

    public RecipeData(String resultJitem){
        this.resultJitem = resultJitem;
        this.itemStacks = new ItemStack[9];
    }

    public void addItemStack(int slot, ItemStack item){
        itemStacks[slot] = item;
    }

    public void setRecipeChance(int recipeChance){
        this.recipeChance = recipeChance;
    }

    public int getRecipeChance(){
        return recipeChance;
    }

    public ItemStack[] getItemStacks() {
        return itemStacks;
    }

    public boolean isSmilar(ItemStack[] itemStacks){
        for(int i = 0; i < 9; i++){
            ItemStack originalItemStack = this.itemStacks[i];
            ItemStack nowItemStack = itemStacks[i];
            if(originalItemStack == null && nowItemStack == null){
                continue;
            }
            if(originalItemStack == null && nowItemStack != null){
                return false;
            }
            if(originalItemStack != null && nowItemStack == null){
                return false;
            }
            if(!originalItemStack.isSimilar(nowItemStack)){
                return false;
            }
            if(originalItemStack.getAmount() != nowItemStack.getAmount()){
                return false;
            }
        }
        return true;
    }

    public String getResultJitem(){
        return resultJitem;
    }



}

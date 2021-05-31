package de.marcely.bedwarsaddon.kits115;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;



public class Kit {
    
    private final String name;
    private ItemStack icon;
    private final List<ItemStack> is;
    
    public Kit(final String name) {
        this.icon = new ItemStack(Material.SUGAR);
        this.is = new ArrayList<>();
        this.name = name;
    }
    
    public void setIcon(final ItemStack is) {
        this.icon = is;
    }
    
    public void addItem(final ItemStack is) {
        this.is.add(is);
    }
    
    public String getName() {
        return this.name;
    }
    
    public ItemStack getIcon() {
        return this.icon;
    }
    
    public List<ItemStack> getItems() {
        return this.is;
    }
}

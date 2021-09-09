package de.marcely.bedwarsaddon.kits115;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;




public class AUtil {
    /*
    public static Kit getKit(final String name) {
        for (final Kit kit : BedwarsAddonKits.kits) {
            if (kit.getName().equalsIgnoreCase(name)) {
                return kit;
            }
        }
        return null;
    }
    
    public static ItemStack getItemStack(final String str) {
        final String[] strs = str.split("\\,");
        final ItemStack is = Util.getItemItemstackByName(strs[0]);
        if (is != null) {
            if (strs.length == 2 && Util.isInteger(strs[1])) {
                is.setAmount((int)Integer.valueOf(strs[1]));
            }
            return is;
        }
        return null;
    }
    
    public static String toString(final ItemStack is) {
        return String.valueOf(Util.itemstackToConfigName(is)) + "," + is.getAmount();
        //return is.getType().toString() + "," + is.getAmount();
    }
    
    public static String getMaterialUserFriendlyName(final Material m) {
        String name = "";
        boolean firstLetter = true;
        for (int ci = 0; ci < m.name().length(); ++ci) {
            final char c = m.name().charAt(ci);
            if (c != '_') {
                if (firstLetter) {
                    name = String.valueOf(name) + c;
                    firstLetter = false;
                }
                else {
                    name = String.valueOf(name) + Character.toLowerCase(c);
                }
            }
            else {
                name = String.valueOf(name) + ' ';
                firstLetter = true;
            }
        }
        return name;
    }*/
}

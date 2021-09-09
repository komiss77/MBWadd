package de.marcely.bedwarsaddon.selectshopdesign;

import org.bukkit.inventory.ItemStack;
import de.marcely.bedwars.api.BedwarsAPI;
import ru.komiss77.BwAdd;


public class Config {

    /*
    public static void load() {
        BwAdd.ssdConfig.load();
        for (final de.marcely.bedwars.libraries.configmanager2.objects.Config c : BwAdd.ssdConfig.getConfigsWhichStartWith("design-enabled-") ) {
            final ShopDesignData design = BedwarsAPI.getShopDesign(c.getName().replaceFirst("design-enabled-", ""));
            if (design != null && Util.isBoolean(c.getValue()) && !Boolean.valueOf(c.getValue())) {
                BedwarsAddonSelectShopDesign.DESIGN_DISABLED.add(design);
            }
        }
        for (final de.marcely.bedwars.libraries.configmanager2.objects.Config c : BwAdd.ssdConfig.getConfigsWhichStartWith("design-icon-")) {
            final ShopDesignData design = BedwarsAPI.getShopDesign(c.getName().replaceFirst("design-icon-", ""));
            final ItemStack is = Util.getItemItemstackByName(c.getValue());
            if (design != null && is != null) {
                BedwarsAddonSelectShopDesign.DESIGN_ICON.put(design, is);
            }
        }
        for (final de.marcely.bedwars.libraries.configmanager2.objects.Config c : BwAdd.ssdConfig.getConfigsWhichStartWith("message-")) {
            final Message msg = Message.getByName(c.getName().replaceFirst("message-", ""));
            if (msg != null) {
                msg.setCustomMessage(BwAdd.stringToChatColor(c.getValue()));
            }
        }
        BwAdd.ssdConfig.clear();
    }
    
    public static void save() {
        BwAdd.ssdConfig.clear();
        BwAdd.ssdConfig.addComment("Enable/disable shop designs");
        for (final ShopDesignData data : BedwarsAPI.getShopDesigns()) {
            if (!data.getType().isBeta()) {
                BwAdd.ssdConfig.addConfig("design-enabled-" + data.getName(), (Object)!BedwarsAddonSelectShopDesign.DESIGN_DISABLED.contains(data));
            }
        }
        BwAdd.ssdConfig.addEmptyLine();
        BwAdd.ssdConfig.addComment("Change the icon of the shop designs");
        for (final ShopDesignData data : BedwarsAPI.getShopDesigns()) {
            if (!data.getType().isBeta()) {
                BwAdd.ssdConfig.addConfig("design-icon-" + data.getName(), (Object)(BedwarsAddonSelectShopDesign.DESIGN_ICON.containsKey(data) ? Util.itemstackToConfigName((ItemStack)BedwarsAddonSelectShopDesign.DESIGN_ICON.get(data)) : Util.itemstackToConfigName(BedwarsAddonSelectShopDesign.DESIGN_ICON_MISSING)));
            }
        }
        BwAdd.ssdConfig.addEmptyLine();
        BwAdd.ssdConfig.addComment("Change messages");
        Message[] values;
        for (int length = (values = Message.values()).length, i = 0; i < length; ++i) {
            final Message msg = values[i];
            BwAdd.ssdConfig.addConfig("message-" + msg.name(), (Object)BwAdd.chatColorToString(msg.getMessage()));
        }
        BwAdd.ssdConfig.save();
    }*/
}

package ru.komiss77.extraSpecItem_old;

import de.marcely.bedwars.api.BedwarsAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ExtraSpecialItemsPlugin extends JavaPlugin {

    public static final int MIN_MBEDWARS_API_VER = 100;
    public static final String MIN_MBEDWARS_VER_NAME = "5.4";
    private static ExtraSpecialItemsAddon addon;
    private static ExtraSpecialItemsPlugin instance;

    public static ExtraSpecialItemsPlugin getInstance() {
        return ExtraSpecialItemsPlugin.instance;
    }

    public static ExtraSpecialItemsAddon getAddon() {
        return ExtraSpecialItemsPlugin.addon;
    }

    @Override
    public void onEnable() {
        ExtraSpecialItemsPlugin.instance = this;
     //   if (this.checkMBedwars()) {
            if (this.registerAddon()) {
                Config.load();
                //PluginDescriptionFile pdf = this.getDescription();
                //this.log("------------------------------", pdf.getName() + " For MBedwars", "By: " + pdf.getAuthors(), "Version: " + pdf.getVersion(), "------------------------------");
                BedwarsAPI.onReady(() -> {
                    //CustomSpecialItem.registerAll();
                    TowerHandler.init();
                });
            }
      //  }
    }
    
    /*
        CustomSpecialItem i = 
        register(new CustomSpecialItem(EggBridgerHandler::new, "egg-bridger", ConfigValue.egg_bridger_icon_name, ConfigValue.egg_bridger_icon_material));
        register(new CustomSpecialItem(IceBridgerHandler::new, "ice-bridger", ConfigValue.ice_bridger_icon_name, ConfigValue.ice_bridger_icon_material));
        register(new CustomSpecialItem(TowerHandler::new, "tower", ConfigValue.tower_icon_name, ConfigValue.tower_icon_material));
        register(new CustomSpecialItem(SilverfishHandler::new, "silverfish", ConfigValue.silverfish_icon_name, ConfigValue.silverfish_icon_material));

        */
        
        
        
  /*  private boolean checkMBedwars() {
        try {
            Class apiClass = Class.forName("de.marcely.bedwars.api.BedwarsAPI");
            int apiVersion = (Integer) apiClass.getMethod("getAPIVersion").invoke((Object) null);

            if (apiVersion < 100) {
                throw new IllegalStateException();
            } else {
                return true;
            }
        } catch (Exception exception) {
            this.getLogger().warning("Sorry, your installed version of MBedwars is not supported. Please install at least v5.4");
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
    }*/

    private boolean registerAddon() {
        ExtraSpecialItemsPlugin.addon = new ExtraSpecialItemsAddon(this);
        if (!ExtraSpecialItemsPlugin.addon.register()) {
            this.getLogger().warning("It seems like this addon has already been loaded. Please delete duplicates and try again.");
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        } else {
            return true;
        }
    }

    private void log(String... args) {
        String[] astring = args;
        int i = args.length;

        for (int j = 0; j < i; ++j) {
            String s = astring[j];

            this.getLogger().info(s);
        }

    }
}

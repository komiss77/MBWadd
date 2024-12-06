package ru.komiss77.extraSpecItem_old;

import de.marcely.bedwars.tools.Helper;
import de.marcely.bedwars.tools.Pair;
import de.marcely.bedwars.tools.YamlConfigurationDescriptor;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import ru.komiss77.extraSpecItem_old.ExtraSpecialItemsPlugin;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class Config {

    //public static final String ADDON_VERSION = ExtraSpecialItemsPlugin.getInstance().getDescription().getVersion();
    public static String CURRENT_CONFIG_VERSION = null;

    public static File getFile() {
        return new File(ExtraSpecialItemsPlugin.getAddon().getDataFolder(), "config.yml");
    }

    public static void load() {
        Class oclass = Config.class;

        synchronized (Config.class) {
            try {
                loadUnchecked();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    }

    public static void loadUnchecked() throws Exception {
        File file = getFile();

        if (!file.exists()) {
            save();
        } else {
            YamlConfiguration config = new YamlConfiguration();

            try {
                config.load(file);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            ConfigValue.dye_tower_ukraine = config.getBoolean("Dye-Tower-Ukraine");
            ConfigValue.tower_icon_name = config.getString("PopUpTower.Icon-Name");
            ConfigValue.tower_icon_material = parseItemStack(config.getString("PopUpTower.Icon-Type"), ConfigValue.tower_icon_material);
            ConfigValue.tower_block_material = parseMaterial(config.getString("PopUpTower.Block-Type"), ConfigValue.tower_block_material);
            ConfigValue.tower_block_place_interval = config.getInt("PopUpTower.Block-Place-Interval", ConfigValue.tower_block_place_interval);
            ConfigValue.tower_block_placed_per_interval = config.getInt("PopUpTower.Blocks-Placed-Per-Interval", ConfigValue.tower_block_placed_per_interval);
            ConfigValue.tower_place_place_sound = parseConfigSound(config, "PopUpTower.Sound", ConfigValue.tower_place_place_sound);
            ConfigValue.silverfish_icon_name = config.getString("Silverfish.Icon-Name");
            ConfigValue.silverfish_icon_material = parseItemStack(config.getString("Silverfish.Icon-Type"), ConfigValue.silverfish_icon_material);
            ConfigValue.silverfish_life_duration = config.getInt("Silverfish.Life-Duration", ConfigValue.silverfish_life_duration);
            List silverfishNameTag = config.getStringList("Silverfish.Name-Tag");

            if (silverfishNameTag != null) {
                ConfigValue.silverfish_name_tag = silverfishNameTag;
            }

            ConfigValue.egg_bridger_icon_name = config.getString("Egg-Bridger.Icon-Name");
            ConfigValue.egg_bridger_icon_material = parseItemStack(config.getString("Egg-Bridger.Icon-Type"), ConfigValue.egg_bridger_icon_material);
            ConfigValue.egg_bridger_block_material = parseMaterial(config.getString("Egg-Bridger.Block-Type"), ConfigValue.egg_bridger_block_material);
            ConfigValue.egg_bridger_max_length = config.getInt("Egg-Bridger.Max-Length", ConfigValue.egg_bridger_max_length);
            ConfigValue.egg_bridger_max_y_variation = config.getInt("Egg-Bridger.Max-Y-Variation", ConfigValue.egg_bridger_max_y_variation);
            ConfigValue.egg_bridger_place_sound = parseConfigSound(config, "Egg-Bridger.Sound", ConfigValue.egg_bridger_place_sound);
            ConfigValue.ice_bridger_icon_name = config.getString("Ice-Bridger.Icon-Name");
            ConfigValue.ice_bridger_icon_material = parseItemStack(config.getString("Ice-Bridger.Icon-Type"), ConfigValue.ice_bridger_material);
            ConfigValue.ice_bridger_material = parseMaterial(config.getString("Ice-Bridger.Block-Type"), ConfigValue.ice_bridger_material);
            ConfigValue.ice_bridger_max_distance = config.getInt("Ice-Bridger.Max-Distance", ConfigValue.ice_bridger_max_distance);
            ConfigValue.command_item_enabled = config.getBoolean("Custom-Items.Enabled");
            ConfigValue.command_item_player_commands.clear();
            ConfigurationSection playerSection = config.getConfigurationSection("Custom-Items.Player-Run");
            
            String id;
            String materialName;
            if (playerSection != null) {
                Iterator iterator = playerSection.getKeys(false).iterator();
                while (iterator.hasNext()) {
                    id = (String) iterator.next();

                    id = playerSection.getString(id + ".Material");
                    materialName = playerSection.getString(id + ".Command");
                    ConfigValue.command_item_player_commands.put(id, new Pair(parseItemStack(id, Material.STONE), materialName));
                }
            }

            ConfigValue.command_item_console_commands.clear();
            ConfigurationSection consoleSection = config.getConfigurationSection("Custom-Items.Console-Run");

            if (consoleSection != null) {
                Iterator iterator1 = consoleSection.getKeys(false).iterator();

                while (iterator1.hasNext()) {
                    id = (String) iterator1.next();
                    materialName = consoleSection.getString(id + ".Material");
                    String command = consoleSection.getString(id + ".Command");

                    ConfigValue.command_item_console_commands.put(id, new Pair(parseItemStack(materialName, Material.STONE), command));
                }
            }

           // Config.CURRENT_CONFIG_VERSION = config.getString("file-version");
            //if (Config.CURRENT_CONFIG_VERSION == null || !Config.CURRENT_CONFIG_VERSION.equals(Config.ADDON_VERSION)) {
           //     loadOldConfigs(config);
           //     save();
           // }

        }
    }

    private static void save() throws Exception {
        YamlConfigurationDescriptor config = new YamlConfigurationDescriptor();

        config.addComment("Used for auto-updating the config file. Ignore it");
      //  config.set("file-version", Config.ADDON_VERSION);
        config.addEmptyLine();
        config.addComment("Join our discord for support: https://discord.gg/3mJuxUW");
        config.addEmptyLine();
        config.addComment("SPECIAL ITEM ID's");
        config.addComment("PopUpTower - 'tower'");
        config.addComment("Silverfish - 'silverfish'");
        config.addComment("Egg-Bridger - 'egg-bridger'");
        config.addComment("Ice-Bridger - 'ice-bridger'");
        config.addComment("Command-Items - You Choose!");
        config.addEmptyLine();
        config.addComment("Special Configs (May be removed in the future)");
        config.set("Dye-Tower-Ukraine", ConfigValue.dye_tower_ukraine);
        config.addEmptyLine();
        config.addComment("Automatically builds you a tower");
        config.addComment("Note: Block-Place-Interval in ticks (20 ticks = 1 second)");
        config.set("PopUpTower.Icon-Name", ConfigValue.tower_icon_name);
        config.set("PopUpTower.Icon-Type", Helper.get().composeItemStack(ConfigValue.tower_icon_material));
        config.set("PopUpTower.Block-Type", ConfigValue.tower_block_material.name());
        config.set("PopUpTower.Block-Place-Interval", ConfigValue.tower_block_place_interval);
        config.set("PopUpTower.Blocks-Placed-Per-Interval", ConfigValue.tower_block_placed_per_interval);
        config.set("PopUpTower.Sound", ConfigValue.tower_place_place_sound.toString());
        config.addEmptyLine();
        config.addComment("Summon a Silverfish to help defend your base");
        config.addComment("Note: Life-Duration in ticks (20 ticks = 1 second)");
        config.addComment("Name-Tag Placeholders: {team-name} {team-color}");
        config.set("Silverfish.Icon-Name", ConfigValue.silverfish_icon_name);
        config.set("Silverfish.Icon-Type", Helper.get().composeItemStack(ConfigValue.silverfish_icon_material));
        config.set("Silverfish.Life-Duration", ConfigValue.silverfish_life_duration);
        config.set("Silverfish.Name-Tag", ConfigValue.silverfish_name_tag);
        config.addEmptyLine();
        config.addComment("Construct a bridger following the path of an egg");
        config.set("Egg-Bridger.Icon-Name", ConfigValue.egg_bridger_icon_name);
        config.set("Egg-Bridger.Icon-Type", Helper.get().composeItemStack(ConfigValue.egg_bridger_icon_material));
        config.set("Egg-Bridger.Block-Type", ConfigValue.egg_bridger_block_material.name());
        config.set("Egg-Bridger.Max-Length", ConfigValue.egg_bridger_max_length);
        config.set("Egg-Bridger.Max-Y-Variation", ConfigValue.egg_bridger_max_y_variation);
        config.set("Egg-Bridger.Sound", ConfigValue.egg_bridger_place_sound.toString());
        config.addEmptyLine();
        config.addComment("Builds a flat bridge that only lasts for a few seconds");
        config.set("Ice-Bridger.Icon-Name", ConfigValue.ice_bridger_icon_name);
        config.set("Ice-Bridger.Icon-Type", ConfigValue.ice_bridger_material.name());
        config.set("Ice-Bridger.Block-Type", ConfigValue.ice_bridger_material.name());
        config.set("Ice-Bridger.Max-Distance", ConfigValue.ice_bridger_max_distance);
        config.addEmptyLine();
        config.addComment("Create as many custom Special items at you want");
        config.addComment("Specify is you want the command to be run as player, or as console");
        config.addComment("Add the item to your shop.cm2 though the use of the specialID");
        config.addComment("PlaceHolders: {player} {player-display-name} {x} {y} {z} {yaw} {pitch}");
        config.set("Custom-Items.Enabled", ConfigValue.command_item_enabled);
        Iterator iterator;
        String itemId;
        Pair pair;

        if (!ConfigValue.command_item_player_commands.isEmpty()) {
            iterator = ConfigValue.command_item_player_commands.keySet().iterator();

            while (iterator.hasNext()) {
                itemId = (String) iterator.next();
                pair = (Pair) ConfigValue.command_item_player_commands.get(itemId);
                config.set("Custom-Items.Player-Run." + itemId + ".Material", pair.getKey() != null ? Helper.get().composeItemStack((ItemStack) pair.getKey()) : "STONE");
                config.set("Custom-Items.Player-Run." + itemId + ".Command", pair.getValue());
            }
        } else {
            config.set("Custom-Items.Player-Run.player-example.Material", "stone");
            config.set("Custom-Items.Player-Run.player-example.Command", "say a command that is run by a player");
        }

        if (!ConfigValue.command_item_console_commands.isEmpty()) {
            iterator = ConfigValue.command_item_console_commands.keySet().iterator();

            while (iterator.hasNext()) {
                itemId = (String) iterator.next();
                pair = (Pair) ConfigValue.command_item_console_commands.get(itemId);
                config.set("Custom-Items.Console-Run." + itemId + ".Material", pair.getKey() != null ? Helper.get().composeItemStack((ItemStack) pair.getKey()) : "STONE");
                config.set("Custom-Items.Console-Run." + itemId + ".Command", pair.getValue());
            }
        } else {
            config.set("Custom-Items.Console-Run.console-example.Material", "stone");
            config.set("Custom-Items.Console-Run.console-example.Command", "say a command that is run by a console");
        }

        config.save(getFile());
    }

    public static void loadOldConfigs(FileConfiguration config) {
        if (config.isConfigurationSection("Custom-Command-Items")) {
            ConfigValue.command_item_enabled = config.getBoolean("Custom-Command-Items.Enabled", ConfigValue.command_item_enabled);
            if (ConfigValue.command_item_enabled) {
                ConfigValue.command_item_player_commands = loadLegacyCommandItems(config.getStringList("Custom-Command-Items.Player"));
                ConfigValue.command_item_console_commands = loadLegacyCommandItems(config.getStringList("Custom-Command-Items.Console"));
            }
        }

        ConfigurationSection section = config.getConfigurationSection("Silverfish.Display-Name");

        if (section != null) {
            ArrayList nameTags = new ArrayList();
            Iterator iterator = section.getKeys(false).iterator();

            while (iterator.hasNext()) {
                String tagVal = (String) iterator.next();
                String tag = section.getString(tagVal, "");

                nameTags.add(tag.replace("{sqr}", "■"));
            }

            ConfigValue.silverfish_name_tag = nameTags;
        }

    }

    private static ItemStack parseItemStack(String string, Material def) {
        return parseItemStack(string, new ItemStack(def));
    }

    private static ItemStack parseItemStack(String string, ItemStack def) {
        ItemStack stack = Helper.get().parseItemStack(string);

        return stack != null ? stack : new ItemStack(def);
    }

    private static Material parseMaterial(String string, Material def) {
        Material mat = Helper.get().getMaterialByName(string);

        return mat != null ? mat : def;
    }

    private static Sound parseConfigSound(FileConfiguration config, String configPath, Sound def) {
        String configSound = config.getString(configPath);

        if (configSound != null) {
            Sound sound = Helper.get().getSoundByName(configSound);

            if (sound != null) {
                return sound;
            }

            Console.printConfigWarning(configPath, "Cannot parse sound " + configSound);
        }

        return def;
    }

    /** @deprecated */
    @Deprecated
    private static HashMap loadLegacyCommandItems(List keys) {
        HashMap idCommand = new HashMap();

        if (keys.isEmpty()) {
            return idCommand;
        } else {
            Iterator iterator = keys.iterator();

            while (iterator.hasNext()) {
                String string = (String) iterator.next();

                if (string.contains(":")) {
                    String[] idCommandString = string.split(":");
                    String command = idCommandString[2].startsWith("/") ? idCommandString[2].substring(1) : idCommandString[2];
                    ItemStack itemStack = parseItemStack(idCommandString[1], Material.STONE);

                    idCommand.put(idCommandString[0], new Pair(itemStack, command));
                }
            }

            return idCommand;
        }
    }
}

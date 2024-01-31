package ru.komiss77.extraSpecItem;

import de.marcely.bedwars.tools.Helper;
import de.marcely.bedwars.tools.Pair;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class ConfigValue {

    public static boolean dye_tower_ukraine = false;
    public static String tower_icon_name = "PopUpTower";
    public static ItemStack tower_icon_material = Helper.get().parseItemStack("CHEST");
    public static Material tower_block_material = Helper.get().getMaterialByName("WHITE_WOOL");
    public static int tower_block_place_interval = 1;
    public static int tower_block_placed_per_interval = 2;
    public static Sound tower_place_place_sound = Helper.get().getSoundByName("ENTITY_CHICKEN_EGG");
    public static String silverfish_icon_name = "Silverfish";
    public static ItemStack silverfish_icon_material = Helper.get().parseItemStack("SNOWBALL");
    public static int silverfish_life_duration = 400;
    public static List silverfish_name_tag = Arrays.asList("{team-color}&l{team-name} {team-color}[■ ■ ■ ■ ■]", "{team-color}&l{team-name} {team-color}[■ ■ ■ ■ &7■]", "{team-color}&l{team-name} {team-color}[■ ■ ■ &7■ ■]", "{team-color}&l{team-name} {team-color}[■ ■ &7■ ■ ■]", "{team-color}&l{team-name} {team-color}[■ &7■ ■ ■ ■]");
    public static String egg_bridger_icon_name = "EggBridger";
    public static ItemStack egg_bridger_icon_material = Helper.get().parseItemStack("EGG");
    public static Material egg_bridger_block_material = Helper.get().getMaterialByName("WHITE_WOOL");
    public static int egg_bridger_max_length = 30;
    public static int egg_bridger_max_y_variation = 18;
    public static Sound egg_bridger_place_sound = Helper.get().getSoundByName("ENTITY_CHICKEN_EGG");
    public static String ice_bridger_icon_name = "IceBridger";
    public static ItemStack ice_bridger_icon_material = Helper.get().parseItemStack("ICE");
    public static Material ice_bridger_material = Helper.get().getMaterialByName("ICE");
    public static int ice_bridger_max_distance = 37;
    public static boolean command_item_enabled = false;
    public static HashMap command_item_player_commands = new HashMap() {
        {
            this.put("player-example", new Pair(new ItemStack(Material.STONE), "say a fancy player command"));
        }
    };
    public static HashMap command_item_console_commands = new HashMap() {
        {
            this.put("console-example", new Pair(new ItemStack(Material.STONE), "say a fancy console command"));
        }
    };
}

package ru.komiss77.extra;

import java.util.function.Function;
import org.bukkit.inventory.ItemStack;
import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItem;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseHandler;
import ru.komiss77.BwAdd;
import ru.komiss77.Ostrov;

public class CustomSpecialItem {

  private final SpecialItemUseHandler handler;
  private final String itemId;
  private final String messagesFileId;
  private final ItemStack itemStack;

  public CustomSpecialItem(
      Function<PlayerUseSpecialItemEvent, CustomSpecialItemUseSession> factory,
      String itemId,
      String messagesFileId,
      ItemStack itemStack) {

    this.handler = new CustomSpecialItemUseHandler(factory);
    this.itemId = itemId;
    this.messagesFileId = messagesFileId;
    this.itemStack = itemStack;
  }

  public static void registerAll() {
//Ostrov.log_warn("CustomSpecialItem.registerAll");
    register(new CustomSpecialItem(
        EggBridgerHandler::new,
        "egg-bridger",
        EggBridgerHandler.egg_bridger_icon_name,
        EggBridgerHandler.egg_bridger_icon_material));
    
    register(new CustomSpecialItem(
        IceBridgerHandler::new,
        "ice-bridger",
        IceBridgerHandler.ice_bridger_icon_name,
        IceBridgerHandler.ice_bridger_icon_material));

    register(new CustomSpecialItem(
        TowerHandler::new,
        "tower",
        TowerHandler.tower_icon_name,
        TowerHandler.tower_icon_material));

    register(new CustomSpecialItem(
        SilverfishHandler::new,
        "silverfish",
        SilverfishHandler.silverfish_icon_name,
        SilverfishHandler.silverfish_icon_material));

        /*
        register(new CustomSpecialItem(
                FloodEmpty.getFloodEmptyHandler(),
                "flooder",
                "Flood Filler",
                new ItemStack(Material.FLINT)));
         */
        
        
 /*
    if (ConfigValue.command_item_enabled) {
      loadCommandItems(ConfigValue.command_item_player_commands, false);
      loadCommandItems(ConfigValue.command_item_console_commands, true);
    }
   
      public static boolean command_item_enabled = false;
  public static HashMap<String, Pair<ItemStack, String>> command_item_player_commands = new HashMap<String, Pair<ItemStack, String>>() {{
    put("player-example", new Pair<>(new ItemStack(Material.STONE), "say a fancy player command"));
  }};
  public static HashMap<String, Pair<ItemStack, String>> command_item_console_commands = new HashMap<String, Pair<ItemStack, String>>() {{
    put("console-example", new Pair<>(new ItemStack(Material.STONE), "say a fancy console command"));
  }};
    */
 
  }



  private static void register(CustomSpecialItem item) {

    final SpecialItem specialItem = GameAPI.get().registerSpecialItem(item.getId(), BwAdd.instance, item.getMessageFileId(), item.getItemStack());

    if (specialItem != null) {
      specialItem.setHandler(item.handler());
        Ostrov.log_ok("SpecialItem added : "+item.getId());

    } else {
      final SpecialItem registeredItem = GameAPI.get().getSpecialItem(item.getId());

      // TODO better way to check?
      // Probably reloading bedwars or something
      if (registeredItem != null && registeredItem.getPlugin().getName().equals(BwAdd.instance.getName())){
        Ostrov.log_err("SpecialItem added :"+ item.getId() );
        return;
      }

      // id is already taken by some other addon
      Ostrov.log_err("SpecialItem Registration Failed : Another addon is probably using the " + item.getId() + " special item id");
    }
  }

  public SpecialItemUseHandler handler() {
    return this.handler;
  }

  public String getId() {
    return this.itemId;
  }

  public String getMessageFileId() {
    return this.messagesFileId;
  }

  public ItemStack getItemStack() {
    return this.itemStack;
  }
}

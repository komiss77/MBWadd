package ru.komiss77.extraSpecItem;

import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.game.specialitem.SpecialItem;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseHandler;
import java.util.function.Function;
import org.bukkit.inventory.ItemStack;

public class CustomSpecialItem {

    private final SpecialItemUseHandler handler;
    private final String itemId;
    private final String messagesFileId;
    private final ItemStack itemStack;

    public CustomSpecialItem(Function factory, String itemId, String messagesFileId, ItemStack itemStack) {
        this.handler = new CustomSpecialItemUseHandler(factory);
        this.itemId = itemId;
        this.messagesFileId = messagesFileId;
        this.itemStack = itemStack;
    }

   /* public static void registerAll() {
        CustomSpecialItem i = 
        register(new CustomSpecialItem(EggBridgerHandler::new, "egg-bridger", ConfigValue.egg_bridger_icon_name, ConfigValue.egg_bridger_icon_material));
        register(new CustomSpecialItem(IceBridgerHandler::new, "ice-bridger", ConfigValue.ice_bridger_icon_name, ConfigValue.ice_bridger_icon_material));
        register(new CustomSpecialItem(TowerHandler::new, "tower", ConfigValue.tower_icon_name, ConfigValue.tower_icon_material));
        register(new CustomSpecialItem(SilverfishHandler::new, "silverfish", ConfigValue.silverfish_icon_name, ConfigValue.silverfish_icon_material));
      //  if (ConfigValue.command_item_enabled) {
       //     loadCommandItems(ConfigValue.command_item_player_commands, false);
      //      loadCommandItems(ConfigValue.command_item_console_commands, true);
       // }

    }*/

  /*  private static void loadCommandItems(HashMap map, boolean console) {
        if (map != null && !map.isEmpty()) {
            map.forEach((idx, materialStringPairxx) -> {
                ItemStack material = (ItemStack) materialStringPairxx.getKey();

                register(new CustomSpecialItem((eventx) -> {
                    return new CommandItemHandler(eventx, (String) materialStringPairxx.getValue(), console);
                }, idx, "%" + idx + "%", material));
            });
        }

    }*/

  /*  private static void register(CustomSpecialItem item) {
        SpecialItem specialItem = GameAPI.get().registerSpecialItem(item.getId(), ExtraSpecialItemsPlugin.getInstance(), item.getMessageFileId(), item.getItemStack());

        if (specialItem != null) {
            specialItem.setHandler(item.handler());
        } else {
            SpecialItem registeredItem = GameAPI.get().getSpecialItem(item.getId());

            if (registeredItem != null && registeredItem.getPlugin().getName().equals(ExtraSpecialItemsPlugin.getInstance().getName())) {
                return;
            }

            Console.printSpecializedInfo("SpecialItem Registration Failed", "Another addon is probably using the " + item.getId() + " special item id");
        }

    }*/

    public SpecialItemUseHandler handler() {
        return handler;
    }

    public String getId() {
        return itemId;
    }

    public String getMessageFileId() {
        return messagesFileId;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}

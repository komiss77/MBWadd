package ru.komiss77.extraSpecItem;

import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseHandler;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import java.util.function.Function;
import org.bukkit.plugin.Plugin;

public class CustomSpecialItemUseHandler implements SpecialItemUseHandler {

    private final Function factory;

    public CustomSpecialItemUseHandler(Function factory) {
        this.factory = factory;
    }

    @Override
    public Plugin getPlugin() {
        return ExtraSpecialItemsPlugin.getInstance();
    }

    @Override
    public SpecialItemUseSession openSession(PlayerUseSpecialItemEvent event) {
        CustomSpecialItemUseSession session = (CustomSpecialItemUseSession) factory.apply(event);

        if (session == null) {
            return null;
        } else {
            try {
                session.run(event);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            return session;
        }
    }
}

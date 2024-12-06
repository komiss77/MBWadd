package ru.komiss77.extra;

import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseHandler;
import de.marcely.bedwars.api.game.specialitem.SpecialItemUseSession;
import java.util.function.Function;
import org.bukkit.plugin.Plugin;
import ru.komiss77.BwAdd;

public class CustomSpecialItemUseHandler implements SpecialItemUseHandler {

  private final Function<PlayerUseSpecialItemEvent, CustomSpecialItemUseSession> factory;

  public CustomSpecialItemUseHandler(Function<PlayerUseSpecialItemEvent, CustomSpecialItemUseSession> factory) {
    this.factory = factory;
  }

  @Override
  public Plugin getPlugin() {
    return BwAdd.instance;
  }

  @Override
  public SpecialItemUseSession openSession(PlayerUseSpecialItemEvent event) {
    final CustomSpecialItemUseSession session = this.factory.apply(event);

    if (session == null) // e.g. magnetic shoes
      return null;

    try {
      session.run(event);
    } catch (Throwable t) {
      t.printStackTrace();
    }

    return session;
  }
}

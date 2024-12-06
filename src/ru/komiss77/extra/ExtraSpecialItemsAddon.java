package ru.komiss77.extra;

import de.marcely.bedwars.api.BedwarsAddon;
import org.bukkit.plugin.Plugin;

public class ExtraSpecialItemsAddon extends BedwarsAddon {

  private final Plugin plugin;

  public ExtraSpecialItemsAddon(Plugin plugin) {
    super(plugin);

    this.plugin = plugin;
  }

  @Override
  public String getName() {
    return "ExtraSpecialItems";
  }
}

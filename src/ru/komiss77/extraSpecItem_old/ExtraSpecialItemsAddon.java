package ru.komiss77.extraSpecItem_old;

import de.marcely.bedwars.api.BedwarsAddon;

public class ExtraSpecialItemsAddon extends BedwarsAddon {

    private final ExtraSpecialItemsPlugin plugin;

    public ExtraSpecialItemsAddon(ExtraSpecialItemsPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public String getName() {
        return "ExtraSpecialItems";
    }
}

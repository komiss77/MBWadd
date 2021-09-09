package de.marcely.bedwarsaddon.deathmatch115;



public class Config {
    
    
  /*  public static void load() {
        if (BwAdd.deadmatchConfig.getRootTree().getChilds().size() == 0) {
            save();
        }
        BwAdd.deadmatchConfig.load();
        final Integer deathmatch_time = BwAdd.deadmatchConfig.getConfigInt("deathmatch-time");
        if (deathmatch_time != null) {
            BedwarsAddonDeathmatch.deathmatchTime = deathmatch_time;
        }
        for (final de.marcely.configmanager2.objects.Config c : BwAdd.deadmatchConfig.getConfigsWhichStartWith("arena_")) {
            final String arenaname = c.getName().split("_")[1].replace("{key;&#95;}", "_");
            final Arena arena = BedwarsAPI.getArena(arenaname);
            final String[] strs = c.getValue().split("\\,");
            if (arena != null && strs.length == 6 && Bukkit.getWorld(strs[0]) != null && Util.isDouble(strs[1]) && Util.isDouble(strs[2]) && Util.isDouble(strs[3]) && Util.isDouble(strs[4]) && Util.isDouble(strs[5])) {
                BedwarsAddonDeathmatch.deathmatchLocations.put(arena, new Location(Bukkit.getWorld(strs[0]), (double)Double.valueOf(strs[1]), (double)Double.valueOf(strs[2]), (double)Double.valueOf(strs[3]), (float)(double)Double.valueOf(strs[4]), (float)(double)Double.valueOf(strs[5])));
            }
        }
        BwAdd.deadmatchConfig.clear();
    }
    
    public static void save() {
        BwAdd.deadmatchConfig.clear();
        BwAdd.deadmatchConfig.addComment("Basic configurations:");
        BwAdd.deadmatchConfig.addConfig("deathmatch-time", (Object)BedwarsAddonDeathmatch.deathmatchTime);
        BwAdd.deadmatchConfig.addEmptyLine();
        BwAdd.deadmatchConfig.addComment("We recommend that you don't change anything down here:");
        for (final Map.Entry<Arena, Location> e : BedwarsAddonDeathmatch.deathmatchLocations.entrySet()) {
            BwAdd.deadmatchConfig.addConfig("arena_" + e.getKey().getName().replace("_", "{key;&#95;}"), (Object)(String.valueOf(e.getValue().getWorld().getName()) + "," + e.getValue().getX() + "," + e.getValue().getY() + "," + e.getValue().getZ() + "," + e.getValue().getYaw() + "," + e.getValue().getPitch()));
        }
        BwAdd.deadmatchConfig.save();
    }*/
}

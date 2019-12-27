package de.marcely.bedwarsaddon.multiplebeds;

import ru.komiss77.BwAdd;
import de.marcely.bedwars.api.Arena;
import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.game.location.XYZD;
import de.marcely.bedwars.api.Util;
import de.marcely.bedwars.api.Team;


public class Config {

    
    public static void load() {
        BwAdd.multibedConfig.load();
        for (final de.marcely.configmanager2.objects.Config c : BwAdd.multibedConfig.getConfigsWhichStartWith("arena_")) {
            if (c.getName().split("_").length >= 2) {
                final AArena arena = AUtil.getAArena(c.getName().replace("arena_", "").split("_")[0].replace("{key;&#95;}", "_"));
                if (arena == null) {
                    continue;
                }
                final String key2 = c.getName().substring(("arena_" + arena.getName().replace("_", "{key;&#95;}") + "_").length(), c.getName().length());
                if (key2.startsWith("bed")) {
                    final String[] strs = key2.replace("bed_", "").split("_");
                    if (strs.length != 2) {
                        continue;
                    }
                    final Team team = Team.getTeamByName(strs[0]);
                    final String[] strs2 = c.getValue().split("\\,");
                    if (team == null || !Util.isInteger(strs[1]) || strs2.length != 4 || !Util.isInteger(strs2[0]) || !Util.isInteger(strs2[1]) || !Util.isInteger(strs2[2]) || !Util.isInteger(strs2[3])) {
                        continue;
                    }
                    arena.getBeds().add(new AArena.ABed(arena, team, Integer.valueOf(strs[1]), new XYZD((double)Integer.valueOf(strs2[0]), (double)Integer.valueOf(strs2[1]), (double)Integer.valueOf(strs2[2]), (int)Integer.valueOf(strs2[3]))));
                }
                else {
                    if (!key2.equals("max") || !Util.isInteger(c.getValue())) {
                        continue;
                    }
                    arena.setMaxBedsAmount(Integer.valueOf(c.getValue()));
                }
            }
        }
        BwAdd.multibedConfig.clear();
    }
    
    public static void save() {
        BwAdd.multibedConfig.clear();
        BwAdd.multibedConfig.addComment("We recommend that you don't change anything down here:");
        for (final Arena bwArena : BedwarsAPI.getArenas()) {
            final AArena arena = AUtil.getAArena(bwArena.getName());
            final String arenaname = arena.getName().replace("_", "{key;&#95;}");
            for (final Team team : bwArena.GetTeamColors().GetEnabledTeams()) {
                for (final AArena.ABed bed : arena.getBeds(team)) {
                    BwAdd.multibedConfig.addConfig("arena_" + arenaname + "_bed_" + team.name() + "_" + bed.getID(), (Object)(String.valueOf((int)bed.getLocation().getX()) + "," + (int)bed.getLocation().getY() + "," + (int)bed.getLocation().getZ() + "," + bed.getLocation().getD()));
                }
            }
            BwAdd.multibedConfig.addConfig("arena_" + arenaname + "_max", (Object)arena.getMaxBedsAmount());
        }
        BwAdd.multibedConfig.save();
    }
}

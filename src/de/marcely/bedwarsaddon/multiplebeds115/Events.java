package de.marcely.bedwarsaddon.multiplebeds115;

import org.bukkit.event.Listener;





public class Events implements Listener {
   /* @EventHandler
    public void onBlockPlaceEvent(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final Arena arena = BedwarsAPI.getArena(player);
        final Arena arenaLoc = BedwarsAPI.getArena(block.getLocation());
        if (arena == null && arenaLoc != null) {
            event.setCancelled(!Util.hasPermission((CommandSender)player, Permission.ArenaBuild));
            if (arenaLoc != null && block.getType() == ConfigValue.bed_block && Util.hasPermission((CommandSender)player, Permission.ArenaBuild)) {
                final ItemStack is = event.getItemInHand();
                if (is == null || is.getItemMeta() == null || is.getItemMeta().getDisplayName() == null) {
                    return;
                }
                String name = is.getItemMeta().getDisplayName();
                if (!name.startsWith(ChatColor.YELLOW + "Bed " + ChatColor.DARK_GRAY + "> " + ChatColor.GRAY)) {
                    return;
                }
                name = name.replace(ChatColor.YELLOW + "Bed " + ChatColor.DARK_GRAY + "> " + ChatColor.GRAY, "");
                String rawID = "";
                for (int i = 0; Util.isInteger(String.valueOf(name.charAt(i))); ++i) {
                    rawID = String.valueOf(rawID) + name.charAt(i);
                }
                if (rawID.equals("")) {
                    return;
                }
                final int id = Integer.valueOf(rawID);
                name = name.substring(rawID.length(), name.length());
                if (!name.startsWith(ChatColor.DARK_GRAY + " <" + ChatColor.AQUA + " >> ")) {
                    return;
                }
                name = name.replace(ChatColor.DARK_GRAY + " <" + ChatColor.AQUA + " >> ", "");
                final Team team = Team.getTeamByName(Util.removeChatColor(name));
                if (team == null) {
                    return;
                }
                final AArena a = AUtil.getAArena(arenaLoc.getName());
                if (id > a.getMaxBedsAmount()) {
                    return;
                }
                a.setBed(team, id, XYZD.valueOf(event.getBlock()));
                Config.save();
                player.sendMessage(ChatColor.GREEN + "Saved bed with the ID " + ChatColor.DARK_GREEN + rawID + ChatColor.GREEN + " for the team " + team.getChatColor() + team.getName(true) + ChatColor.GREEN + " in the arena " + ChatColor.DARK_GREEN + a.getName());
            }
        }
    }
    
    @EventHandler
    public void onBlockBreakEvent(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final Material material = block.getType();
        final Arena bwArena = BedwarsAPI.getArena(player);
        if (bwArena != null && bwArena.GetStatus() == ArenaStatus.Running && material == ConfigValue.bed_block) {
            final AArena arena = AUtil.getAArena(bwArena.getName());
            final AArena.ABed bed = arena.getBedInNear(XYZD.valueOf(block));
            if (bed != null) {
                event.setCancelled(true);
                if (!ConfigValue.ownbed_destroyable && bed.getTeam() == arena.getArena().GetPlayerTeam(player)) {
                    player.sendMessage(Language.NotAllowed_BedDestroy.getMessage());
                }
                else {
                    arena.bedBreak(player, bed);
                }
            }
        }
    }
    
    @EventHandler
    public void onArenaStatusUpdateEvent(final ArenaStatusUpdateEvent event) {
        if (event.getStatusBefore() == ArenaStatus.Lobby && event.getStatus() == ArenaStatus.Running) {
            AUtil.getAArena(event.getArena().getName()).prepareStart();
        }
    }
    
    @EventHandler
    public void onEnabledArenaEvent(final EnableArenaEvent event) {
        final AArena arena = AUtil.getAArena(event.getArena().getName());
        for (final CrashMessage msg : new ArrayList<>(event.getCrashMessages())) {
            if (msg.getType() == CrashMessage.CrashMessageType.missingBed) {
                event.getCrashMessages().remove(msg);
            }
        }
        for (final Team team : arena.getArena().GetTeamColors().GetEnabledTeams()) {
            for (int i = 1; i <= arena.getMaxBedsAmount(); ++i) {
                if (arena.getBed(team, i) == null) {
                    event.getCrashMessages().add(new CrashMessage(CrashMessage.CrashMessageType.missingBed, String.valueOf(team.getName()) + ", " + i));
                }
            }
        }
    }
    
    @EventHandler
    public void onBedPlaceEvent(final BedPlaceEvent event) {
        event.setCancelled(true);
        event.getWhoPlaced().sendMessage(ChatColor.RED + "Please use the " + ChatColor.DARK_RED + "/bw addon command " + BwAdd.bedwarsAddon.getID() + " getteambed <team color> <bed ID>" + ChatColor.RED + " command when using the MultipleBeds addon!");
    }
    
    @EventHandler
    public void onBedBreakEvent(final BedBreakEvent event) {
        final AArena arena = AUtil.getAArena(event.getArena().getName());
        if (arena.getMaxBedsAmount() > 1) {
            event.setSolution(BedBreakEvent.BedBreakEventSolution.Cancell);
        }
    }
    
    @EventHandler
    public void onPlayerRespawnEvent(final PlayerRespawnEvent event) {
        final AArena arena = AUtil.getAArena(event.getArena().getName());
        event.kickPlayer(arena.getDestroyedBedsAmount(event.getArena().GetPlayerTeam(event.getPlayer())) >= arena.getMaxBedsAmount());
    }*/
}

package de.marcely.bedwarsaddon.multiplebeds115;



public class AArena {
  /*  private Arena arena;
    private int maxBedsAmount;
    private List<ABed> beds;
    private HashMap<Team, Integer> destroyedBedsAmount;
    
    public AArena(final Arena arena) {
        this(arena, 1);
    }
    
    public AArena(final Arena arena, final int maxBedsAmount) {
        this.beds = new ArrayList<>();
        this.destroyedBedsAmount = new HashMap<>();
        if (arena == null) {
            new NullPointerException("The arena is null!").printStackTrace();
            return;
        }
        this.arena = arena;
        this.maxBedsAmount = maxBedsAmount;
    }
    
    public void setMaxBedsAmount(final int maxBedsAmount) {
        this.maxBedsAmount = maxBedsAmount;
    }
    
    public void setDestroyedBedsAmount(final Team team, final int amount) {
        this.destroyedBedsAmount.put(team, amount);
    }
    
    public String getName() {
        return this.arena.getName();
    }
    
    public Arena getArena() {
        return this.arena;
    }
    
    public int getMaxBedsAmount() {
        return this.maxBedsAmount;
    }
    
    public List<ABed> getBeds() {
        return this.beds;
    }
    
    public int getDestroyedBedsAmount(final Team team) {
        return this.destroyedBedsAmount.get(team);
    }
    
    public List<ABed> getBeds(final Team team) {
        final List<ABed> list = new ArrayList<>();
        for (final ABed bed : this.beds) {
            if (bed.team == team) {
                list.add(bed);
            }
        }
        return list;
    }
    
    public ABed getBed(final Team team, final int id) {
        for (final ABed bed : this.getBeds(team)) {
            if (bed.id == id) {
                return bed;
            }
        }
        return null;
    }
    
    public ABed getBed(final XYZD loc) {
        for (final ABed bed : this.beds) {
            if (bed.loc.equals(loc)) {
                return bed;
            }
        }
        return null;
    }
    
    public ABed getBedInNear(final XYZD loc) {
        for (final ABed bed : this.beds) {
            if (loc.distance((XYZ)bed.loc) <= 1.0) {
                return bed;
            }
        }
        return null;
    }
    
    public void setBed(final Team team, final int id, final XYZD loc) {
        ABed bed = this.getBed(team, id);
        if (bed != null) {
            this.beds.remove(bed);
        }
        bed = new ABed(this, team, id, loc);
        this.beds.add(bed);
    }
    
    public void prepareStart() {
        this.placeBeds();
        this.destroyedBedsAmount.clear();
        for (final Team team : this.arena.GetTeamColors().GetEnabledTeams()) {
            this.destroyedBedsAmount.put(team, 0);
        }
    }
    
    public void placeBeds() {
        for (final ABed bed : this.beds) {
            bed.place();
        }
    }
    
    public void bedBreak(final Player player, final ABed bed) {
        bed.getLocation().toBukkit(this.arena.getWorld()).getBlock().setType(Material.AIR);
        this.destroyedBedsAmount.put(bed.getTeam(), this.destroyedBedsAmount.get(bed.getTeam()) + 1);
        if (this.destroyedBedsAmount.get(bed.getTeam()) == this.maxBedsAmount) {
            this.arena.GetTeamColors()._0setBedDestroyed(bed.getTeam(), true);
        }
        if (ConfigValue.bed_drops_amount >= 1) {
            final DropType type = BedwarsAPI.getDropType(Language.Spawner_Gold.getMessage());
            if (type != null) {
                final ItemStack is = type.getActualItemstack().clone();
                is.setAmount(ConfigValue.bed_drops_amount);
                final ItemMeta im = is.getItemMeta();
                im.setDisplayName(type.getChatColor() + type.getName());
                is.setItemMeta(im);
                player.getInventory().addItem(new ItemStack[] { is });
            }
        }
        final StatsAPI stats = BedwarsAPI.getPlayerStats((OfflinePlayer)player);
        stats.setBedsDestroyed(stats.getBedsDestroyed() + 1);
        stats.save();
        this.arena.broadcast(Sound.BED_DESTROY);
        final String msg = Language.Destroyed_Bed.getMessage().replace("{color}", bed.team.getName()).replace("{colorcode}", new StringBuilder().append(bed.team.getChatColor()).toString());
        for (final Player p : this.arena.getPlayers()) {
            this.arena._0setIngameScoreboard(p);
            VersionAPI.showSmallTitle(p, msg);
        }
        for (final Player p : this.arena.getSpectators()) {
            this.arena._0setIngameScoreboard(p);
            VersionAPI.showSmallTitle(p, msg);
        }
    }
    
    public static class ABed
    {
        private AArena arena;
        private Team team;
        private int id;
        private XYZD loc;
        
        public ABed(final AArena arena, final Team team, final int id, final XYZD loc) {
            this.arena = arena;
            this.team = team;
            this.id = id;
            this.loc = loc;
        }
        
        public AArena getArena() {
            return this.arena;
        }
        
        public Team getTeam() {
            return this.team;
        }
        
        public int getID() {
            return this.id;
        }
        
        public XYZD getLocation() {
            return this.loc;
        }
        
        public void place() {
            //if (ConfigValue.bed_block == Material.BED_BLOCK) {
                //this.arena.arena.getWorld().getBlockAt((int)this.loc.getX(), (int)this.loc.getY(), (int)this.loc.getZ()).setType(Material.BED_BLOCK);
                this.arena.arena.getWorld().getBlockAt((int)this.loc.getX(), (int)this.loc.getY(), (int)this.loc.getZ()).setType(Material.RED_BED);
                //this.arena.arena.getWorld().getBlockAt((int)this.loc.getX(), (int)this.loc.getY(), (int)this.loc.getZ()).setData((byte)Byte.valueOf(String.valueOf(this.loc.getD())));
                if (this.loc.getD() == 2) {
                    //this.arena.arena.getWorld().getBlockAt((int)this.loc.getX(), (int)this.loc.getY(), (int)this.loc.getZ() - 1).setType(Material.BED_BLOCK);
                    this.arena.arena.getWorld().getBlockAt((int)this.loc.getX(), (int)this.loc.getY(), (int)this.loc.getZ() - 1).setType(Material.RED_BED);
                    //this.arena.arena.getWorld().getBlockAt((int)this.loc.getX(), (int)this.loc.getY(), (int)this.loc.getZ() - 1).setData((byte)Byte.valueOf(String.valueOf(this.loc.getD() + 8)));
                }
                else if (this.loc.getD() == 3) {
                    //this.arena.arena.getWorld().getBlockAt((int)this.loc.getX() + 1, (int)this.loc.getY(), (int)this.loc.getZ()).setType(Material.BED_BLOCK);
                    this.arena.arena.getWorld().getBlockAt((int)this.loc.getX() + 1, (int)this.loc.getY(), (int)this.loc.getZ()).setType(Material.RED_BED);
                    //this.arena.arena.getWorld().getBlockAt((int)this.loc.getX() + 1, (int)this.loc.getY(), (int)this.loc.getZ()).setData((byte)Byte.valueOf(String.valueOf(this.loc.getD() + 8)));
                }
                else if (this.loc.getD() == 0) {
                    //this.arena.arena.getWorld().getBlockAt((int)this.loc.getX(), (int)this.loc.getY(), (int)this.loc.getZ() + 1).setType(Material.BED_BLOCK);
                    this.arena.arena.getWorld().getBlockAt((int)this.loc.getX(), (int)this.loc.getY(), (int)this.loc.getZ() + 1).setType(Material.RED_BED);
                    //this.arena.arena.getWorld().getBlockAt((int)this.loc.getX(), (int)this.loc.getY(), (int)this.loc.getZ() + 1).setData((byte)Byte.valueOf(String.valueOf(this.loc.getD() + 8)));
                }
                else if (this.loc.getD() == 1) {
                    //this.arena.arena.getWorld().getBlockAt((int)this.loc.getX() - 1, (int)this.loc.getY(), (int)this.loc.getZ()).setType(Material.BED_BLOCK);
                    this.arena.arena.getWorld().getBlockAt((int)this.loc.getX() - 1, (int)this.loc.getY(), (int)this.loc.getZ()).setType(Material.RED_BED);
                    //this.arena.arena.getWorld().getBlockAt((int)this.loc.getX() - 1, (int)this.loc.getY(), (int)this.loc.getZ()).setData((byte)Byte.valueOf(String.valueOf(this.loc.getD() + 8)));
                }
            //}
            //else {
            //    this.arena.arena.getWorld().getBlockAt((int)this.loc.getX(), (int)this.loc.getY(), (int)this.loc.getZ()).setType(ConfigValue.bed_block);
            //}
        }
    }*/
}

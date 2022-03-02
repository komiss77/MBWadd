package ru.komiss77;

import org.bukkit.entity.Player;
import java.util.Set;
import org.bukkit.scoreboard.Team;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.Bukkit;
import java.util.HashMap;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class CustomScoreboard {
    
    private final Scoreboard scoreboard;
    private final Objective objective;
    private final HashMap<Integer, String> scores;
    private final boolean showHealth;
    
    public CustomScoreboard(final BwAdd plugin, final boolean showHealth, final String s, final String... array) {
        this.scores = new HashMap<>();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        (this.objective = this.scoreboard.registerNewObjective("SW", "dummy")).setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(this.format(s));
        for (int i = 0; i < array.length; ++i) {
            while (this.scoreboard.getEntries().contains(array[i])) {
                final int n = i;
                array[n] = String.valueOf(array[n]) + " ";
            }
            final String format = this.format(array[i]);
            final int score = array.length - i;
            this.objective.getScore(format).setScore(score);
            this.scores.put(score, format);
        }
        this.showHealth = showHealth;
        if (showHealth) {
            final Objective registerNewObjective = this.scoreboard.registerNewObjective("SW_HEALTH", "health");
            registerNewObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);
            registerNewObjective.setDisplayName(ChatColor.DARK_RED + "\u2764");
        }
    }
    
    
    public void update(final String masterString, String newString, final boolean modyfyStringBelow) {
        for (final String s2 : this.scoreboard.getEntries()) {
            if (s2.contains(masterString)) {
                int score = this.objective.getScore(s2).getScore();
                if (modyfyStringBelow) {
                    --score;
                }
                this.scoreboard.resetScores(this.scores.get(score));
                while (this.scoreboard.getEntries().contains(newString)) {
                    newString = String.valueOf(newString) + " ";
                }
                final String format = this.format(newString);
                this.objective.getScore(format).setScore(score);
                this.scores.put(score, format);
                break;
            }
        }
    }
    
    public void update(final String s, final int n, final boolean b) {
        this.update(s, String.valueOf(n), b);
    }
    
    private String format(final String s) {
        return (s.length() > 16) ? (Bukkit.getBukkitVersion().contains("1.7") ? s.substring(0, 16) : ((s.length() > 32) ? s.substring(0, 32) : s)) : s;
    }
    
    public Team registerTeam(final String s) {
        return this.scoreboard.registerNewTeam(s);
    }
    
    public Set<Team> getTeams() {
        return (Set<Team>)this.scoreboard.getTeams();
    }
    
    public void setName(final String displayName) {
        this.objective.setDisplayName(displayName);
    }
    
    public void apply(final Player player) {
        player.setScoreboard(scoreboard);
        if (showHealth) {
            player.setHealth(player.getHealth() - 1.0E-4);
        }
    }
    
    
    
}

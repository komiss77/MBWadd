package ru.komiss77.extraSpecItem;

import de.marcely.bedwars.api.event.player.PlayerUseSpecialItemEvent;
import de.marcely.bedwars.api.message.Message;
import de.marcely.bedwars.tools.Helper;
import ru.komiss77.extraSpecItem.CustomSpecialItemUseSession;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandItemHandler extends CustomSpecialItemUseSession {

    private final String command;
    private final boolean console;

    public CommandItemHandler(PlayerUseSpecialItemEvent event, String command, boolean console) {
        super(event);
        this.command = command;
        this.console = console;
    }

    public void run(PlayerUseSpecialItemEvent event) {
        this.takeItem();
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        String commandFormatted = Message.build(this.command).placeholder("player", player.getName()).placeholder("player-display-name", Helper.get().getPlayerDisplayName(player)).placeholder("x", (int) loc.getX()).placeholder("y", (int) loc.getY()).placeholder("z", (int) loc.getZ()).placeholder("yaw", (int) loc.getYaw()).placeholder("pitch", (int) loc.getPitch()).done();

        Bukkit.getServer().dispatchCommand((CommandSender) (this.console ? Bukkit.getServer().getConsoleSender() : player), commandFormatted);
        this.stop();
    }

    protected void handleStop() {}
}

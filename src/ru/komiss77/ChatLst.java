package ru.komiss77;


import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.arena.Team;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.komiss77.events.ChatPrepareEvent;
import ru.komiss77.utils.TCUtils;





class ChatLst implements Listener  {
    
    private static final Component onlyTeam;
    
    static {
        onlyTeam = Component.text("§7Сообщение видно только вашей команде.\n"
                                + "§7Чтобы сказать всем командам,\n"
                                + "§7в начале сообщения добавьте !");
    }
    
    public ChatLst() {
//            for (RegisteredListener listener : AsyncPlayerChatEvent.getHandlerList().getRegisteredListeners() ) {
//System.out.println("111111 AsyncPlayerChatEvent listener="+listener.getListener().toString());
//            }

            AsyncPlayerChatEvent.getHandlerList().unregister(BwAdd.marcelyBWplugin);
            AsyncChatEvent.getHandlerList().unregister(BwAdd.marcelyBWplugin);
          //  for (RegisteredListener listener : AsyncPlayerChatEvent.getHandlerList().getRegisteredListeners()) {
          //      if (listener.getPlugin().getName().equalsIgnoreCase("DeluxeChat")) {
           //         listener.getPriority().NORMAL;
           //     }
            //}
            
//            for (RegisteredListener listener : AsyncPlayerChatEvent.getHandlerList().getRegisteredListeners() ) {
//System.out.println("222222 AsyncPlayerChatEvent listener="+listener.getListener().toString());
//            }
        
    }
    
    
    

    
    @EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
    public void onChat(ChatPrepareEvent e) {

        final Player p = e.getPlayer();
        
        if ( p.getGameMode() == GameMode.SPECTATOR ) {
            return;
        }
        
        final Arena arena = de.marcely.bedwars.api.GameAPI.get().getArenaByPlayer(p);
        
        Component c;
        
        if (arena==null) {
            
            c = TCUtils.format("§8<карта?> §7")
                    .hoverEvent(HoverEvent.showText(TCUtils.format("§bBedWars \n§7Арена не выбрана")))
                ;
            e.setSenderGameInfo(c);
            e.setViewerGameInfo(c);
            
        } else {
            
            final Team team = arena.getPlayerTeam(p);
            
            if (arena.getStatus() == ArenaStatus.RUNNING) {
                
                if (team !=null) {
                    Component msg;
        
                    if ( e.getMessage().startsWith("!") ) { //если всем
                        
                        msg = TCUtils.format("§8[§fВсем§8] §8["+team.getDisplayName()+ "§8] §f"+p.getName()+" §o"+TCUtils.toChat(team.getDyeColor())+"≫ §f"+e.getMessage().substring(1) )
                                .hoverEvent(HoverEvent.showText(Component.text("§7Сообщение всем командам")));

                        for (Player pl: e.viewers()) {
                            pl.sendMessage(msg);
                        }
                        e.viewers().clear();
                        //return;
                        
                    } else { //только в команде

                        Arena recipientArena;
                        Team recipientTeam;

                        msg = TCUtils.format("§f"+p.getName()+"§o"+TCUtils.toChat(team.getDyeColor())+"≫ §f"+e.getMessage() )
                                .hoverEvent(HoverEvent.showText(onlyTeam));

                        for (Player pl: e.viewers()) {
                            recipientArena =de.marcely.bedwars.api.GameAPI.get().getArenaByPlayer(pl);
                            if (recipientArena!=null) {
                                recipientTeam = recipientArena.getPlayerTeam(pl);
                                    if (recipientTeam!=null && recipientTeam.getDisplayName().equalsIgnoreCase(team.getDisplayName())) {
                                        pl.sendMessage(msg);
                                    }

                            }
                        }
                        e.viewers().clear();
                        //return;
                    }
                    e.setCancelled(true);
                }
                
            } else {
                
                if (team==null) {   //команда еще не выбрана
                    c = TCUtils.format("§7<"+arena.getDisplayName()+"§8:команда?§7> §7")
                    .hoverEvent(HoverEvent.showText(TCUtils.format("§bBedWars \n§77Арена: §e"+arena.getDisplayName())))
                    ;
                } else {//команда уже выбрана
                    c = TCUtils.format( TCUtils.toChat(team.getDyeColor())+"<"+team.getDisplayName()+"> §7" )
                    .hoverEvent(HoverEvent.showText(TCUtils.format("§bBedWars \n§7Арена : §e"+arena.getDisplayName()+"\n§7Команда : "+team.getDisplayName())))
                    ;
                }
                e.setSenderGameInfo(c);
                e.setViewerGameInfo(c);
                
            }
            
            
        }
        
        
        
    /*    
        
        
        e.setSenderGameInfo(c);
        e.setViewerGameInfo(c);
        
        //if (p.getWorld().getName().equalsIgnoreCase("lobby")) {
        if (p.getWorld().getName().equalsIgnoreCase("lobby")) {
            
             
            if (arena!=null) {
    //System.out.println("Арена: " + arena.getDisplayName() + " статус:" + arena.GetStatus() + (team == null ? " команда не определена":"") );

                //далее - уже на арене
                final Team team = arena.getPlayerTeam(p);
                //DeluxeFormat df = e.getDeluxeFormat();

                if (team==null) {       //команда еще не выбрана
    //System.out.println("Команда: " + team.getChatColor() + team.getName() + " игроки:" + arena.getPlayersInTeam(team));
                    //e.setChatMessage( team.getChatColor()+"<"+team.getName()+"> §7"+msg);
                    c = TCUtils.format("§7<"+arena.getDisplayName()+"§8:команда?§7> §7")
                    .hoverEvent(HoverEvent.showText(TCUtils.format("§bBedWars \n§77Арена: §e"+arena.getDisplayName())))
                    ;

                } else {//команда уже выбрана

                    c = TCUtils.format( TCUtils.toChat(team.getDyeColor())+"<"+team.getDisplayName()+"> §7" )
                    .hoverEvent(HoverEvent.showText(TCUtils.format("§bBedWars \n§7Арена : §e"+arena.getDisplayName()+"\n§7Команда : "+team.getDisplayName())))
                    ;

                }

            } else {
                c = TCUtils.format("§8<карта?> §7")
                    .hoverEvent(HoverEvent.showText(TCUtils.format("§bBedWars \n§7Арена не выбрана")))
                ;
            }
           
            e.setSenderGameInfo(c);
            e.setViewerGameInfo(c);
            //return;
            
            
        } else {  //не в мире лобби
            
            if (arena == null) return;
       
            final Team team = arena.getPlayerTeam(p);

            if (team == null) return; //нет команда - хз что делать? скорее всего это зритель или билдер

            Component msg;
        
            if ( e.getMessage().startsWith("!") ) { //если всем
    //System.out.println("message.startsWith !");            
                msg = TCUtils.format("§8[§fВсем§8] §8["+team.getDisplayName()+ "§8] §f"+p.getName()+" §o"+TCUtils.toChat(team.getDyeColor())+"≫ §f"+e.getMessage().replaceFirst("!", "") )
                        .hoverEvent(HoverEvent.showText(Component.text("§7Сообщение всем командам")));

                for (Player pl: e.viewers()) {
    //System.out.println("---> ! "+pl.getName()+" [Всем] "+e.getMessage());            
                    pl.sendMessage(msg);
                }
                e.viewers().clear();

            } else { //только в команде

                Arena recipientArena;
                Team recipientTeam;

                msg = TCUtils.format("§f"+p.getName()+"§o"+TCUtils.toChat(team.getDyeColor())+"≫ §f"+e.getMessage() )
                        .hoverEvent(HoverEvent.showText(Component.text("§7Сообщение видно только вашей команде.\n"
                        + "§7Чтобы сказать всем командам,\n"
                        + "§7в начале сообщения добавьте !")));

                //msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ " ) );

                for (Player pl: e.viewers()) {
                    recipientArena =de.marcely.bedwars.api.GameAPI.get().getArenaByPlayer(pl);
                    if (recipientArena!=null) {
                        recipientTeam = recipientArena.getPlayerTeam(pl);
                            if (recipientTeam!=null && recipientTeam.getDisplayName().equalsIgnoreCase(team.getDisplayName())) {
                                pl.sendMessage(msg);
                            }

                    }
                }
                e.viewers().clear();
            }

        }
        */
    }    
    
    
    
    
    
    
}

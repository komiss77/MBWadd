package ru.komiss77;


import de.marcely.bedwars.api.arena.Arena;
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





class ChatListener implements Listener  {
    
    
    
    public ChatListener() {
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
//System.out.println("---AsyncPlayerChatEvent sender="+e.getPlayer().getName()+" msg="+e.getMessage()+" reciep="+e.getRecipients());

        final Player p = e.getPlayer();
        
        if ( p.getGameMode() == GameMode.SPECTATOR ) {
            return;
        }
        
        final Arena arena = de.marcely.bedwars.api.GameAPI.get().getArenaByPlayer(p);
        
        if (p.getWorld().getName().equalsIgnoreCase("lobby")) {
            
            Component c;
             
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
       // Component c = Component.text("§8[§3"+PlayerLevel.fromPlayer(p)+ "§8");
        //e.setSenderGameInfo(c);
       // e.setViewerGameInfo(c);
        
        //if ( arena.getSpectators().contains(p) ) {  //не работает!! у зрителя arena == null //если пишет зритель, получают все игроки в мире
        //if ( p.getGameMode() == GameMode.SPECTATOR ) {  
       //     e.setFormat("§8[Зритель] %1$s §f§o≫§f %2$s");
        //    return;
       // }
        
        //final ArenaStatus arenaStatus = arena.GetStatus();
//System.out.println("---2 arena="+arena);
        //if (arena == null || arena.getStatus()!=ArenaStatus.RUNNING ) return; //арена не выбрана или не игра - делюксчат подставляет команду вместо префикса
        
//System.out.println("---2 getSpectators="+arena.getSpectators());
      //  if ( p.getGameMode() == GameMode.SPECTATOR ) {
            
        /////    e.setFormat("§8[Зритель] %1$s §f§o≫§f %2$s");
      //      return;
      //  }
                  /* recipients = e.getRecipients().iterator();
                while (recipients.hasNext()) {
                    recipient = recipients.next();
                    recipientArena = s.a(recipient);
                    if (recipientArena!=null) {
                        recipientTeam = recipientArena.GetPlayerTeam(recipient);
                        if (recipientTeam!=null && recipientTeam.getName().equalsIgnoreCase(team.getName())) {
                            //e.setFormat ( "§f%1$s §o"+team.getChatColor()+"-> §f%2$s" );
                            //message = message.trim()+"§8 !-всем";
                            //e.setMessage(message);
                            recipient.spigot().sendMessage(msg);
                        } //else  recipients.remove();
                    } //else  recipients.remove();
                    recipients.remove();
                }   */
                
            //message = message.trim()+"§8 !+текст -> всем";
            //e.setMessage(message);
        }
        
    }    
    
    
    
    

    


//по входящим с банжи
    //после входящего PluginMessageReceived сообщение отправляется в sendBungeeChat (CompatibilityManager)
    //в sendBungeeChat по какому-то флагу либо сразу рассылается, либо рассылается тем, у кого не локальный
    //переключение локальный/глобальный: e.getPlayer().getUniqueId().toString()
    /*
        public boolean setLocal(final String s) {
        if (DeluxeChat.localPlayers == null) {
            (DeluxeChat.localPlayers = new ArrayList<String>()).add(s);
            return true;
        }
        if (DeluxeChat.localPlayers.contains(s)) {
            return false;
        }
        DeluxeChat.localPlayers.add(s);
        return true;
    }
    
    public boolean setGlobal(final String s) {
        if (DeluxeChat.localPlayers == null) {
            DeluxeChat.localPlayers = new ArrayList<String>();
            return false;
        }
        if (!DeluxeChat.localPlayers.contains(s)) {
            return false;
        }
        DeluxeChat.localPlayers.remove(s);
        return true;
    }
    
    public static boolean isLocal(final String s) {
        return DeluxeChat.localPlayers != null && DeluxeChat.localPlayers.contains(s);
    }

    */

    
    

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //обработка AsyncPlayerChatEvent (исходящих от игрока) этапы 1,2,3
    
    // 1
    //dchat получает AsyncPlayerChatEvent и создаёт DeluxeChatEvent, отмена делает return из AsyncPlayerChatEvent
    //можно играть getRecipients
    /*@EventHandler 
    public void chat(DeluxeChatEvent e) {
        final Player p = e.getPlayer();
//System.out.println("1 DeluxeChatEvent name="+p.getName()+" local?"+DeluxeChat.isLocal(p.getUniqueId().toString())+" arena="+arena);

        if (!p.getWorld().getName().equalsIgnoreCase("lobby")) {
//System.out.println("2 DeluxeChatEvent cancel!!");
            e.setCancelled(true);
            return;
        }
        
        //разделяем по мирам - делюксчат срабатывает раньше
        Player recipient;
        Iterator<Player> recipients = e.getRecipients().iterator();
        while (recipients.hasNext()) {
            recipient = recipients.next(); //если получатель в другом мире, ему не отправляем
            if ( !recipient.getWorld().getName().equalsIgnoreCase(p.getWorld().getName()) ) {
                recipients.remove();
            }
        }

        
        //далее определяем команду для отображения в чате лобби
        //final Arena arena = s.a(p);
        final Arena arena = de.marcely.bedwars.api.GameAPI.get().getArenaByPlayer(p);
        if (arena!=null) {
//System.out.println("Арена: " + arena.getDisplayName() + " статус:" + arena.GetStatus() + (team == null ? " команда не определена":"") );

            //String msg = e.getChatMessage();
            //String dfPrefix = df.getPrefix();
            //if (arena.GetStatus()==ArenaStatus.Running) { //во время игры исходящие не отправляем в банжи
            //    e.setCancelled(true);
            //    return;
            //}

            //далее - уже на арене
            final Team team = arena.getPlayerTeam(p);
            //DeluxeFormat df = e.getDeluxeFormat();

            if (team==null) {       //команда еще не выбрана
//System.out.println("Команда: " + team.getChatColor() + team.getName() + " игроки:" + arena.getPlayersInTeam(team));
                //e.setChatMessage( team.getChatColor()+"<"+team.getName()+"> §7"+msg);
                e.getDeluxeFormat().setPrefix("§8<команда?> §7");
                
            } else {//команда уже выбрана
                
                e.getDeluxeFormat().setPrefix( team.getChatColor()+"<"+team.getDisplayName()+"> §7" );
                
            }
            
        }
        
        
    }
  */
    
    
    // 2
    //после DeluxeChatEvent сообщение форматируется и рассылается локально тем, кто остался в deluxeChatEvent.getRecipients(). 
    //getJSONFormat() == null || getJSONChatMessage() == null || getJSONFormat().isEmpty(), return из AsyncPlayerChatEvent
    //после этого эвента локальные получатели удаляются
    //@EventHandler 
    //public void chat(DeluxeChatJSONEvent e) { 
//System.out.println("2 DeluxeChatJSONEvent");
    //}
    
    
    
    // 3
    //после рассылки локальным игрокам по списку getRecipients, результат этого эвента отправляется в банжи
    //getJSONFormat() == null || getChatMessage() == null || getJSONFormat().isEmpty() || getChatMessage().isEmpty())  return из AsyncPlayerChatEvent
    //@EventHandler 
    //public void chat(ChatToPlayerEvent e) { 
//System.out.println("3 ChatToPlayerEvent");
    //}
    
   
    
    
    
    
    
    
}

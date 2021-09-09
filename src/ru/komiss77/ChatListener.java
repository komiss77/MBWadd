package ru.komiss77;


import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import de.marcely.bedwars.api.arena.Team;
import java.util.Iterator;
import me.clip.deluxechat.events.DeluxeChatEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;





class ChatListener implements Listener  {
    
    
    
    public ChatListener() {
//            for (RegisteredListener listener : AsyncPlayerChatEvent.getHandlerList().getRegisteredListeners() ) {
//System.out.println("111111 AsyncPlayerChatEvent listener="+listener.getListener().toString());
//            }

            AsyncPlayerChatEvent.getHandlerList().unregister(BwAdd.marcelyBWplugin);
            
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
    public void onChat(AsyncPlayerChatEvent e) {
//System.out.println("---AsyncPlayerChatEvent sender="+e.getPlayer().getName()+" msg="+e.getMessage()+" reciep="+e.getRecipients());

        final Player p = e.getPlayer();
        Player recipient;
        Iterator<Player> recipients;
        
        //разделяем по мирам - делюксчат не пропускает глобальный, но если в игре и кто-то зашел в лобби, то пишет
        recipients = e.getRecipients().iterator();
        while (recipients.hasNext()) {
            recipient = recipients.next(); //если получатель в другом мире, ему не отправляем
            if ( !recipient.getWorld().getName().equalsIgnoreCase(p.getWorld().getName()) ) {
                recipients.remove();
            }
        }
            
        if (p.getWorld().getName().equalsIgnoreCase("lobby")) return; //если в лобби - на обработку делюксчата
        
        
        //if ( arena.getSpectators().contains(p) ) {  //не работает!! у зрителя arena == null //если пишет зритель, получают все игроки в мире
        if ( p.getGameMode() == GameMode.SPECTATOR ) {  
            e.setFormat("§8[Зритель] %1$s §f§o≫§f %2$s");
            return;
        }
        
        final Arena arena = de.marcely.bedwars.api.GameAPI.get().getArenaByPlayer(p);
        //final ArenaStatus arenaStatus = arena.GetStatus();
//System.out.println("---2 arena="+arena);
        if (arena == null || arena.getStatus()!=ArenaStatus.RUNNING ) return; //арена не выбрана или не игра - делюксчат подставляет команду вместо префикса
        
//System.out.println("---2 getSpectators="+arena.getSpectators());
        
        final Team team = arena.getPlayerTeam(p);

        if (team == null) return; //нет команда - хз что делать? скорее всего это зритель
        
        TextComponent msg;
        HoverEvent he;
        
        if ( e.getMessage().startsWith("!") ) { //если всем
//System.out.println("message.startsWith !");            
            msg = new TextComponent( "§8[§fВсем§8] "+team.getChatColor()+"§8["+team.getDisplayName()+ "§8] §f"+p.getName()+" §o"+team.getChatColor()+"≫ §f"+e.getMessage().replaceFirst("!", "") );
            he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Сообщение всем командам").create());
            msg.setHoverEvent( he );
            
            for (Player pl: e.getRecipients()) {
//System.out.println("---> ! "+pl.getName()+" [Всем] "+e.getMessage());            
                //if (arena.getPlayers().contains(pl)) pl.spigot().sendMessage(msg);
                pl.spigot().sendMessage(msg);
            }
            e.getRecipients().clear();
            //e.setFormat("§e[Всем] "+team.getChatColor()+"["+team.getName()+ "] §f%1$s §o≫ %2$s");
            //e.setMessage(message.trim().replaceFirst("!", ""));

        } else { //только в команде
           // if (e.getMessage().startsWith("§e[Всем]")) return; //pl.spigot().sendMessage вызывает эвент еще раз когда для всех, пропускам.
            //e.setFormat( team.getChatColor()+ " %1$s §f§o≫§f %2$s");
            
            Arena recipientArena;
            Team recipientTeam;
            
            msg = new TextComponent( "§f"+p.getName()+"§o"+team.getChatColor()+"≫ §f"+e.getMessage());
            he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
                    "§7Сообщение видно только вашей команде.\n"
                    + "§7Чтобы сказать всем командам,\n"
                    + "§7в начале сообщения добавьте !"
            ).create());
            msg.setHoverEvent( he );
            //msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ " ) );
            
            for (Player pl: e.getRecipients()) {
                recipientArena =de.marcely.bedwars.api.GameAPI.get().getArenaByPlayer(pl);
                    if (recipientArena!=null) {
                        recipientTeam = recipientArena.getPlayerTeam(pl);
                            if (recipientTeam!=null && recipientTeam.getDisplayName().equalsIgnoreCase(team.getDisplayName())) {
                            //e.setFormat ( "§f%1$s §o"+team.getChatColor()+"-> §f%2$s" );
                            //message = message.trim()+"§8 !-всем";
                            //e.setMessage(message);
                                pl.spigot().sendMessage(msg);
                            }
                            
                    }
            }
            e.getRecipients().clear();

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
    @EventHandler 
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
//System.out.println("Арена: " + arena.getName() + " статус:" + arena.GetStatus() + (team == null ? " команда не определена":"") );

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

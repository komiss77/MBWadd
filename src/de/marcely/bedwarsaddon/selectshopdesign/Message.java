// 
// Decompiled by Procyon v0.5.36
// 

package de.marcely.bedwarsaddon.selectshopdesign;

import org.bukkit.ChatColor;
import java.util.HashMap;

public enum Message
{
    GUI_TITLE("GUI_TITLE", 0, ChatColor.AQUA + "Дизайн Магазина"), 
    DESIGN_CHOOSE("DESIGN_CHOOSE", 1, ChatColor.GREEN + "Вы выбрали дизайн " + ChatColor.DARK_GREEN + "{design}"), 
    DESIGN_CHOOSE_ALREADY("DESIGN_CHOOSE_ALREADY", 2, ChatColor.RED + "У вас уже такой дизайн!"), 
    DESIGN_CHOOSEN("DESIGN_CHOOSEN", 3, ChatColor.GOLD + "Дизайн выбран.");
    
    private static final HashMap<Message, String> customMessages;
    private final String message;
    
    static {
        customMessages = new HashMap<>();
    }
    
    private Message(final String s, final int n, final String msg) {
        this.message = msg;
    }
    
    public String getMessage() {
        if (Message.customMessages.containsKey(this)) {
            return Message.customMessages.get(this);
        }
        return this.message;
    }
    
    public void setCustomMessage(final String msg) {
        Message.customMessages.put(this, msg);
    }
    
    public static Message getByName(final String msg) {
        Message[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final Message m = values[i];
            if (m.name().equalsIgnoreCase(msg)) {
                return m;
            }
        }
        return null;
    }
}

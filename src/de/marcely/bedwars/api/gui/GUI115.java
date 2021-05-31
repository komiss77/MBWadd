package de.marcely.bedwars.api.gui;

import static de.marcely.bedwars.api.gui.SimpleGUI.openInventories;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
/*
public class GUI115 extends SimpleGUI implements Clickable {
   private String title;
   private int height;
   private GUIItem[][] items;
   public boolean autoRefresh = true;
   public static List cachePlayers = new ArrayList();

   public GUI115(String var1, int var2) {
      this.title = var1;
      this.height = var2;
      this.items = new GUIItem[9][var2];
   }

   @Override
   public boolean isCancellable() {
      return true;
   }

   @Override
   public boolean hasAntiDrop() {
      return true;
   }

   public boolean addItem(GUIItem var1) {
      return this.addItem(var1, (GUI115.AddItemFlag)null);
   }

   public boolean addItem(GUIItem var1, @Nullable GUI115.AddItemFlag var2) {
      int var4;
      int var5;
      label69: {
         var1.gui = this;
         if (this.height > 0) {
            int var3 = -1;

            while((var3 = this.getNextSpace(var3 + 1)) != -1) {
               var4 = var3 / 9;
               var5 = var3 - var4 * 9;
               if (var2 == null) {
                  break label69;
               }

               int var6;
               int var7;
               if (var2.type == 0) {
                  var6 = (Integer)var2.param[0];
                  var7 = (Integer)var2.param[1];
                  if (var5 >= var6 && var5 <= var7) {
                     break label69;
                  }
               } else if (var2.type == 1) {
                  var6 = (Integer)var2.param[0];
                  var7 = (Integer)var2.param[1];
                  if (var4 >= var6 && var4 <= var7) {
                     break label69;
                  }
               } else {
                  if (var2.type != 2) {
                     if (var2.type == 3) {
                        var5 = 4 + (var5 % 2 == 0 ? -var5 : var5 + 1) / 2;
                     }
                     break label69;
                  }

                  var6 = (Integer)var2.param[0];
                  var7 = (Integer)var2.param[1];
                  int var8 = (Integer)var2.param[2];
                  int var9 = (Integer)var2.param[3];
                  if (var5 >= var6 && var5 <= var7 && var4 >= var8 && var4 <= var9) {
                     break label69;
                  }
               }
            }
         }

         if (this.height < 6) {
            this.setHeight(this.height + 1);
            this.addItem(var1, var2);
         }

         return false;
      }

      this.items[var5][var4] = var1;
      this.onSetItem(var5, var4, var1);
      if (this.autoRefresh) {
         this.update();
      }

      return true;
   }

   @Override
   public void setTitle(String var1) {
      this.title = var1;
   }

   public void setHeight(int var1) {
      GUIItem[][] var2 = (GUIItem[][])this.items.clone();
      this.items = new GUIItem[9][var1];

      for(int var3 = 0; var3 < 9; ++var3) {
         for(int var4 = 0; var4 < this.height; ++var4) {
            if (var4 < var1) {
               this.items[var3][var4] = var2[var3][var4];
            }
         }
      }

      this.height = var1;
      if (this.autoRefresh) {
         this.update();
      }

   }

   public void setItemAt(GUIItem var1, int var2, int var3) {
      if (this.items[var2][var3] != null) {
         this.items[var2][var3].gui = null;
      }

      this.items[var2][var3] = var1;
      this.onSetItem(var2, var3, var1);
      var1.gui = this;
      if (this.autoRefresh) {
         this.update();
      }

   }

   @Override
   public void setItemAt(GUIItem var1, int var2) {
      int var3 = var2 / 9;
      this.setItemAt(var1, var2 - var3 * 9, var3);
   }

   @Override
   public String getTitle() {
      return this.title;
   }

   public int getHeight() {
      return this.height;
   }

   @Nullable
   @Override
   public GUIItem getItemAt(int var1) {
      int var2 = var1 / 9;
      return this.getItemAt(var1 - var2 * 9, var2);
   }

   @Nullable
   public GUIItem getItemAt(int var1, int var2) {
      return var1 >= 0 && var2 >= 0 && var1 < 9 && var2 < this.height ? this.items[var1][var2] : null;
   }

   @Override
   public void open(Player var1) {
      String invTitle = this.title;
      if (invTitle.length() >= 32) {
         invTitle = invTitle.substring(0, 29);
         invTitle = invTitle + "...";
      }
      int slots = (height*9==0 ? 9 :  height*9);
      //if (slots==0) slots=9;
      Inventory var3 = Bukkit.createInventory(var1, slots, invTitle);

      for(int var4 = 0; var4 < 9; ++var4) {
         for(int var5 = 0; var5 < this.height; ++var5) {
            if (this.items[var4][var5] != null) {
               var3.setItem(var5 * 9 + var4, this.items[var4][var5].getItemStack());
            } else {
               var3.setItem(var5 * 9 + var4, (ItemStack)null);
            }
         }
      }

      var1.openInventory(var3);
      openInventories.put(var1, this);
   }

   public void update() {
      Iterator var2 = this.getPlayersWhoUseThisGUI().iterator();

      while(var2.hasNext()) {
         Player var1 = (Player)var2.next();
         cachePlayers.add(var1);
         this.open(var1);
      }

   }

   public List getPlayersWhoUseThisGUI() {
      ArrayList var1 = new ArrayList();
      Iterator var3 = openInventories.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var2 = (Entry)var3.next();
         if (((SimpleGUI)var2.getValue()).equals(this)) {
            var1.add((Player)var2.getKey());
         }
      }

      return var1;
   }

   public void clear() {
      for(int var1 = 0; var1 < 9; ++var1) {
         for(int var2 = 0; var2 < this.height; ++var2) {
            this.items[var1][var2] = null;
         }
      }

      this.update();
   }

   public int getNextSpace() {
      return this.getNextSpace(0, 0);
   }

   public int getNextSpace(int var1) {
      int var2 = var1 / 9;
      int var3 = var1 - var2 * 9;
      return this.getNextSpace(var3, var2);
   }

   public int getNextSpace(int var1, int var2) {
      for(int var3 = var2; var3 < this.height; ++var3) {
         for(int var4 = var1; var4 < 9; ++var4) {
            if (this.items[var4][var3] == null || this.items[var4][var3].getItemStack().getType() == Material.AIR) {
               return var3 * 9 + var4;
            }
         }
      }

      return -1;
   }

   public void centerAtY(int var1, GUI115.CenterFormatType var2, int var3, int var4) {
      ArrayList var5 = new ArrayList();

      int var6;
      for(var6 = var3; var6 < var4; ++var6) {
         if (this.items[var6][var1] != null && this.items[var6][var1].getItemStack().getType() != Material.AIR) {
            var5.add(this.items[var6][var1]);
         }

         this.items[var6][var1] = null;
      }

      for(var6 = 0; var6 < var5.size(); ++var6) {
         GUIItem var7 = (GUIItem)var5.get(var6);
         int var8 = -1;
         if (var2 == GUI115.CenterFormatType.Normal) {
            var8 = GUI_Style.getSlotCenter_Normal(var6, var5.size(), var3, var4);
         } else if (var2 == GUI115.CenterFormatType.Beautiful) {
            var8 = GUI_Style.getSlotCenter_Beautiful(var6, var5.size(), var3, var4);
         }

         this.items[var8][var1] = var7;
      }

   }

   public void centerAtX(int var1, GUI115.CenterFormatType var2, int var3, int var4) {
      ArrayList var5 = new ArrayList();

      int var6;
      for(var6 = var3; var6 < var4; ++var6) {
         if (this.items[var1][var6] != null && this.items[var1][var6].getItemStack().getType() != Material.AIR) {
            var5.add(this.items[var1][var6]);
         }

         this.items[var1][var6] = null;
      }

      for(var6 = 0; var6 < var5.size(); ++var6) {
         GUIItem var7 = (GUIItem)var5.get(var6);
         int var8 = -1;
         if (var2 == GUI115.CenterFormatType.Normal) {
            var8 = GUI_Style.getSlotCenter_Normal(var6, var5.size(), var3, var4);
         } else if (var2 == GUI115.CenterFormatType.Beautiful) {
            var8 = GUI_Style.getSlotCenter_Beautiful(var6, var5.size(), var3, var4);
         }

         this.items[var1][var8] = var7;
      }

   }

   public void centerAtY(int var1, GUI115.CenterFormatType var2) {
      this.centerAtY(var1, var2, 0, 9);
   }

   public void centerAtYAll(GUI115.CenterFormatType var1, int var2, int var3) {
      for(int var4 = 0; var4 < this.getHeight(); ++var4) {
         this.centerAtY(var4, var1, var2, var3);
      }

   }

   public void centerAtYAll(GUI115.CenterFormatType var1) {
      for(int var2 = 0; var2 < this.getHeight(); ++var2) {
         this.centerAtY(var2, var1);
      }

   }

   public void centerAtX(int var1, GUI115.CenterFormatType var2) {
      this.centerAtX(var1, var2, 0, this.getHeight());
   }

   public void centerAtXAll(GUI115.CenterFormatType var1, int var2, int var3) {
      for(int var4 = 0; var4 < 9; ++var4) {
         this.centerAtX(var4, var1, var2, var3);
      }

   }

   public void centerAtXAll(GUI115.CenterFormatType var1) {
      for(int var2 = 0; var2 < 9; ++var2) {
         this.centerAtY(var2, var1);
      }

   }

   public int setBackground(GUIItem var1) {
      int var2 = 0;
      var1.gui = this;

      for(int var3 = 0; var3 < 9; ++var3) {
         for(int var4 = 0; var4 < this.height; ++var4) {
            if (this.items[var3][var4] == null || this.items[var3][var4].getItemStack().getType() == Material.AIR) {
               this.items[var3][var4] = var1;
               ++var2;
            }
         }
      }

      return var2;
   }

   public int fill(GUIItem var1) {
      for(int var2 = 0; var2 < 9; ++var2) {
         for(int var3 = 0; var3 < this.height; ++var3) {
            this.items[var2][var3] = var1;
         }
      }

      return this.height * 9;
   }

   public void onClose(Player var1) {
   }

   protected void onSetItem(int var1, int var2, GUIItem var3) {
   }

   public GUIItem[][] getItems() {
      return this.items;
   }

   public static class AddItemFlag {
      public static final int WITHIN_X = 0;
      public static final int WITHIN_Y = 1;
      public static final int WITHIN = 2;
      public static final int CENTERIZED_HORIZONTAL = 3;
      private int type;
      private Object[] param;

      public static GUI115.AddItemFlag createWithinX(int var0, int var1) {
         GUI115.AddItemFlag var2 = new GUI115.AddItemFlag();
         var2.type = 0;
         var2.param = new Object[2];
         var2.param[0] = var0;
         var2.param[1] = var1;
         return var2;
      }

      public static GUI115.AddItemFlag createWithinY(int var0, int var1) {
         GUI115.AddItemFlag var2 = new GUI115.AddItemFlag();
         var2.type = 1;
         var2.param = new Object[2];
         var2.param[0] = var0;
         var2.param[1] = var1;
         return var2;
      }

      public static GUI115.AddItemFlag createWithin(int var0, int var1, int var2, int var3) {
         GUI115.AddItemFlag var4 = new GUI115.AddItemFlag();
         var4.type = 2;
         var4.param = new Object[4];
         var4.param[0] = var0;
         var4.param[1] = var1;
         var4.param[2] = var2;
         var4.param[3] = var3;
         return var4;
      }

      public static GUI115.AddItemFlag createCenterizedHorizontal() {
         GUI115.AddItemFlag var0 = new GUI115.AddItemFlag();
         var0.type = 3;
         return var0;
      }

      public int getType() {
         return this.type;
      }
   }

   public static enum CenterFormatType {
      Normal,
      Beautiful;
   }
}
*/
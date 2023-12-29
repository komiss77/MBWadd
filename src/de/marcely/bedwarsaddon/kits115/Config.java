package de.marcely.bedwarsaddon.kits115;



public class Config {
/*
    public static void load() {
        
        BwAdd.kitConfig.load();
        
        if (BwAdd.kitConfig.getRootTree().getChilds().isEmpty()) {
            
            Kit kit = new Kit("Строитель");
            kit.setIcon(new ItemStack(Material.SANDSTONE));
            kit.addItem(new ItemStack(Material.SANDSTONE, 64));
            kit.addItem(new ItemStack(Material.APPLE, 5));
            kit.addItem(new ItemStack(Material.END_STONE, 10));
            BedwarsAddonKits.kits.add(kit);
            
            kit = new Kit("Минёр");
            kit.setIcon(new ItemStack(Material.STONE_PICKAXE));
            kit.addItem(new ItemStack(Material.STONE_PICKAXE));
            kit.addItem(new ItemStack(Material.LADDER, 10));
            kit.addItem(new ItemStack(Material.APPLE, 5));
            BedwarsAddonKits.kits.add(kit);
            
            kit = new Kit("Эндэр");
            kit.setIcon(new ItemStack(Material.ENDER_PEARL));
            kit.addItem(new ItemStack(Material.ENDER_PEARL, 1));
            BedwarsAddonKits.kits.add(kit);
            
            kit = new Kit("Рекрут");
            kit.setIcon(new ItemStack(Material.WOODEN_SWORD));
            kit.addItem(new ItemStack(Material.WOODEN_SWORD));
            kit.addItem(new ItemStack(Material.BREAD, 3));
            BedwarsAddonKits.kits.add(kit);
            
            kit = new Kit("Снеговик");
            kit.setIcon(new ItemStack(Material.PUMPKIN));
            kit.addItem(new ItemStack(Material.SNOWBALL, 10));
            kit.addItem(new ItemStack(Material.PUMPKIN, 1));
            kit.addItem(new ItemStack(Material.COOKIE, 4));
            BedwarsAddonKits.kits.add(kit);
            
            
            kit = new Kit("Фермер");
            kit.setIcon(new ItemStack(Material.WOODEN_HOE));
            kit.addItem(new ItemStack(Material.APPLE, 4));
            kit.addItem(new ItemStack(Material.BREAD, 5));
            kit.addItem(new ItemStack(Material.COD, 6));
            kit.addItem(new ItemStack(Material.COOKED_BEEF, 2));
            kit.addItem(new ItemStack(Material.WOODEN_HOE));
            BedwarsAddonKits.kits.add(kit);
            
            save();
            return;
        }
        
        
        
        
        final String version = BwAdd.kitConfig.getDescription("version");
        final String gui_title = BwAdd.kitConfig.getConfigString("gui-title");
        final Boolean permissions_enabled = BwAdd.kitConfig.getConfigBoolean("permissions-enabled");
        final String permissions_insufficient_item_name = BwAdd.kitConfig.getConfigString("permissions-insufficient-item-name");
        final String message_no_permissions = BwAdd.kitConfig.getConfigString("message-no-permissions");
        final String message_set_kit = BwAdd.kitConfig.getConfigString("message-set-kit");
        final String message_lore_items = BwAdd.kitConfig.getConfigString("message-lore-items");
        final String message_lore_items_item = BwAdd.kitConfig.getConfigString("message-lore-items-item");
        if (gui_title != null) {
            BedwarsAddonKits.kitsGUITitle = BwAdd.stringToChatColor(gui_title);
        }
        if (permissions_enabled != null) {
            BedwarsAddonKits.permissionsEnabled = permissions_enabled;
        }
        if (permissions_insufficient_item_name != null) {
            BedwarsAddonKits.permissionsMissingItemName = BwAdd.stringToChatColor(permissions_insufficient_item_name);
        }
        if (message_no_permissions != null) {
            BedwarsAddonKits.message_noPermissions = BwAdd.stringToChatColor(message_no_permissions);
        }
        if (message_set_kit != null) {
            BedwarsAddonKits.message_setKit = BwAdd.stringToChatColor(message_set_kit);
        }
        if (message_lore_items != null) {
            BedwarsAddonKits.message_loreItems = BwAdd.stringToChatColor(message_lore_items);
        }
        if (message_lore_items_item != null) {
            BedwarsAddonKits.message_loreItems = BwAdd.stringToChatColor(message_lore_items_item);
        }
        
        
        
        BedwarsAddonKits.kits.clear();
        
        for (final Tree t : BwAdd.kitConfig.getRootTree().getTreeChilds()) {
            Kit kit2 = AUtil.getKit(t.getName());
            if (kit2 == null) {
                kit2 = new Kit(t.getName());
                BedwarsAddonKits.kits.add(kit2);
            }
            
            Material mat;
            ItemStack is;
            for (final de.marcely.bedwars.libraries.configmanager2.objects.Config c : t.getChilds()) {
                if (c.getName().equalsIgnoreCase("seticon")) {
                    //final ItemStack is = Util.getItemItemstackByName(c.getValue());
                    is = AUtil.getItemStack(c.getValue());
                    if (is == null) {
                        Bukkit.getConsoleSender().sendMessage("§6BwAdd §cНабор "+kit2.getName()+" ошибка в строке "+c.getValue());
                        mat = Material.BEDROCK;
                    } else {
                        mat = is.getType();
                    }
                    is = new ItemBuilder(mat).name(kit2.getName()).build();
                    kit2.setIcon(is);
                    
                }
                else {
                    if (!c.getName().equalsIgnoreCase("additem")) {
                        continue;
                    }
                    //mat = Material.matchMaterial(c.getValue());
                    is = AUtil.getItemStack(c.getValue());
                    if (is != null) {
                        kit2.addItem(is);
                    } else {
                        Bukkit.getConsoleSender().sendMessage("§6BwAdd §cНабор "+kit2.getName()+" ошибка в строке "+c.getValue());
                    }
                    //is = new ItemBuilder(mat).build();
                    
                }
            }
        }
        BwAdd.kitConfig.clear();
        if (version == null || !version.equals(BwAdd.instance.getDescription().getVersion())) {
            save();
        }
        
        
    }
    
    public static void save() {
        BwAdd.kitConfig.clear();
        BwAdd.kitConfig.addDescription("version", BwAdd.instance.getDescription().getVersion());
        BwAdd.kitConfig.addComment("### Basic configurations:");
        BwAdd.kitConfig.addConfig("gui-title", (Object)BwAdd.chatColorToString(BedwarsAddonKits.kitsGUITitle));
        BwAdd.kitConfig.addComment("Permissions are formatted like this: ");
        BwAdd.kitConfig.addComment("mbedwars.addon.kits.<kit name>");
        BwAdd.kitConfig.addComment("Example: mbedwars.addon.kits.Miner");
        BwAdd.kitConfig.addConfig("permissions-enabled", (Object)BedwarsAddonKits.permissionsEnabled);
        BwAdd.kitConfig.addComment("The name of the item which'll be displayed if the player has insufficient permissions");
        BwAdd.kitConfig.addConfig("permissions-insufficient-item-name", (Object)BwAdd.chatColorToString(BedwarsAddonKits.permissionsMissingItemName));
        BwAdd.kitConfig.addEmptyLine();
        BwAdd.kitConfig.addEmptyLine();
        BwAdd.kitConfig.addComment("### Messages");
        BwAdd.kitConfig.addConfig("message-message-no-permissions", (Object)BwAdd.chatColorToString(BedwarsAddonKits.message_noPermissions));
        BwAdd.kitConfig.addConfig("message-set-kit", (Object)BwAdd.chatColorToString(BedwarsAddonKits.message_setKit));
        BwAdd.kitConfig.addEmptyLine();
        BwAdd.kitConfig.addEmptyLine();
        BwAdd.kitConfig.addComment("### Kits:");
        for (final Kit kit : BedwarsAddonKits.kits) {
            BwAdd.kitConfig.addConfig(kit.getName() + ".setIcon", AUtil.toString(kit.getIcon()));
            for (final ItemStack is : kit.getItems()) {
                BwAdd.kitConfig.addConfig(kit.getName() + ".addItem", AUtil.toString(is));
            }
            BwAdd.kitConfig.addEmptyLine();
        }
        BwAdd.kitConfig.save();
    }
*/
}

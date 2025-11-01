package com.progiple.satellitedev.lightkits.kits;

import com.progiple.satellitedev.lightkits.LightKits;
import com.progiple.satellitedev.lightkits.configs.Config;
import com.progiple.satellitedev.lightkits.configs.PlayersConfig;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.novasparkle.lunaspring.API.configuration.Configuration;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter @Accessors(fluent = true)
public class Kit {
    private final Configuration config;
    private final String id;
    private final String name;
    private final long cooldown;
    private final boolean visible;
    private final KitVisuals visuals;
    private final ItemStack[] items = new ItemStack[45];
    private final String permission;

    private boolean enabled = true;
    public Kit(String id) {
        this.id = id;
        this.config = new Configuration(
                LightKits.getINSTANCE().getDataFolder(),
                "kits/" + id);
        this.name = "Default Name";
        this.cooldown = -1;
        this.visible = false;
        this.visuals = new KitVisuals(-1, 0, Material.AIR, "Example Lore Line", "");
        this.permission = "lightkits.kit." + this.name;

        this.config.setString("name", this.name);
        this.config.setLong("cooldown", this.cooldown);
        this.config.setBoolean("enabled", this.enabled);
        this.config.setBoolean("visible", this.visible);

        String visualPath = "visuals.";
        this.config.setInt(visualPath + "slot", this.visuals.slot());
        this.config.setInt(visualPath + "page", this.visuals.page());
        this.config.setString(visualPath + "material", this.visuals.material().name());
        this.config.setString(visualPath + "loreLine", this.visuals.loreLine());

        this.config.setString("permission", this.permission);
        this.config.createSection((String) null, "items");
        this.config.save();
    }

    public Kit(File file) {
        this.config = new Configuration(file);
        this.id = file.getName().replace(".yml", "");
        this.name = ColorManager.color(this.config.getString("name"));
        this.cooldown = this.config.getLong("cooldown");

        ConfigurationSection visualSection = this.config.getSection("visuals");
        this.visuals = new KitVisuals(
                visualSection.getInt("slot"),
                visualSection.getInt("page"),
                Utils.getMaterial(visualSection.getString("material")),
                ColorManager.color(visualSection.getString("loreLine")),
                visualSection.getString("baseHead"));
        this.visible = this.visuals.page() > 0 &&
                            this.visuals.slot() >= 0 &&
                                this.visuals.material() != null &&
                                        !this.visuals.material().isAir() &&
                                            this.config.getBoolean("visible");
        this.permission = this.config.getString("permission");
        this.enabled = this.config.getBoolean("enabled");

        ConfigurationSection itemSection = this.config.getSection("items");
        for (int i = 0; i < this.items.length; i++) {
            this.items[i] = itemSection.getItemStack(String.valueOf(i));
        }
    }

    public void switchStatus(boolean status) {
        String id = status ? "Disable" : "Enable";
        List<String> list = Config.getList("not" + id + "Kits");
        if (list.contains(this.id)) { return; }

        this.enabled = status;
        this.config.setBoolean("enabled", this.enabled);
        this.config.save();
    }

    public void uploadItems(Inventory inventory) {
        int size = inventory.getSize();
        for (int i = 0; i < this.items.length; i++) {
            ItemStack itemStack = i >= size ? null : inventory.getItem(i);
            this.items[i] = itemStack == null ? null : itemStack.clone();

            this.config.setItemStack("items." + i, this.items[i]);
        }
        this.config.save();
    }

    public void downloadItems(Inventory inventory) {
        int size = inventory.getSize();
        for (int i = 0; i < Math.min(size, this.items.length); i++) {
            inventory.setItem(i, this.items[i]);
        }
    }

    public void give(Player player) {
        List<ItemStack> itemStacks = Arrays.stream(this.items).filter(Objects::nonNull).toList();
        Utils.Items.give(player, itemStacks, false);
    }

    public long getExpiredTime(Player player) {
        long lastClaimed = PlayersConfig.getLastClaimed(player.getName(), this);
        return lastClaimed + this.cooldown * 1000L;
    }

    public boolean hasCooldown(Player player) {
        return !player.hasPermission("lightkits.bypass") && System.currentTimeMillis() < getExpiredTime(player);
    }

    public void claim(Player player) {
        this.give(player);
        PlayersConfig.setLastClaimed(player.getName(), this);
    }

    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("lightkits.kit.*") || sender.hasPermission(permission);
    }

    public boolean remove() {
        return this.config.getFile().delete();
    }
}

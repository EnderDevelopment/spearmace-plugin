package com.enderdevelopment.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public final class Main extends JavaPlugin implements Listener {
    private NamespacedKey weaponKey;
    private NamespacedKey abilityKey;
    private final Map<UUID, Long> abilityCooldowns = new HashMap<>();

    @Override
    public void onEnable() {
        weaponKey = new NamespacedKey(this, "spear_mace");
        abilityKey = new NamespacedKey(this, "wind_launch");
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        abilityCooldowns.clear();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("spearmace")) return false;
        if (!(sender instanceof org.bukkit.entity.Player playerSender && playerSender.isOp())
                && !(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
            sender.sendMessage(Component.text("You must be an operator to obtain the SpearMace.", NamedTextColor.RED));
            return true;
        }
        if (args.length < 1 || !args[0].equalsIgnoreCase("give")) {
            sender.sendMessage(Component.text("Usage: /spearmace give [player]", NamedTextColor.YELLOW));
            return true;
        }
        Player target;
        if (args.length >= 2) {
            target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage(Component.text("That player is not online.", NamedTextColor.RED));
                return true;
            }
        } else if (sender instanceof Player player) {
            target = player;
        } else {
            sender.sendMessage(Component.text("Specify an online player.", NamedTextColor.RED));
            return true;
        }
        target.getInventory().addItem(createWeapon()).values().forEach(item ->
                target.getWorld().dropItemNaturally(target.getLocation(), item));
        sender.sendMessage(Component.text("Gave a SpearMace to " + target.getName() + ".", NamedTextColor.GREEN));
        return true;
    }

    private ItemStack createWeapon() {
        ItemStack item = new ItemStack(Material.MACE);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        meta.displayName(Component.text("SpearMace", NamedTextColor.BLUE));
        meta.lore(java.util.List.of(
                Component.text("Breeze-forged spear with a mace head", NamedTextColor.DARK_BLUE),
                Component.text("Swap hands or right-click to launch upward", NamedTextColor.GRAY)));
        meta.getPersistentDataContainer().set(weaponKey, PersistentDataType.BYTE, (byte) 1);
        addEnchantment(meta, "wind_burst", 3);
        addEnchantment(meta, "lunge", 3);
        addEnchantment(meta, "mending", 1);
        addEnchantment(meta, "unbreaking", 6);
        item.setItemMeta(meta);
        return item;
    }

    private void addEnchantment(ItemMeta meta, String key, int level) {
        Enchantment enchantment = Registry.ENCHANTMENT.get(NamespacedKey.minecraft(key));
        if (enchantment != null) meta.addEnchant(enchantment, level, true);
    }

    private boolean isSpearMace(ItemStack item) {
        if (item == null || item.getType() != Material.MACE) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer().has(weaponKey, PersistentDataType.BYTE);
    }

    @EventHandler
    public void onSwapHands(PlayerSwapHandItemsEvent event) {
        if (isSpearMace(event.getMainHandItem())) {
            event.setCancelled(true);
            launch(event.getPlayer());
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND || !isSpearMace(event.getItem())) return;
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) launch(event.getPlayer());
    }

    private void launch(Player player) {
        long now = System.currentTimeMillis();
        long readyAt = abilityCooldowns.getOrDefault(player.getUniqueId(), 0L);
        if (now < readyAt) {
            player.sendActionBar(Component.text("Wind ability ready in " + ((readyAt - now + 999) / 1000) + "s", NamedTextColor.GRAY));
            return;
        }
        Vector velocity = player.getVelocity();
        velocity.setY(1.25);
        player.setVelocity(velocity);
        player.setFallDistance(0);
        abilityCooldowns.put(player.getUniqueId(), now + 5000L);
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_TRIDENT_RIPTIDE_3, 0.8f, 1.4f);
    }

    @EventHandler
    public void onSpearHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player) || !isSpearMace(player.getInventory().getItemInMainHand())) return;
        Entity target = event.getEntity();
        double speed = player.getVelocity().length();
        double speedBonus = Math.min(1.5, Math.max(0.0, speed - 0.1) * 0.75);
        double fallBonus = Math.min(8.0, Math.max(0.0, player.getFallDistance() - 1.5) * 0.65);
        event.setDamage(event.getDamage() + speedBonus + fallBonus);
        if (fallBonus > 0.5) {
            player.setFallDistance(0);
            target.getWorld().playSound(target.getLocation(), Sound.ITEM_MACE_SMASH_GROUND, 0.8f, 1.0f);
        }
    }
}

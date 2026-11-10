# Minecraft Plugin: SpearMace

SpearMace is a Minecraft combat plugin that adds a custom mace-and-spear weapon with a blue spear-style identity, wind launch ability, speed-based damage, fall-based mace damage, custom enchantments, and operator-controlled distribution.

## Install

1. Build the plugin with Java 21 and Maven:
   ```bash
   mvn package
   ```
2. Copy the generated `target/plugin.jar` file into your server's `plugins` folder.
3. Start or restart a Paper server running Minecraft 1.21.1 or a compatible 1.21 version.
4. Confirm that the plugin loads successfully in the server console.

Requirements:

- Paper 1.21.1 or a compatible Paper 1.21 server
- Java 21
- Operator access or the `spearmace.give` permission

## Use

1. Give yourself the weapon:
   ```text
   /spearmace give
   ```
2. Give it to another online player:
   ```text
   /spearmace give <player>
   ```
3. Hold the SpearMace in your main hand.
4. Right-click in the air or on a block to activate the wind launch ability.
5. The ability launches the player upward, resets fall distance, plays a wind sound, and has a five-second cooldown.
6. Swapping the main-hand item with the off-hand item also activates the launch ability.
7. Attack an entity while holding the SpearMace to apply its enhanced damage system:
   - Movement speed adds bonus damage.
   - Falling distance adds mace-style bonus damage.
   - A successful falling attack resets the player's fall distance and plays a mace impact sound.
8. The weapon can be obtained only through the `/spearmace give` command. It has no crafting recipe.

The weapon is a MACE item marked with plugin data and includes the configured Wind Burst III, Lunge III, Mending I, and Unbreaking VI enchantments when those enchantments are available on the server.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/ad312c38-09b6-4c3b-9f8f-1c5577a19136?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=ad312c38-09b6-4c3b-9f8f-1c5577a19136

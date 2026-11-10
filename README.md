# Minecraft Plugin: SpearMace

SpearMace is a Minecraft plugin that adds a custom blue and dark blue mace weapon designed as a spear and mace combination, with a wind launch ability, fall damage bonuses, speed-based damage, custom enchantments, and operator-controlled item distribution.

## Install

1. Build the project with Java 21 and Maven:

   ```text
   mvn package
   ```

2. Copy the generated `target/plugin.jar` file into your server's `plugins` folder.

3. Start or restart a Paper Minecraft 1.21.1 server.

4. Ensure the server is running Java 21 or newer.

5. Give yourself the weapon with:

   ```text
   /spearmace give
   ```

   Operators can give it to another online player with:

   ```text
   /spearmace give <player>
   ```

## Use

1. Hold the SpearMace in your main hand.

2. Activate the wind launch ability by right-clicking the air or a block, or by pressing the swap-hands key while holding the weapon. The default swap-hands key is F. The plugin does not register a separate G key.

3. The launch ability sends the player upward with a strong vertical boost, resets fall distance, plays a wind-style sound, and has a five-second cooldown.

4. Attack entities with the SpearMace to use its damage system. Additional damage is calculated from the player's movement speed and current fall distance.

5. Falling attacks receive extra damage based on fall distance. Faster attacks receive an additional speed-based damage bonus.

6. The weapon has no crafting recipe. Operators or the console must distribute it with the `/spearmace give` command.

7. The weapon is marked internally as a SpearMace so that its abilities only work with this custom item.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/ad312c38-09b6-4c3b-9f8f-1c5577a19136?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=ad312c38-09b6-4c3b-9f8f-1c5577a19136

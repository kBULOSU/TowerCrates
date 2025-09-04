# TowerCrates
📜 Overview
TowerCrates is a Minecraft plugin developed as a commission. This plugin provides a highly customizable crate system, allowing server administrators to create loot crates that can be opened with specific keys. The project is no longer maintained.

✨ Features
Customizable Crates: Define multiple types of crates, each with its own set of items, display names, and rewards.

Weighted Rewards: Assign chances (weights) to each item within a crate to control the rarity of rewards.

Physical Representation: Crates are represented in-game by a spinning player head (Armor Stand) and a hologram displaying information.

Custom Keys: Create unique keys for each crate type with custom display names and lore.

Reward Commands: Execute commands as rewards, in addition to giving items.

Preview Inventory: Players can left-click a crate to see a preview of its possible rewards and their chances.

Database Integration: Crate locations are stored in a MySQL database.

⚙️ Commands
The main command is /crate. All subcommands require the permission crates.admin.

/crate givekey <player> <crate> <amount>: Gives a specified amount of keys for a specific crate to a player.

/crate keyall <crate> <amount>: Gives a specified amount of keys for a crate to all online players.

/crate setheadpos <crate>: Sets the location of the crate's armor stand to the player's current location.

📦 Dependencies
API-Spigot: A core dependency for the plugin's functionality.

HolographicDisplays: Used to display holograms above the crates.

⚠️ Disclaimer
This project was a commission and is no longer actively maintained. The code is provided as-is, and there is no guarantee of future updates or support.

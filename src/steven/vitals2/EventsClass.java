package steven.vitals2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.lang.Math;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EventsClass implements Listener {

	// get config
	private Main plugin;

	public EventsClass(Main pl) {
		plugin = pl;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!(plugin.cfgm.getPlayer().contains(player.getUniqueId().toString()))) {
			List<String> guildRegions = plugin.getConfig().getStringList("guild_regions");
			for (String s : guildRegions) {
				String name = cfgGuild(s);
				plugin.cfgm.getPlayer().set(player.getUniqueId().toString() + "." + name, 0);
			}
		}
		plugin.cfgm.savePlayers();
	}

	@EventHandler
	public void onPlayerClickyEvent(PlayerInteractEntityEvent event) {
		Entity entity = event.getRightClicked();
		Player player = event.getPlayer();
		String guild = plugin.isInGuild(player);
		
		if (!guild.equals("Unknown")) {
			if (guild.equals("brewingguildzone")) {
				if (entity instanceof Witch) {
					Inv I = new Inv();

					I.GShopInventory(player, guild);

					return;
				}
			}
			else {
				if (entity instanceof Villager) {
					Inv I = new Inv();

					I.GShopInventory(player, guild);

					return;
				}
			}
		}
		else
			return;
	}

	@EventHandler
	public void InvClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();

		String guild = plugin.isInGuild(player);
		String name = readableGuild(guild);
		Material clickable = zoneItem(guild);

		Inventory open = event.getInventory();
		ItemStack item = event.getCurrentItem();

		if (open.getName().equals(
				ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', name)))) {
			if (item != null && item.getType() != Material.AIR && item.hasItemMeta()) {
				if (item.getType() == Material.STAINED_GLASS_PANE) {
					event.setCancelled(true);
					return;
				}
				else if (item.getType() == clickable && item.getAmount() == 1) {
					if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Tribute")) {
						event.setCancelled(true);
						player.sendMessage(ChatColor.GOLD + "You have paid tribute to " + ChatColor.translateAlternateColorCodes('&', name));
						Tribute(player, guild);
					}
					else {
						return;
					}
				}
				else {
					return;
				}
			} else
				return;
		}
		else if (open.getName().equals(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', name)) + " Shop")) {
			if (item != null && item.getType() != Material.AIR && item.hasItemMeta()) {
				if (item.getType() == Material.STAINED_GLASS_PANE) {
					event.setCancelled(true);
					return;
				}
				else if (item.getType() == clickable && item.getAmount() == 1) {
					if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Test")) {
						event.setCancelled(true);
						player.sendMessage(ChatColor.GOLD + "You now have this item");
						Main.economy.withdrawPlayer(player, 200);
						player.getInventory().addItem(new ItemStack(clickable, 1));
					}
					else {
						return;
					}
				}
				else {
					return;
				}
			}
			else
				return;
		}
		else
			return;
	}

	@EventHandler
	public void autoCook(PlayerFishEvent event) {
		Player player = event.getPlayer();
		Random randomNum = new Random();
		int fishProb = randomNum.nextInt(100) + 1;
		int rank = plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Fishing");

		if (rank == 30) {
			if (event.getState() == State.CAUGHT_FISH) {
				if (fishProb <= 60) {
					ItemStack cookedFish = new ItemStack(Material.COOKED_FISH, 1);
					event.setCancelled(true);
					player.getInventory().addItem(cookedFish);
					event.getHook().remove();
				}
				else if (fishProb <= 76 && !(fishProb <= 60)) {
					ItemStack cookedFish = new ItemStack(Material.COOKED_FISH, 1, (byte) 1);
					event.setCancelled(true);
					player.getInventory().addItem(cookedFish);
					event.getHook().remove();
				}
				else
					return;
			}
		}
		else
			return;
	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void fishBait(PlayerFishEvent event) {
		Player player = event.getPlayer();
		Material bait = Material.getMaterial(plugin.getConfig().getString("bait_item"));
		int count = -1;
		boolean itemfound = false;

		if (plugin.getConfig().getBoolean("Fishing_bait")) {
			if (event.getState() == State.FISHING) {
				if (player.getInventory().contains(bait)) {
					for (ItemStack item : player.getInventory().getContents()) {
						if (item.getType().equals(bait)) {
							count = item.getAmount();
							itemfound = true;
							break;
						}
					}
				}
				else {
					player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You cannot cast your line without bait!");
					event.setCancelled(true);
					return;
				}

				if (itemfound) {
					if (count > 1) {
						count--;
						player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC
								+ "You cast your line out. You now have " + count + " bait left.");
						for (ItemStack item : player.getInventory().getContents()) {
							if (item.getType().equals(bait)) {
								item.setAmount(count);
								return;
							}
						}
					}
					else if (count == 1) {
						count--;
						player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You cast your line out. You have no more bait!");
						for (ItemStack item : player.getInventory().getContents()) {
							if (item.getType().equals(bait)) {
								item.setAmount(count);
								return;
							}
						}
					}
				}
			} 
			else
				return;
		} 
		else
			return;
	}
	
	private void Tribute(Player player, String guild) {
		int totalrank = rankCount(player);
		int numof = guildCount(player);
		int nummax = maxCount(player);
		int currentRank = plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + guild);
		int cost = tributeCost(totalrank, numof, nummax, currentRank);
		player.sendMessage("Test:" + cost);
		
		

		return;
	}

	private int maxCount(Player player) {
		List<Integer> Guilds = new ArrayList<Integer>();
		int count = 0;
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Brewing"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Enchanting"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Farming"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Fishing"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Mining"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Rancher"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Slayer"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Woodcutting"));

		for (int i : Guilds) {
			if (i == 30) {
				count++;
			}
		}
		return count;
	}

	private int guildCount(Player player) {
		List<Integer> Guilds = new ArrayList<Integer>();
		int count = 0;
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Brewing"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Enchanting"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Farming"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Fishing"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Mining"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Rancher"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Slayer"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Woodcutting"));

		for (int i : Guilds) {
			if (!(i == 0)) {
				count++;
			}
		}
		return count;
	}

	private int tributeCost(int totalrank, int guildcount, int maxcount, int currentRank) {
		List<Integer> price = new ArrayList<Integer>();
		int cost = 0;
		int mod = plugin.getConfig().getInt("monopoly_modifier");
		for (int i=0; i == currentRank; i++) {
			cost = price.get(i);
		}
		
		if ((currentRank + 1) == 30) {
			return (int) (totalrank*(Math.pow(guildcount, 1.2))*cost+(mod*maxcount));
		}
		else {
			return (int) (totalrank*(Math.pow(guildcount, 1.2))*cost);
		}
		/*
		 if (nextrank == max)
		 	totalguildrank*numofguild^1.2*tribute*(50000*nummaxlvl)
		 else
		 	totalguildrank*numofguild^1.2*tribute
		 */
	}

	private int rankCount(Player player) {
		List<Integer> Guilds = new ArrayList<Integer>();
		int count = 0;
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Brewing"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Enchanting"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Farming"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Fishing"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Mining"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Rancher"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Slayer"));
		Guilds.add(plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + "Woodcutting"));

		for (int i : Guilds) {
			count += i;
		}
		return count;
	}

	private String readableGuild(String guild) {
		switch (guild) {
		case "brewingguildzone":
			return "&5&l&nBrewing &5&l&nGuild";
		case "enchantguildzone":
			return "&d&l&nEnchanting &d&l&nGuild";
		case "farmingguildzone":
			return "&2&l&nFarming &2&l&nGuild";
		case "fishingguildzone":
			return "&b&l&nFishing &b&l&nGuild";
		case "miningguildzone":
			return "&7&l&nMining &7&l&nGuild";
		case "rancherguildzone":
			return "&4&l&nRancher &4&l&nGuild";
		case "slayerguildzone":
			return "&8&l&nSlayer &8&l&nGuild";
		case "wcguildzone":
			return "&a&l&nWoodcutting &a&l&nGuild";
		default:
			return "Unknown";
		}
	}

	private Material zoneItem(String guild) {
		switch (guild) {
		case "brewingguildzone":
			return Material.BREWING_STAND_ITEM;
		case "enchantguildzone":
			return Material.ENCHANTMENT_TABLE;
		case "farmingguildzone":
			return Material.WHEAT;
		case "fishingguildzone":
			return Material.RAW_FISH;
		case "miningguildzone":
			return Material.DIAMOND_PICKAXE;
		case "rancherguildzone":
			return Material.RAW_BEEF;
		case "slayerguildzone":
			return Material.ROTTEN_FLESH;
		case "wcguildzone":
			return Material.DIAMOND_AXE;
		default:
			return Material.STICK;
		}
	}

	private String cfgGuild(String guild) {
		switch (guild) {
		case "brewingguildzone":
			return "Brewing";
		case "enchantguildzone":
			return "Enchanting";
		case "farmingguildzone":
			return "Farming";
		case "fishingguildzone":
			return "Fishing";
		case "miningguildzone":
			return "Mining";
		case "rancherguildzone":
			return "Rancher";
		case "slayerguildzone":
			return "Slayer";
		case "wcguildzone":
			return "Woodcutting";
		default:
			return "Unknown";
		}
	}
}

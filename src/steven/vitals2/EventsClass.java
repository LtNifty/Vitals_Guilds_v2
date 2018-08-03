package steven.vitals2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
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
		if (!(plugin.cfgm.getPlayers().contains(player.getUniqueId().toString()))) {
			List<String> guildRegions = plugin.getConfig().getStringList("guild_regions");
			for (String s : guildRegions) {
				String name = cfgGuild(s);
				plugin.cfgm.getPlayers().set(player.getUniqueId().toString() + "." + name, 0);
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
				if (entity instanceof Villager) {
					Inv I = new Inv();
					event.setCancelled(true);
					I.GShopInventory(player, guild);

					return;
				}
			}
			else if (guild.equals("enchantguildzone")){
				if (entity instanceof Villager) {
					Inv I = new Inv();

					I.GShopInventory(player, guild);

					return;
				}
			}
			else if (guild.equals("farmingguildzone")){
				if (entity instanceof Villager) {
					Inv I = new Inv();

					I.GShopInventory(player, guild);

					return;
				}
			}
			else if (guild.equals("fishingguildzone")){
				if (entity instanceof Villager) {
					Inv I = new Inv();

					I.GShopInventory(player, guild);

					return;
				}
			}
			else if (guild.equals("miningguildzone")){
				if (entity instanceof Villager) {
					Inv I = new Inv();

					I.GShopInventory(player, guild);

					return;
				}
			}
			else if (guild.equals("rancherguildzone")){
				if (entity instanceof Villager) {
					Inv I = new Inv();

					I.GShopInventory(player, guild);

					return;
				}
			}
			else if (guild.equals("slayerguildzone")){
				if (entity instanceof Villager) {
					Inv I = new Inv();

					I.GShopInventory(player, guild);

					return;
				}
			}
			else if (guild.equals("wcguildzone")){
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

		if (open.getName().equals(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', name)))) {
			if (item != null && item.getType() != Material.AIR && item.hasItemMeta()) {
				if (item.getType() == Material.STAINED_GLASS_PANE) {
					event.setCancelled(true);
					return;
				}
				else if (item.getType() == clickable || item.getType() == Material.WRITTEN_BOOK && item.getAmount() == 1) {
					if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Tribute")) {
						event.setCancelled(true);
						Tribute(player, guild);
						nextRank(player, guild);
					}
					else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Next rank information")) {
						event.setCancelled(true);
						nextRank(player, guild);
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
						plugin.economy.withdrawPlayer(player, 200);
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
		int rank = plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Fishing");

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
		int count = 0, slot = 0, total = 0;
		boolean bothSides = false;
		
		if (plugin.getConfig().getBoolean("Fishing_bait")) {
			if (event.getState() == State.FISHING) {
				for (int i = 0; i < 9; i++) {
					if (player.getInventory().getItem(i) != null && player.getInventory().getItem(i).getType() == Material.FISHING_ROD) {
						slot = i;
						break;
					}
				}
				if ((slot+1) < 9 && player.getInventory().getItem(slot+1) != null && player.getInventory().getItem(slot+1).getType() == bait) {
					count = player.getInventory().getItem(slot+1).getAmount();
					if ((slot-1) >= 0 && player.getInventory().getItem(slot-1) != null && player.getInventory().getItem(slot-1).getType() == bait) {
						total = count + player.getInventory().getItem(slot-1).getAmount() - 1;
						bothSides = true;
					}
					if (total == 0)
						total = count - 1;
					takeBait(slot+1, count, total, player, bait, bothSides);
					return;
				}
				else if ((slot-1) >= 0 && player.getInventory().getItem(slot-1) != null && player.getInventory().getItem(slot-1).getType() == bait) {
					count = player.getInventory().getItem(slot-1).getAmount();
					total = count - 1;
					takeBait(slot-1, count, total, player, bait, false);
					return;
				}
				else {
					if (player.getInventory().contains(bait)) {
						player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You must have the bait next to your fishing rod on your hotbar.");
						event.setCancelled(true);
						return;
					}
					else
						player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You must have bait to fish.");
					event.setCancelled(true);
					return;
				}
			}
		}
		else
			return;

		/*if (plugin.getConfig().getBoolean("Fishing_bait")) {
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
						player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You cast your line out. You now have " + count + " bait left.");
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
			return;*/
	}
	
	private void takeBait(int i, int count, int total, Player player, Material bait, boolean bothSides) {
		if (count > 1) {
			count--;
			player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You cast your line out. You now have " + total + " bait left.");
			player.getInventory().getItem(i).setAmount(count);
		}
		else if (count == 1) {
			count--;
			if (bothSides) {
				player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You cast your line out. You now have " + player.getInventory().getItem(i-2).getAmount() + " bait left.");
				player.getInventory().getItem(i).setAmount(count);
			}
			else {
				player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You cast your line out. You have no more bait!");
				player.getInventory().getItem(i).setAmount(count);
			}
		}
	}
	
	private void nextRank(Player player, String guildzone) {
		String guild = cfgGuild(guildzone);
		int currentRank = plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + guild);
		String item = cfgItem(guild);
		
		if (currentRank == 30) {
			player.sendMessage(ChatColor.GOLD + "You are max level! You cannot pay any more tribute!");
			return;
		}
		
		List<String> materialList = plugin.cfgm.getGuildItems().getStringList(guild);
		List<Integer> amountList = plugin.cfgm.getGuildItems().getIntegerList(item);
		Material material = Material.getMaterial(materialList.get(currentRank));
		int amount = amountList.get(currentRank);
		int totalrank = rankCount(player);
		int numof = guildCount(player);
		int nummax = maxCount(player);
		int cost = tributeCost(totalrank, numof, nummax, currentRank);
		
		player.sendMessage(ChatColor.GOLD + "For you next rank in " + guild + " you need: $" + cost + " and " + material.toString() + " x" + amount);
	}

	private String cfgItem(String guild) {
		switch (guild) {
			case "Brewing":
				return "Brewing_Amount";
			case "Enchanting":
				return "Enchanting_Amount";
			case "Farming":
				return "Farming_Amount";
			case "Fishing":
				return "Fishing_Amount";
			case "Mining":
				return "Mining_Amount";
			case "Rancher":
				return "Rancher_Amount";
			case "Slayer":
				return "Slayer_Amount";
			case "Woodcutting":
				return "Woodcutting_Amount";
		}
		return null;
	}

	private void Tribute(Player player, String guildzone) {
		String guild = cfgGuild(guildzone);
		int currentRank = plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + guild);
		String item = cfgItem(guild);
		
		if (currentRank == 30) {
			player.sendMessage(ChatColor.GOLD + "You are max level! You cannot pay any more tribute!");
			return;
		}
		
		int totalrank = rankCount(player);
		int numof = guildCount(player);
		int nummax = maxCount(player);
		int cost = tributeCost(totalrank, numof, nummax, currentRank);
		double balance = plugin.economy.getBalance(player);
		boolean hasItem = false;
		List<String> materialList = plugin.cfgm.getGuildItems().getStringList(guild);
		List<Integer> amountList = plugin.cfgm.getGuildItems().getIntegerList(item);
		Material material = Material.getMaterial(materialList.get(currentRank));
		int amount = amountList.get(currentRank);
		
		for (ItemStack i : player.getInventory().getContents()) {
			if (i != null && i.getType() == material && i.getAmount() >= amount) {
				hasItem = true;
				break;
			}
		}
		
		if (hasItem && balance >= cost) {
			player.sendMessage(ChatColor.GOLD + "Rank up successful! You are now a level " + (currentRank + 1) + " " + guild + "!");
			plugin.economy.withdrawPlayer(player, cost);
			plugin.cfgm.getPlayers().set(player.getUniqueId().toString() + "." + guild, (currentRank + 1));
			plugin.cfgm.savePlayers();
			for (ItemStack i : player.getInventory().getContents()) {
				if (i != null && i.getType() == material) {
					i.setAmount(i.getAmount()-amount);
					break;
				}
			}
			return;
		}
		else {
			player.sendMessage(ChatColor.GOLD + "Failed to rank up! Are you sure you have everything?");
			return;
		}
	}

	private int maxCount(Player player) {
		List<Integer> Guilds = new ArrayList<Integer>();
		int count = 0;
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Brewing"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Enchanting"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Farming"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Fishing"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Mining"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Rancher"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Slayer"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Woodcutting"));

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
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Brewing"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Enchanting"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Farming"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Fishing"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Mining"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Rancher"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Slayer"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Woodcutting"));

		for (int i : Guilds) {
			if (!(i == 0)) {
				count++;
			}
		}
		return count;
	}

	private int tributeCost(int totalrank, int guildcount, int maxcount, int currentRank) {
		List<Integer> price = plugin.getConfig().getIntegerList("Price_base");
		int mod = plugin.getConfig().getInt("monopoly_modifier");
		int cost = price.get(currentRank);
		
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
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Brewing"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Enchanting"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Farming"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Fishing"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Mining"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Rancher"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Slayer"));
		Guilds.add(plugin.cfgm.getPlayers().getInt(player.getUniqueId().toString() + "." + "Woodcutting"));

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

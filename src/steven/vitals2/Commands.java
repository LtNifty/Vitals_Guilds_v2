package steven.vitals2;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import net.minecraft.server.v1_12_R1.CommandExecute;

public class Commands extends CommandExecute implements Listener, CommandExecutor {
	
	//get config
	private Main plugin;
	
	public Commands(Main pl) {
		plugin = pl;
	}
	
	public String cmd1 = "guild";
	public String cmd2 = "guildstats";
	public String cmd3 = "reloadguilds";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			if (cmd.getName().equalsIgnoreCase(cmd1)) {
				if (args.length == 0) {
					String guild = plugin.isInGuild((Player) sender);
					if (!guild.equals("Unknown")) {
						Inv I = new Inv();
						
						I.GuildInventory((Player) sender, guild);
						
						return true;
					}
					else {
						sender.sendMessage(ChatColor.GOLD + "Guild help:");
						return true;
					}
				}
			}
			else if (cmd.getName().equalsIgnoreCase(cmd2)) {
				printStats((Player) sender);
				return true;
			}
			else if (cmd.getName().equalsIgnoreCase(cmd3)) {
				plugin.cfgm.reloadPlayers();
				return true;
			}
		}
		else {
			sender.sendMessage(ChatColor.RED + "Only players can use this command.");
			return true;
		}
		return false;		
	}

	private void printStats(Player player) {
		player.sendMessage(ChatColor.GRAY + "-----------------------------");
		player.sendMessage(ChatColor.BLUE + player.getDisplayName() + ChatColor.BLUE + "'s Guild Stats");
		player.sendMessage(ChatColor.DARK_PURPLE + "Brewing Guild" + ChatColor.WHITE + ": " + ChatColor.YELLOW + "Lvl " + ChatColor.translateAlternateColorCodes('&', lvlString(player, "Brewing")));
		player.sendMessage(ChatColor.DARK_GREEN + "Farming Guild" + ChatColor.WHITE + ": " + ChatColor.YELLOW + "Lvl " + ChatColor.translateAlternateColorCodes('&', lvlString(player, "Farming")));
		player.sendMessage(ChatColor.GREEN + "Woodcutting Guild" + ChatColor.WHITE + ": " + ChatColor.YELLOW + "Lvl " + ChatColor.translateAlternateColorCodes('&', lvlString(player, "Woodcutting")));
		player.sendMessage(ChatColor.RED + "Rancher Guild" + ChatColor.WHITE + ": " + ChatColor.YELLOW + "Lvl " + ChatColor.translateAlternateColorCodes('&', lvlString(player, "Rancher")));
		player.sendMessage(ChatColor.LIGHT_PURPLE + "Enchanting Guild" + ChatColor.WHITE + ": " + ChatColor.YELLOW + "Lvl " + ChatColor.translateAlternateColorCodes('&', lvlString(player, "Enchanting")));
		player.sendMessage(ChatColor.AQUA + "Fishing Guild" + ChatColor.WHITE + ": " + ChatColor.YELLOW + "Lvl " + ChatColor.translateAlternateColorCodes('&', lvlString(player, "Fishing")));
		player.sendMessage(ChatColor.GRAY + "Mining Guild" + ChatColor.WHITE + ": " + ChatColor.YELLOW + "Lvl " + ChatColor.translateAlternateColorCodes('&', lvlString(player, "Mining")));
		player.sendMessage(ChatColor.DARK_GRAY + "Slayer Guild" + ChatColor.WHITE + ": " + ChatColor.YELLOW + "Lvl " + ChatColor.translateAlternateColorCodes('&', lvlString(player, "Slayer")));
		player.sendMessage(ChatColor.GRAY + "-----------------------------");
		plugin.cfgm.savePlayers();
		//config("users").set(p.getName() + ".guildrank", rankNext);
		//int rank = config("users").getInt(p.getName() + ".guildrank", 0);
	}

	private String lvlString(Player player, String string) {
		int lvl = plugin.cfgm.getPlayer().getInt(player.getUniqueId().toString() + "." + string);
		if (lvl == 30) {
			return "&e&l30/30";
		}
		else {
			String a = String.format("&e%d/30", lvl);
			return a;
		}
	}

	
	/*private Sign signCreate(Block b, Block wall) {
		BlockFace f = null;
		for (BlockFace bf : BlockFace.values())
			if (b.getRelative(bf).equals(wall))
				f = bf.getOppositeFace();
		return signCreate(b, f);
	}
	private Sign signCreate(Block b, BlockFace f) {
		b.setType(Material.WALL_SIGN);
		Sign sign = (Sign)b.getState();
		org.bukkit.material.Sign signData = (org.bukkit.material.Sign) sign.getData();
		signData.setFacingDirection(f);
		sign.update();
		return sign;
	}
	
	
	List<Block> los = p.getLineOfSight(null, 50);
			Block wall = los.get(los.size()-1), target = los.get(los.size()-2);
			Sign sign = signCreate(target, wall);
			
			List<String> signs = config("savedata").getStringList("guildsigns");
			if (signs == null) signs = new ArrayList<String>();
			String action = args[1];
			String opt1 = args.length >= 3 ? args[2] : "", opt2 = args.length >= 4 ? args[3] : "";
			String levelNeeded = args.length >= 5 ? args[4] : "", guildNeeded = args.length >= 6 ? args[5] : "";
			signs.add(locationString(target) + "`" + action + "`" + opt1 + "`" + opt2 + "`" + levelNeeded + "`" + guildNeeded);
			config("savedata").set("guildsigns", signs);
			saveMyConfig("savedata"); p.sendMessage("Guild sign created");

			sign.setLine(0, colorize("&1<" + args[1] + ">"));
			if (action.equals("FastCraft")) {
				sign.setLine(1, opt1.toLowerCase().replaceAll("_", ""));
				sign.setLine(2, "-> " + opt2.toLowerCase().replaceAll("_", ""));
				sign.setLine(3, colorize("&4L" + levelNeeded + " " + guildNeeded));
			} else if (action.equals("Tribute")) {
				sign.setLine(1, "Pay tribute to");
				sign.setLine(2, "this guild to");
				sign.setLine(3, "join or rank up");
			} else if (action.equals("Aura")) {
				String effect = opt1.split(":")[0];
				sign.setLine(1, capitalize(effect));
				sign.setLine(2, opt2 + " minutes");
				sign.setLine(3, colorize("&4L" + levelNeeded + " " + guildNeeded));
			}
			sign.update();*/
}
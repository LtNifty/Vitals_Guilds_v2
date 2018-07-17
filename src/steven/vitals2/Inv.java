package steven.vitals2;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Inv implements Listener {
	
	private Main plugin = Main.getPlugin(Main.class);
	
	
	public void GuildInventory(Player player, String guild) {
		String name = readableGuild(guild);
		Material item = zoneItem(guild);
		Inventory I = plugin.getServer().createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', name));
		ItemStack a = new ItemStack(item, 1);
		ItemStack empty = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0);
		ItemStack b = new ItemStack(Material.WRITTEN_BOOK, 1);
		ItemMeta eMeta = empty.getItemMeta();
		ItemMeta aMeta = a.getItemMeta();
		ItemMeta bMeta = b.getItemMeta();
		aMeta.setDisplayName(ChatColor.GOLD + "Tribute");
		eMeta.setDisplayName(ChatColor.GRAY + " ");
		bMeta.setDisplayName(ChatColor.GOLD + "Next rank information");
		ArrayList<String> lorea = new ArrayList<String>();
		lorea.add("Pay tribute to");
		lorea.add("this guild to");
		lorea.add("join or rank up");
		ArrayList<String> loreb = new ArrayList<String>();
		loreb.add("Check what items");
		loreb.add("and money you");
		loreb.add("need to rank up");
		aMeta.setLore(lorea);
		bMeta.setLore(loreb);
		empty.setItemMeta(eMeta);
		a.setItemMeta(aMeta);
		b.setItemMeta(bMeta);
		
		I.setItem(0, empty);
		I.setItem(1, a);
		I.setItem(2, empty);
		I.setItem(3, b);
		I.setItem(4, empty);
		I.setItem(5, empty);
		I.setItem(6, empty);
		I.setItem(7, empty);
		I.setItem(8, empty);
		
		player.openInventory(I);
	}
	
	public void GShopInventory(Player player, String guild) {
		String name = readableGuild(guild);
		Material item = zoneItem(guild);
		Inventory I = plugin.getServer().createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', name) + " Shop");
		ItemStack a = new ItemStack(item, 1);
		ItemStack empty = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 0);
		ItemMeta eMeta = empty.getItemMeta();
		ItemMeta aMeta = a.getItemMeta();
		aMeta.setDisplayName(ChatColor.GOLD + "Test");
		eMeta.setDisplayName(ChatColor.GRAY + " ");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("Take $200");
		lore.add("Gives guild Item");
		aMeta.setLore(lore);
		empty.setItemMeta(eMeta);
		a.setItemMeta(aMeta);
		
		I.setItem(0, empty);
		I.setItem(1, a);
		I.setItem(2, empty);
		I.setItem(3, empty);
		I.setItem(4, empty);
		I.setItem(5, empty);
		I.setItem(6, empty);
		I.setItem(7, empty);
		I.setItem(8, empty);
		
		player.openInventory(I);
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

}

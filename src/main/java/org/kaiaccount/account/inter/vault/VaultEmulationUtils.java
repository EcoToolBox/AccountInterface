package org.kaiaccount.account.inter.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;

public final class VaultEmulationUtils {

	public static @NotNull VaultPluginWrapper loadVault() throws IOException, NoSuchFieldException, IllegalAccessException {
		PluginManager manager = Bukkit.getPluginManager();
		if (!(manager instanceof SimplePluginManager simpleManager)) {
			throw new IOException("Unknown plugin manager. Cannot load Vault emulation");
		}
		Field pluginsField = simpleManager.getClass().getDeclaredField("plugins");
		pluginsField.setAccessible(true);
		Collection<Plugin> plugins = (Collection<Plugin>) pluginsField.get(simpleManager);
		pluginsField.setAccessible(false);
		VaultPluginWrapper plugin = new VaultPluginWrapper();
		plugins.add(plugin);
		return plugin;
	}

	public static @NotNull KaiEco loadService(@NotNull Plugin plugin) {
		KaiEco eco = new KaiEco(() -> AccountInterface.getGlobal().getDefaultCurrency(), 3);
		loadService(plugin, eco);
		return eco;
	}

	public static void loadService(@NotNull Plugin plugin, @NotNull Economy eco) {
		Bukkit.getServicesManager().register(Economy.class, eco, plugin, ServicePriority.Normal);
	}

	private VaultEmulationUtils(){
		throw new RuntimeException("Dont do that");
	}
}

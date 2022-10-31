package org.kaiaccount;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;


/**
 * This is the main class for AccountInterface. This will hold the manager for all your needs, but that is only what
 * you should be accessing this for
 */
public class AccountInterface extends JavaPlugin {

	private AccountInterfaceManager manager;

	private static AccountInterface plugin;

	public AccountInterface() {
		plugin = this;
	}

	private Optional<AccountInterfaceManager> getFromService() {
		RegisteredServiceProvider<AccountInterfaceManager> reg =
				Bukkit.getServicesManager().getRegistration(AccountInterfaceManager.class);
		if (reg == null) {
			return Optional.empty();
		}
		return Optional.of(reg.getProvider());
	}

	/**
	 * Checks if a currency plugin has taken ownership and provided a {@link AccountInterfaceManager} to the Bukkit
	 * services
	 *
	 * @return true if the manager can be found
	 */
	public static synchronized boolean isReady() {
		if (getPlugin().manager != null) {
			return true;
		}
		Optional<AccountInterfaceManager> opManager = getPlugin().getFromService();
		if (opManager.isEmpty()) {
			return false;
		}
		getPlugin().manager = opManager.get();
		return true;
	}

	/**
	 * Gets the manager (if ready)
	 *
	 * @return The manager for all things AccountInterface
	 */
	public static @NotNull AccountInterfaceManager getManager() {
		if (!isReady()) {
			throw new RuntimeException("Currency plugin has not registered itself yet");
		}
		return getPlugin().manager;
	}

	/**
	 * Gets AccountInterface Bukkit plugin
	 *
	 * @return this plugin
	 */
	public static AccountInterface getPlugin() {
		return plugin;
	}


}

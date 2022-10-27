package org.kaiaccount;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

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

	public static @NotNull AccountInterfaceManager getManager() {
		if (!isReady()) {
			throw new RuntimeException("Currency plugin has not registered itself yet");
		}
		return getPlugin().manager;
	}

	public static AccountInterface getPlugin() {
		return plugin;
	}


}

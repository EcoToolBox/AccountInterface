package org.kaiaccount;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.Currency;

import java.util.Collection;
import java.util.concurrent.LinkedTransferQueue;

public class AccountInterface extends JavaPlugin {

	private final Collection<Currency> currencies = new LinkedTransferQueue<>();

	private static AccountInterface plugin;

	public AccountInterface() {
		plugin = this;
	}

	public @NotNull Collection<Currency> getCurrencies() {
		return this.currencies;
	}

	public static AccountInterface getPlugin() {
		return plugin;
	}


}

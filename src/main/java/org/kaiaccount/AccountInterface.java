package org.kaiaccount;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.io.Serializers;
import org.kaiaccount.account.inter.type.player.PlayerAccount;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

public class AccountInterface extends JavaPlugin {

	private final @NotNull Collection<Currency> currencies = new LinkedTransferQueue<>();
	private final @NotNull Collection<PlayerAccount> account = new LinkedBlockingQueue<>();

	private static AccountInterface plugin;

	public AccountInterface() {
		plugin = this;
	}

	public @NotNull Collection<Currency> getCurrencies() {
		return this.currencies;
	}

	public @NotNull Currency getDefaultCurrency() {
		return this.currencies.parallelStream()
				.filter(Currency::isDefault)
				.findAny()
				.orElseThrow(() -> new RuntimeException("No default currency"));
	}

	public @NotNull PlayerAccount getPlayerAccount(@NotNull OfflinePlayer player) {
		Optional<PlayerAccount> opAccount =
				this.account.parallelStream().filter(account -> account.getPlayer().equals(player)).findAny();
		if (opAccount.isPresent()) {
			return opAccount.get();
		}
		File file = PlayerAccount.getFile(player.getUniqueId());
		if (!file.exists()) {
			PlayerAccount playerAccount = new PlayerAccount(player);
			this.account.add(playerAccount);
			return playerAccount;
		}
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		try {
			PlayerAccount playerAccount = Serializers.PLAYER_ACCOUNT.deserialize(config);
			this.account.add(playerAccount);
			return playerAccount;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static AccountInterface getPlugin() {
		return plugin;
	}


}

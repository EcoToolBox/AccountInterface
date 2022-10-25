package org.kaiaccount;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.currency.CurrencyBuilder;
import org.kaiaccount.account.inter.currency.ToCurrency;
import org.kaiaccount.account.inter.io.Serializers;
import org.kaiaccount.account.inter.type.bank.player.PlayerBankAccount;
import org.kaiaccount.account.inter.type.bank.player.PlayerBankAccountBuilder;
import org.kaiaccount.account.inter.type.bank.player.ToBankAccount;
import org.kaiaccount.account.inter.type.player.PlayerAccount;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public interface AccountInterfaceManager {

	Plugin getVaultPlugin();

	Collection<ToCurrency> getToCurrencies();

	Collection<ToBankAccount> getToBankAccount();

	@NotNull
	Collection<Currency> getCurrencies();

	@NotNull
	Collection<PlayerAccount> getPlayerAccounts();

	void registerPlayerAccount(@NotNull PlayerAccount account);

	void deregisterPlayerAccount(@NotNull PlayerAccount account);

	void registerCurrency(@NotNull Currency currency);

	void deregisterCurrency(@NotNull Currency currency);

	default PlayerBankAccount<?> toBankAccount(@NotNull PlayerBankAccountBuilder builder) {
		Exception e = null;
		for (ToBankAccount function : this.getToBankAccount()) {
			try {
				return function.toBankAccount(builder);
			} catch (Exception e1) {
				e = e1;
			}
		}
		if (e == null) {
			throw new RuntimeException("No 'ToCurrency' registered");
		}
		throw new RuntimeException(e);
	}

	default Currency toCurrency(@NotNull CurrencyBuilder builder) {
		Exception e = null;
		for (ToCurrency function : this.getToCurrencies()) {
			try {
				return function.toCurrency(builder);
			} catch (Exception e1) {
				e = e1;
			}
		}
		if (e == null) {
			throw new RuntimeException("No 'ToCurrency' registered");
		}
		throw new RuntimeException(e);
	}

	default @NotNull Currency getDefaultCurrency() {
		return this.getCurrencies().parallelStream()
				.filter(Currency::isDefault)
				.findAny()
				.orElseThrow(() -> new RuntimeException("No default currency"));
	}

	default @NotNull Optional<Currency> getCurrency(@NotNull String symbol) {
		return this.getCurrencies().parallelStream().filter(cur -> cur.getSymbol().equalsIgnoreCase(symbol)).findAny();
	}

	default @NotNull Optional<Currency> getCurrency(@NotNull Plugin plugin, String name) {
		return this.getCurrencies()
				.parallelStream()
				.filter(cur -> cur.getPlugin().equals(plugin))
				.filter(cur -> cur.getKeyName().equalsIgnoreCase(name))
				.findAny();
	}

	default @NotNull PlayerAccount getPlayerAccount(@NotNull OfflinePlayer player) {
		Optional<PlayerAccount> opAccount =
				this.getPlayerAccounts()
						.parallelStream()
						.filter(account -> account.getPlayer().equals(player))
						.findAny();
		if (opAccount.isPresent()) {
			return opAccount.get();
		}
		File file = PlayerAccount.getFile(player.getUniqueId());
		if (!file.exists()) {
			PlayerAccount playerAccount = new PlayerAccount(player);
			this.registerPlayerAccount(playerAccount);
			return playerAccount;
		}
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		try {
			PlayerAccount playerAccount = Serializers.PLAYER_ACCOUNT.deserialize(config);
			this.registerPlayerAccount(playerAccount);
			return playerAccount;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


}

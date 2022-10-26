package org.kaiaccount;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.currency.CurrencyBuilder;
import org.kaiaccount.account.inter.currency.ToCurrency;
import org.kaiaccount.account.inter.type.bank.player.PlayerBankAccount;
import org.kaiaccount.account.inter.type.bank.player.PlayerBankAccountBuilder;
import org.kaiaccount.account.inter.type.bank.player.ToBankAccount;
import org.kaiaccount.account.inter.type.player.PlayerAccount;
import org.kaiaccount.account.inter.type.player.PlayerAccountBuilder;
import org.kaiaccount.account.inter.type.player.ToPlayerAccount;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface AccountInterfaceManager {

	Plugin getVaultPlugin();

	ToCurrency getToCurrencies();

	ToBankAccount getToBankAccount();

	@NotNull
	Collection<Currency<?>> getCurrencies();

	@NotNull
	Collection<PlayerAccount<?>> getPlayerAccounts();

	ToPlayerAccount getToPlayerAccount();

	void registerPlayerAccount(@NotNull PlayerAccount<?> account);

	PlayerAccount<?> loadPlayerAccount(@NotNull OfflinePlayer player);

	void deregisterPlayerAccount(@NotNull PlayerAccount<?> account);

	void registerCurrency(@NotNull Currency<?> currency);

	void deregisterCurrency(@NotNull Currency<?> currency);

	default PlayerBankAccount<?> toBankAccount(@NotNull PlayerBankAccountBuilder builder) {
		try {
			return this.getToBankAccount()
					.toBankAccount(builder);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	default Currency<?> toCurrency(@NotNull CurrencyBuilder builder) {
		try {
			return this.getToCurrencies()
					.toCurrency(builder);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	default PlayerAccount<?> toPlayerAccount(@NotNull PlayerAccountBuilder builder) {
		try {
			return this.getToPlayerAccount()
					.toPlayerAccount(builder);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	default @NotNull Currency<?> getDefaultCurrency() {
		return this.getCurrencies().parallelStream()
				.filter(Currency::isDefault)
				.findAny()
				.orElseThrow(() -> new RuntimeException("No default currency"));
	}

	default @NotNull Optional<Currency<?>> getCurrency(@NotNull String symbol) {
		return this.getCurrencies().parallelStream().filter(cur -> cur.getSymbol().equalsIgnoreCase(symbol)).findAny();
	}

	default @NotNull Optional<Currency<?>> getCurrency(@NotNull Plugin plugin, String name) {
		return this.getCurrencies()
				.parallelStream()
				.filter(cur -> cur.getPlugin().equals(plugin))
				.filter(cur -> cur.getKeyName().equalsIgnoreCase(name))
				.findAny();
	}

	default @NotNull PlayerAccount<?> getPlayerAccount(@NotNull UUID playerId) {
		return this.getPlayerAccount(Bukkit.getServer().getOfflinePlayer(playerId));
	}

	default @NotNull PlayerAccount<?> getPlayerAccount(@NotNull OfflinePlayer player) {
		Optional<PlayerAccount<?>> opAccount =
				this.getPlayerAccounts()
						.parallelStream()
						.filter(account -> account.getPlayer().equals(player))
						.findAny();
		if (opAccount.isPresent()) {
			return opAccount.get();
		}
		return this.loadPlayerAccount(player);
	}


}

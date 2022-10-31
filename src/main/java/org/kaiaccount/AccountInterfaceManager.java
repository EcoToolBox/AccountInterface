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

	/**
	 * Gets the plugin that vault will act as. This is typically the
	 * {@link org.kaiaccount.account.inter.vault.VaultPluginWrapper} however may not be if it could not be registered
	 *
	 * @return The vault plugin
	 */
	Plugin getVaultPlugin();

	/**
	 * Gets the mapper to map a {@link CurrencyBuilder} to the currency plugins {@link Currency}
	 * This should not be used unless you are making a currency plugin
	 *
	 * @return The mapper
	 */
	ToCurrency getToCurrencies();

	/**
	 * gets the mapper to map a {@link PlayerBankAccountBuilder} to a {@link PlayerBankAccount}
	 * This should not be used unless you are making a currency plugin
	 *
	 * @return The mapper
	 */
	ToBankAccount getToBankAccount();

	/**
	 * Gets all registered currencies
	 *
	 * @return A collection of all currencies
	 */
	@NotNull
	Collection<Currency<?>> getCurrencies();

	/**
	 * Gets all registered player accounts
	 *
	 * @return A collection of all Player Accounts
	 */
	@NotNull
	Collection<PlayerAccount<?>> getPlayerAccounts();

	/**
	 * Get the mapper to map a {@link PlayerAccountBuilder} to a {@link PlayerAccount}
	 * This should not be used unless you are making a currency plugin
	 *
	 * @return The mapper
	 */
	ToPlayerAccount getToPlayerAccount();

	/**
	 * Registers a new Player Account. This is typically used for {@link #getPlayerAccount(UUID)}
	 *
	 * @param account The account to be registered
	 */
	void registerPlayerAccount(@NotNull PlayerAccount<?> account);

	/**
	 * Loads the player account data based upon the players {@link UUID}
	 *
	 * @param player The player to load data for
	 * @return A PlayerAccount of the provided player
	 */
	PlayerAccount<?> loadPlayerAccount(@NotNull OfflinePlayer player);

	/**
	 * Saves and deloads the player. This does not delete the player
	 *
	 * @param account The player account to deload
	 */
	void deregisterPlayerAccount(@NotNull PlayerAccount<?> account);

	/**
	 * Registers a new currency into the game
	 *
	 * @param currency The currency to register
	 * @throws IllegalArgumentException thrown when the currency has the same plugin and name combo or symbol as
	 * another registered currency
	 */
	void registerCurrency(@NotNull Currency<?> currency);

	/**
	 * Saves and deloads the currency. This does not delete the currency
	 *
	 * @param currency The currency to register
	 */
	void deregisterCurrency(@NotNull Currency<?> currency);

	/**
	 * Ignore - This is for builder logic
	 *
	 * @param builder The builder
	 * @return A built edition
	 */
	default PlayerBankAccount<?> toBankAccount(@NotNull PlayerBankAccountBuilder builder) {
		try {
			return this.getToBankAccount()
					.toBankAccount(builder);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Ignore - This is for builder logic
	 *
	 * @param builder The builder
	 * @return A built edition
	 */
	default Currency<?> toCurrency(@NotNull CurrencyBuilder builder) {
		try {
			return this.getToCurrencies()
					.toCurrency(builder);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Ignore - This is for builder logic
	 *
	 * @param builder The builder
	 * @return A built edition
	 */
	default PlayerAccount<?> toPlayerAccount(@NotNull PlayerAccountBuilder builder) {
		try {
			return this.getToPlayerAccount()
					.toPlayerAccount(builder);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * gets the default currency of this server
	 *
	 * @return The default currency
	 */
	default @NotNull Currency<?> getDefaultCurrency() {
		return this.getCurrencies().parallelStream()
				.filter(Currency::isDefault)
				.findAny()
				.orElseThrow(() -> new RuntimeException("No default currency"));
	}

	/**
	 * Gets a currency based upon the symbol
	 *
	 * @param symbol The symbol of the currency
	 * @return A potential of the currency
	 */
	default @NotNull Optional<Currency<?>> getCurrency(@NotNull String symbol) {
		return this.getCurrencies().parallelStream().filter(cur -> cur.getSymbol().equalsIgnoreCase(symbol)).findAny();
	}

	/**
	 * Gets a currency based upon the plugin and name
	 *
	 * @param plugin The plugin of the currency
	 * @param name   The key name of the currency
	 * @return A potential of the currency
	 */
	default @NotNull Optional<Currency<?>> getCurrency(@NotNull Plugin plugin, String name) {
		return this.getCurrencies()
				.parallelStream()
				.filter(cur -> cur.getPlugin().equals(plugin))
				.filter(cur -> cur.getKeyName().equalsIgnoreCase(name))
				.findAny();
	}

	/**
	 * Gets the PlayerAccount based upon the players UUID
	 *
	 * @param playerId the UUID of the player
	 * @return A PlayerAccount of UUID
	 */
	default @NotNull PlayerAccount<?> getPlayerAccount(@NotNull UUID playerId) {
		return this.getPlayerAccount(Bukkit.getServer().getOfflinePlayer(playerId));
	}

	/**
	 * gets the PlayerAccount based upon the players object
	 *
	 * @param player The Players object
	 * @return A PlayerAccount of OfflinePlayer
	 */
	default @NotNull PlayerAccount<?> getPlayerAccount(@NotNull OfflinePlayer player) {
		Optional<PlayerAccount<?>> opAccount =
				this.getPlayerAccounts()
						.parallelStream()
						.filter(account -> account.getPlayer().getUniqueId().equals(player.getUniqueId()))
						.findAny();
		if (opAccount.isPresent()) {
			return opAccount.get();
		}
		return this.loadPlayerAccount(player);
	}


}

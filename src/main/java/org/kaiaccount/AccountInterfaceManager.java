package org.kaiaccount;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.currency.CurrencyBuilder;
import org.kaiaccount.account.inter.currency.ToCurrency;
import org.kaiaccount.account.inter.type.Account;
import org.kaiaccount.account.inter.type.named.NamedAccount;
import org.kaiaccount.account.inter.type.named.NamedAccountBuilder;
import org.kaiaccount.account.inter.type.named.ToNamedAccount;
import org.kaiaccount.account.inter.type.named.bank.player.PlayerBankAccount;
import org.kaiaccount.account.inter.type.named.bank.player.PlayerBankAccountBuilder;
import org.kaiaccount.account.inter.type.named.bank.player.ToBankAccount;
import org.kaiaccount.account.inter.type.player.PlayerAccount;
import org.kaiaccount.account.inter.type.player.PlayerAccountBuilder;
import org.kaiaccount.account.inter.type.player.ToPlayerAccount;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface AccountInterfaceManager {

    /**
     * @return The vault plugin
     */
    @CheckReturnValue
    default @NotNull Plugin getVaultPlugin() {
        return Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Vault"));
    }

    /**
     * Gets the mapper to map a {@link CurrencyBuilder} to the currency plugins {@link Currency}
     * This should not be used unless you are making a currency plugin
     *
     * @return The mapper
     */
    @CheckReturnValue
    @NotNull ToCurrency getToCurrencies();

    /**
     * gets the mapper to map a {@link PlayerBankAccountBuilder} to a {@link PlayerBankAccount}
     * This should not be used unless you are making a currency plugin
     *
     * @return The mapper
     */
    @CheckReturnValue
    @NotNull ToBankAccount getToBankAccount();

    /**
     * Gets all registered currencies
     *
     * @return A collection of all currencies
     */
    @NotNull
    @UnmodifiableView
    @CheckReturnValue
    Collection<Currency<?>> getCurrencies();

    /**
     * Gets all registered player accounts
     *
     * @return A collection of all Player Accounts
     */
    @NotNull
    @UnmodifiableView
    @CheckReturnValue
    Collection<PlayerAccount> getPlayerAccounts();

    /**
     * Gets all registered named accounts
     *
     * @return A collection of all named accounts
     */
    @NotNull
    @UnmodifiableView
    Collection<NamedAccount> getNamedAccounts();

    /**
     * Gets all accounts, no matter the type
     *
     * @return All accounts
     */
    default @NotNull Stream<Account> getAccounts() {
        Collection<NamedAccount> named = this.getNamedAccounts();
        Collection<PlayerAccount> players = this.getPlayerAccounts();
        Stream<PlayerBankAccount> playerBanks = players.parallelStream().flatMap(account -> account.getBanks().parallelStream());

        Stream<Account> accounts = Stream.concat(playerBanks, players.parallelStream());
        accounts = Stream.concat(accounts, named.parallelStream());

        return accounts.distinct();
    }

    /**
     * Get the mapper to map a {@link PlayerAccountBuilder} to a {@link PlayerAccount}
     * This should not be used unless you are making a currency plugin
     *
     * @return The mapper
     */
    @CheckReturnValue
    @NotNull ToPlayerAccount getToPlayerAccount();

    /**
     * Gets the mapper to map a {@link NamedAccountBuilder} to a {@link NamedAccount}
     * This should not be used unless you are making a currency plugin
     *
     * @return The mapper
     */
    @NotNull
    ToNamedAccount getToNamedAccount();

    /**
     * Registers a new Player Account. This is typically used for {@link #getPlayerAccount(UUID)}
     *
     * @param account The account to be registered
     */
    void registerPlayerAccount(@NotNull PlayerAccount account);

    /**
     * Registers a new Named Account
     *
     * @param account The account to be registered
     */
    void registerNamedAccount(@NotNull NamedAccount account);

    /**
     * Loads the player account data based upon the players {@link UUID}
     *
     * @param player The player to load data for
     * @return A PlayerAccount of the provided player
     */
    @NotNull
    PlayerAccount loadPlayerAccount(@NotNull OfflinePlayer player);

    /**
     * deloads the player. This does not delete the player
     *
     * @param account The player account to deload
     */
    void deregisterPlayerAccount(@NotNull PlayerAccount account);

    /**
     * deloads the named account. This does not delete the named account
     *
     * @param account The account to deload
     */
    void deregisterNamedAccount(@NotNull NamedAccount account);

    /**
     * Registers a new currency into the game
     *
     * @param currency The currency to register
     * @throws IllegalArgumentException thrown when the currency has the same plugin and name combo or symbol as
     *                                  another registered currency
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
    @NotNull
    @ApiStatus.Internal
    default PlayerBankAccount toBankAccount(@NotNull PlayerBankAccountBuilder builder) {
        try {
            return this.getToBankAccount()
                    .toBankAccount(builder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Ignore - this is for builder logic
     *
     * @param builder the builder
     * @return a built edition
     */
    @ApiStatus.Internal
    default NamedAccount toNamedAccount(@NotNull NamedAccountBuilder builder) {
        try {
            return this.getToNamedAccount().toNamedAccount(builder);
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
    @ApiStatus.Internal
    default @NotNull Currency<?> toCurrency(@NotNull CurrencyBuilder builder) {
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
    @ApiStatus.Internal
    default @NotNull PlayerAccount toPlayerAccount(@NotNull PlayerAccountBuilder builder) {
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
    default @NotNull Optional<Currency<?>> getCurrency(@NotNull Plugin plugin, @NotNull String name) {
        return this.getCurrencies()
                .parallelStream()
                .filter(cur -> cur.getPlugin().equals(plugin))
                .filter(cur -> cur.getKeyName().equalsIgnoreCase(name))
                .findAny();
    }

    /**
     * Gets a named account with the provided account name
     *
     * @param accountName The name on the account
     * @return The account
     */
    default @NotNull Optional<NamedAccount> getNamedAccount(@NotNull String accountName) {
        return this.getNamedAccounts().parallelStream().filter(account -> account.getAccountName().equals(accountName)).findAny();
    }

    /**
     * Gets the PlayerAccount based upon the players UUID
     *
     * @param playerId the UUID of the player
     * @return A PlayerAccount of UUID
     */
    default @NotNull PlayerAccount getPlayerAccount(@NotNull UUID playerId) {
        return this.getPlayerAccount(Bukkit.getServer().getOfflinePlayer(playerId));
    }

    /**
     * gets the PlayerAccount based upon the players object
     *
     * @param player The Players object
     * @return A PlayerAccount of OfflinePlayer
     */
    default @NotNull PlayerAccount getPlayerAccount(@NotNull OfflinePlayer player) {
        Optional<PlayerAccount> opAccount =
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

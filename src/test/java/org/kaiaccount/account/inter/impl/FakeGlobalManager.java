package org.kaiaccount.account.inter.impl;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.kaiaccount.AccountInterfaceManager;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.currency.ToCurrency;
import org.kaiaccount.account.inter.impl.currency.ToFakeCurrency;
import org.kaiaccount.account.inter.type.named.NamedAccount;
import org.kaiaccount.account.inter.type.named.ToNamedAccount;
import org.kaiaccount.account.inter.type.named.bank.player.ToBankAccount;
import org.kaiaccount.account.inter.type.player.PlayerAccount;
import org.kaiaccount.account.inter.type.player.ToPlayerAccount;

import java.util.Collection;

public class FakeGlobalManager implements AccountInterfaceManager {
    @Override
    public @NotNull Plugin getVaultPlugin() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public @NotNull ToCurrency getToCurrencies() {
        return new ToFakeCurrency();
    }

    @Override
    public @NotNull ToBankAccount getToBankAccount() {
        throw new RuntimeException("Not implemented yet");
    }

    @NotNull
    @Override
    public Collection<Currency<?>> getCurrencies() {
        throw new RuntimeException("Not implemented yet");
    }

    @NotNull
    @Override
    public Collection<PlayerAccount> getPlayerAccounts() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public @NotNull @UnmodifiableView Collection<NamedAccount> getNamedAccounts() {
        throw new RuntimeException("Not implemented yet");

    }

    @Override
    public @NotNull ToPlayerAccount getToPlayerAccount() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public @NotNull ToNamedAccount getToNamedAccount() {
        throw new RuntimeException("Not implemented yet");

    }

    @Override
    public @NotNull PlayerAccount loadPlayerAccount(@NotNull OfflinePlayer player) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void registerPlayerAccount(@NotNull PlayerAccount account) {
        throw new RuntimeException("Not implemented yet");

    }

    @Override
    public void registerNamedAccount(@NotNull NamedAccount account) {
        throw new RuntimeException("Not implemented yet");

    }

    @Override
    public void deregisterPlayerAccount(@NotNull PlayerAccount account) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void deregisterNamedAccount(@NotNull NamedAccount account) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void registerCurrency(@NotNull Currency<?> currency) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void deregisterCurrency(@NotNull Currency<?> currency) {
        throw new RuntimeException("Not implemented yet");
    }
}

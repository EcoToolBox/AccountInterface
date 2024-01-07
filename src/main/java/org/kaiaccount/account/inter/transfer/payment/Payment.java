package org.kaiaccount.account.inter.transfer.payment;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.type.Account;

import java.math.BigDecimal;
import java.util.Optional;

public interface Payment {

    @NotNull
    @CheckReturnValue
    BigDecimal getAmount();

    @NotNull
    @CheckReturnValue
    Currency<?> getCurrency();

    @NotNull
    @CheckReturnValue
    Plugin getPlugin();

    @NotNull
    @CheckReturnValue
    Optional<String> getReason();

    @NotNull
    @CheckReturnValue
    Optional<Account> getFrom();

    @CheckReturnValue
    boolean isPriority();
}

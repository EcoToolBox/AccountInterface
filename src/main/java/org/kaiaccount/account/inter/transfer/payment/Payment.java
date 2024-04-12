package org.kaiaccount.account.inter.transfer.payment;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.type.named.NamedAccountLike;
import org.kaiaccount.utils.builder.Buildable;

import java.math.BigDecimal;
import java.util.Optional;

public interface Payment extends Buildable<Payment, PaymentBuilder> {

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
    Optional<NamedAccountLike> getFrom();

    @NotNull
    @CheckReturnValue
    Optional<NamedAccountLike> getTo();

    @CheckReturnValue
    boolean isPriority();

    @Override
    default PaymentBuilder toBuilder() {
        return new PaymentBuilder()
                .setPlugin(this.getPlugin())
                .setAmount(this.getAmount())
                .setCurrency(this.getCurrency())
                .setFrom(this.getFrom().orElse(null))
                .setPriority(this.isPriority())
                .setReason(this.getReason().orElse(null))
                .setTo(this.getTo().orElse(null));
    }
}

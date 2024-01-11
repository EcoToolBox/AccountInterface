package org.kaiaccount.account.inter.transfer.payment;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.type.named.NamedAccountLike;

import java.math.BigDecimal;
import java.math.BigInteger;

public class PaymentBuilder {

    private BigDecimal amount;
    private Currency<?> currency;
    private String resource;
    private NamedAccountLike from;
    private boolean priority;

    @CheckReturnValue
    public Payment build(@NotNull Plugin plugin) {
        return new KaiPayment(this, plugin);
    }

    public boolean isPriority() {
        return this.priority;
    }

    public PaymentBuilder setPriority(boolean priority) {
        this.priority = priority;
        return this;
    }

    public NamedAccountLike getFrom() {
        return this.from;
    }

    public PaymentBuilder setFrom(@Nullable NamedAccountLike from) {
        this.from = from;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentBuilder setAmount(double amount) {
        this.amount = BigDecimal.valueOf(amount);
        return this;
    }

    public PaymentBuilder setAmount(@NotNull BigInteger amount) {
        this.amount = new BigDecimal(amount);
        return this;
    }

    public PaymentBuilder setAmount(@NotNull BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Currency<?> getCurrency() {
        return currency;
    }

    public PaymentBuilder setCurrency(@NotNull Currency<?> currency) {
        this.currency = currency;
        return this;
    }

    public String getReason() {
        return resource;
    }

    public PaymentBuilder setReason(@Nullable String resource) {
        this.resource = resource;
        return this;
    }
}

package org.kaiaccount.account.inter.transfer.payment;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.type.named.NamedAccountLike;
import org.kaiaccount.utils.builder.Builder;

import java.math.BigDecimal;
import java.math.BigInteger;

public class PaymentBuilder implements Builder<Payment, PaymentBuilder> {

    private BigDecimal amount;
    private Currency<?> currency;
    private String resource;
    private NamedAccountLike from;
    private NamedAccountLike to;
    private boolean priority;
    private Plugin plugin;

    @CheckReturnValue
    @Deprecated
    public Payment build(@NotNull Plugin plugin) {
        return new KaiPayment(this, plugin);
    }

    @Override
    public Payment build() {
        return new KaiPayment(this, this.plugin);
    }

    @Override
    public PaymentBuilder from(PaymentBuilder builder) {
        this.setTo(builder.getTo());
        this.setPriority(builder.isPriority());
        this.setFrom(builder.getFrom());
        this.setPlugin(builder.getPlugin());
        this.setReason(builder.getReason());
        this.setCurrency(builder.getCurrency());
        this.setAmount(builder.getAmount());
        return this;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public PaymentBuilder setPlugin(@NotNull Plugin plugin) {
        this.plugin = plugin;
        return this;
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

    public NamedAccountLike getTo() {
        return this.to;
    }

    public PaymentBuilder setTo(@Nullable NamedAccountLike to) {
        this.to = to;
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

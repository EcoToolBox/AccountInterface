package org.kaiaccount.account.inter.transfer.payment;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kaiaccount.account.inter.Account;
import org.kaiaccount.account.inter.Currency;

import java.math.BigDecimal;
import java.math.BigInteger;

public class PaymentBuilder {

	private BigDecimal amount;
	private Currency currency;
	private String resource;
	private Account from;

	public Payment build(Plugin plugin) {
		return new KaiPayment(this, plugin);
	}

	public Account getFrom() {
		return this.from;
	}

	public PaymentBuilder setFrom(@Nullable Account from) {
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

	public Currency getCurrency() {
		return currency;
	}

	public PaymentBuilder setCurrency(@NotNull Currency currency) {
		this.currency = currency;
		return this;
	}

	public String getResource() {
		return resource;
	}

	public PaymentBuilder setResource(@Nullable String resource) {
		this.resource = resource;
		return this;
	}
}

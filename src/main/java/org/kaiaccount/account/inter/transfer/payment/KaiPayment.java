package org.kaiaccount.account.inter.transfer.payment;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kaiaccount.account.inter.Account;
import org.kaiaccount.account.inter.Currency;

import java.math.BigDecimal;
import java.util.Optional;

public class KaiPayment implements Payment {

	private final @NotNull Currency currency;
	private final @NotNull BigDecimal bigDecimal;
	private final @NotNull Plugin plugin;
	private final @Nullable String reason;
	private final @Nullable Account<?> from;

	public KaiPayment(@NotNull PaymentBuilder builder, @NotNull Plugin plugin) {
		this.currency = builder.getCurrency();
		this.bigDecimal = builder.getAmount();
		this.plugin = plugin;
		this.reason = builder.getReason();
		this.from = builder.getFrom();
	}


	@NotNull
	@Override
	public BigDecimal getAmount() {
		return this.bigDecimal;
	}

	@NotNull
	@Override
	public Currency getCurrency() {
		return this.currency;
	}

	@NotNull
	@Override
	public Plugin getPlugin() {
		return this.plugin;
	}

	@NotNull
	@Override
	public Optional<String> getReason() {
		return Optional.ofNullable(this.reason);
	}

	@NotNull
	@Override
	public Optional<Account<?>> getFrom() {
		return Optional.ofNullable(this.from);
	}
}

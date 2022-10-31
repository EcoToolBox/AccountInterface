package org.kaiaccount.account.inter.transfer.payment;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.type.Account;

import java.math.BigDecimal;
import java.util.Optional;

public class KaiPayment implements Payment {

	private final @NotNull Currency<?> currency;
	private final @NotNull BigDecimal bigDecimal;
	private final @NotNull Plugin plugin;
	private final @Nullable String reason;
	private final @Nullable Account from;
	private final boolean priority;

	public KaiPayment(@NotNull PaymentBuilder builder, @NotNull Plugin plugin) {
		this.currency = builder.getCurrency();
		this.bigDecimal = builder.getAmount();
		this.plugin = plugin;
		this.reason = builder.getReason();
		this.from = builder.getFrom();
		this.priority = builder.isPriority();
		if (this.currency == null) {
			throw new IllegalArgumentException("No currency specified");
		}
		if (this.bigDecimal == null) {
			throw new IllegalArgumentException("No amount specified");
		}
		if (this.bigDecimal.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Negative number can not be used in payments");
		}
		//noinspection ConstantValue
		if (this.plugin == null) {
			throw new IllegalArgumentException("No plugin specified");
		}
	}

	@Override
	public boolean isPriority() {
		return this.priority;
	}

	@NotNull
	@Override
	public BigDecimal getAmount() {
		return this.bigDecimal;
	}

	@NotNull
	@Override
	public Currency<?> getCurrency() {
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
	public Optional<Account> getFrom() {
		return Optional.ofNullable(this.from);
	}
}

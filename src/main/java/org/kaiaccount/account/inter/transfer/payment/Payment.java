package org.kaiaccount.account.inter.transfer.payment;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.type.Account;
import org.kaiaccount.account.inter.currency.Currency;

import java.math.BigDecimal;
import java.util.Optional;

public interface Payment {

	@NotNull
	BigDecimal getAmount();

	@NotNull
	Currency getCurrency();

	@NotNull
	Plugin getPlugin();

	@NotNull
	Optional<String> getReason();

	@NotNull
	Optional<Account<?>> getFrom();
}

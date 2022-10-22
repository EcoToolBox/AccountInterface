package org.kaiaccount.account.inter;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.io.Serializable;
import org.kaiaccount.account.inter.transfer.Transaction;
import org.kaiaccount.account.inter.transfer.payment.Payment;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface Account<Self> extends Serializable<Self> {

	@NotNull
	BigDecimal getBalance(@NotNull Currency currency);

	@NotNull
	Map<Currency, BigDecimal> getBalances();

	@NotNull
	CompletableFuture<Transaction> withdraw(@NotNull Payment payment);

	@NotNull
	CompletableFuture<Transaction> deposit(@NotNull Payment payment);
}

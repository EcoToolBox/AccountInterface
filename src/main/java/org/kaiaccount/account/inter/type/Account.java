package org.kaiaccount.account.inter.type;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.io.Serializable;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.transfer.result.TransactionResult;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface Account<Self> extends Serializable<Self> {

	@NotNull
	BigDecimal getBalance(@NotNull Currency currency);

	@NotNull
	Map<Currency, BigDecimal> getBalances();

	@NotNull
	CompletableFuture<TransactionResult> withdraw(@NotNull Payment payment);

	@NotNull
	CompletableFuture<TransactionResult> deposit(@NotNull Payment payment);

	@NotNull
	CompletableFuture<TransactionResult> set(@NotNull Payment payment);

	@NotNull
	CompletableFuture<String> multipleTransaction(@NotNull
	Function<Self, CompletableFuture<TransactionResult>>... transactions);
}

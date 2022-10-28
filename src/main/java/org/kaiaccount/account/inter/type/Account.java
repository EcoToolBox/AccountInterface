package org.kaiaccount.account.inter.type;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.transfer.Transaction;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.transfer.result.SingleTransactionResult;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface Account {

	@NotNull
	BigDecimal getBalance(@NotNull Currency<?> currency);

	@NotNull
	Map<Currency<?>, BigDecimal> getBalances();

	@NotNull
	CompletableFuture<SingleTransactionResult> withdraw(@NotNull Payment payment);

	@NotNull
	CompletableFuture<SingleTransactionResult> deposit(@NotNull Payment payment);

	@NotNull
	CompletableFuture<SingleTransactionResult> set(@NotNull Payment payment);

	@NotNull
	CompletableFuture<SingleTransactionResult> refund(@NotNull Transaction payment);

	@NotNull
	CompletableFuture<Void> forceSet(Payment payment);
}

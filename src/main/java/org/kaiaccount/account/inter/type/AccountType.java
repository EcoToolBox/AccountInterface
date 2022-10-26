package org.kaiaccount.account.inter.type;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.transfer.result.TransactionResult;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.IntFunction;

public interface AccountType<Self> extends Account<Self> {

	IsolatedAccount getIsolated();

	@NotNull
	@Override
	default BigDecimal getBalance(@NotNull Currency currency) {
		return this.getIsolated().getBalance(currency);
	}

	@NotNull
	@Override
	default Map<Currency, BigDecimal> getBalances() {
		return this.getIsolated().getBalances();
	}

	@NotNull
	@Override
	default CompletableFuture<TransactionResult> withdraw(@NotNull Payment payment) {
		return this.getIsolated().withdraw(payment);
	}

	@NotNull
	@Override
	default CompletableFuture<TransactionResult> deposit(@NotNull Payment payment) {
		return this.getIsolated().deposit(payment);
	}

	@NotNull
	@Override
	default CompletableFuture<TransactionResult> set(@NotNull Payment payment) {
		return this.getIsolated().set(payment);
	}

	@NotNull
	@Override
	default CompletableFuture<String> multipleTransaction(
			@NotNull Function<Self, CompletableFuture<TransactionResult>>... transactions) {
		return this.getIsolated()
				.multipleTransaction(Arrays.stream(transactions)
						.map(function -> new Function<IsolatedAccount, CompletableFuture<TransactionResult>>() {

							@Override
							public CompletableFuture<TransactionResult> apply(IsolatedAccount isolatedAccount) {
								return function.apply((Self) this);
							}
						})
						.toArray(
								(IntFunction<Function<IsolatedAccount, CompletableFuture<TransactionResult>>[]>) Function[]::new));
	}
}

package org.kaiaccount.account.inter.type;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.transfer.Transaction;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.transfer.result.SingleTransactionResult;
import org.kaiaccount.account.inter.transfer.result.TransactionResult;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface AccountType extends AccountSynced {

	IsolatedAccount getIsolated();

	@NotNull
	@Override
	default BigDecimal getBalance(@NotNull Currency<?> currency) {
		return this.getIsolated().getBalance(currency);
	}

	@NotNull
	@Override
	default Map<Currency<?>, BigDecimal> getBalances() {
		return this.getIsolated().getBalances();
	}

	@NotNull
	@Override
	default CompletableFuture<SingleTransactionResult> withdraw(@NotNull Payment payment) {
		return this.getIsolated().withdraw(payment, this);
	}

	@NotNull
	@Override
	default SingleTransactionResult withdrawSynced(@NotNull Payment payment) {
		return this.getIsolated().withdrawSynced(payment, this);
	}

	@NotNull
	@Override
	default CompletableFuture<SingleTransactionResult> deposit(@NotNull Payment payment) {
		return this.getIsolated().deposit(payment, this);
	}

	@NotNull
	@Override
	default SingleTransactionResult depositSynced(@NotNull Payment payment) {
		return this.getIsolated().depositSynced(payment, this);
	}

	@NotNull
	@Override
	default CompletableFuture<SingleTransactionResult> set(@NotNull Payment payment) {
		return this.getIsolated().set(payment, this);
	}

	@NotNull
	@Override
	default SingleTransactionResult setSynced(@NotNull Payment payment) {
		return this.getIsolated().setSynced(payment, this);
	}

	@NotNull
	@Override
	default CompletableFuture<SingleTransactionResult> refund(@NotNull Transaction payment) {
		return this.getIsolated().refund(payment, this);
	}

	@NotNull
	@Override
	default CompletableFuture<Void> forceSet(@NotNull Payment payment) {
		return this.getIsolated().forceSet(payment, this);
	}

	@NotNull
	@Override
	default SingleTransactionResult refundSynced(@NotNull Transaction payment) {
		return this.getIsolated().refundSynced(payment, this);
	}

	@Override
	default void forceSetSynced(@NotNull Payment payment) {
		this.getIsolated().forceSetSynced(payment, this);
	}

	@NotNull
	CompletableFuture<TransactionResult> multipleTransaction(@NotNull
	Function<IsolatedAccount, CompletableFuture<? extends TransactionResult>>... transactions);

	/*@NotNull
	@Override
	default CompletableFuture<String> multipleTransaction(
			@NotNull Function<Self, CompletableFuture<TransactionResult>>... transactions) {
		return this.getIsolated()
				.multipleTransaction(Arrays.stream(transactions)
						.map(function -> new Function<IsolatedAccount, CompletableFuture<TransactionResult>>() {

							@Override
							public CompletableFuture<TransactionResult> apply(IsolatedAccount isolatedAccount) {
								//noinspection DataFlowIssue
								return function.apply((Self) (Object) this);
							}
						})
						.toArray(
								(IntFunction<Function<IsolatedAccount, CompletableFuture<TransactionResult>>[]>)
								Function[]::new));
	}*/
}

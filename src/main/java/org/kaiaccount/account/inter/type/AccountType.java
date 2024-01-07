package org.kaiaccount.account.inter.type;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Async;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
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

    @NotNull IsolatedAccount getIsolated();

    @NotNull
    @Override
    @CheckReturnValue
    default BigDecimal getBalance(@NotNull Currency<?> currency) {
        return this.getIsolated().getBalance(currency);
    }

    @NotNull
    @UnmodifiableView
    @Override
    @CheckReturnValue
    default Map<Currency<?>, BigDecimal> getBalances() {
        return this.getIsolated().getBalances();
    }

    @NotNull
    @Override
    @Async.Execute
    @CheckReturnValue
    default CompletableFuture<SingleTransactionResult> withdraw(@NotNull Payment payment) {
        return this.getIsolated().withdraw(payment, this);
    }

    @NotNull
    @Override
    @Async.Execute
    @CheckReturnValue
    default SingleTransactionResult withdrawSynced(@NotNull Payment payment) {
        return this.getIsolated().withdrawSynced(payment, this);
    }

    @NotNull
    @Override
    @Async.Execute
    @CheckReturnValue
    default CompletableFuture<SingleTransactionResult> deposit(@NotNull Payment payment) {
        return this.getIsolated().deposit(payment, this);
    }

    @NotNull
    @Override
    @CheckReturnValue
    default SingleTransactionResult depositSynced(@NotNull Payment payment) {
        return this.getIsolated().depositSynced(payment, this);
    }

    @NotNull
    @Override
    @CheckReturnValue
    default CompletableFuture<SingleTransactionResult> set(@NotNull Payment payment) {
        return this.getIsolated().set(payment, this);
    }

    @NotNull
    @Override
    @CheckReturnValue
    default SingleTransactionResult setSynced(@NotNull Payment payment) {
        return this.getIsolated().setSynced(payment, this);
    }

    @NotNull
    @Override
    @Async.Execute
    @CheckReturnValue
    default CompletableFuture<SingleTransactionResult> refund(@NotNull Transaction payment) {
        return this.getIsolated().refund(payment, this);
    }

    @NotNull
    @Override
    @Async.Execute
    default CompletableFuture<Void> forceSet(@NotNull Payment payment) {
        return this.getIsolated().forceSet(payment, this);
    }

    @NotNull
    @Override
    @CheckReturnValue
    default SingleTransactionResult refundSynced(@NotNull Transaction payment) {
        return this.getIsolated().refundSynced(payment, this);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    default void forceSetSynced(@NotNull Payment payment) {
        this.getIsolated().forceSetSynced(payment, this);
    }

    @NotNull
    @Async.Execute
    @CheckReturnValue
    CompletableFuture<TransactionResult> multipleTransaction(@NotNull
                                                             Function<IsolatedAccount, CompletableFuture<? extends TransactionResult>>... transactions);

}

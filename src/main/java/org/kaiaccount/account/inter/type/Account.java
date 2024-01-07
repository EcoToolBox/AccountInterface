package org.kaiaccount.account.inter.type;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Async;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.transfer.Transaction;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.transfer.result.SingleTransactionResult;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface Account {

    @NotNull
    @CheckReturnValue
    BigDecimal getBalance(@NotNull Currency<?> currency);

    @NotNull
    @UnmodifiableView
    @CheckReturnValue
    Map<Currency<?>, BigDecimal> getBalances();

    @NotNull
    @Async.Execute
    @CheckReturnValue
    CompletableFuture<SingleTransactionResult> withdraw(@NotNull Payment payment);

    @NotNull
    @Async.Execute
    @CheckReturnValue
    CompletableFuture<SingleTransactionResult> deposit(@NotNull Payment payment);

    @NotNull
    @Async.Execute
    @CheckReturnValue
    CompletableFuture<SingleTransactionResult> set(@NotNull Payment payment);

    @NotNull
    @Async.Execute
    @CheckReturnValue
    CompletableFuture<SingleTransactionResult> refund(@NotNull Transaction payment);

    @NotNull
    @Async.Execute
    CompletableFuture<Void> forceSet(@NotNull Payment payment);
}

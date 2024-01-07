package org.kaiaccount.account.inter.transfer.result.failed;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.kaiaccount.account.inter.transfer.Transaction;
import org.kaiaccount.account.inter.transfer.result.MultipleTransactionResult;
import org.kaiaccount.account.inter.transfer.result.TransactionResult;

import java.util.Arrays;
import java.util.Collection;

public class MultipleFailedTransactionResult implements TransactionResult, FailedTransactionResult,
        MultipleTransactionResult {

    private final @NotNull Collection<Transaction> transaction;
    private final @NotNull String failReason;

    public MultipleFailedTransactionResult(@NotNull String failReason, @NotNull Transaction... transactions) {
        this(failReason, Arrays.asList(transactions));
    }

    public MultipleFailedTransactionResult(@NotNull String failReason, @NotNull Collection<Transaction> transaction) {
        this.transaction = transaction;
        this.failReason = failReason;
    }

    @Override
    @CheckReturnValue
    @Nls
    public @NotNull String getReason() {
        return this.failReason;
    }

    @Override
    @CheckReturnValue
    @UnmodifiableView
    public @NotNull Collection<Transaction> getTransactions() {
        return this.transaction;
    }
}

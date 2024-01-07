package org.kaiaccount.account.inter.transfer.result.successful;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.transfer.Transaction;
import org.kaiaccount.account.inter.transfer.result.MultipleTransactionResult;

import java.util.Arrays;
import java.util.Collection;

public class MultipleSuccessfulTransactionResult implements SuccessfulTransactionResult, MultipleTransactionResult {

    private final @NotNull Collection<Transaction> transaction;

    public MultipleSuccessfulTransactionResult(@NotNull Transaction... transactions) {
        this(Arrays.asList(transactions));
    }

    public MultipleSuccessfulTransactionResult(@NotNull Collection<Transaction> transaction) {
        this.transaction = transaction;
    }

    @CheckReturnValue
    @Override
    public @NotNull Collection<Transaction> getTransactions() {
        return this.transaction;
    }
}

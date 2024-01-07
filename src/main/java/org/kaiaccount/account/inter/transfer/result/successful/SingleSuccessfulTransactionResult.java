package org.kaiaccount.account.inter.transfer.result.successful;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.transfer.Transaction;
import org.kaiaccount.account.inter.transfer.result.SingleTransactionResult;

public class SingleSuccessfulTransactionResult implements SuccessfulTransactionResult, SingleTransactionResult {

    private final @NotNull Transaction transaction;

    public SingleSuccessfulTransactionResult(@NotNull Transaction transactionResult) {
        this.transaction = transactionResult;
    }

    @NotNull
    @Override
    @CheckReturnValue
    public Transaction getTransaction() {
        return this.transaction;
    }
}

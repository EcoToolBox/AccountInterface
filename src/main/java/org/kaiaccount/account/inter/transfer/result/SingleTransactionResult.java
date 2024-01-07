package org.kaiaccount.account.inter.transfer.result;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.kaiaccount.account.inter.transfer.Transaction;

import java.util.Collection;
import java.util.Collections;

public interface SingleTransactionResult extends TransactionResult {

    @NotNull
    @CheckReturnValue
    Transaction getTransaction();

    @Override
    @NotNull
    @UnmodifiableView
    @CheckReturnValue
    default Collection<Transaction> getTransactions() {
        return Collections.singleton(getTransaction());
    }
}

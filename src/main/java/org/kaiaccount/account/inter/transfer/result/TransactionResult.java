package org.kaiaccount.account.inter.transfer.result;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.kaiaccount.account.inter.transfer.Transaction;

import java.util.Collection;

public interface TransactionResult {

    @NotNull
    @UnmodifiableView
    @CheckReturnValue

    Collection<Transaction> getTransactions();

}

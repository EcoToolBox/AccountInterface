package org.kaiaccount.account.inter.transfer.result;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.transfer.Transaction;

import java.util.Collection;
import java.util.Collections;

public interface SingleTransactionResult extends TransactionResult {

	@NotNull
	Transaction getTransaction();

	default Collection<Transaction> getTransactions() {
		return Collections.singleton(getTransaction());
	}
}

package org.kaiaccount.account.inter.transfer.result;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.transfer.Transaction;

import java.util.Collection;

public interface TransactionResult {

	@NotNull
	Collection<Transaction> getTransactions();

}

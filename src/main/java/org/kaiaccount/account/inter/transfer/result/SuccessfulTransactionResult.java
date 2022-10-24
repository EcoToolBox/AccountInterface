package org.kaiaccount.account.inter.transfer.result;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.transfer.Transaction;

public class SuccessfulTransactionResult implements TransactionResult {

	private final @NotNull Transaction transaction;

	public SuccessfulTransactionResult(@NotNull Transaction transaction) {
		this.transaction = transaction;
	}

	@Override
	public @NotNull Transaction getTransaction() {
		return this.transaction;
	}
}

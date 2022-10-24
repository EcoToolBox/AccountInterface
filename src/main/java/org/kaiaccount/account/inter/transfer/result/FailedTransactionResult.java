package org.kaiaccount.account.inter.transfer.result;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.transfer.Transaction;

public class FailedTransactionResult implements TransactionResult {

	private final @NotNull Transaction transaction;
	private final @NotNull String failReason;

	public FailedTransactionResult(@NotNull Transaction transaction, @NotNull String failReason) {
		this.transaction = transaction;
		this.failReason = failReason;
	}

	public @NotNull String getFailReason() {
		return this.failReason;
	}

	@Override
	public @NotNull Transaction getTransaction() {
		return this.transaction;
	}
}

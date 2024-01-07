package org.kaiaccount.account.inter.transfer.result.failed;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.transfer.Transaction;
import org.kaiaccount.account.inter.transfer.result.SingleTransactionResult;

public class SingleFailedTransactionResult implements FailedTransactionResult, SingleTransactionResult {

	private final @NotNull Transaction transaction;
	private final @NotNull String reason;

	public SingleFailedTransactionResult(@NotNull String reason, @NotNull Transaction transaction) {
		this.transaction = transaction;
		this.reason = reason;
	}

	@NotNull
	@Override
	@CheckReturnValue
	public Transaction getTransaction() {
		return this.transaction;
	}

	@NotNull
	@Override
	@CheckReturnValue
	public String getReason() {
		return this.reason;
	}
}

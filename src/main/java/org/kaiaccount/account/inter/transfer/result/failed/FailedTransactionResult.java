package org.kaiaccount.account.inter.transfer.result.failed;

import org.jetbrains.annotations.NotNull;

public interface FailedTransactionResult {

	@NotNull
	String getReason();
}

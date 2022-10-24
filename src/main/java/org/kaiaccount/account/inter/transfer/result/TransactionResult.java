package org.kaiaccount.account.inter.transfer.result;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.transfer.Transaction;

public interface TransactionResult {

	@NotNull
	Transaction getTransaction();

}

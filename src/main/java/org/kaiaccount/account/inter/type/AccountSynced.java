package org.kaiaccount.account.inter.type;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.transfer.Transaction;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.transfer.result.SingleTransactionResult;

public interface AccountSynced extends Account {

	@NotNull
	SingleTransactionResult withdrawSynced(@NotNull Payment payment);

	@NotNull
	SingleTransactionResult depositSynced(@NotNull Payment payment);

	@NotNull
	SingleTransactionResult setSynced(@NotNull Payment payment);

	@NotNull
	SingleTransactionResult refundSynced(@NotNull Transaction payment);

	void forceSetSynced(@NotNull Payment payment);
}

package org.kaiaccount.account.inter.type;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.transfer.Transaction;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.transfer.result.SingleTransactionResult;

public interface AccountSynced extends Account {

	@NotNull
	@CheckReturnValue
	SingleTransactionResult withdrawSynced(@NotNull Payment payment);

	@NotNull
	@CheckReturnValue
	SingleTransactionResult depositSynced(@NotNull Payment payment);

	@NotNull
	@CheckReturnValue
	SingleTransactionResult setSynced(@NotNull Payment payment);

	@NotNull
	@CheckReturnValue
	SingleTransactionResult refundSynced(@NotNull Transaction payment);

	void forceSetSynced(@NotNull Payment payment);
}

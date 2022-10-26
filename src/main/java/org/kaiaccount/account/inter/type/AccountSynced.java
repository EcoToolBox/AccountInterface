package org.kaiaccount.account.inter.type;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.transfer.result.TransactionResult;

public interface AccountSynced<Self extends AccountSynced<Self>> extends Account<Self> {

	@NotNull
	TransactionResult withdrawSynced(@NotNull Payment payment);

	@NotNull
	TransactionResult depositSynced(@NotNull Payment payment);

	@NotNull
	TransactionResult setSynced(@NotNull Payment payment);
}

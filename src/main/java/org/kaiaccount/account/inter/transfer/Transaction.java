package org.kaiaccount.account.inter.transfer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.type.Account;

import java.math.BigDecimal;

public interface Transaction {

	@NotNull
	Payment getPayment();

	@NotNull
	BigDecimal getNewPaymentAmount();

	@NotNull
	TransactionType getType();

	void setNewPaymentAmount(@Nullable BigDecimal decimal);

	@NotNull
	Account getTarget();

	default void setDefaultAmount() {
		this.setNewPaymentAmount(null);
	}

	default @NotNull Currency<?> getCurrency() {
		return this.getPayment().getCurrency();
	}
}

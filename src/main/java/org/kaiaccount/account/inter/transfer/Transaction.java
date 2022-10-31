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

	default @NotNull BigDecimal getAmountAdded() {
		BigDecimal amount = getTarget().getBalance(this.getCurrency());
		switch (this.getType()) {
			case WITHDRAW -> {
				return amount.subtract(this.getNewPaymentAmount());
			}
			case SET -> {
				return this.getNewPaymentAmount().subtract(amount);
			}
			case DEPOSIT -> {
				return amount.add(this.getNewPaymentAmount());
			}
			default -> throw new RuntimeException("Unknown type of " + this.getType().name());
		}
	}
}

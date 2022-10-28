package org.kaiaccount.account.inter.transfer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.type.Account;

import java.math.BigDecimal;

public class KaiTransaction implements Transaction {

	private final @NotNull Payment payment;
	private final @NotNull TransactionType type;
	private final @NotNull Account account;
	private @Nullable BigDecimal amount;

	public KaiTransaction(@NotNull TransactionBuilder builder) {
		this.payment = builder.getPayment();
		this.type = builder.getType();
		this.account = builder.getAccount();
	}

	@NotNull
	@Override
	public Payment getPayment() {
		return this.payment;
	}

	@NotNull
	@Override
	public BigDecimal getNewPaymentAmount() {
		if (this.amount == null) {
			return this.payment.getAmount();
		}
		return this.amount;
	}

	@NotNull
	@Override
	public TransactionType getType() {
		return this.type;
	}

	@Override
	public void setNewPaymentAmount(@Nullable BigDecimal decimal) {
		this.amount = decimal;
	}

	@NotNull
	@Override
	public Account getTarget() {
		return this.account;
	}
}

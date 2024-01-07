package org.kaiaccount.account.inter.transfer;

import org.jetbrains.annotations.CheckReturnValue;
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
    @CheckReturnValue
    public Payment getPayment() {
        return this.payment;
    }

    @NotNull
    @Override
    @CheckReturnValue
    public BigDecimal getNewPaymentAmount() {
        if (this.amount == null) {
            return this.payment.getAmount();
        }
        return this.amount;
    }

    @Override
    public void setNewPaymentAmount(@Nullable BigDecimal decimal) {
        this.amount = decimal;
    }

    @NotNull
    @Override
    @CheckReturnValue
    public TransactionType getType() {
        return this.type;
    }

    @NotNull
    @CheckReturnValue
    @Override
    public Account getTarget() {
        return this.account;
    }
}

package org.kaiaccount.account.inter.transfer;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.type.Account;
import org.kaiaccount.utils.builder.Buildable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface Transaction extends Buildable<Transaction, TransactionBuilder> {

    @NotNull
    @CheckReturnValue
    Payment getPayment();

    @NotNull
    @CheckReturnValue
    BigDecimal getNewPaymentAmount();

    void setNewPaymentAmount(@Nullable BigDecimal decimal);

    @NotNull
    @CheckReturnValue
    TransactionType getType();

    @NotNull
    @CheckReturnValue
    Account getTarget();

    @Override
    default TransactionBuilder toBuilder() {
        return new TransactionBuilder()
                .setType(this.getType())
                .setTime(this.getTime())
                .setPayment(this.getPayment())
                .setAmount(this.getNewPaymentAmount())
                .setAccount(this.getTarget());
    }

    @NotNull
    @CheckReturnValue
    LocalDateTime getTime();

    default void setDefaultAmount() {
        this.setNewPaymentAmount(null);
    }

    @CheckReturnValue
    default @NotNull Currency<?> getCurrency() {
        return this.getPayment().getCurrency();
    }

    @CheckReturnValue
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

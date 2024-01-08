package org.kaiaccount.account.inter.transfer;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.type.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class KaiTransaction implements Transaction {

    private final @NotNull Payment payment;
    private final @NotNull TransactionType type;
    private final @NotNull Account account;
    private final @NotNull LocalDateTime time;
    private @Nullable BigDecimal amount;


    public KaiTransaction(@NotNull TransactionBuilder builder) {
        this.payment = Objects.requireNonNull(builder.getPayment(), "Payment is null");
        this.type = Objects.requireNonNull(builder.getType(), "Type is null");
        this.account = Objects.requireNonNull(builder.getAccount(), "Account is null");
        this.time = Objects.requireNonNullElseGet(builder.getTime(), LocalDateTime::now);
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

    @Override
    public @NotNull LocalDateTime getTime() {
        return this.time;
    }
}

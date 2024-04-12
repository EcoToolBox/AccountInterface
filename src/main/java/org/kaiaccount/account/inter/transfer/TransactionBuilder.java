package org.kaiaccount.account.inter.transfer;

import org.jetbrains.annotations.CheckReturnValue;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.type.Account;
import org.kaiaccount.utils.builder.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionBuilder implements Builder<Transaction, TransactionBuilder> {

    private Payment payment;
    private TransactionType type;
    private Account account;
    private BigDecimal amount;
    private LocalDateTime time;

    public LocalDateTime getTime() {
        return this.time;
    }

    public TransactionBuilder setTime(LocalDateTime time) {
        this.time = time;
        return this;
    }

    @Override
    public TransactionBuilder from(TransactionBuilder builder) {
        this.setAccount(builder.getAccount());
        this.setPayment(builder.getPayment());
        this.setTime(builder.getTime());
        this.setType(builder.getType());
        this.setAmount(builder.getAmount());
        return this;
    }

    @CheckReturnValue
    @Override
    public Transaction build() {
        return new KaiTransaction(this);
    }

    public Payment getPayment() {
        return payment;
    }

    public TransactionBuilder setPayment(Payment payment) {
        this.payment = payment;
        return this;
    }

    public TransactionType getType() {
        return type;
    }

    public TransactionBuilder setType(TransactionType type) {
        this.type = type;
        return this;
    }

    public Account getAccount() {
        return account;
    }

    public TransactionBuilder setAccount(Account account) {
        this.account = account;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionBuilder setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }
}

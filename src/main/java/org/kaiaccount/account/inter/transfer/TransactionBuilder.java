package org.kaiaccount.account.inter.transfer;

import org.jetbrains.annotations.CheckReturnValue;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.type.Account;

import java.math.BigDecimal;

public class TransactionBuilder {

    private Payment payment;
    private TransactionType type;
    private Account account;
    private BigDecimal amount;

    @CheckReturnValue
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

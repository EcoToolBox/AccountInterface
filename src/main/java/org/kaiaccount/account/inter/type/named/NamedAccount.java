package org.kaiaccount.account.inter.type.named;

import org.kaiaccount.utils.builder.Buildable;

public interface NamedAccount extends NamedAccountLike, Buildable<NamedAccount, NamedAccountBuilder> {

    @Override
    default NamedAccountBuilder toBuilder() {
        return new NamedAccountBuilder().setAccountName(this.getAccountName()).setInitialBalance(this.getBalances());
    }
}

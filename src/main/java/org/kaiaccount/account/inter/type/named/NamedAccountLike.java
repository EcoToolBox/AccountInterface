package org.kaiaccount.account.inter.type.named;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.type.Account;

public interface NamedAccountLike extends Account {

    @NotNull
    @Nls
    String getAccountName();

}

package org.kaiaccount.account.inter.type.bank.player;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

public interface ToBankAccount {

    @NotNull
    @CheckReturnValue
    PlayerBankAccount<?> toBankAccount(@NotNull PlayerBankAccountBuilder builder);
}

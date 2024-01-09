package org.kaiaccount.account.inter.type.named.bank.player;

import org.jetbrains.annotations.NotNull;

public interface ToBankAccount {

    @NotNull
    PlayerBankAccount toBankAccount(@NotNull PlayerBankAccountBuilder builder);
}

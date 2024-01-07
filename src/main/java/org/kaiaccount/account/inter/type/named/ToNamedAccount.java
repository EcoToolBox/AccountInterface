package org.kaiaccount.account.inter.type.named;

import org.jetbrains.annotations.NotNull;

public interface ToNamedAccount {

    @NotNull
    NamedAccount toNamedAccount(@NotNull NamedAccountBuilder builder);
}

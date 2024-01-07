package org.kaiaccount.account.inter.transfer.result.failed;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public interface FailedTransactionResult {

    @NotNull
    @CheckReturnValue
    @Nls
    String getReason();
}

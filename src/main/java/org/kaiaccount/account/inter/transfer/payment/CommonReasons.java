package org.kaiaccount.account.inter.transfer.payment;

import org.jetbrains.annotations.Nls;

public final class CommonReasons {

    @Nls
    public static final String REFUND = "refund";

    @Nls
    public static final String DIRECT_DEBIT = "direct debit";

    private CommonReasons() {
        throw new RuntimeException("Common reasons");
    }

}

package org.kaiaccount.account.inter.transfer.payment;

public final class CommonReasons {

	public static final String REFUND = "refund";
	public static final String DIRECT_DEBIT = "direct debit";

	private CommonReasons() {
		throw new RuntimeException("Common reasons");
	}

}

package org.kaiaccount.account.inter.vault;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.transfer.Transaction;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.transfer.payment.PaymentBuilder;
import org.kaiaccount.account.inter.transfer.result.TransactionResult;
import org.kaiaccount.account.inter.transfer.result.failed.FailedTransactionResult;
import org.kaiaccount.account.inter.transfer.result.successful.MultipleSuccessfulTransactionResult;
import org.kaiaccount.account.inter.transfer.result.successful.SuccessfulTransactionResult;
import org.kaiaccount.account.inter.type.AccountSynced;
import org.kaiaccount.account.inter.type.player.PlayerAccount;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class KaiEco implements Economy {

	private final Supplier<Currency<?>> currency;
	private final int toDecimal;

	public KaiEco(Supplier<Currency<?>> currency, int toDecimal) {
		this.currency = currency;
		this.toDecimal = toDecimal;
	}


	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getName() {
		return "Vault to account";
	}

	@Override
	public boolean hasBankSupport() {
		return true;
	}

	@Override
	public int fractionalDigits() {
		return this.toDecimal;
	}

	@Override
	public String format(double v) {
		return this.currency.get().formatName(BigDecimal.valueOf(v), this.toDecimal);
	}

	@Override
	public String currencyNamePlural() {
		return this.currency.get().getDisplayNameMultiple();
	}

	@Override
	public String currencyNameSingular() {
		return this.currency.get().getDisplayNameSingle();
	}

	@Override
	@Deprecated
	public boolean hasAccount(String s) {
		return false;
	}

	@Override
	public boolean hasAccount(OfflinePlayer offlinePlayer) {
		return true;
	}

	@Override
	@Deprecated
	public boolean hasAccount(String s, String s1) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
		throw new RuntimeException("Not looked at yet -> Not implelemented");
	}

	@Override
	@Deprecated
	public double getBalance(String s) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public double getBalance(OfflinePlayer offlinePlayer) {
		return AccountInterface.getManager()
				.getPlayerAccount(offlinePlayer)
				.getBalance(this.currency.get())
				.doubleValue();
	}

	@Override
	@Deprecated
	public double getBalance(String s, String s1) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public double getBalance(OfflinePlayer offlinePlayer, String s) {
		throw new RuntimeException("Not looked at yet -> Not implelemented");

	}

	@Override
	@Deprecated
	public boolean has(String s, double v) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public boolean has(OfflinePlayer offlinePlayer, double v) {
		return AccountInterface.getManager()
				.getPlayerAccount(offlinePlayer)
				.getBalance(this.currency.get())
				.compareTo(BigDecimal.valueOf(v)) > 0;
	}

	@Override
	@Deprecated
	public boolean has(String s, String s1, double v) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	@Override
	@Deprecated
	public EconomyResponse withdrawPlayer(String s, double v) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
		Payment payment =
				new PaymentBuilder().setAmount(v)
						.setCurrency(this.currency.get())
						.build(AccountInterface.getManager().getVaultPlugin());
		PlayerAccount<?> account = AccountInterface.getManager().getPlayerAccount(offlinePlayer);
		if (!(account instanceof AccountSynced syncedAccount)) {
			throw new RuntimeException("Currency plugin does not have support for vault. -> "
					+ account.getClass().getSimpleName()
					+ " does not implement AccountSynced");
		}
		TransactionResult transaction = syncedAccount.withdrawSynced(payment);
		return createResponse(transaction);
	}

	@Override
	@Deprecated
	public EconomyResponse withdrawPlayer(String s, String s1, double v) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	@Override
	@Deprecated
	public EconomyResponse depositPlayer(String s, double v) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
		Payment payment =
				new PaymentBuilder().setAmount(v)
						.setCurrency(this.currency.get())
						.build(AccountInterface.getManager().getVaultPlugin());
		PlayerAccount<?> account = AccountInterface.getManager().getPlayerAccount(offlinePlayer);
		if (!(account instanceof AccountSynced syncedAccount)) {
			throw new RuntimeException("Currency plugin does not have support for vault. -> "
					+ account.getClass().getSimpleName()
					+ " does not implement AccountSynced");
		}
		TransactionResult transaction = syncedAccount.depositSynced(payment);
		return createResponse(transaction);
	}

	@Override
	@Deprecated
	public EconomyResponse depositPlayer(String s, String s1, double v) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	@Override
	@Deprecated
	public EconomyResponse createBank(String s, String s1) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	@Override
	public EconomyResponse deleteBank(String s) {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	@Override
	public EconomyResponse bankBalance(String s) {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	@Override
	public EconomyResponse bankHas(String s, double v) {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	@Override
	public EconomyResponse bankWithdraw(String s, double v) {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	@Override
	public EconomyResponse bankDeposit(String s, double v) {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	@Override
	@Deprecated
	public EconomyResponse isBankOwner(String s, String s1) {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	@Override
	public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	@Override
	@Deprecated
	public EconomyResponse isBankMember(String s, String s1) {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	@Override
	public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	@Override
	public List<String> getBanks() {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	@Override
	@Deprecated
	public boolean createPlayerAccount(String s) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
		return true;
	}

	@Override
	@Deprecated
	public boolean createPlayerAccount(String s, String s1) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	private EconomyResponse createResponse(@NotNull TransactionResult result) {
		Collection<Transaction> transactions = result.getTransactions();
		if (transactions.isEmpty()) {
			return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED,
					"Unknown transaction occurred");
		}
		Transaction transaction = transactions.iterator().next();

		BigDecimal currentBalance = transaction.getTarget().getBalance(transaction.getCurrency());
		double takeAmount = transaction.getNewPaymentAmount().doubleValue();
		BigDecimal newBalance;
		switch (transaction.getType()) {
			case WITHDRAW -> newBalance = currentBalance.subtract(transaction.getNewPaymentAmount());
			case SET -> newBalance = transaction.getNewPaymentAmount();
			case DEPOSIT -> newBalance = currentBalance.add(transaction.getNewPaymentAmount());
			default -> throw new RuntimeException("Unknown transaction type of " + transaction.getType().name());
		}
		if (result instanceof SuccessfulTransactionResult) {
			return new EconomyResponse(takeAmount, newBalance.doubleValue(), EconomyResponse.ResponseType.SUCCESS,
					null);
		}
		if (result instanceof FailedTransactionResult failed) {
			return new EconomyResponse(takeAmount, newBalance.doubleValue(), EconomyResponse.ResponseType.FAILURE,
					failed.getReason());
		}
		throw new RuntimeException("Unknown result type of " + result.getClass().getName());
	}
}

package org.kaiaccount.account.inter.vault;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;
import org.kaiaccount.account.inter.Account;
import org.kaiaccount.account.inter.Currency;
import org.kaiaccount.account.inter.transfer.Transaction;
import org.kaiaccount.account.inter.transfer.TransactionType;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.transfer.payment.PaymentBuilder;
import org.kaiaccount.account.inter.type.player.PlayerAccount;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public class KaiEco implements Economy {

	private final Supplier<Currency> currency;
	private final int toDecimal;

	public KaiEco(Supplier<Currency> currency, int toDecimal) {
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
	public double getBalance(String s) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public double getBalance(OfflinePlayer offlinePlayer) {
		return AccountInterface.getPlugin()
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
	public boolean has(String s, double v) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public boolean has(OfflinePlayer offlinePlayer, double v) {
		return AccountInterface.getPlugin()
				.getPlayerAccount(offlinePlayer)
				.getBalance(this.currency.get())
				.compareTo(BigDecimal.valueOf(v)) > 0;
	}

	@Override
	public boolean has(String s, String s1, double v) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	@Override
	public EconomyResponse withdrawPlayer(String s, double v) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
		Payment payment =
				new PaymentBuilder().setAmount(v).setCurrency(this.currency.get()).build(AccountInterface.getPlugin());
		PlayerAccount account = AccountInterface.getPlugin().getPlayerAccount(offlinePlayer);
		try {
			Transaction transaction = account.withdraw(payment).get(1, TimeUnit.MILLISECONDS);
			return createResponse(transaction);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			return createCancelled(account, "Cancelled event");
		}
	}

	@Override
	public EconomyResponse withdrawPlayer(String s, String s1, double v) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	@Override
	public EconomyResponse depositPlayer(String s, double v) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
		Payment payment =
				new PaymentBuilder().setAmount(v).setCurrency(this.currency.get()).build(AccountInterface.getPlugin());
		PlayerAccount account = AccountInterface.getPlugin().getPlayerAccount(offlinePlayer);
		try {
			Transaction transaction = account.deposit(payment).get(1, TimeUnit.MILLISECONDS);
			return createResponse(transaction);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			return createCancelled(account, "Cancelled event");
		}
	}

	@Override
	public EconomyResponse depositPlayer(String s, String s1, double v) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	@Override
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
	public EconomyResponse isBankOwner(String s, String s1) {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	@Override
	public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	@Override
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
	public boolean createPlayerAccount(String s) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
		return true;
	}

	@Override
	public boolean createPlayerAccount(String s, String s1) {
		throw new RuntimeException("Old vault method -> Not implemented yet");
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
		throw new RuntimeException("Not looked at yet -> Not implemented");
	}

	private EconomyResponse createCancelled(@NotNull Account<?> account, @NotNull String issue) {
		return new EconomyResponse(0, account.getBalance(this.currency.get()).doubleValue(),
				EconomyResponse.ResponseType.FAILURE, issue);
	}

	private EconomyResponse createResponse(Transaction result) {
		BigDecimal currentBalance = result.getTarget().getBalance(result.getCurrency());
		double takeAmount = result.getNewPaymentAmount().doubleValue();
		BigDecimal newBalance;
		if (result.getType() == TransactionType.DEPOSIT) {
			newBalance = currentBalance.add(result.getNewPaymentAmount());
		} else {
			newBalance = currentBalance.subtract(result.getNewPaymentAmount());
		}
		return new EconomyResponse(takeAmount, newBalance.doubleValue(), EconomyResponse.ResponseType.SUCCESS, null);
	}
}

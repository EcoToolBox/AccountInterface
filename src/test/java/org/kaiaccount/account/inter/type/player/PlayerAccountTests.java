package org.kaiaccount.account.inter.type.player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kaiaccount.AccountInterface;
import org.kaiaccount.AccountInterfaceManager;
import org.kaiaccount.account.inter.Account;
import org.kaiaccount.account.inter.MockHelpers;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.currency.CurrencyBuilder;
import org.kaiaccount.account.inter.event.TransactionCompletedEvent;
import org.kaiaccount.account.inter.event.TransactionEvent;
import org.kaiaccount.account.inter.impl.FakeGlobalManager;
import org.kaiaccount.account.inter.transfer.TransactionType;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.transfer.payment.PaymentBuilder;
import org.kaiaccount.account.inter.transfer.result.FailedTransactionResult;
import org.kaiaccount.account.inter.transfer.result.SuccessfulTransactionResult;
import org.kaiaccount.account.inter.transfer.result.TransactionResult;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.*;

public class PlayerAccountTests {

	private OfflinePlayer testPlayer;
	private Plugin testPlugin;
	private Currency testCurrency;

	private MockedStatic<Bukkit> bukkitMock;

	@BeforeEach
	public void setup() {
		if (!AccountInterface.isReady()) {
			AccountInterfaceManager manager = new FakeGlobalManager();
			AccountInterface.setGlobal(manager);
		}


		bukkitMock = Mockito.mockStatic(Bukkit.class);
		testPlugin = Mockito.mock(Plugin.class);
		testPlayer = Mockito.mock(OfflinePlayer.class);
		Mockito.when(testPlayer.getUniqueId()).thenReturn(UUID.randomUUID());
		testCurrency =
				new CurrencyBuilder().setDefault(true).setName("Pound").setSymbol("£").setPlugin(testPlugin).build();
	}

	@AfterEach
	public void remove() {
		bukkitMock.close();
		Mockito.clearAllCaches();
	}

	@Test
	public void testGetBalanceWithValidCurrency() {
		//setup
		Map<Currency, BigDecimal> currencies = new HashMap<>();
		currencies.put(testCurrency, BigDecimal.ONE);
		Account<PlayerAccount> account = new PlayerAccount(testPlayer, currencies);

		//run
		BigDecimal result = account.getBalance(testCurrency);

		//test
		Assertions.assertEquals(BigDecimal.ONE, result);

	}

	@Test
	public void testGetBalanceWithoutValidCurrency() {
		//setup
		Account<PlayerAccount> account = new PlayerAccount(testPlayer);

		//run
		BigDecimal result = account.getBalance(testCurrency);

		//test
		Assertions.assertEquals(BigDecimal.ZERO, result);
	}

	@Test
	public void testSyncedWithdrawWithValidAmount() {
		//setup
		PluginManager pluginManager = Mockito.mock(PluginManager.class);
		//noinspection unchecked
		MockHelpers.mockCallEvent(pluginManager, TransactionEvent.class);
		bukkitMock.when(Bukkit::getPluginManager).thenReturn(pluginManager);

		Map<Currency, BigDecimal> currencies = new HashMap<>();
		currencies.put(testCurrency, BigDecimal.valueOf(100));
		PlayerAccount account = new PlayerAccount(testPlayer, currencies);
		Payment payment = new PaymentBuilder().setAmount(10).setCurrency(testCurrency).build(testPlugin);

		//run
		TransactionResult result = account.withdrawSynced(payment);
		BigDecimal newAmount = account.getBalance(testCurrency);

		//test
		Assertions.assertTrue(result instanceof SuccessfulTransactionResult, "Result was not successful");
		Assertions.assertEquals(payment, result.getTransaction().getPayment());
		Assertions.assertEquals(TransactionType.WITHDRAW, result.getTransaction().getType());
		Assertions.assertEquals(testCurrency, result.getTransaction().getCurrency());
		Assertions.assertEquals(10.0, result.getTransaction().getNewPaymentAmount().doubleValue());
		Assertions.assertEquals(account, result.getTransaction().getTarget());
		Assertions.assertEquals(90.0, newAmount.doubleValue());
	}

	@Test
	public void testSyncedWithdrawWithInvalidAmount() {
		//setup
		PluginManager pluginManager = Mockito.mock(PluginManager.class);
		//noinspection unchecked
		MockHelpers.mockCallEvent(pluginManager, TransactionEvent.class);
		bukkitMock.when(Bukkit::getPluginManager).thenReturn(pluginManager);

		Map<Currency, BigDecimal> currencies = new HashMap<>();
		currencies.put(testCurrency, BigDecimal.valueOf(100));
		PlayerAccount account = new PlayerAccount(testPlayer, currencies);
		Payment payment = new PaymentBuilder().setAmount(101).setCurrency(testCurrency).build(testPlugin);

		//run
		TransactionResult result = account.withdrawSynced(payment);
		BigDecimal newAmount = account.getBalance(testCurrency);

		//test
		Assertions.assertTrue(result instanceof FailedTransactionResult, "Result was successful");
		Assertions.assertEquals("Account does not have 101.0", ((FailedTransactionResult) result).getFailReason());
		Assertions.assertEquals(payment, result.getTransaction().getPayment());
		Assertions.assertEquals(TransactionType.WITHDRAW, result.getTransaction().getType());
		Assertions.assertEquals(testCurrency, result.getTransaction().getCurrency());
		Assertions.assertEquals(101.0, result.getTransaction().getNewPaymentAmount().doubleValue());
		Assertions.assertEquals(account, result.getTransaction().getTarget());
		Assertions.assertEquals(100.0, newAmount.doubleValue());
	}

	@Test
	public void testSyncedWithdrawWithValidAmountThatIsCancelled() {
		//setup
		PluginManager pluginManager = Mockito.mock(PluginManager.class);
		//noinspection unchecked
		MockHelpers.mockCallEvent(pluginManager, TransactionEvent.class, event -> event.setCancelledReason("For "
				+ "Test"));
		bukkitMock.when(Bukkit::getPluginManager).thenReturn(pluginManager);

		Map<Currency, BigDecimal> currencies = new HashMap<>();
		currencies.put(testCurrency, BigDecimal.valueOf(100));
		PlayerAccount account = new PlayerAccount(testPlayer, currencies);
		Payment payment = new PaymentBuilder().setAmount(10).setCurrency(testCurrency).build(testPlugin);

		//run
		TransactionResult result = account.withdrawSynced(payment);
		BigDecimal newAmount = account.getBalance(testCurrency);

		//test
		Assertions.assertTrue(result instanceof FailedTransactionResult, "Result was successful");
		Assertions.assertEquals("For Test", ((FailedTransactionResult) result).getFailReason());
		Assertions.assertEquals(payment, result.getTransaction().getPayment());
		Assertions.assertEquals(TransactionType.WITHDRAW, result.getTransaction().getType());
		Assertions.assertEquals(testCurrency, result.getTransaction().getCurrency());
		Assertions.assertEquals(10.0, result.getTransaction().getNewPaymentAmount().doubleValue());
		Assertions.assertEquals(account, result.getTransaction().getTarget());
		Assertions.assertEquals(100.0, newAmount.doubleValue());
	}

	@Test
	public void testSyncedWithdrawWithValidAmountThatIsChangedInEvent() {
		//setup
		PluginManager pluginManager = Mockito.mock(PluginManager.class);
		//noinspection unchecked
		MockHelpers.mockCallEvent(pluginManager, TransactionEvent.class,
				event -> event.getTransaction().setNewPaymentAmount(BigDecimal.ONE));
		bukkitMock.when(Bukkit::getPluginManager).thenReturn(pluginManager);

		Map<Currency, BigDecimal> currencies = new HashMap<>();
		currencies.put(testCurrency, BigDecimal.valueOf(100));
		PlayerAccount account = new PlayerAccount(testPlayer, currencies);
		Payment payment = new PaymentBuilder().setAmount(10).setCurrency(testCurrency).build(testPlugin);

		//run
		TransactionResult result = account.withdrawSynced(payment);
		BigDecimal newAmount = account.getBalance(testCurrency);

		//test
		Assertions.assertTrue(result instanceof SuccessfulTransactionResult, "Result was not successful");
		Assertions.assertEquals(payment, result.getTransaction().getPayment());
		Assertions.assertEquals(TransactionType.WITHDRAW, result.getTransaction().getType());
		Assertions.assertEquals(testCurrency, result.getTransaction().getCurrency());
		Assertions.assertEquals(10.0, result.getTransaction().getPayment().getAmount().doubleValue());
		Assertions.assertEquals(BigDecimal.ONE, result.getTransaction().getNewPaymentAmount());
		Assertions.assertEquals(account, result.getTransaction().getTarget());
		Assertions.assertEquals(99.0, newAmount.doubleValue());
	}

	@Test
	public void testSyncedWithdrawWithValidAmountThatIsCapturedOnComplete() {
		List<String> result = new ArrayList<>();

		//setup
		PluginManager pluginManager = Mockito.mock(PluginManager.class);
		//noinspection unchecked
		MockHelpers.mockCallEvent(pluginManager, TransactionCompletedEvent.class,
				event -> result.add("Test 1121"));
		bukkitMock.when(Bukkit::getPluginManager).thenReturn(pluginManager);

		Map<Currency, BigDecimal> currencies = new HashMap<>();
		currencies.put(testCurrency, BigDecimal.valueOf(100));
		PlayerAccount account = new PlayerAccount(testPlayer, currencies);
		Payment payment = new PaymentBuilder().setAmount(10).setCurrency(testCurrency).build(testPlugin);

		//run
		account.withdrawSynced(payment);

		//test
		Assertions.assertFalse(result.isEmpty(), "Completed event did not fire");
		Assertions.assertEquals(result.get(0), "Test 1121");
	}

	@Test
	public void testSyncedDepositWithValidAmount() {
		//setup
		PluginManager pluginManager = Mockito.mock(PluginManager.class);
		//noinspection unchecked
		MockHelpers.mockCallEvent(pluginManager, TransactionEvent.class);
		bukkitMock.when(Bukkit::getPluginManager).thenReturn(pluginManager);

		Map<Currency, BigDecimal> currencies = new HashMap<>();
		currencies.put(testCurrency, BigDecimal.valueOf(100));
		PlayerAccount account = new PlayerAccount(testPlayer, currencies);
		Payment payment = new PaymentBuilder().setAmount(10).setCurrency(testCurrency).build(testPlugin);

		//run
		TransactionResult result = account.depositSynced(payment);
		BigDecimal newAmount = account.getBalance(testCurrency);

		//test
		Assertions.assertTrue(result instanceof SuccessfulTransactionResult, "Result was not successful");
		Assertions.assertEquals(payment, result.getTransaction().getPayment());
		Assertions.assertEquals(TransactionType.DEPOSIT, result.getTransaction().getType());
		Assertions.assertEquals(testCurrency, result.getTransaction().getCurrency());
		Assertions.assertEquals(10.0, result.getTransaction().getNewPaymentAmount().doubleValue());
		Assertions.assertEquals(account, result.getTransaction().getTarget());
		Assertions.assertEquals(110.0, newAmount.doubleValue());
	}

	@Test
	public void testSyncedDepositWithValidAmountThatIsCancelled() {
		//setup
		PluginManager pluginManager = Mockito.mock(PluginManager.class);
		//noinspection unchecked
		MockHelpers.mockCallEvent(pluginManager, TransactionEvent.class, event -> event.setCancelledReason("For "
				+ "Test"));
		bukkitMock.when(Bukkit::getPluginManager).thenReturn(pluginManager);

		Map<Currency, BigDecimal> currencies = new HashMap<>();
		currencies.put(testCurrency, BigDecimal.valueOf(100));
		PlayerAccount account = new PlayerAccount(testPlayer, currencies);
		Payment payment = new PaymentBuilder().setAmount(10).setCurrency(testCurrency).build(testPlugin);

		//run
		TransactionResult result = account.depositSynced(payment);
		BigDecimal newAmount = account.getBalance(testCurrency);

		//test
		Assertions.assertTrue(result instanceof FailedTransactionResult, "Result was successful");
		Assertions.assertEquals("For Test", ((FailedTransactionResult) result).getFailReason());
		Assertions.assertEquals(payment, result.getTransaction().getPayment());
		Assertions.assertEquals(TransactionType.DEPOSIT, result.getTransaction().getType());
		Assertions.assertEquals(testCurrency, result.getTransaction().getCurrency());
		Assertions.assertEquals(10.0, result.getTransaction().getNewPaymentAmount().doubleValue());
		Assertions.assertEquals(account, result.getTransaction().getTarget());
		Assertions.assertEquals(100.0, newAmount.doubleValue());
	}

	@Test
	public void testSyncedDepositWithValidAmountThatIsChangedInEvent() {
		//setup
		PluginManager pluginManager = Mockito.mock(PluginManager.class);
		//noinspection unchecked
		MockHelpers.mockCallEvent(pluginManager, TransactionEvent.class,
				event -> event.getTransaction().setNewPaymentAmount(BigDecimal.ONE));
		bukkitMock.when(Bukkit::getPluginManager).thenReturn(pluginManager);

		Map<Currency, BigDecimal> currencies = new HashMap<>();
		currencies.put(testCurrency, BigDecimal.valueOf(100));
		PlayerAccount account = new PlayerAccount(testPlayer, currencies);
		Payment payment = new PaymentBuilder().setAmount(10).setCurrency(testCurrency).build(testPlugin);

		//run
		TransactionResult result = account.depositSynced(payment);
		BigDecimal newAmount = account.getBalance(testCurrency);

		//test
		Assertions.assertTrue(result instanceof SuccessfulTransactionResult, "Result was not successful");
		Assertions.assertEquals(payment, result.getTransaction().getPayment());
		Assertions.assertEquals(TransactionType.DEPOSIT, result.getTransaction().getType());
		Assertions.assertEquals(testCurrency, result.getTransaction().getCurrency());
		Assertions.assertEquals(10.0, result.getTransaction().getPayment().getAmount().doubleValue());
		Assertions.assertEquals(BigDecimal.ONE, result.getTransaction().getNewPaymentAmount());
		Assertions.assertEquals(account, result.getTransaction().getTarget());
		Assertions.assertEquals(101.0, newAmount.doubleValue());
	}

	@Test
	public void testSyncedDepositWithValidAmountThatIsCapturedOnComplete() {
		List<String> result = new ArrayList<>();

		//setup
		PluginManager pluginManager = Mockito.mock(PluginManager.class);
		//noinspection unchecked
		MockHelpers.mockCallEvent(pluginManager, TransactionCompletedEvent.class,
				event -> result.add("Test 1121"));
		bukkitMock.when(Bukkit::getPluginManager).thenReturn(pluginManager);

		Map<Currency, BigDecimal> currencies = new HashMap<>();
		currencies.put(testCurrency, BigDecimal.valueOf(100));
		PlayerAccount account = new PlayerAccount(testPlayer, currencies);
		Payment payment = new PaymentBuilder().setAmount(10).setCurrency(testCurrency).build(testPlugin);

		//run
		account.depositSynced(payment);

		//test
		Assertions.assertFalse(result.isEmpty(), "Completed event did not fire");
		Assertions.assertEquals(result.get(0), "Test 1121");
	}
}

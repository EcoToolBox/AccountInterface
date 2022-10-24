package org.kaiaccount.account.inter.payment;

import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.transfer.payment.PaymentBuilder;
import org.kaiaccount.account.inter.type.player.PlayerAccount;
import org.mockito.Mockito;

import java.math.BigDecimal;

public class PaymentBuilderTests {

	private Plugin testPlugin;
	private Currency testCurrency;

	@BeforeEach
	public void setup() {
		testPlugin = Mockito.mock(Plugin.class);
		testCurrency = Mockito.mock(Currency.class);
	}

	@Test
	public void testBuildValidPaymentFull() {
		//setup
		BigDecimal amount = BigDecimal.ONE;
		PlayerAccount from = Mockito.mock(PlayerAccount.class);

		//run
		Payment payment = new PaymentBuilder().setCurrency(testCurrency)
				.setAmount(amount)
				.setFrom(from)
				.setReason("test")
				.build(testPlugin);

		//test
		Assertions.assertEquals(testPlugin, payment.getPlugin());
		Assertions.assertTrue(payment.getFrom().isPresent(), "'From' is missing");
		Assertions.assertEquals(from, payment.getFrom().orElse(null));
		Assertions.assertEquals(BigDecimal.ONE, payment.getAmount());
		Assertions.assertTrue(payment.getReason().isPresent(), "'reason' is missing");
		Assertions.assertEquals("test", payment.getReason().orElse(null));
	}

	@Test
	public void testBuildValidPaymentShort() {
		//setup
		BigDecimal amount = BigDecimal.ONE;

		//run
		Payment payment = new PaymentBuilder().setCurrency(testCurrency)
				.setAmount(amount)
				.build(testPlugin);

		//test
		Assertions.assertEquals(testPlugin, payment.getPlugin());
		Assertions.assertFalse(payment.getFrom().isPresent(), "'From' isn't missing");
		Assertions.assertEquals(BigDecimal.ONE, payment.getAmount());
		Assertions.assertFalse(payment.getReason().isPresent(), "'reason' isn't missing");
	}

	@Test
	public void testBuildInvalidPaymentMissingCurrency() {
		//setup
		BigDecimal amount = BigDecimal.ONE;

		//run and test
		Assertions.assertThrows(IllegalArgumentException.class, () -> new PaymentBuilder()
				.setAmount(amount)
				.build(testPlugin));
	}

	@Test
	public void testBuildWithNegativeNumber() {
		//setup
		BigDecimal amount = BigDecimal.valueOf(-1);

		//run and test
		Assertions.assertThrows(IllegalArgumentException.class, () -> new PaymentBuilder().setCurrency(testCurrency)
				.setAmount(amount)
				.build(testPlugin));

	}
}

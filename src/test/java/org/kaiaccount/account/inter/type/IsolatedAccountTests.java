package org.kaiaccount.account.inter.type;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.currency.CurrencyBuilder;
import org.kaiaccount.account.inter.impl.bank.FakeBankAccount;
import org.kaiaccount.account.inter.impl.currency.FakeCurrency;
import org.kaiaccount.account.inter.transfer.IsolatedTransaction;
import org.kaiaccount.account.inter.transfer.payment.Payment;
import org.kaiaccount.account.inter.transfer.payment.PaymentBuilder;
import org.kaiaccount.account.inter.transfer.result.SingleTransactionResult;
import org.kaiaccount.account.inter.transfer.result.TransactionResult;
import org.kaiaccount.account.inter.transfer.result.successful.SuccessfulTransactionResult;
import org.kaiaccount.account.inter.type.named.AbstractNamedAccountLike;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class IsolatedAccountTests {

    static Server bukkitServer = Mockito.mock(Server.class);
    static Logger logger = Logger.getGlobal();

    @BeforeAll
    public static void setup() {
        Mockito.when(bukkitServer.getLogger()).thenReturn(logger);
        Bukkit.setServer(bukkitServer);
    }

    @Test
    public void testSuccessfulTransactionResult() {
        //setup
        PluginManager pluginManager = Mockito.mock(PluginManager.class);
        Mockito.when(bukkitServer.getPluginManager()).thenReturn(pluginManager);

        BigDecimal bal = BigDecimal.valueOf(100);
        Plugin plugin = Mockito.mock(Plugin.class);
        Currency<?> currency = new FakeCurrency(new CurrencyBuilder().setPlugin(plugin).setName("test").setSymbol(
                "%"));

        AbstractNamedAccountLike bank1 = new FakeBankAccount("bank1", currency, bal);

        AbstractNamedAccountLike bank2 = new FakeBankAccount("bank2");

        //run
        CompletableFuture<TransactionResult> future = new IsolatedTransaction(accounts -> {
            Payment payment = new PaymentBuilder().setAmount(bal).setCurrency(currency).build(plugin);
            CompletableFuture<SingleTransactionResult> withdraw = accounts.get(bank1).withdraw(payment);
            CompletableFuture<SingleTransactionResult> deposit = accounts.get(bank2).deposit(payment);
            return List.of(withdraw, deposit);
        }, bank1, bank2).start();

        //test
        LocalTime startTime = LocalTime.now();
        while (!future.isDone()) {
            if (LocalTime.now().isAfter(startTime.plusSeconds(2))) {
                Assertions.fail("Time took too long to complete isolated transactions");
                return;
            }
        }
        TransactionResult result;
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertInstanceOf(SuccessfulTransactionResult.class, result);
        Assertions.assertEquals(BigDecimal.ZERO, bank1.getBalance(currency));
        Assertions.assertEquals(bal, bank2.getBalance(currency));


    }
}

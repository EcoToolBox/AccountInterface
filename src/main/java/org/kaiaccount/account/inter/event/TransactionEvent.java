package org.kaiaccount.account.inter.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kaiaccount.account.inter.transfer.Transaction;

import java.util.Optional;

public class TransactionEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final @NotNull Transaction transaction;
	private @Nullable String cancelledReason;

	public TransactionEvent(@NotNull Transaction transaction) {
		this.transaction = transaction;
	}

	public @NotNull Transaction getTransaction() {
		return this.transaction;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelledReason != null;
	}

	public void setCancelledReason(@Nullable String reason) {
		this.cancelledReason = reason;
	}

	public Optional<String> getCancelledReason() {
		return Optional.ofNullable(this.cancelledReason);
	}

	@Override
	@Deprecated
	public void setCancelled(boolean b) {
		if (b) {
			throw new RuntimeException("Must specify a reason with .setCancelledReason(String reason)");
		}
		this.cancelledReason = null;
	}
}

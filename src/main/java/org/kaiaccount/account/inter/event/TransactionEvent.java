package org.kaiaccount.account.inter.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.inter.transfer.Transaction;

public class TransactionEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final @NotNull Transaction transaction;
	private boolean cancelled;

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
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		this.cancelled = b;
	}
}

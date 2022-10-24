package org.kaiaccount.account.inter;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.mockito.Mockito;

import java.util.function.Consumer;

public class MockHelpers {

	public static <T extends Event> PluginManager mockCallEvent(PluginManager manager, Class<T> clazz,
			Consumer<T>... consumers) {
		Mockito.doAnswer(invocationOnMock -> {
			T event = invocationOnMock.getArgument(0);
			for (Consumer<T> eventHandler : consumers) {
				eventHandler.accept(event);
			}
			//noinspection ReturnOfNull
			return null;
		}).when(manager).callEvent(Mockito.any(clazz));
		return manager;
	}
}

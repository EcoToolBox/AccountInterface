package org.kaiaccount.account.inter.io;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public interface Serializable<Self> {

	Serializer<Self> getSerializer();

	default void save(@NotNull YamlConfiguration configuration) {
		this.getSerializer().serialize(configuration, (Self) this);
	}
}

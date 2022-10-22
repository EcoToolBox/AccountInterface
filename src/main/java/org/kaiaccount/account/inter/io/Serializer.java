package org.kaiaccount.account.inter.io;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface Serializer<T> {

	void serialize(@NotNull YamlConfiguration configuration, @NotNull T value);

	T deserialize(@NotNull YamlConfiguration configuration) throws IOException;

}

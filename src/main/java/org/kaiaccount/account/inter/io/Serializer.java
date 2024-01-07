package org.kaiaccount.account.inter.io;

import org.jetbrains.annotations.CheckReturnValue;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface Serializer<T extends Serializable<T>> {

    void serialize(@NotNull YamlConfiguration configuration, @NotNull T value);

    @CheckReturnValue
    T deserialize(@NotNull YamlConfiguration configuration) throws IOException;

}

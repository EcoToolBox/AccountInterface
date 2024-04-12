package org.kaiaccount.utils.builder;

import org.jetbrains.annotations.CheckReturnValue;

public interface Buildable<Self extends Buildable<Self, Build>, Build extends Builder<Self, Build>> {

    @CheckReturnValue
    Build toBuilder();
}

package com.clubfactory.dtree;

import java.util.function.Function;

public class ThreadScopeValueAccessor<IN, OUT> extends ValueAccessor<IN, OUT> {

    public ThreadScopeValueAccessor(String desc, Function<IN, OUT> getter) {
        super(desc, new ThreadScopeGetter<>(getter));
    }

}

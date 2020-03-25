package com.clubfactory.dtree;

import java.util.WeakHashMap;
import java.util.function.Function;

public class ThreadScopeGetter<T, R> implements Function<T, R> {

    private static final Object NULL = new Object();

    private final ThreadLocal<WeakHashMap<T, Object>> scope = ThreadLocal.withInitial(WeakHashMap::new);

    private static Object mask(Object object) {
        if (object == null) {
            return  NULL;
        }
        return object;
    }

    private static Object unmask(Object object) {
        if (object == NULL) {
            return null;
        }
        return object;
    }

    private Function<T, R> getter;

    public ThreadScopeGetter(Function<T, R> getter) {
        this.getter = getter;
    }

    @Override
    @SuppressWarnings("unchecked")
    public R apply(T input) {
        WeakHashMap<T, Object> cache = scope.get();
        Object output = cache.get(input);
        if (output == null) {
            output = this.getter.apply(input);
            cache.put(input, mask(output));
        } else {
            output = unmask(output);
        }
        return (R) output;
    }

}

package com.clubfactory.dtree;


@FunctionalInterface
public interface Policy<T> {

    void run(DTree<T> dtree, T target) throws NoMatchException;

    Policy<?> ONCE_POLICY = new OncePolicy<>();

    Policy<?> QUIET_ONCE_POLICY = ((dtree, target) -> {
        try {
            getOncePolicy().run(dtree, target);
        } catch (NoMatchException ignore) {}
    });

    Policy<?> RECURSIVE_POLICY = new RecursivePolicy<>();

    Policy<?> QUIET_RECURSIVE_POLICY = ((dtree, target) -> {
        try {
            getRecursivePolicy().run(dtree, target);
        } catch (NoMatchException ignore) {}
    });

    @SuppressWarnings("unchecked")
    static <T> Policy<T> getOncePolicy() {
        return (Policy<T>) ONCE_POLICY;
    }

    @SuppressWarnings("unchecked")
    static <T> Policy<T> getQuietOncePolicy() {
        return (Policy<T>) QUIET_ONCE_POLICY;
    }

    @SuppressWarnings("unchecked")
    static <T> Policy<T> getRecursivePolicy() {
        return (Policy<T>) RECURSIVE_POLICY;
    }

    @SuppressWarnings("unchecked")
    static <T> Policy<T> getQuietRecursivePolicy() {
        return (Policy<T>) QUIET_RECURSIVE_POLICY;
    }

}

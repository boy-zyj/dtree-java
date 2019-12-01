package com.clubfactory.dtree;


@FunctionalInterface
public interface Policy<T> {

    void run(DTree<T> dtree, T target) throws NoMatchException;

    Policy<?> ONCE_POLICY = new OncePolicy<>();

    Policy<?> RECURSIVE_POLICY = new RecursivePolicy<>();

    @SuppressWarnings("unchecked")
    static <T> Policy<T> getOncePolicy() {
        return (Policy<T>) ONCE_POLICY;
    }

    @SuppressWarnings("unchecked")
    static <T> Policy<T> getRecursivePolicy() {
        return (Policy<T>) RECURSIVE_POLICY;
    }

}

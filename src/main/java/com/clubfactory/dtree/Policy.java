package com.clubfactory.dtree;


@FunctionalInterface
public interface Policy<T> {

    void run(DTree<T> dtree, T target) throws NoMatchException;

    Policy<?> ONCE_POLICY = new OncePolicy<>();

    Policy<?> RECURSIVE_POLICY = new RecursivePolicy<>();

}

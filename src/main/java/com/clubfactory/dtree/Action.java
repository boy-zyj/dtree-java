package com.clubfactory.dtree;

public class Action<T> extends AbstractRunner<T> {

    public static final Action<?> PASS = new Action<>();

    @Override
    public void run(T target) throws NoMatchException {}

    @SuppressWarnings("unchecked")
    public static <T> Action<T> getPassInstance() {
        return (Action<T>) PASS;
    }

}

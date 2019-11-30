package com.clubfactory.dtree;

public class Action<T> extends AbstractRunner<T> {

    public static Action<?> PASS = new Action<>();

    @Override
    public void run(T target) throws NoMatchException {}

    @SuppressWarnings("unchecked")
    public static <T> Action<T> getInstance() {
        return (Action<T>) PASS;
    }

}

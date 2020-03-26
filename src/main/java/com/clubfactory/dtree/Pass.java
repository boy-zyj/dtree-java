package com.clubfactory.dtree;

public class Pass<T> extends Action<T> {

    public static final Action<?> PASS = new Pass<>();

    @Override
    public final void run(T target) {}

    @SuppressWarnings("unchecked")
    public static <T> Action<T> getPassInstance() {
        return (Action<T>) PASS;
    }

    @Override
    public String getDescription() {
        return "PASS";
    }

}

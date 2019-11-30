package com.clubfactory.dtree;

public class Action<T> extends AbstractRunner<T> {

    public static Action<?> PASS = new Action<>();

    @Override
    public void run(T target) throws NoMatchException {}

}

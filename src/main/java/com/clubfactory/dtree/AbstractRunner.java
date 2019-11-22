package com.clubfactory.dtree;

public abstract class AbstractRunner<T> extends SimpleDescription implements Runner<T>  {

    @Override
    public abstract void run(T target) throws NoMatchException;

}

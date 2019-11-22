package com.clubfactory.dtree;

public interface Runner<T> extends Description {

    void run(T target) throws NoMatchException;

}

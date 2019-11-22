package com.clubfactory.dtree;

public abstract class AbstractCondtion <T> extends SimpleDescription implements Condition<T> {

    @Override
    public abstract boolean validate(T target);

}

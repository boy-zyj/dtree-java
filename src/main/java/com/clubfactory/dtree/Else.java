package com.clubfactory.dtree;

public class Else<T> extends AbstractCondtion<T> {

    public static Else<?> ELSE = new Else<>();

    @Override
    public final boolean validate(T target) {
        return true;
    }

    @Override
    public final String getDescription() {
        return "ELSE";
    }

}

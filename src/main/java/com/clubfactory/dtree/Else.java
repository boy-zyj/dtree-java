package com.clubfactory.dtree;

public class Else<T> extends AbstractCondtion<T> {

    public static final Else<?> ELSE = new Else<>();

    @Override
    public final boolean validate(T target) {
        return true;
    }

    @Override
    public final String getDescription() {
        return "ELSE";
    }

    @SuppressWarnings("unchecked")
    public static <T> Else<T> getElseInstance() {
        return (Else<T>) ELSE;
    }

}

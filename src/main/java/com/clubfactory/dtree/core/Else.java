package com.clubfactory.dtree.core;

public class Else extends Condition {

    final static Else else_ = new Else();

    @Override
    public boolean validate(Center data) {
        return true;
    }

}

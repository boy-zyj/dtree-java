package com.clubfactory.dtree.core;


public class Not extends Condition {

    protected Condition condition;

    public Not(Condition condition) {
        this.condition = condition;
    }

    @Override
    public boolean validate(Center data) {
        return !condition.validate(data);
    }

    @Override
    public String getDescription() {
        return "NOT(" + condition.getDescription() + ")";
    }
}

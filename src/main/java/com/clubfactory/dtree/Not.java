package com.clubfactory.dtree;


public class Not<T> extends AbstractCondtion<T> {

    private Condition<T> condition;

    public Not(Condition<T> condition) {
        this.condition = condition;
    }

    @Override
    public boolean validate(T target) {
        return !condition.validate(target);
    }

    @Override
    protected String getDefaultDescription() {
        return "NOT(" + condition.getDescription() + ")";
    }

}


package com.clubfactory.dtree;


import java.util.Objects;

public class AssertAction<T> extends AbstractRunner<T> {

    private Condition<T> condition;

    public AssertAction(Condition<T> condition) {
        Objects.requireNonNull(condition, "condition cannot be null");
        this.condition = condition;
    }

    @Override
    public void run(T target) throws NoMatchException {
        if (!condition.validate(target)) {
            throw new DTreeAssertionException(getDescription());
        }
    }

    @Override
    protected String getDefaultDescription() {
        return  "assert " + condition.getDescription();
    }

}

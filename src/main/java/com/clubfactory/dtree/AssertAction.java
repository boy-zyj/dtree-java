package com.clubfactory.dtree;


public class AssertAction<T> extends AbstractRunner<T> {

    private Condition<T> condition;

    public AssertAction(Condition<T> condition) {
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

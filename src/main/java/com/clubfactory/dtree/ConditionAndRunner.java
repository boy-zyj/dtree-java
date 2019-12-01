package com.clubfactory.dtree;


class ConditionAndRunner<T> {

    private Condition<T> condition;
    private Runner<T> runner;

    ConditionAndRunner(Condition<T> condition, Runner<T> runner) {
        this.condition = condition;
        this.runner = runner;
    }

    public Condition<T> getCondition() {
        return condition;
    }

    public Runner<T> getRunner() {
        return runner;
    }

}



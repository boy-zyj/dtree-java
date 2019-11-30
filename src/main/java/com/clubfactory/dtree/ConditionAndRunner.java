package com.clubfactory.dtree;


class ConditionAndRunner<T> {

    Condition<T> condition;
    Runner<T> runner;

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



package com.clubfactory.dtree.core;

public class Child {

    private Condition condition;
    private Runner runner;

    public Child(Condition condition, Runner runner) {
        this.condition = condition;
        this.runner = runner;
    }

    public Condition getCondition() {
        return condition;
    }

    public Runner getRunner() {
        return runner;
    }
}

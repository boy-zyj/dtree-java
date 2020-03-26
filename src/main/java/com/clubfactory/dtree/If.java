package com.clubfactory.dtree;

public class If<T> {

    Condition<T> condition;
    Node<T> node;
    Runner<T> runner;

    public If(Condition<T> condition, Node<T> node) {
        this.condition = condition;
        this.node = node;
    }

    public If(Condition<T> condition, Runner<T> runner) {
        this.condition = condition;
        this.runner = runner;
    }

}

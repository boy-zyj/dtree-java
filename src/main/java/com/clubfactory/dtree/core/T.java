package com.clubfactory.dtree.core;

public class T {

    final static int NODE = 0;
    final static int RUNNER = 1;
    final static int DTREE = 2;

    Condition condition;
    DTree tree;
    Runner runner;
    Node node;
    int rightType;

    public T(Condition condition, DTree tree) {
        rightType = DTREE;
        this.tree = tree;
    }

    public T(Condition condition, Runner runner) {
        rightType = RUNNER;
        this.condition = condition;
        this.runner = runner;
    }

    public T(Condition condition, Node node) {
        rightType = NODE;
        this.condition = condition;
        this.node = node;
    }

}

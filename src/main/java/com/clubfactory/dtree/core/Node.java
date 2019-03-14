package com.clubfactory.dtree.core;


public class Node {

    public T[] ts;
    public Policy policy;

    public Node(T... ts) {
        this.ts = ts;
    }

    public Node(Policy policy, T... ts) {
        this.ts = ts;
        this.policy = policy;
    }

}

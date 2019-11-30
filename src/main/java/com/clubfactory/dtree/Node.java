package com.clubfactory.dtree;

public class Node<T> {

    If<T>[] iFs;
    Policy<T> policy;

    @SafeVarargs
    Node(If<T> ...iFs) {
        this.iFs = iFs;
    }

    @SafeVarargs
    Node(Policy<T> policy, If<T> ...iFs) {
        this.iFs = iFs;
        this.policy = policy;
    }

    public Node<T> policy(Policy<T> policy) {
        return new Node<>(policy, iFs);
    }

}

package com.clubfactory.dtree;

public class Node<T> {

    If<T>[] ifs;
    Policy<T> policy;

    @SafeVarargs
    Node(If<T> ... ifs) {
        this.ifs = ifs;
    }

    @SafeVarargs
    Node(Policy<T> policy, If<T> ... ifs) {
        this.ifs = ifs;
        this.policy = policy;
    }

    public Node<T> policy(Policy<T> policy) {
        return new Node<>(policy, ifs);
    }

}

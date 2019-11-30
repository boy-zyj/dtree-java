package com.clubfactory.dtree;


public class DTreeContext<T> {

    @SuppressWarnings("unchecked")
    final Else<T> ELSE = (Else<T>) Else.ELSE;

    @SuppressWarnings("unchecked")
    final Action<T> PASS = (Action<T>) Action.PASS;

    @SuppressWarnings("unchecked")
    public Condition<T> and(Condition<T> ...conditions) {
        return new And<>(conditions);
    }

    @SuppressWarnings("unchecked")
    public Condition<T> or (Condition<T> ...conditions) {
        return new Or<>(conditions);
    }

    public Condition<T> not(Condition<T> condition) {
        return new Not<>(condition);
    }

    @SuppressWarnings("unchecked")
    public Node<T> node(If<T> ...ifs) {
        return new Node<>(ifs);
    }

    public If<T> fi(Condition<T> condition, Runner<T> runner) {
        return new If<>(condition, runner);
    }

    public If<T> fi(Condition<T> condition, Node<T> node) {
        return new If<>(condition, node);
    }

    public Runner<T> asserT(Condition<T> condition) {
        return new AssertAction<>(condition);
    }

}

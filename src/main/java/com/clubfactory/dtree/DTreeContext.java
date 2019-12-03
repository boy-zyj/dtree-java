package com.clubfactory.dtree;


public class DTreeContext<T> {

    public final Else<T> ELSE = Else.getElseInstance();

    public final Action<T> PASS = Pass.getPassInstance();

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

    public DTree<T> dtree(Node<T> node) {
        return new DTree<>(node);
    }

    @SuppressWarnings("unchecked")
    public DTree<T> dtree(If<T> ...ifs) {
        return new DTree<>(ifs);
    }

}

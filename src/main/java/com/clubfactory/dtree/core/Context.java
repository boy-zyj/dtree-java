package com.clubfactory.dtree.core;


public class Context {

    public static Else else_ = Else.else_;

    public static Else ELSE = else_;

    public static And And(Condition... conditions) {
        return new And(conditions);
    }

    public static Or Or(Condition... conditions) {
        return new Or(conditions);
    }

    public static Not Not(Condition condition) {
        return new Not(condition);
    }

    public static T T(Condition condition, DTree tree) {
        return new T(condition, tree);
    }

    public static T T(Condition condition, Runner runner) {
        return new T(condition, runner);
    }

    public static T T(Condition condition, Node node) {
        return new T(condition, node);
    }

    public static Node Node(T... ts) {
        return new Node(ts);
    }

    public static Node Node(Policy policy, T... ts) {
        return new Node(policy, ts);
    }

}

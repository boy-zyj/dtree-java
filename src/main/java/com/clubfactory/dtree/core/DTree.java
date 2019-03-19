package com.clubfactory.dtree.core;

import java.util.ArrayList;
import java.util.Collections;


public abstract class DTree extends Runner {

    private Node node;
    private Policy policy;
    private ArrayList<Child> children;
    private DTree parent;
    private Runner else_;

    public DTree(Node node) {
        this.node = node;
        T[] ts = node.ts;
        if (node.policy != null) {
            policy = node.policy;
        }
        children = new ArrayList<>(ts.length);
        for (int i = 0; i < ts.length; i++) {
            T t = ts[i];
            if (t.rightType == T.NODE) {
                addChild(t.condition, t.node);
            } else if (t.rightType == T.DTREE) {
                addChild(t.condition, t.tree);
            } else {
                addChild(t.condition, t.runner);
            }
        }
    }

    public Node getNode() {
        return node;
    }

    public Policy getPolicy() {
        return policy;
    }

    public ArrayList<Child> getChildren() {
        return children;
    }

    public DTree getParent() {
        return parent;
    }

    public Runner getElse() {
        return else_;
    }

    protected void addChild(Condition condition, Node node) {
        DTree runner = newInstance(node);
        runner.parent = this;
        if (runner.policy == null) {
            runner.policy = policy;
        }
        if (condition instanceof Else) {
            this.else_ = runner;
        } else {
            children.add(new Child(condition, runner));
        }
    }

    protected void addChild(Condition condition, Runner runner) {
        if (condition instanceof Else) {
            this.else_ = runner;
        } else {
            children.add(new Child(condition, runner));
        }
    }

    protected void addChild(Condition condition, DTree tree) {
        Node node = tree.node;
        addChild(condition, node);
    }

    public int getDepth() {
        int depth = 0;
        DTree myParent = parent;
        while (myParent != null) {
            depth++;
            myParent = myParent.parent;
        }
        return depth;
    }

    public abstract DTree newInstance(Node node);

    @Override
    public void run(Center data) throws NoMatchException {
        if (policy == null) {
            policy = Consts.DEFAULT_POLICY;
        }
        policy.runTree(this, data);
    }

    @Override
    public String toString() {
        String s = "";
        String indent = "|      ";
        String dtreeMark = "+++";
        String actionMark = "---";
        int depth = getDepth();
        if (depth == 0) {
            s += dtreeMark + "root:\n";
        }
        ArrayList<Child> all = new ArrayList<>(children);
        all.add(new Child(Else.else_, else_));
        for (Child child: all) {
            Condition condition = child.getCondition();
            if (child.getRunner() instanceof DTree) {
                DTree tree = (DTree) child.getRunner();
                s += String.join("", Collections.nCopies(depth + 1, indent))
                        + dtreeMark + condition.getDescription()
                        + ":\n";
                s += tree.toString();
            } else {
                Runner runner = child.getRunner();
                s += String.join("", Collections.nCopies(depth + 1, indent))
                        + actionMark
                        + condition.getDescription()
                        + " --> "
                        + runner.getDescription() + "\n";
            }
        }
        return s;
    }
}

package com.clubfactory.dtree;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class DTree<T> extends AbstractRunner<T> {

    private Node<T> node;
    private Policy<T> policy;
    private List<ConditionAndRunner<T>> children;
    private DTree<T> parent;
    private Runner<T> elseRunner;

    public DTree(Node<T> node) {
        this.node = node;
        If<T>[] iFs = node.iFs;
        if (node.policy != null) {
            policy = node.policy;
        }
        children = new ArrayList<>(iFs.length);
        for (If<T> iF: iFs) {
            add(iF);
        }
        children = Collections.unmodifiableList(children);
    }

    @SafeVarargs
    public DTree(If<T> ...ifs) {
        this(new Node<>(ifs));
    }

    private void add(If<T> iF) {
        Condition<T> condition = Objects.requireNonNull(iF.condition);
        if (iF.node == null) {
            add(condition, Objects.requireNonNull(iF.runner));
        } else {
            add(condition, iF.node);
        }
    }

    private void add(Condition<T> condition, Node<T> node) {
        DTree<T> dtree = new DTree<>(node);
        dtree.parent = this;
        children.add(new ConditionAndRunner<>(condition, dtree));
    }

    private void add(Condition<T> condition, Runner<T> runner) {
        if (condition instanceof Else) {
            if (elseRunner != null) {
                throw new IllegalArgumentException("Else Condition must be only one");
            }
            elseRunner = runner;
        } else {
            children.add(new ConditionAndRunner<>(condition, runner));
        }
    }

    protected Policy<T> getDefaultPolicy() {
        return getOncePolicy();
    }

    public final Policy<T> getOncePolicy() {
        @SuppressWarnings("unchecked")
        Policy<T> policy = (Policy<T>) Policy.ONCE_POLICY;
        return policy;
    }

    public final Policy<T> getRecursivePolicy() {
        @SuppressWarnings("unchecked")
        Policy<T> policy = (Policy<T>) Policy.RECURSIVE_POLICY;
        return policy;
    }

    public Policy<T> getPolicy() {
        DTree<T> parent = this;
        Policy<T> policy = null;
        while (parent != null) {
            policy = parent.policy;
            if (policy != null) {
                return policy;
            }
            parent = parent.parent;
        }
        return getDefaultPolicy();
    }

    public List<ConditionAndRunner<T>> getChildren() {
        return children;
    }

    public Runner<T> getElseRunner() {
        return elseRunner;
    }

    public Node<T> getNode() {
        return node;
    }

    @Override
    public void run(T target) throws NoMatchException {
        getPolicy().run(this, target);
    }

    public int getDepth() {
        int depth = 0;
        DTree<T> myParent = parent;
        while (myParent != null) {
            depth++;
            myParent = myParent.parent;
        }
        return depth;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String indent = "|      ";
        String dtreeMark = "+++";
        String actionMark = "---";
        int depth = getDepth();
        if (depth == 0) {
            sb.append("root:\n");
        }
        List<ConditionAndRunner<T>> all = new ArrayList<>(children);
        if (elseRunner != null) {
            all.add(new ConditionAndRunner<>(Else.getInstance(), elseRunner));
        }
        for (ConditionAndRunner<T> child: all) {
            Condition<T> condition = child.getCondition();
            Runner<T> runner = child.getRunner();
            if (runner instanceof DTree) {
                DTree<T> dtree = (DTree<T>) runner;
                sb.append(String.join("", Collections.nCopies(depth + 1, indent)));
                sb.append(dtreeMark);
                sb.append(condition.getDescription());
                sb.append(":\n");
                sb.append(dtree.toString());
            } else {
                sb.append(String.join("", Collections.nCopies(depth + 1, indent)));
                sb.append(actionMark);
                sb.append(condition.getDescription());
                sb.append(" --> ");
                sb.append(runner.getDescription());
                sb.append("\n");
            }
        }
        return sb.toString();
    }

}

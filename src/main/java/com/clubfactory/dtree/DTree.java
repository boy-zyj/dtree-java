package com.clubfactory.dtree;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class DTree<T> extends AbstractRunner<T> {

    private Node<T> node;
    private Policy<T> policy;
    private List<ConditionAndRunner<T>> conditionAndRunners;
    private DTree<T> parent;
    private Runner<T> elseRunner;

    public DTree(Node<T> node) {
        this.node = node;
        If<T>[] ifs = node.ifs;
        if (node.policy != null) {
            policy = node.policy;
        }
        conditionAndRunners = new ArrayList<>(ifs.length);
        for (If<T> fi: ifs) {
            add(fi);
        }
        conditionAndRunners = Collections.unmodifiableList(conditionAndRunners);
    }

    @SafeVarargs
    public DTree(If<T> ...ifs) {
        this(new Node<>(ifs));
    }

    private void add(If<T> fi) {
        Condition<T> condition = Objects.requireNonNull(fi.condition);
        if (fi.node == null) {
            add(condition, Objects.requireNonNull(fi.runner));
        } else {
            add(condition, fi.node);
        }
    }

    private void add(Condition<T> condition, Node<T> node) {
        DTree<T> dtree = newDTreeInstance(node);
        dtree.parent = this;
        conditionAndRunners.add(new ConditionAndRunner<>(condition, dtree));
    }

    private void add(Condition<T> condition, Runner<T> runner) {
        if (condition instanceof Else) {
            if (elseRunner != null) {
                throw new IllegalArgumentException("Else Condition must be only one");
            }
            elseRunner = runner;
        } else {
            conditionAndRunners.add(new ConditionAndRunner<>(condition, runner));
        }
    }

    protected DTree<T> newDTreeInstance(Node<T> node) {
        return new DTree<>(node);
    }

    protected Policy<T> getDefaultPolicy() {
        return getOncePolicy();
    }

    public final Policy<T> getOncePolicy() {
        return Policy.getOncePolicy();
    }

    public final Policy<T> getRecursivePolicy() {
        return Policy.getRecursivePolicy();
    }

    public Policy<T> getPolicy() {
        DTree<T> parent = this;
        Policy<T> policy;
        while (parent != null) {
            policy = parent.policy;
            if (policy != null) {
                return policy;
            }
            parent = parent.parent;
        }
        return getDefaultPolicy();
    }

    public List<ConditionAndRunner<T>> getConditionAndRunners() {
        return conditionAndRunners;
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
        List<ConditionAndRunner<T>> all = new ArrayList<>(conditionAndRunners);
        if (elseRunner != null) {
            all.add(new ConditionAndRunner<>(Else.getElseInstance(), elseRunner));
        }
        for (ConditionAndRunner<T> child: all) {
            Condition<T> condition = child.getCondition();
            Runner<T> runner = child.getRunner();
            if (runner instanceof DTree) {
                sb.append(String.join("", Collections.nCopies(depth + 1, indent)));
                sb.append(dtreeMark);
                sb.append(condition.getDescription());
                sb.append(":\n");
                sb.append(runner.toString());
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

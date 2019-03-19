package com.clubfactory.dtree;

import java.util.ArrayList;
import java.util.Collections;

class NoMatchException extends Exception {}


public class Context<E> {

    final private static int NODE = 0;
    final private static int RUNNER = 1;
    final private static int ACTION = RUNNER;
    final private static int CHAIN = 2;
    final private static int DTREE = 3;

    public class Descriptor {
        public String getDescription() {
            return getClass().getSimpleName();
        }
    }

    public abstract class AbstractRunner extends Descriptor {

        private int type = RUNNER;

        public int getType() {
            return type;
        }

        public abstract void run(E data) throws NoMatchException;

        public Chain then(AbstractRunner runner) {
            return new Chain(this, runner);
        }

    }

    public abstract class AbstractAction extends AbstractRunner {

        private int type = ACTION;

    }

    public class Chain extends AbstractRunner {

        private int type = CHAIN;
        private AbstractRunner[] runners;

        public Chain(AbstractRunner... runners) {
            this.runners = runners;
        }

        @Override
        public void run(E data) throws NoMatchException {
            for (AbstractRunner runner:runners) {
                runner.run(data);
            }
        }

        public AbstractRunner[] getRunners() {
            return runners;
        }

        @Override
        public String getDescription() {
            String[] descriptions = new String[runners.length];
            for (int i = 0; i < runners.length; i++) {
                descriptions[i] = runners[i].getDescription();
            }
            return String.join("==>", descriptions);
        }
    }

    public abstract class AbstractCondition extends Descriptor {
        /**
         * 对data进行判断，判断其是否满足某特定条件
         *
         * @param data
         * @return
         */
        public abstract boolean validate(E data);

        boolean alwaysTrue = false;

    }

    public class And extends AbstractCondition {

        private AbstractCondition[] conditions;

        public And(AbstractCondition... conditions) {
            this.conditions = conditions;
        }

        @Override
        public boolean validate(E data) {
            for (AbstractCondition condition:conditions) {
                if (!condition.validate(data)) {
                    return false;
                }
            }
            return true;
        }

        public AbstractCondition[] getConditions() {
            return conditions;
        }

        @Override
        public String getDescription() {
            String[] descriptions = new String[conditions.length];
            for (int i = 0; i < conditions.length ; i++) {
                descriptions[i] = conditions[i].getDescription();
            }
            return "AND(" + String.join(",", descriptions) + ")";
        }
    }

    public class Or extends AbstractCondition {

        private AbstractCondition[] conditions;

        public Or(AbstractCondition... conditions) {
            this.conditions = conditions;
        }

        @Override
        public boolean validate(E data) {
            for (AbstractCondition condition:conditions) {
                if (condition.validate(data)) {
                    return true;
                }
            }
            return false;
        }

        public AbstractCondition[] getConditions() {
            return conditions;
        }

        @Override
        public String getDescription() {
            String[] descriptions = new String[conditions.length];
            for (int i = 0; i < conditions.length ; i++) {
                descriptions[i] = conditions[i].getDescription();
            }
            return "OR(" + String.join(",", descriptions) + ")";
        }

    }

    public class Not extends AbstractCondition {

        private AbstractCondition condition;

        public Not(AbstractCondition condition) {
            this.condition = condition;
        }

        @Override
        public boolean validate(E data) {
            return !condition.validate(data);
        }

        public AbstractCondition getCondition() {
            return condition;
        }

        @Override
        public String getDescription() {
            return "NOT(" + condition.getDescription() + ")";
        }

    }

    public class Else extends AbstractCondition {

        boolean alwaysTrue = true;

        @Override
        public boolean validate(E data) {
            return true;
        }
    }

    public class T {

        AbstractCondition condition;
        Node node;
        AbstractRunner runner;
        int rightType;

        public T(AbstractCondition condition, Node node) {
            this.condition = condition;
            this.node = node;
            rightType = NODE;
        }

        public T(AbstractCondition condition, AbstractRunner runner) {
            this.condition = condition;
            this.runner = runner;
            rightType = RUNNER;
        }
    }

    public class Node {

        private T[] ts;
        private AbstractPolicy policy;

        public Node(T... ts) {
            this.ts = ts;
        }

        public Node(AbstractPolicy policy, T... ts) {
            this.ts = ts;
            this.policy = policy;
        }

        public T[] getTs() {
            return ts;
        }

        public AbstractPolicy getPolicy() {
            return policy;
        }

    }

    public class Child {

        private AbstractCondition condition;
        private AbstractRunner runner;

        Child(AbstractCondition condition, AbstractRunner runner) {
            this.condition = condition;
            this.runner = runner;
        }

        public AbstractCondition getCondition() {
            return condition;
        }

        public AbstractRunner getRunner() {
            return runner;
        }
    }

    public class Dtree extends AbstractRunner {

        private int type = DTREE;
        private Node node;
        private AbstractPolicy policy;
        private ArrayList<Child> children;
        private Dtree parent;
        private AbstractRunner eLse;

        public Dtree(Node node) {
            this.node = node;
            T[] ts = node.ts;
            if (node.policy == null) {
                policy = new OncePolicy();
            } else {
                policy = node.policy;
            }
            children = new ArrayList<>(ts.length);
            for (int i = 0; i < ts.length; i++) {
                T t = ts[i];
                if (t.rightType == NODE) {
                    addChild(t.condition, t.node);
                } else {
                    addChild(t.condition, t.runner);
                }
            }
        }

        private void addChild(AbstractCondition condition, Node node) {
            Dtree tree = newInstance(node);
            tree.parent = this;
            if (tree.policy == null) {
                tree.policy = policy;
            }
            if (condition.alwaysTrue) {
                this.eLse = tree;
            } else {
                children.add(new Child(condition, tree));
            }
        }

        private void addChild(AbstractCondition condition, AbstractRunner runner) {
            if (condition.alwaysTrue) {
                this.eLse = runner;
            } else {
                children.add(new Child(condition, runner));
            }
        }

        public Node getNode() {
            return node;
        }

        public AbstractPolicy getPolicy() {
            return policy;
        }

        public ArrayList<Child> getChildren() {
            return children;
        }

        public Dtree getParent() {
            return parent;
        }

        public AbstractRunner getElse() {
            return eLse;
        }

        @Override
        public void run(E data) throws NoMatchException {
            policy.runTree(this, data);
        }

        public Dtree newInstance(Node node) {
            return new Dtree(node);
        }

        public int getDepth() {
            int depth = 0;
            Dtree myParent = parent;
            while (myParent != null) {
                depth++;
                myParent = myParent.parent;
            }
            return depth;
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
            all.add(new Child(new Else(), eLse));
            for (Child child: all) {
                AbstractCondition condition = child.getCondition();
                AbstractRunner runner = child.getRunner();
                if (runner.type == DTREE) {
                    Dtree tree = (Dtree) runner;
                    s += String.join("", Collections.nCopies(depth + 1, indent))
                            + dtreeMark + condition.getDescription()
                            + ":\n";
                    s += tree.toString();
                } else {
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

    public abstract class AbstractPolicy {

        public abstract void runTree(Dtree tree, E data) throws NoMatchException;

    }

    public class OncePolicy extends AbstractPolicy {

        @Override
        public void runTree(Dtree tree, E data) throws NoMatchException {
            for (Child child: tree.getChildren()) {
                AbstractCondition condition = child.getCondition();
                AbstractRunner runner = child.getRunner();
                if (condition.validate(data)) {
                    runner.run(data);
                    return;
                }
            }
            AbstractRunner eLse = tree.getElse();
            if (eLse != null) {
                eLse.run(data);
                return;
            } else {
                throw new NoMatchException();
            }
        }
    }

    public class RepeatPolicy extends AbstractPolicy {

        @Override
        public void runTree(Dtree tree, E data) throws NoMatchException {
            for (Child child: tree.getChildren()) {
                AbstractCondition condition = child.getCondition();
                AbstractRunner runner = child.getRunner();
                try {
                    if (condition.validate(data)) {
                        runner.run(data);
                        return;
                    }
                } catch (NoMatchException e) {
                    continue;
                }
            }
            AbstractRunner eLse = tree.getElse();
            if (eLse != null) {
                eLse.run(data);
                return;
            } else {
                throw new NoMatchException();
            }
        }
    }

}

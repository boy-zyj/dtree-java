package com.clubfactory.dtree;

import java.util.ArrayList;
import java.util.Collections;


/**
 * @author yao
 * @date 2019/03/19
 * @param <E>
 */
public class Context<E> {

    final private static int NODE = 0;
    final private static int RUNNER = 1;
    final private static int ACTION = RUNNER;
    final private static int CHAIN = 2;
    final private static int DTREE = 3;

    public class Descriptor {

        protected String description;

        public String getDescription() {
            if (description == null) {
                return getClass().getSimpleName();
            }
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }

    public abstract class AbstractRunner extends Descriptor {

        /**
         * 返回Runner的类型
         *
         * @return int
         */
        public abstract int getType();

        /**
         * 实现相应条件的触发动作
         *
         * @param data 执行动作的对象
         * @throws NoMatchException 说明没有相对应的触发动作
         */
        public abstract void run(E data) throws NoMatchException;

        public Chain then(AbstractRunner runner) {
            return new Chain(this, runner);
        }

    }

    public abstract class AbstractAction extends AbstractRunner {

        @Override
        final public int getType() {
            return ACTION;
        }

    }

    public class Chain extends AbstractRunner {

        private AbstractRunner[] runners;

        @SafeVarargs
        public Chain(AbstractRunner... runners) {
            this.runners = runners;
        }

        @Override
        final public int getType() {
            return CHAIN;
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
         * @param data 判断的对象
         * @return boolean
         */
        public abstract boolean validate(E data);

        public boolean isAlwaysTrue() {
            return false;
        }

    }

    public class And extends AbstractCondition {

        private AbstractCondition[] conditions;

        @SafeVarargs
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

        @SafeVarargs
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

        @Override
        public boolean isAlwaysTrue() {
            return true;
        }

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

        T(AbstractCondition condition, Node node) {
            this.condition = condition;
            this.node = node;
            rightType = NODE;
        }

        T(AbstractCondition condition, AbstractRunner runner) {
            this.condition = condition;
            this.runner = runner;
            rightType = RUNNER;
        }
    }

    public class Node {

        private T[] ts;
        private AbstractPolicy policy;

        @SafeVarargs
        Node(T... ts) {
            this.ts = ts;
        }

        @SafeVarargs
        Node(AbstractPolicy policy, T... ts) {
            this.ts = ts;
            this.policy = policy;
        }

        public T[] getTs() {
            return ts;
        }

        public AbstractPolicy getPolicy() {
            return policy;
        }

        public void setPolicy(AbstractPolicy policy) {
            this.policy = policy;
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

        private Node node;
        private AbstractPolicy policy;
        private ArrayList<Child> children;
        private Dtree parent;
        private AbstractRunner eLse;

        public Dtree(Node node) {
            this.node = node;
            T[] ts = node.ts;
            if (node.policy != null) {
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

        @Override
        final public int getType() {
            return DTREE;
        }

        private void addChild(AbstractCondition condition, Node node) {
            Dtree tree = newInstance(node);
            tree.parent = this;
            if (tree.policy == null) {
                tree.policy = policy;
            }
            if (condition.isAlwaysTrue()) {
                this.eLse = tree;
            } else {
                children.add(new Child(condition, tree));
            }
        }

        private void addChild(AbstractCondition condition, AbstractRunner runner) {
            if (condition.isAlwaysTrue()) {
                this.eLse = runner;
            } else {
                children.add(new Child(condition, runner));
            }
        }

        public Node getNode() {
            return node;
        }

        public AbstractPolicy getPolicy() {
            if (policy == null) {
                policy = DEFAULT_POLICY;
            }
            return policy;
        }

        public ArrayList<Child> getChildren() {
            return children;
        }

        public Dtree getParent() {
            return parent;
        }

        public AbstractRunner getElseRunner() {
            return eLse;
        }

        @Override
        public void run(E data) throws NoMatchException {
            AbstractPolicy policy = getPolicy();
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
            if (eLse != null) {
                all.add(new Child(new Else(), eLse));
            }
            for (Child child: all) {
                AbstractCondition condition = child.getCondition();
                AbstractRunner runner = child.getRunner();
                if (runner.getType() == DTREE) {
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

        /**
         * 运行引擎的实现方法
         *
         * @param tree
         * @param data
         * @throws NoMatchException
         */
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
            AbstractRunner eLse = tree.getElseRunner();
            if (eLse != null) {
                eLse.run(data);
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
                } catch (NoMatchException ignored) {
                }
            }
            AbstractRunner eLse = tree.getElseRunner();
            if (eLse != null) {
                eLse.run(data);
                return;
            } else {
                throw new NoMatchException();
            }
        }
    }

    final public AbstractPolicy ONCE_POLICY = new OncePolicy();
    final public AbstractPolicy DEFAULT_POLICY = ONCE_POLICY;
    final public AbstractPolicy REPEAT_POLICY = new RepeatPolicy();

    public Else ELSE = new Else();

    @SuppressWarnings("unchecked")
    public And and(AbstractCondition... conditions) {
        return new And(conditions);
    }

    @SuppressWarnings("unchecked")
    public Or or(AbstractCondition... conditions) {
        return new Or(conditions);
    }

    public Not not(AbstractCondition condition) {
        return new Not(condition);
    }

    @SuppressWarnings("unchecked")
    public Node node(T... ts) {
        return new Node(ts);
    }

    public T x(AbstractCondition condition, Node node) {
        return new T(condition, node);
    }

    public T x(AbstractCondition condition, AbstractRunner runner) {
        return new T(condition, runner);
    }

    public class ValueOf<OUTPUT> {

        private String desc;
        private Getter<E, OUTPUT> getter;

        public ValueOf(String desc, Getter<E, OUTPUT> getter) {
            this.desc = desc;
            this.getter = getter;
        }

        public String getDesc() {
            return desc;
        }

        public OUTPUT getOutput(E input) {
            return getter.get(input);
        }

        private class Ge<OTHER extends Comparable<OUTPUT>> extends AbstractCondition {

            ValueOf<OUTPUT> me;
            OTHER other;

            Ge(ValueOf<OUTPUT> me, OTHER other) {
                this.me = me;
                this.other = other;
            }

            @Override
            public boolean validate(E input) {
                OUTPUT meOutput = me.getOutput(input);
                return other.compareTo(meOutput) < 0;
            }

            @Override
            public String getDescription() {
                if (description == null) {
                    return desc + ">=" + other;
                }
                return description;
            }

        }

        private class Gt<OTHER extends Comparable<OUTPUT>> extends AbstractCondition {

            ValueOf<OUTPUT> me;
            OTHER other;

            Gt(ValueOf<OUTPUT> me, OTHER other) {
                this.me = me;
                this.other = other;
            }

            @Override
            public boolean validate(E input) {
                OUTPUT meOutput = me.getOutput(input);
                return other.compareTo(meOutput) <= 0;
            }

            @Override
            public String getDescription() {
                if (description == null) {
                    return desc + ">" + other;
                }
                return description;
            }

        }

        private class Le<OTHER extends Comparable<OUTPUT>> extends AbstractCondition {

            ValueOf<OUTPUT> me;
            OTHER other;

            Le(ValueOf<OUTPUT> me, OTHER other) {
                this.me = me;
                this.other = other;
            }

            @Override
            public boolean validate(E input) {
                OUTPUT meOutput = me.getOutput(input);
                return other.compareTo(meOutput) >= 0;
            }

            @Override
            public String getDescription() {
                if (description == null) {
                    return desc + "<=" + other;
                }
                return description;
            }

        }

        private class Lt<OTHER extends Comparable<OUTPUT>> extends AbstractCondition {

            ValueOf<OUTPUT> me;
            OTHER other;

            Lt(ValueOf<OUTPUT> me, OTHER other) {
                this.me = me;
                this.other = other;
            }

            @Override
            public boolean validate(E input) {
                OUTPUT meOutput = me.getOutput(input);
                return other.compareTo(meOutput) > 0;
            }

            @Override
            public String getDescription() {
                if (description == null) {
                    return desc + "<" + other;
                }
                return description;
            }

        }

        private class Eq<OTHER extends Comparable<OUTPUT>> extends AbstractCondition {

            ValueOf<OUTPUT> me;
            OTHER other;

            Eq(ValueOf<OUTPUT> me, OTHER other) {
                this.me = me;
                this.other = other;
            }

            @Override
            public boolean validate(E input) {
                OUTPUT meOutput = me.getOutput(input);
                return other.compareTo(meOutput) == 0;
            }

            @Override
            public String getDescription() {
                if (description == null) {
                    return desc + "=" + other;
                }
                return description;
            }

        }

        public <OTHER extends Comparable<OUTPUT>> AbstractCondition eq(OTHER other) {
            return new Eq<>(this, other);
        }

        public <OTHER extends Comparable<OUTPUT>> AbstractCondition lt(OTHER other) {
            return new Lt<>(this, other);
        }

        public <OTHER extends Comparable<OUTPUT>> AbstractCondition le(OTHER other) {
            return new Le<>(this, other);
        }

        public <OTHER extends Comparable<OUTPUT>> AbstractCondition gt(OTHER other) {
            return new Gt<>(this, other);
        }

        public <OTHER extends Comparable<OUTPUT>> AbstractCondition ge(OTHER other) {
            return new Ge<>(this, other);
        }

    }

}

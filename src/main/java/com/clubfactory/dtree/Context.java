package com.clubfactory.dtree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;


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

    public class If {

        AbstractCondition condition;
        Node node;
        AbstractRunner runner;
        int rightType;

        If(AbstractCondition condition, Node node) {
            this.condition = condition;
            this.node = node;
            rightType = NODE;
        }

        If(AbstractCondition condition, AbstractRunner runner) {
            this.condition = condition;
            this.runner = runner;
            rightType = RUNNER;
        }
    }

    public class Node {

        private If[] iFs;
        private AbstractPolicy policy;

        @SafeVarargs
        Node(If... iFs) {
            this.iFs = iFs;
        }

        @SafeVarargs
        Node(AbstractPolicy policy, If... iFs) {
            this.iFs = iFs;
            this.policy = policy;
        }

        public If[] getiFs() {
            return iFs;
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
            If[] iFs = node.iFs;
            if (node.policy != null) {
                policy = node.policy;
            }
            children = new ArrayList<>(iFs.length);
            for (int i = 0; i < iFs.length; i++) {
                If iF = iFs[i];
                if (iF.rightType == NODE) {
                    addChild(iF.condition, iF.node);
                } else {
                    addChild(iF.condition, iF.runner);
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

    final public Else ELSE = new Else();

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
    public Node node(If... iFs) {
        return new Node(iFs);
    }

    public If iF(AbstractCondition condition, Node node) {
        return new If(condition, node);
    }

    public If iF(AbstractCondition condition, AbstractRunner runner) {
        return new If(condition, runner);
    }


    public class ValueOf<E_OUTPUT> {

        private String desc;
        private Function<E, E_OUTPUT> getter;

        public ValueOf(String desc, Function<E, E_OUTPUT> getter) {
            this.desc = desc;
            this.getter = getter;
        }

        public String getDesc() {
            return desc;
        }

        public E_OUTPUT getOutput(E input) {
            return getter.apply(input);
        }

        private class Ge<E_OTHER extends Comparable<E_OUTPUT>> extends AbstractCondition {

            ValueOf<E_OUTPUT> me;
            E_OTHER other;

            Ge(ValueOf<E_OUTPUT> me, E_OTHER other) {
                this.me = me;
                this.other = other;
            }

            @Override
            public boolean validate(E input) {
                E_OUTPUT meOutput = me.getOutput(input);
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

        private class Gt<E_OTHER extends Comparable<E_OUTPUT>> extends AbstractCondition {

            ValueOf<E_OUTPUT> me;
            E_OTHER other;

            Gt(ValueOf<E_OUTPUT> me, E_OTHER other) {
                this.me = me;
                this.other = other;
            }

            @Override
            public boolean validate(E input) {
                E_OUTPUT meOutput = me.getOutput(input);
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

        private class Le<E_OTHER extends Comparable<E_OUTPUT>> extends AbstractCondition {

            ValueOf<E_OUTPUT> me;
            E_OTHER other;

            Le(ValueOf<E_OUTPUT> me, E_OTHER other) {
                this.me = me;
                this.other = other;
            }

            @Override
            public boolean validate(E input) {
                E_OUTPUT meOutput = me.getOutput(input);
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

        private class Lt<E_OTHER extends Comparable<E_OUTPUT>> extends AbstractCondition {

            ValueOf<E_OUTPUT> me;
            E_OTHER other;

            Lt(ValueOf<E_OUTPUT> me, E_OTHER other) {
                this.me = me;
                this.other = other;
            }

            @Override
            public boolean validate(E input) {
                E_OUTPUT meOutput = me.getOutput(input);
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

        private class Eq<E_OTHER extends Comparable<E_OUTPUT>> extends AbstractCondition {

            ValueOf<E_OUTPUT> me;
            E_OTHER other;

            Eq(ValueOf<E_OUTPUT> me, E_OTHER other) {
                this.me = me;
                this.other = other;
            }

            @Override
            public boolean validate(E input) {
                E_OUTPUT meOutput = me.getOutput(input);
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

        private class Test<E_PREDICATE extends Predicate<E_OUTPUT>> extends AbstractCondition {

            ValueOf<E_OUTPUT> me;
            E_PREDICATE predicate;

            Test(ValueOf<E_OUTPUT> me, E_PREDICATE predicate) {
                this.me = me;
                this.predicate = predicate;
            }

            @Override
            public boolean validate(E input) {
                E_OUTPUT meOutput = me.getOutput(input);
                return predicate.test(meOutput);
            }

            @Override
            public String getDescription() {
                if (description == null) {
                    return "TEST";
                }
                return description;
            }

        }

        private class In<E_OTHER extends List<E_OUTPUT>> extends AbstractCondition {

            ValueOf<E_OUTPUT> me;
            E_OTHER other;

            In(ValueOf<E_OUTPUT> me, E_OTHER other) {
                this.me = me;
                this.other = other;
            }

            @Override
            public boolean validate(E input) {
                E_OUTPUT meOutput = me.getOutput(input);
                return other.contains(meOutput);
            }

            @Override
            public String getDescription() {
                if (description == null) {
                    return "in " + other;
                }
                return description;
            }

        }

        public <E_OTHER extends Comparable<E_OUTPUT>> AbstractCondition eq(E_OTHER other) {
            return new Eq<>(this, other);
        }

        public <E_OTHER extends Comparable<E_OUTPUT>> AbstractCondition lt(E_OTHER other) {
            return new Lt<>(this, other);
        }

        public <E_OTHER extends Comparable<E_OUTPUT>> AbstractCondition le(E_OTHER other) {
            return new Le<>(this, other);
        }

        public <E_OTHER extends Comparable<E_OUTPUT>> AbstractCondition gt(E_OTHER other) {
            return new Gt<>(this, other);
        }

        public <E_OTHER extends Comparable<E_OUTPUT>> AbstractCondition ge(E_OTHER other) {
            return new Ge<>(this, other);
        }

        public <E_PREDICATE extends Predicate<E_OUTPUT>> AbstractCondition test(E_PREDICATE predicate) {
            return new Test<>(this, predicate);
        }

        public <E_OTHER extends List<E_OUTPUT>> AbstractCondition in(E_OTHER other) {
            return new In<>(this, other);
        }

    }

}

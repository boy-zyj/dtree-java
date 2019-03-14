package com.clubfactory.dtree.core;


class OncePolicy extends Policy {

    @Override
    public void runTree(DTree tree, Center data) throws NoMatchException {
        for (Child child: tree.children) {
            Condition condition = child.condition;
            Runner runner = child.runner;
            if (condition.validate(data)) {
                runner.run(data);
                return;
            }
        }
        if (tree.else_ != null) {
            tree.else_.run(data);
            return;
        } else {
            throw new NoMatchException();
        }
    }

}


class RepeatPolicy extends Policy {

    @Override
    public void runTree(DTree tree, Center data) throws NoMatchException {
        for (Child child: tree.children) {
            Condition condition = child.condition;
            Runner runner = child.runner;
            try {
                if (condition.validate(data)) {
                    runner.run(data);
                    return;
                }
            } catch (NoMatchException e) {
                continue;
            }
        }
        if (tree.else_ != null) {
            tree.else_.run(data);
            return;
        } else {
            throw new NoMatchException();
        }
    }

}


public class Consts {

    public final static Policy ONCE_POLICY = new OncePolicy();

    public final static Policy DEFAULT_POLICY = ONCE_POLICY;

    public final static Policy REPEAT_POLICY = new RepeatPolicy();

}

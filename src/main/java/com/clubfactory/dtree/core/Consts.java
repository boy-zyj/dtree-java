package com.clubfactory.dtree.core;


class OncePolicy extends Policy {

    @Override
    public void runTree(DTree tree, Center data) throws NoMatchException {
        for (Child child: tree.getChildren()) {
            Condition condition = child.getCondition();
            Runner runner = child.getRunner();
            if (condition.validate(data)) {
                runner.run(data);
                return;
            }
        }
        Runner else_ = tree.getElse();
        if (else_ != null) {
            else_.run(data);
            return;
        } else {
            throw new NoMatchException();
        }
    }

}


class RepeatPolicy extends Policy {

    @Override
    public void runTree(DTree tree, Center data) throws NoMatchException {
        for (Child child: tree.getChildren()) {
            Condition condition = child.getCondition();
            Runner runner = child.getRunner();
            try {
                if (condition.validate(data)) {
                    runner.run(data);
                    return;
                }
            } catch (NoMatchException e) {
                continue;
            }
        }
        Runner else_ = tree.getElse();
        if (else_ != null) {
            else_.run(data);
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

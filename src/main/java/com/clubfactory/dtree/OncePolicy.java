package com.clubfactory.dtree;

class OncePolicy<T> implements Policy<T> {

    @Override
    public void run(DTree<T> dtree, T target) throws NoMatchException {
        for (ConditionAndRunner<T> child: dtree.getChildren()) {
            Condition<T> condition = child.getCondition();
            Runner<T> runner = child.getRunner();
            if (condition.validate(target)) {
                runner.run(target);
                return;
            }
        }
        Runner<T> elseRunner = dtree.getElseRunner();
        if (elseRunner != null) {
            elseRunner.run(target);
        } else {
            throw new NoMatchException();
        }
    }

}

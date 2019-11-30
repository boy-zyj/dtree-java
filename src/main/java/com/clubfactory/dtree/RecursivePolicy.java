package com.clubfactory.dtree;

public class RecursivePolicy<T> implements Policy<T> {

    @Override
    public void run(DTree<T> dtree, T target) throws NoMatchException {
        for (ConditionAndRunner<T> child: dtree.getChildren()) {
            Condition<T> condition = child.getCondition();
            Runner<T> runner = child.getRunner();
            try {
                if (condition.validate(target)) {
                    runner.run(target);
                    return;
                }
            } catch (NoMatchException ignored) {}
        }
        Runner<T> elseRunner = dtree.getElseRunner();
        if (elseRunner != null) {
            elseRunner.run(target);
        } else {
            throw new NoMatchException();
        }
    }

}

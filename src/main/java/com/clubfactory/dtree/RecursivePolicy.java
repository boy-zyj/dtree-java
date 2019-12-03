package com.clubfactory.dtree;

class RecursivePolicy<T> implements Policy<T> {

    @Override
    public void run(DTree<T> dtree, T target) throws NoMatchException {
        for (ConditionAndRunner<T> conditionAndRunner: dtree.getConditionAndRunners()) {
            Condition<T> condition = conditionAndRunner.getCondition();
            Runner<T> runner = conditionAndRunner.getRunner();
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

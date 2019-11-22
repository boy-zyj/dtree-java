package com.clubfactory.dtree;

public class And<T> extends AbstractCondtion<T> {

    private Condition<T>[] conditions;

    @SafeVarargs
    public And(Condition<T> ...conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean validate(T target) {
        for (Condition<T> condition: conditions) {
            if (!condition.validate(target)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getDefaultDescription() {
        String[] descriptions = new String[conditions.length];
        for (int i = 0; i < conditions.length ; i++) {
            descriptions[i] = conditions[i].getDescription();
        }
        return "AND(" + String.join(",", descriptions) + ")";
    }
}

package com.clubfactory.dtree;


public class Or<T> extends AbstractCondtion<T> {

    private Condition<T>[] conditions;

    @SafeVarargs
    public Or(Condition<T> ...conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean validate(T target) {
        for (Condition<T> condition: conditions) {
            if (condition.validate(target)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected String getDefaultDescription() {
        String[] descriptions = new String[conditions.length];
        for (int i = 0; i < conditions.length ; i++) {
            descriptions[i] = conditions[i].getDescription();
        }
        return "OR(" + String.join(",", descriptions) + ")";
    }

}


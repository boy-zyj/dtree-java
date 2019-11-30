package com.clubfactory.dtree;

import java.util.Objects;
import java.util.function.Predicate;


public class ToCondition<T> extends AbstractCondtion<T> {

    private Predicate<T> validator;

    private Condition<T> condition;

    public ToCondition(Predicate<T> validator) {
        Objects.requireNonNull(validator, "validator cannot be null");
        this.validator = validator;
    }

    public ToCondition(String description, Predicate<T> validator) {
        this(validator);
        this.description = description;
    }

    public ToCondition(Condition<T> condition) {
        this(condition.getDescription(), condition);
    }

    public ToCondition(String description, Condition<T> condition) {
        Objects.requireNonNull(condition, "condition cannot be null");
        this.condition = condition;
        this.description = description;
    }

    @Override
    public boolean validate(T target) {
        if (validator == null) {
            return condition.validate(target);
        } else {
            return validator.test(target);
        }
    }

}

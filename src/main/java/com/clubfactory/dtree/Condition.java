package com.clubfactory.dtree;


public interface Condition<T> extends Description {

    boolean validate(T target);

    default Condition<T> and(Condition<T> condition) {
        return new And<>(this, condition);
    }

    default Condition<T> or(Condition<T> condition) {
        return new Or<>(this, condition);
    }

    default Condition<T> negate() {
        return new Not<>(this);
    }

}

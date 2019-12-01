package com.clubfactory.dtree;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;


public class ValueGetter<IN, OUT> {

    private String desc;

    private Function<IN, OUT> getter;

    public ValueGetter(String desc, Function<IN, OUT> getter) {
        this.desc = desc;
        this.getter = getter;
    }

    public ValueGetter(Function<IN, OUT> getter) {
        this.getter = getter;
    }

    public String getDescription() {
        return desc;
    }

    public OUT getRequiredOutput(IN in) {
        OUT out = getter.apply(in);
        return Objects.requireNonNull(out, "Got null when getting value of " + desc);
    }

    public OUT getOutput(IN in) {
        return getter.apply(in);
    }

    protected Condition<IN> toConditionImpl(String description, Predicate<IN> predicate) {
        return new ToCondition<>(description, predicate);
    }

    public Condition<IN> eq(Comparable<OUT> other) {
        return toConditionImpl(desc + "=" + other, in -> other.compareTo(getter.apply(in)) == 0);
    }

    public Condition<IN> eq(ValueGetter<IN, Comparable<OUT>> otherVallueGetter) {
        return toConditionImpl(
                desc + "=" + otherVallueGetter.getDescription(),
                in -> otherVallueGetter.getRequiredOutput(in).compareTo(getter.apply(in)) == 0
        );
    }

    public Condition<IN> lt(Comparable<OUT> other) {
        return toConditionImpl(desc + "<" + other, in -> other.compareTo(getter.apply(in)) > 0);
    }

    public Condition<IN> lt(ValueGetter<IN, Comparable<OUT>> otherValueGetter) {
        return toConditionImpl(
                desc + "<" + otherValueGetter.getDescription(),
                in -> otherValueGetter.getRequiredOutput(in).compareTo(getter.apply(in)) > 0
        );
    }

    public Condition<IN> le(Comparable<OUT> other) {
        return toConditionImpl(desc + "<=" + other, in -> other.compareTo(getter.apply(in)) >= 0);
    }

    public Condition<IN> le(ValueGetter<IN, Comparable<OUT>> otherValueGetter) {
        return toConditionImpl(
                desc + "<=" + otherValueGetter.getDescription(),
                in -> otherValueGetter.getRequiredOutput(in).compareTo(getter.apply(in)) >= 0
        );
    }

    public Condition<IN> gt(Comparable<OUT> other) {
        return toConditionImpl(desc + ">" + other, in -> other.compareTo(getter.apply(in)) < 0);
    }

    public Condition<IN> gt(ValueGetter<IN, Comparable<OUT>> otherValueGetter) {
        return toConditionImpl(
                desc + ">" + otherValueGetter.getDescription(),
                in -> otherValueGetter.getRequiredOutput(in).compareTo(getter.apply(in)) < 0
        );
    }

    public Condition<IN> ge(Comparable<OUT> other) {
        return toConditionImpl(desc + ">=" + other, in -> other.compareTo(getter.apply(in)) <= 0);
    }

    public Condition<IN> ge(ValueGetter<IN, Comparable<OUT>> otherValueGetter) {
        return toConditionImpl(
                desc + ">=" + otherValueGetter.getDescription(),
                in -> otherValueGetter.getRequiredOutput(in).compareTo(getter.apply(in)) <= 0
        );
    }

    public Condition<IN> in(Collection<OUT> outs) {
        return toConditionImpl(desc + " in " + outs, in -> outs.contains(getter.apply(in)));
    }

    public Condition<IN> in(ValueGetter<IN, Collection<OUT>> outsValueGetter) {
        return toConditionImpl(
                desc + " in " + outsValueGetter.getDescription(),
                in -> outsValueGetter.getRequiredOutput(in).contains(getter.apply(in))
        );
    }

    public Condition<IN> test(String desc, Predicate<IN> predicate) {
        return toConditionImpl(desc, predicate);
    }

    public Condition<IN> isNull() {
        return toConditionImpl(desc + " is null", in -> getter.apply(in) == null);
    }

    public Condition<IN> isNonNull() {
        return toConditionImpl(desc + " is not null", in -> getter.apply(in) != null);
    }

}

package com.clubfactory.dtree;

import com.clubfactory.dtree.core.Center;
import com.clubfactory.dtree.core.Condition;


public class ValueOf<INPUT, OUTPUT> {

    private String desc;
    private Getter<INPUT, OUTPUT> getter;

    public ValueOf(String desc, Getter<INPUT, OUTPUT> getter) {
        this.desc = desc;
        this.getter = getter;
    }

    public String getDesc() {
        return desc;
    }

    public OUTPUT getOutput(INPUT input) {
        OUTPUT output = getter.get(input);
        return output;
    }

    private class Ge<OTHER extends Comparable<OUTPUT>> extends Condition {

        ValueOf<INPUT, OUTPUT> me;
        OTHER other;

        public Ge(ValueOf me, OTHER other) {
            this.me = me;
            this.other = other;
        }

        @Override
        public boolean validate(Center center) {
            INPUT input = (INPUT) center;
            OUTPUT meOutput = me.getOutput(input);
            return other.compareTo(meOutput) < 0;
        }

    }

    private class Gt<OTHER extends Comparable<OUTPUT>> extends Condition {

        ValueOf<INPUT, OUTPUT> me;
        OTHER other;

        public Gt(ValueOf me, OTHER other) {
            this.me = me;
            this.other = other;
        }

        @Override
        public boolean validate(Center center) {
            INPUT input = (INPUT) center;
            OUTPUT meOutput = me.getOutput(input);
            return other.compareTo(meOutput) <= 0;
        }

    }

    private class Le<OTHER extends Comparable<OUTPUT>> extends Condition {

        ValueOf<INPUT, OUTPUT> me;
        OTHER other;

        public Le(ValueOf me, OTHER other) {
            this.me = me;
            this.other = other;
        }

        @Override
        public boolean validate(Center center) {
            INPUT input = (INPUT) center;
            OUTPUT meOutput = me.getOutput(input);
            return other.compareTo(meOutput) >= 0;
        }

    }

    private class Lt<OTHER extends Comparable<OUTPUT>> extends Condition {

        ValueOf<INPUT, OUTPUT> me;
        OTHER other;

        public Lt(ValueOf me, OTHER other) {
            this.me = me;
            this.other = other;
        }

        @Override
        public boolean validate(Center center) {
            INPUT input = (INPUT) center;
            OUTPUT meOutput = me.getOutput(input);
            return other.compareTo(meOutput) > 0;
        }

    }

    private class Eq<OTHER extends Comparable<OUTPUT>> extends Condition {

        ValueOf<INPUT, OUTPUT> me;
        OTHER other;

        public Eq(ValueOf me, OTHER other) {
            this.me = me;
            this.other = other;
        }

        @Override
        public boolean validate(Center center) {
            INPUT input = (INPUT) center;
            OUTPUT meOutput = me.getOutput(input);
            return other.compareTo(meOutput) == 0;
        }

    }

    public <E extends Comparable<OUTPUT>> Eq eq(E other) {
        return new Eq(this, other);
    }

    public <E extends Comparable<OUTPUT>> Lt lt(E other) {
        return new Lt(this, other);
    }

    public <E extends Comparable<OUTPUT>> Le le(E other) {
        return new Le(this, other);
    }

    public <E extends Comparable<OUTPUT>> Gt gt(E other) {
        return new Gt(this, other);
    }

    public <E extends Comparable<OUTPUT>> Ge ge(E other) {
        return new Ge(this, other);
    }

}

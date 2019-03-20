package com.clubfactory;

import com.clubfactory.dtree.Context;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

class Obj {
    String country;
    String payType;
    boolean valid = true;
    int qty;

    Obj(String country, String payType) {
        this.country = country;
        this.payType = payType;
        qty = 10;
    }
}


class MyContext<E extends Obj> extends Context<E> {

    class IsIndia extends AbstractCondition {
        @Override
        public boolean validate(E data) {
            return data.country.equals("india");
        }
    }

    class IsCod extends AbstractCondition {
        @Override
        public boolean validate(E data) {
            return data.payType.equals("cod");
        }
    }

    class IsValid extends AbstractCondition {
        @Override
        public boolean validate(E data) {
            return data.valid;
        }
    }

    class ToHn extends AbstractAction {
        @Override
        public void run(E data) {
            System.out.println("to hn");
        }
    }

    class ToXs extends AbstractAction {
        @Override
        public void run(E center) {
            System.out.println("to xs");
        }
    }

    IsIndia isIndia = new IsIndia();
    IsCod isCod = new IsCod();
    IsValid isValid = new IsValid();

    ToHn toHn = new ToHn();
    ToXs toXs = new ToXs();

    ValueOf<String> country = new ValueOf<String>("country", x -> x.country);
    ValueOf<Boolean> valid = new ValueOf<Boolean>("valid", x -> x.valid);

    Dtree getRule() {
        Node node = node(
                x(country.eq("india"), toXs),
                x(and(isIndia, isValid), toHn),
                x(isCod, toXs),
                x(ELSE, node(
                        x(isCod, toHn),
                        x(new Else(), toXs)
                ))
        );
        Dtree rule = new Dtree(node);
        System.out.println(rule);
        return rule;
    }

}


public class ContextTest
        extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ContextTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ContextTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testDTree()
    {
        Obj obj = new Obj("india", "prepay");
        MyContext<Obj> myContext = new MyContext<>();
        MyContext<Obj>.Dtree rule = myContext.getRule();
        try {
            rule.run(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

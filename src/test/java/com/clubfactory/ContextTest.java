package com.clubfactory;

import com.clubfactory.dtree.Context;
import com.clubfactory.dtree.core.Center;
import com.sun.corba.se.impl.ior.OldJIDLObjectKeyTemplate;
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

    Dtree getRule() {
        Node node = new Node(
                new T(isIndia, toHn),
                new T(isCod, toXs),
                new T(new Else(), new Node(
                        new T(isCod, toHn),
                        new T(new Else(), toXs)
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
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testDTree()
    {
        Obj obj = new Obj("india", "prepay");
        MyContext<Obj> myContext = new MyContext<>();
        myContext.getRule();
    }
}
package com.clubfactory;

import com.clubfactory.dtree.Context;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.List;


class Obj {
    String country;
    String payType;
    boolean valid = true;
    int qty;
    List<Integer> integers = new ArrayList<>();

    Obj(String country, String payType) {
        this.country = country;
        this.payType = payType;
        qty = 10;
        integers.add(qty);
        integers.add(20);
    }
}


class MyContext extends Context<Obj> {

    private MyContext() {}

    private static MyContext instance;

    static MyContext newInstance() {
        if (instance == null) {
            instance = new MyContext();
        }
        return instance;
    }

    class IsIndia extends AbstractCondition {
        @Override
        public boolean validate(Obj data) {
            return data.country.equals("india");
        }
    }

    class IsCod extends AbstractCondition {
        @Override
        public boolean validate(Obj data) {
            return data.payType.equals("cod");
        }
    }

    class IsValid extends AbstractCondition {
        @Override
        public boolean validate(Obj data) {
            return data.valid;
        }
    }

    class ToHn extends AbstractAction {
        @Override
        public void run(Obj data) {
            System.out.println("to hn");
        }
    }

    class ToXs extends AbstractAction {
        @Override
        public void run(Obj center) {
            System.out.println("to xs");
        }
    }

    IsIndia isIndia = new IsIndia();
//    IsCod isCod = new IsCod();
    IsValid isValid = new IsValid();

    ToHn toHn = new ToHn();
    ToXs toXs = new ToXs();

    ValueOf<String> country = new ValueOf<String>("country", x -> x.country);
    ValueOf<Boolean> valid = new ValueOf<Boolean>("valid", x -> x.valid);
    ValueOf<String> payType = new ValueOf<String>("payType", x -> x.payType);
    ValueOf<Integer> qty = new ValueOf<Integer>("qty", x -> x.qty);
    ValueOf<List<Integer>> integers = new ValueOf<List<Integer>>("integers", x -> x.integers);
    AbstractCondition test = qty.in(integers);

    AbstractCondition isCod = payType.eq("cod");

    Dtree getRule() {
        isCod.setDescription("isCod");
        Node node = node(
                iF(country.eq("india"), toXs),
                iF(and(isIndia, isValid), toHn),
                iF(isCod, toXs),
                iF(ELSE, node(
                        iF(isCod, toHn),
                        iF(ELSE, toXs)
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
        String s = "test";
        Obj obj = new Obj("india", "prepay");
        MyContext myContext = MyContext.newInstance();
        MyContext.Dtree rule = myContext.getRule();
        try {
            rule.run(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.clubfactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.clubfactory.dtree.core.*;

class Data implements Center {
    String country;
    String payType;
    boolean valid = true;

    Data(String country, String payType) {
        this.country = country;
        this.payType = payType;
    }
}

class IsIndia extends Condition {
    @Override
    public boolean validate(Center center) {
        Data data = (Data) center;
        return data.country.equals("india");
    }
}

class IsCod extends Condition {
    @Override
    public boolean validate(Center center) {
        Data data = (Data) center;
        return data.payType.equals("cod");
    }
}

class IsValid extends Condition {
    @Override
    public boolean validate(Center center) {
        Data data = (Data) center;
        return data.valid;
    }
}

class ToHn extends Action {
    @Override
    public void run(Center center) {
        Data data = (Data) center;
        System.out.println("to hn");
    }
}

class ToXs extends Action {
    @Override
    public void run(Center center) {
        Data data = (Data) center;
        System.out.println("to xs");
    }
}

class MyDTree extends DTree {

    public MyDTree(Node node) {
        super(node);
    }

    public DTree newInstance(Node node) {
        return new MyDTree(node);
    }
}


class Rule extends Context {
    public static MyDTree rule;

    public static MyDTree getRule() {
        if (rule == null) {
            IsIndia isIndia = new IsIndia();
            IsCod isCod = new IsCod();
            IsValid isValid = new IsValid();

            ToHn toHn = new ToHn();
            ToXs toXs = new ToXs();

            Node node = Node(
                    T(isCod, toHn),
                    T(isIndia, Node(
                            T(isValid, toXs),
                            T(else_, toHn)
                    )),
                    T(else_, toXs)
            );

            rule = new MyDTree(node);
        }
        return rule;
    }

}

/**
 * Unit test for simple App.
 */
public class AppTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
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
        Data data = new Data("india", "prepay");
        MyDTree rule = Rule.getRule();
        System.out.println(rule);
        try {
            rule.run(data);
        } catch (NoMatchException e) {
            System.out.println(e);
        }

    }
}

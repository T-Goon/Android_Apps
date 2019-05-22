package com.example.calculator;

import org.junit.Test;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static NumberFormat formatter = new DecimalFormat("#0.00000");

    @Test
    public void addition_isCorrect() {
        assertEquals( "2.0" , MainActivity.evaluateExpression("1+1"));
        assertEquals( "3.0" , MainActivity.evaluateExpression("1+1+1"));
        assertEquals( "Invalid Expression", MainActivity.evaluateExpression("1+1+1+"));
        assertEquals( "Invalid Expression", MainActivity.evaluateExpression("+1+1+1"));
        assertEquals( "Invalid Expression", MainActivity.evaluateExpression("1++1+1"));
        assertEquals( "Invalid Expression", MainActivity.evaluateExpression("+"));
        assertEquals( String.valueOf(Math.E), MainActivity.evaluateExpression("e"));
    }

    @Test
    public void negNumbers_isCorrect(){
        assertEquals("-3", MainActivity.evaluateExpression("-3"));
        assertEquals("-3.141592653589793", MainActivity.evaluateExpression("-π"));
    }

    @Test
    public void trig_isCorrect(){
        assertEquals(formatter.format(Math.tan(5)), MainActivity.evaluateExpression("tan(5)"));
        assertEquals(formatter.format(Math.cos(5)), MainActivity.evaluateExpression("cos(5)"));
        assertEquals(formatter.format(Math.sin(5)), MainActivity.evaluateExpression("sin(5)"));
        assertEquals(formatter.format(Math.sin(-5)), MainActivity.evaluateExpression("sin(-5)"));

        assertEquals(formatter.format(Math.asin(1)), MainActivity.evaluateExpression("asin(1)"));
        assertEquals(formatter.format(Math.acos(1)), MainActivity.evaluateExpression("acos(1)"));
        assertEquals(formatter.format(Math.atan(1)), MainActivity.evaluateExpression("atan(1)"));

        assertEquals(formatter.format(Math.sin(1+1)), MainActivity.evaluateExpression("sin(1+1)"));
        assertEquals(formatter.format(Math.asin(1+1)), MainActivity.evaluateExpression("asin(1+1)"));

        assertTrue(1.5081367-.001 < Double.parseDouble(MainActivity.evaluateExpression("sin(1)+1*2/3")) &&
                1.5081367+.001 > Double.parseDouble(MainActivity.evaluateExpression("sin(1)+1*2/3")));
        assertTrue( 2.23746299667-.001 < Double.parseDouble(MainActivity.evaluateExpression("asin(1)+1*2/3")) &&
                2.23746299667+.001 > Double.parseDouble(MainActivity.evaluateExpression("asin(1)+1*2/3")));

        assertEquals(formatter.format(Math.sin(Math.PI)), MainActivity.evaluateExpression("sin(π)"));
        assertEquals(formatter.format(-Math.sin(Math.PI)), MainActivity.evaluateExpression("sin(-π)"));
    }

    @Test
    public void parens_isCorrect(){
        assertEquals("1", MainActivity.evaluateExpression("(1)"));
        assertEquals("1", MainActivity.evaluateExpression("((1))"));
        assertEquals("2.0", MainActivity.evaluateExpression("(1+1)"));
        assertEquals("4.0", MainActivity.evaluateExpression("(1+1)*2"));
        assertEquals("6.0", MainActivity.evaluateExpression("(1+2)*(1+1)"));
        assertEquals("2.0", MainActivity.evaluateExpression("(1)(2)"));
        assertEquals("2.0", MainActivity.evaluateExpression("2(1)"));
        assertEquals("2.0", MainActivity.evaluateExpression("(1)2"));

        assertEquals("53.0", MainActivity.evaluateExpression("(2(5(8/2)+(5)))+3"));
        assertEquals("53.0", MainActivity.evaluateExpression("3+(((5)+(8/2)5)2)"));

        assertEquals("-3.141592653589793", MainActivity.evaluateExpression("(-π)"));
    }

    @Test
    public void log_isCorrect(){
        assertEquals(formatter.format(Math.log10(5)), MainActivity.evaluateExpression("log(5)"));
        assertEquals(formatter.format(Math.log(5)), MainActivity.evaluateExpression("ln(5)"));

        assertEquals(formatter.format(Math.log10(5-2)), MainActivity.evaluateExpression("log(5-2)"));
        assertEquals(formatter.format(Math.log(5+6)), MainActivity.evaluateExpression("ln(5+6)"));

        assertTrue(5*Math.log10(5)-.001 < Double.parseDouble(MainActivity.evaluateExpression("5*log(5)")) &&
        5*Math.log10(5)+.001 > Double.parseDouble(MainActivity.evaluateExpression("5*log(5)")));
        assertEquals(formatter.format(8+Math.log(5)), MainActivity.evaluateExpression("8+ln(5)"));

        assertEquals("-∞", MainActivity.evaluateExpression("log(0)"));
        assertEquals("-∞", MainActivity.evaluateExpression("ln(0)"));
    }

    @Test
    public void abs_isCorrect(){
        assertEquals(formatter.format(Math.abs(-Math.PI)), MainActivity.evaluateExpression("abs(-π)"));
        assertEquals(formatter.format(Math.abs(Math.PI)), MainActivity.evaluateExpression("abs(π)"));

        assertEquals(formatter.format(Math.abs(1-99)), MainActivity.evaluateExpression("abs(1-99)"));
        assertEquals(formatter.format(Math.abs(1+99)), MainActivity.evaluateExpression("abs(1+99)"));

        assertTrue(Math.abs(-Math.PI)*14-.001 < Double.parseDouble(MainActivity.evaluateExpression("abs(-π)*14")) &&
                Math.abs(-Math.PI)*14+.001 > Double.parseDouble(MainActivity.evaluateExpression("abs(-π)*14")));
        assertTrue(Math.abs(-Math.PI)/6-.001 < Double.parseDouble(MainActivity.evaluateExpression("abs(-π)/6")) &&
                Math.abs(-Math.PI)/6+.001 > Double.parseDouble(MainActivity.evaluateExpression("abs(-π)/6")));
    }
}
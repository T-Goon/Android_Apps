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
    }

    @Test
    public void trig_isCorrect(){
        assertEquals(formatter.format(Math.tan(5)), MainActivity.evaluateExpression("tan(5)"));
        assertEquals(formatter.format(Math.cos(5)), MainActivity.evaluateExpression("cos(5)"));
        assertEquals(formatter.format(Math.sin(5)), MainActivity.evaluateExpression("sin(5)"));

        assertEquals(formatter.format(Math.asin(1)), MainActivity.evaluateExpression("asin(1)"));
        assertEquals(formatter.format(Math.acos(1)), MainActivity.evaluateExpression("acos(1)"));
        assertEquals(formatter.format(Math.atan(1)), MainActivity.evaluateExpression("atan(1)"));

        assertEquals(formatter.format(Math.sin(1+1)), MainActivity.evaluateExpression("sin(1+1)"));
        assertEquals(formatter.format(Math.asin(1+1)), MainActivity.evaluateExpression("asin(1+1)"));

        assertEquals(formatter.format(Math.sin(1)+1*2/3), MainActivity.evaluateExpression("sin(1)+1*2/3"));
        assertEquals(formatter.format(Math.asin(1)+1*2/3), MainActivity.evaluateExpression("asin(1)+1*2/3"));

        assertEquals(formatter.format(Math.sin(Math.PI)), MainActivity.evaluateExpression("sin(π)"));
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
    }
}
package com.example.calculator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals( "2.0" , MainActivity.evaluateExpression("1+1"));
        assertEquals( "3.0" , MainActivity.evaluateExpression("1+1+1"));
        assertEquals( "Invalid Expression", MainActivity.evaluateExpression("1+1+1+"));
        assertEquals( "Invalid Expression", MainActivity.evaluateExpression("+1+1+1"));
        assertEquals( "Invalid Expression", MainActivity.evaluateExpression("1++1+1"));
        assertEquals( "Invalid Expression", MainActivity.evaluateExpression("+"));
    }

    @Test
    public void negNumbers_isCorrect(){
        assertEquals("-3", MainActivity.evaluateExpression("-3"));
    }

    @Test
    public void trig_isCorrect(){
        assertEquals(String.valueOf(Math.tan(5)), MainActivity.evaluateExpression("tan(5)"));
        assertEquals(String.valueOf(Math.cos(5)), MainActivity.evaluateExpression("cos(5)"));
        assertEquals(String.valueOf(Math.sin(5)), MainActivity.evaluateExpression("sin(5)"));

        assertEquals(String.valueOf(Math.asin(1)), MainActivity.evaluateExpression("asin(1)"));
        assertEquals(String.valueOf(Math.acos(1)), MainActivity.evaluateExpression("acos(1)"));
        assertEquals(String.valueOf(Math.atan(1)), MainActivity.evaluateExpression("atan(1)"));

        assertEquals(String.valueOf(Math.sin(1+1)), MainActivity.evaluateExpression("sin(1+1)"));
        assertEquals(String.valueOf(Math.asin(1+1)), MainActivity.evaluateExpression("asin(1+1)"));

        assertEquals(String.valueOf(Math.sin(1)+1*2/3), MainActivity.evaluateExpression("sin(1)+1*2/3"));
        assertEquals(String.valueOf(Math.asin(1)+1*2/3), MainActivity.evaluateExpression("asin(1)+1*2/3"));
    }

    @Test
    public void parens_isCorrect(){
        assertEquals("1", MainActivity.evaluateExpression("(1)"));
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
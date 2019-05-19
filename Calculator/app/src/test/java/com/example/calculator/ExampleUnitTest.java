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
    }

    @Test
    public void negNumbers_isCorrect(){
        assertEquals("-3", MainActivity.evaluateExpression("-3"));
    }

    @Test
    public void trig_isCorrect(){
        assertEquals(String.valueOf(Math.sin(5)), MainActivity.evaluateExpression("sin(5)"));
    }
}
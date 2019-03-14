package com.example.calculator;

import android.arch.core.util.Function;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.TimerTask;
import java.util.function.BiFunction;

public class MainActivity extends AppCompatActivity {
    private static Editable text;
    private static java.util.Timer cursor = new java.util.Timer();
    private static int cursorLocation = 0;
    Handler cursorHandler = new Handler();
    long startTime = 0;

    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            blink();

            cursorHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView display = findViewById(R.id.display);
        text = display.getEditableText();

        startTime = System.currentTimeMillis();
        cursorHandler.postDelayed(timerRunnable, 0);

    }

    /**
     * Adds a component to the mathematical expression.
     * @param view Button view object
     */
    public void addToExpression(View view){
        if((view instanceof Button) && text.toString().length() <= 300){
            Button button = (Button)view;
            text.insert(cursorLocation, button.getText());
            cursorLocation++;
        }

    }

    /**
     * Removes a component from the mathematical expression.
     * @param view Button view object
     */
    public void removeFromExpression(View view){
        if(text.length() > 1 && cursorLocation > 0) {
            text.delete(cursorLocation - 1, cursorLocation);
            cursorLocation--;
        }
    }

    /**
     * Clears all text from the mathematical expression.
     * @param view Button view object
     */
    public void clearExpression(View view){
        text.replace(0, text.toString().length(), " ");
        cursorLocation = 0;
    }

    /**
     * Changes the cursor between a space and a |
     */
    private void blink(){
        if(text.toString().charAt(cursorLocation) == '|'){
            text.replace(cursorLocation,cursorLocation+1, " ");
        }
        else if(text.toString().charAt(cursorLocation) == ' '){
            text.replace(cursorLocation,cursorLocation+1, "|");
        }
    }

    /**
     * Moves the cursor to the left in the display
     * @param view Button view object
     */
    public void moveLeft(View view){
        if(cursorLocation > 0) {
            text.delete(cursorLocation, cursorLocation + 1);
            cursorLocation--;
            text.insert(cursorLocation , "|");
        }
    }

    /**
     * Moves the cursor to the right in the display
     * @param view Button view object
     */
    public void moveRight(View view){
        if(cursorLocation < text.toString().length() - 1){
            text.delete(cursorLocation, cursorLocation + 1);
            cursorLocation++;
            text.insert(cursorLocation, "|");
        }
    }

    /**
     * Evaluates the mathematical expression in the display and displays it
     * @param view Button view object
     */
    public void evaluate(View view){
        String expression = text.toString();
        expression = expression.replace(" ", "");
        expression = expression.replace("|", "");

        expression = evaluateExpression(expression);

        text.replace(0, text.length(), expression+" ");
        cursorLocation = expression.length();
    }

    private String evaluateExpression(String expression){
        expression = evaluateParens(expression);

        //evaluateExponents(expression);

        expression = evaluateOpp(expression, '*', '/');

        expression = evaluateOpp(expression, '+', '-');

        return expression;
    }

    /**
     * Checks if there is an expression in parentheses to evaluate and if there is evaluate it
     * and update the given expression
     * @param expression Mathematical expression in string form
     * @return The expression with the parentheses evaluated if there were any
     */
    private String evaluateParens(String expression){
        // Has a valid set of open and close parens
        int indexOfOpenParen = expression.indexOf('(');
        int indexOfCloseParen = expression.lastIndexOf(')');

        if(indexOfOpenParen != -1 && indexOfCloseParen != -1) {
            if(indexOfOpenParen != 0)
                expression = expression.substring(0, indexOfOpenParen) +
                        evaluateExpression(expression.substring(indexOfOpenParen + 1, indexOfCloseParen)) +
                        expression.substring(indexOfCloseParen + 1, expression.length());
            else
                expression = evaluateExpression(expression.substring(indexOfOpenParen + 1, indexOfCloseParen)) +
                        expression.substring(indexOfCloseParen + 1, expression.length());
        }
        else if(indexOfOpenParen != -1 || indexOfCloseParen != -1)
            expression = "Invalid Expression 1";


        return expression;
    }

    private void evaluateExponents(String expression){

    }

    /**
     * Used to evaluate multiplication, division, addition, and subtraction.
     * @param expression mathematical expression in string form
     * @param op1 First operator to look for. If '*' then the other operator is '/',
     *            if '+' then the other operator is '-' and vice versa.
     * @param op2 Second operator to look for. If '*' then the other operator is '/',
     *      *            if '+' then the other operator is '-' and vice versa.
     * @return The mathematical expression with the given operators evaluated.
     * EX: 1+2*2 -> 1+4
     *     1+1 -> 2
     */
    private String evaluateOpp(String expression, char op1, char op2){
        // Loops through expression string looking for opp1 or opp2
        for(int i=0;i<expression.length();i++){

            if(expression.charAt(i) == op1){
                String result = oppHelper(expression, i, op1);
                if(result.equals("Abort Action")){
                    continue;
                }
                else
                    expression = result;
            }
            else if(expression.charAt(i) == op2){
                String result = oppHelper(expression, i, op2);
                if(result.equals("Abort Action")){
                    continue;
                }
                else
                    expression = result;
            }
        }

        return expression;
    }

    private String oppHelper(String expression, int indexOfOpp, char opp){
        float constants[] = findConstants(indexOfOpp, expression); //!!!

        int indexOfOps[] = findIndexOfOpps(indexOfOpp, expression);
        float result = 0;

        if(constants == null)
            return "Abort Action";

        if(opp == '*')
            result = constants[0] * constants[1];
        else if(opp == '/')
            result = constants[0] / constants[1];
        else if(opp == '+')
            result = constants[0] + constants[1];
        else if(opp == '-' )
            result = constants[0] - constants[1];

        if(indexOfOps[0] != 0)
            expression = expression.substring(0, indexOfOps[0] + 1) +
                Float.toString(result)+
                expression.substring(indexOfOps[1], expression.length());
        else
            expression = Float.toString(result)+
                    expression.substring(indexOfOps[1], expression.length());

        return expression;
    }

    /**
     * Find the constants that are to the left and right of the center operator.
     * @param locOfOp Index of the center operator
     * @param expression mathematical expression in string form
     * @return the constants left and right of the operator
     *  [left constant, right constant]
     */
    private float[] findConstants(int locOfOp, String expression){
        String left = "";
        float negl = 1;

        // Loops backward through the expression starting at locOfOp
        for(int i=locOfOp-1;i>=0;i--){
            if(Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.'){
                left = expression.charAt(i) + left;
            }
            else if(expression.charAt(i) == '-' && ((i == 0) || isOp(expression.charAt(i - 1)))){
                negl = -1;
            }
            else{
                break;
            }
        }

        // If there are no numbers
        if(left.equals("")){
            return null;
        }

        String right = "";
        float negr = 1;

        // Loops forward through the expression starting at locOfOp
        for(int i=locOfOp+1;i<expression.length();i++){
            if(Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.'){
                right = right + expression.charAt(i);
            }
            else if(expression.charAt(i) == '-' && i!= expression.length() && Character.isDigit(expression.charAt(i + 1))){
                negr = -1;
            }
            else{
                break;
            }
        }

        if(right.equals(""))
            return null;

        return new float[]{Float.parseFloat(left) * negl, Float.parseFloat(right) * negr};
    }

    /**
     *
     * @param locOfOp Index of the center operator
     * @param expression mathematical expression in string form
     * @return the operators left and right of the operator
     *  [index of left operator, index of right operator]
     */
    private int[] findIndexOfOpps(int locOfOp, String expression){
        int leftIndex = 0;
        int rightIndex = expression.length();

        for(int i=locOfOp-1;i>=0;i--){
            if(expression.charAt(i) == '-' && i != 0 && isOp(expression.charAt(i - 1))){
                leftIndex = i - 1;
                break;
            }
            else if(isOp(expression.charAt(i)) && expression.charAt(i) != '-'){
                leftIndex = i;
                break;
            }
        }

        for(int i=locOfOp+1;i<expression.length();i++){
            if(expression.charAt(i) == '-' && Character.isDigit(expression.charAt(i-1))){
                rightIndex = i;
                break;
            }
            else if(isOp(expression.charAt(i)) && expression.charAt(i) != '-'){
                rightIndex = i;
                break;
            }
        }

        return new int[]{leftIndex, rightIndex};
    }

    private boolean isOp(char ch){
        switch (ch){
            case '+':
                return true;
            case '-':
                return true;
            case '*':
                return true;
            case '/':
                return true;
            case ')':
                return true;
            case '(':
                return true;
        }

        return false;
    }
}
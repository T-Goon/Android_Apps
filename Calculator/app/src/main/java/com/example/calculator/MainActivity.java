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

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static Editable text;
    private static java.util.Timer cursor = new java.util.Timer();
    private static int cursorLocation = 0;
    private static Handler cursorHandler = new Handler();
    private static long startTime = 0;
    private static String abort = "Abort Action";
    private static String[] opList = {"(", ")", "+", "-", "*", "/", "^", "sin", "cos", "tan"};

    private static Runnable timerRunnable = new Runnable() {
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
    public static void addToExpression(View view){
        if((view instanceof Button) && text.toString().length() <= 300){
            Button button = (Button)view;
            text.insert(cursorLocation, button.getText());
            cursorLocation += button.getText().length();
        }

    }

    /**
     * Removes a component from the mathematical expression.
     * @param view Button view object
     */
    public static void removeFromExpression(View view){
        if(text.length() > 1 && cursorLocation > 0) {
            text.delete(cursorLocation - 1, cursorLocation);
            cursorLocation--;
        }
    }

    /**
     * Clears all text from the mathematical expression.
     * @param view Button view object
     */
    public static void clearExpression(View view){
        text.replace(0, text.toString().length(), " ");
        cursorLocation = 0;
    }

    /**
     * Changes the cursor between a space and a |
     */
    private static void blink(){
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
    public static void moveLeft(View view){
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
    public static void moveRight(View view){
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
    public static void evaluate(View view){
        String expression = text.toString();
        // Remove the cursor from the expression before evaluating it
        expression = expression.replace(" ", "");
        expression = expression.replace("|", "");

        expression = evaluateExpression(expression);

        text.replace(0, text.length(), expression+" ");
        cursorLocation = expression.length();
    }

    /**
     * Follows PEMDAS to evaluate the expression.
     * @param expression String representation of the expression
     * @return The evaluated expression.
     */
    public static String evaluateExpression(String expression){
        expression = evaluateParens(expression);

        expression = evaluateTrig(expression, opList[7]);
        expression = evaluateTrig(expression, opList[8]);
        expression = evaluateTrig(expression, opList[9]);

        expression = evaluateExponents(expression);

        expression = evaluateOp(expression, "*", "/");

        expression = evaluateOp(expression, "+", "-");

        return expression;
    }

    private static String evaluateTrig(String expression, String op){
        while(expression.indexOf(op) != -1) {
            try {
                Double num = Double.parseDouble(evaluateExpression(expression.substring(expression.indexOf(op) + 3,
                                findIndexOfOpps(expression.indexOf(op), expression)[1])));
                Double temp = 0d;

                if(op.equals(opList[7]))
                    temp = Math.sin(num);
                else if(op.equals(opList[8]))
                    temp = Math.cos(num);
                else if(op.equals(opList[9]))
                    temp = Math.tan(num);

                expression = expression.replaceFirst(op + "\\d+(\\.\\d+)?([-+/*]\\d+(\\.\\d+)?)*",
                        String.valueOf(temp));
            }
            catch (Exception e){
                return "Invalid Expression";
            }
        }

        return expression;
    }

    /**
     * Checks if there is an expression in parentheses to evaluate and if there is evaluate it
     * and update the given expression
     * @param expression Mathematical expression in string form
     * @return The expression with the parentheses evaluated if there were any or a message if the expression
     * is invalid.
     */
    private static String evaluateParens(String expression){
        expression = parenPrep(expression);

        int parenPair[] = new int[2];
        // Initialize array with -1's
        parenPair[0] = -1;
        parenPair[1] = -1;

        int deepestOpenParen = expression.indexOf('(');

        // Find the first and deepest pair of parens.
        while(deepestOpenParen != -1){
            boolean nextIt = false;
            int closestCloseParen = -1;

            // Look for the closest close paren
            for(int i=deepestOpenParen + 1;i<expression.length();i++){
                // If it finds another open paren first then set it was the deepest open paren
                // and go to the next loop iteration.
                if(expression.charAt(i) == '('){
                    deepestOpenParen = i;
                    nextIt = true;
                    break;
                }
                else if(expression.charAt(i) == ')'){
                    closestCloseParen = i;
                }
            }

            if(nextIt)
                continue;

            parenPair[0] = deepestOpenParen;
            parenPair[1] = closestCloseParen;

            break;
        }

        // If there is a valid paren pair
        if(parenPair[0] != -1 && parenPair[1] != -1) {
            // Normal case where the open paren is not at index 0.
            if(parenPair[0] != 0)
                // expression = part before the open paren + evaluated expression inside the paren
                // + part after the open paren
                // (Removes the paren pair from the expression)
                expression = expression.substring(0, parenPair[0]) +
                        evaluateExpression(expression.substring(parenPair[0] + 1, parenPair[1])) +
                        expression.substring(parenPair[1] + 1, expression.length());
            // Special case where the open paren is at index 0.
            else
                // expression = evaluated expression inside the paren + part after the open paren
                // (Removes the paren pair from the expression)
                expression = evaluateExpression(expression.substring(parenPair[0] + 1, parenPair[1])) +
                        expression.substring(parenPair[1] + 1, expression.length());
        }
        // If the expression contains imbalanced parens send a message to the user.
        else if(parenPair[0] != -1 || parenPair[1] != -1)
            expression = "Invalid Expression";


        // Recurse if there is a possibility for more than one paren pair.
        if(expression.indexOf('(') != -1)
            expression = evaluateParens(expression);

        return expression;
    }

    /**
     * If there are any parens that are like this -> ")("
     * put a * sign between them
     * @param expression the expression in string form
     * @return the expressions with the added *'s if they are needed
     */
    private static String parenPrep(String expression){
        expression.replaceAll("\\)\\(", ")*(");

        // Looks for "digit(" pattern and places a * between them
        int i = expression.indexOf("(");
        while(i > 0 && Character.isDigit(expression.substring(i-1, i).charAt(0))){
            expression = expression.substring(0, i) + "*" + expression.substring(i);

            i = expression.indexOf("(", i+2);
        }

        // Looks for the ")digit" pattern or the ")." pattern and places a * between them
        int j = expression.indexOf(")");
        while(j != -1 && j < expression.length()-1 && (Character.isDigit(expression.substring(j+1, j+2).charAt(0))
                || expression.substring(j+1, j+2).charAt(0) == '.')){
            expression = expression.substring(0, j+1) + "*" + expression.substring(j+1);

            j = expression.indexOf(")", j+1);
        }

        return expression;
    }

    /**
     * Finds and evaluates any exponents in the expression.
     * @param expression expression in string form
     * @return The expression with any exponents evaluated or the original expression.
     */
    private static String evaluateExponents(String expression){
        // Loop through the expression looking for '^''s
        for(int i=0;i<expression.length();i++){
            // If it finds one have opHelper evaluate it
            if(expression.charAt(i) == '^'){
                expression = opHelper(expression, i, "^");
            }
        }

        if(expression.equals(abort)){
            return "Misuse of Exponents";
        }

        return expression;
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
    private static String evaluateOp(String expression, String op1, String op2){
        // Loops through expression string looking for op1 or op2
        for(int i=1;i<expression.length();i++){

            if(expression.substring(i,i+1).equals(op1)){
                String result = opHelper(expression, i, op1);
                if(result.equals(abort)){
                    return "Invalid Expression";
                }
                else {
                    expression = evaluateOp(result, op1, op2);
                    break;
                }
            }
            else if(expression.substring(i, i+1).equals(op2)){
                String result = opHelper(expression, i, op2);
                if(result.equals(abort)){
                    return "Invalid Expression";
                }
                else {
                    expression = evaluateOp(result, op1, op2);
                    break;
                }
            }
        }

        return expression;
    }

    private static String opHelper(String expression, int indexOfOpp, String opp){
        float constants[] = findConstants(indexOfOpp, expression);

        int indexOfOps[] = findIndexOfOpps(indexOfOpp, expression);
        float result = 0;

        if(constants == null)
            return abort;

        if(opp.equals(opList[6]))
            result = (float)Math.pow(constants[0], constants[1]);
        else if(opp.equals(opList[4]))
            result = constants[0] * constants[1];
        else if(opp.equals(opList[5]))
            result = constants[0] / constants[1];
        else if(opp.equals(opList[2]))
            result = constants[0] + constants[1];
        else if(opp.equals(opList[3]))
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
    private static float[] findConstants(int locOfOp, String expression){
        String left = "";
        float negl = 1;

        // Loops backward through the expression starting at locOfOp
        for(int i=locOfOp-1;i>=0;i--){
            if(Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.'){
                left = expression.charAt(i) + left;
            }
            else if(expression.charAt(i) == '-' && ((i == 0) || isOp(expression.substring(i - 1, i)))){
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

        try {
            return new float[]{Float.parseFloat(left) * negl, Float.parseFloat(right) * negr};
        }
        catch(Exception e){
            return null;
        }
    }

    /**
     *
     * @param locOfOp Index of the center operator
     * @param expression mathematical expression in string form
     * @return the operators left and right of the operator
     *  [index of left operator, index of right operator]
     */
    private static int[] findIndexOfOpps(int locOfOp, String expression){
        int leftIndex = 0;
        int rightIndex = expression.length();

        for(int i=locOfOp-1;i>0;i--){
            if(expression.charAt(i) == '-' && i != 0 && isOp(expression.substring(i - 1, i))){
                leftIndex = i - 1;
                break;
            }
            else if(isOp(expression.substring(i - 1, i)) && expression.charAt(i) != '-'){
                leftIndex = i - 1;
                break;
            }
        }

        for(int i=locOfOp+1;i<expression.length();i++){
            if(expression.charAt(i) == '-' && Character.isDigit(expression.charAt(i-1))){
                rightIndex = i;
                break;
            }
            else if(isOp(expression.substring(i, i + 1)) && expression.charAt(i) != '-'){
                rightIndex = i;
                break;
            }
        }

        return new int[]{leftIndex, rightIndex};
    }

    /**
     * Checks the passed in string to check if it is an operator or not
     * @param op the passed in string
     * @return true|false, depending on if op is an operator or not
     */
    private static boolean isOp(String op){
        for(int i=0;i<opList.length;i++) {
            if(opList[i].equals(op)){
                return true;
            }
        }

        return false;
    }
}
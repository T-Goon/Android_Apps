package com.example.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static Editable text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView display = findViewById(R.id.display);
        text = display.getEditableText();
    }

    /**
     * Adds a component to the mathematical expression.
     * @param view Button view object
     */
    public void addToExpression(View view){
        if(view instanceof Button){
            Button button = (Button)view;
            text.append(button.getText());
        }

    }

    /**
     * Removes a component from the mathematical expression.
     * @param view Button view object
     */
    public void removeFromExpression(View view){
        if(text.length() > 0)
            text.delete(text.length()-1, text.length());
    }

    /**
     * Clears all text from the mathematical expression.
     * @param view Button view object
     */
    public void clearExpression(View view){
        text.clear();
    }

}

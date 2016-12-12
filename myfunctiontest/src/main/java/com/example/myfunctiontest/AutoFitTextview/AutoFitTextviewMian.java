package com.example.myfunctiontest.AutoFitTextview;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myfunctiontest.R;

/**
 * author: Administrator
 * created on: 2016/12/12 11:23
 * description:
 */

public class AutoFitTextviewMian extends Activity{
    private TextView mOutput, mAutofitOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autofittextview);
        mOutput = (TextView)findViewById(R.id.output);
        mAutofitOutput = (TextView)findViewById(R.id.output_autofit);

        ((EditText)findViewById(R.id.input)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                mOutput.setText(charSequence);
                mAutofitOutput.setText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do nothing
            }
        });
    }
}

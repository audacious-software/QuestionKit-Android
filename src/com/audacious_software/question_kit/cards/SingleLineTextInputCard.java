package com.audacious_software.question_kit.cards;

import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.audacious_software.question_kit.QuestionsActivity;
import com.audacious_software.question_kit.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SingleLineTextInputCard extends MultiLineTextInputCard {
    private AppCompatAutoCompleteTextView mField = null;

    public SingleLineTextInputCard(QuestionsActivity activity, JSONObject prompt, String defaultLanguage) {
        super(activity, prompt, defaultLanguage);
    }

    protected void initializeView(JSONObject prompt, ViewGroup parent) throws JSONException {
        super.initializeView(prompt, parent);

        this.mField = parent.findViewById(R.id.answer_field);

        this.mField.setMaxLines(1);
        this.mField.setLines(1);
        this.mField.setMinLines(1);
        this.mField.setSingleLine(true);
        this.mField.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS);
    }

    public void updateValue(Object value) {
        String text  = (String) value;

        this.mField.setText(text);

        this.updateValue(this.key(), text);
    }
}


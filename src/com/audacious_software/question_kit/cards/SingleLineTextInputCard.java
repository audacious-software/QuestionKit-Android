package com.audacious_software.question_kit.cards;

import android.text.InputType;
import android.view.ViewGroup;

import com.audacious_software.question_kit.QuestionsActivity;
import com.audacious_software.question_kit.R;
import com.audacious_software.question_kit.views.TextInputAutoCompleteTextView;

import org.json.JSONException;
import org.json.JSONObject;

public class SingleLineTextInputCard extends MultiLineTextInputCard {
    public SingleLineTextInputCard(QuestionsActivity activity, JSONObject prompt, String defaultLanguage) {
        super(activity, prompt, defaultLanguage);
    }

    protected void initializeView(JSONObject prompt, ViewGroup parent) throws JSONException {
        super.initializeView(prompt, parent);

        TextInputAutoCompleteTextView field = parent.findViewById(R.id.answer_field);

        field.setMaxLines(1);
        field.setLines(1);
        field.setMinLines(1);
        field.setSingleLine(true);
        field.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS);
    }
}


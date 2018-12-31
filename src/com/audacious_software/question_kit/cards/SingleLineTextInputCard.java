package com.audacious_software.question_kit.cards;

import android.view.ViewGroup;
import android.widget.TextView;

import com.audacious_software.question_kit.QuestionsActivity;
import com.audacious_software.question_kit.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class SingleLineTextInputCard extends MultiLineTextInputCard {
    public SingleLineTextInputCard(QuestionsActivity activity, JSONObject prompt) {
        super(activity, prompt);
    }

    protected void initializeView(JSONObject prompt, ViewGroup parent) throws JSONException {
        super.initializeView(prompt, parent);

        TextInputEditText field = parent.findViewById(R.id.answer_field);

        field.setSingleLine(true);
    }
}


package com.audacious_software.question_kit.cards;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.TextView;

import com.audacious_software.question_kit.QuestionsActivity;
import com.audacious_software.question_kit.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class MultiLineTextInputCard extends QuestionCard {
    public MultiLineTextInputCard(QuestionsActivity activity, JSONObject prompt) {
        super(activity, prompt);
    }

    protected void initializeView(JSONObject prompt, ViewGroup parent) throws JSONException {
        TextView promptLabel = parent.findViewById(R.id.prompt_label);

        TextInputLayout layout = parent.findViewById(R.id.answer_layout);

        promptLabel.setText(this.getLocalizedValue(prompt, "prompt"));

        if (prompt.has("hint")) {
            layout.setHint(this.getLocalizedValue(prompt, "hint"));
        }

        this.setupChangeListener(parent);
    }

    protected void setupChangeListener(ViewGroup parent) {
        final MultiLineTextInputCard me = this;

        TextInputEditText field = parent.findViewById(R.id.answer_field);

        field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                me.updateValue(me.key(), text.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    protected int getCardLayoutResource() {
        return R.layout.card_question_multiline;
    }
}

package com.audacious_software.question_kit.cards;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.audacious_software.question_kit.QuestionsActivity;
import com.audacious_software.question_kit.R;
// import com.audacious_software.question_kit.views.TextInputAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MultiLineTextInputCard extends QuestionCard {
    private AppCompatAutoCompleteTextView mTextView = null;

    public MultiLineTextInputCard(QuestionsActivity activity, JSONObject prompt, String defaultLanguage) {
        super(activity, prompt, defaultLanguage);
    }

    protected void initializeView(JSONObject prompt, ViewGroup parent) throws JSONException {
        TextView promptLabel = parent.findViewById(R.id.prompt_label);

        TextInputLayout layout = parent.findViewById(R.id.answer_layout);

        promptLabel.setText(this.getLocalizedValue(prompt, "prompt"));

        if (prompt.has("hint")) {
            layout.setHint(this.getLocalizedValue(prompt, "hint"));
        }

        this.setupChangeListener(parent);

        final Activity activity = this.getActivity();

        final AppCompatAutoCompleteTextView textView = parent.findViewById(R.id.answer_field);

        if (activity instanceof QuestionCard.QuestionAutofillSuggestionProvider) {
            QuestionCard.QuestionAutofillSuggestionProvider provider = (QuestionCard.QuestionAutofillSuggestionProvider) activity;

            textView.setThreshold(1);

            provider.fetchSuggestions(prompt.getString("key"), new QuestionAutofillSuggestionResults() {
                @Override
                public void onSuggestions(List<String> suggestions) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, suggestions);

                    textView.setAdapter(adapter);
                }
            });
        }

        if (prompt.has("value")) {
            this.updateValue(prompt.getString("value"));
        }

        this.mTextView = textView;
    }

    public String description() {
        try {
            return this.getLocalizedValue(this.getPrompt(), "prompt");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return super.description();
    }

    protected void setupChangeListener(ViewGroup parent) {
        final MultiLineTextInputCard me = this;

        AppCompatAutoCompleteTextView field = parent.findViewById(R.id.answer_field);

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

    public void updateValue(Object value) {
        String text  = (String) value;

        this.mTextView.setText(text);

        this.updateValue(this.key(), text);
    }
}

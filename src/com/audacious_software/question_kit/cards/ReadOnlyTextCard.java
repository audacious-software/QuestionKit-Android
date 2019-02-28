package com.audacious_software.question_kit.cards;

import android.view.ViewGroup;
import android.widget.TextView;

import com.audacious_software.question_kit.QuestionsActivity;
import com.audacious_software.question_kit.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ReadOnlyTextCard extends QuestionCard {
    public ReadOnlyTextCard(QuestionsActivity activity, JSONObject prompt) {
        super(activity, prompt);
    }

    protected void initializeView(JSONObject prompt, ViewGroup parent) throws JSONException {
        TextView textLabel = parent.findViewById(R.id.text_label);

        textLabel.setText(prompt.getString("text"));
    }

    protected int getCardLayoutResource() {
        return R.layout.card_question_read_only_text;
    }
}
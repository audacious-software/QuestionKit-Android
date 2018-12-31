package com.audacious_software.question_kit.cards;

import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.audacious_software.question_kit.QuestionsActivity;
import com.audacious_software.question_kit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelectOneCard extends QuestionCard {
    public SelectOneCard(QuestionsActivity activity, JSONObject prompt) {
        super(activity, prompt);
    }

    protected void initializeView(JSONObject prompt, ViewGroup parent) throws JSONException {
        TextView promptLabel = parent.findViewById(R.id.prompt_label);
        promptLabel.setText(prompt.getString("prompt"));

        RadioGroup radios = this.findViewById(R.id.radio_group);

        final SelectOneCard me = this;

        if (prompt.has("options")) {
            final JSONArray options = prompt.getJSONArray("options");

            for (int i = 0; i < options.length(); i++) {
                JSONObject option = options.getJSONObject(i);

                RadioButton radio = new RadioButton(this.getActivity());
                radio.setId(i);
                radio.setText(option.getString("label"));

                radios.addView(radio, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            radios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    try {
                        JSONObject option = options.getJSONObject(checkedId);

                        String value = option.getString("value");

                        me.updateValue(me.key(), value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    protected int getCardLayoutResource() {
        return R.layout.card_question_select_one;
    }
}

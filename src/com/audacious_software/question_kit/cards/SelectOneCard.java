package com.audacious_software.question_kit.cards;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.audacious_software.question_kit.QuestionsActivity;
import com.audacious_software.question_kit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelectOneCard extends QuestionCard {
    private RadioGroup mRadios = null;

    public SelectOneCard(QuestionsActivity activity, JSONObject prompt, String defaultLanguage) {
        super(activity, prompt, defaultLanguage);
    }

    protected void initializeView(JSONObject prompt, ViewGroup parent) throws JSONException {
        TextView promptLabel = parent.findViewById(R.id.prompt_label);
        promptLabel.setText(this.getLocalizedValue(prompt, "prompt"));

        this.mRadios = this.findViewById(R.id.radio_group);

        final SelectOneCard me = this;

        if (prompt.has("options")) {
            final JSONArray options = prompt.getJSONArray("options");

            RadioButton preselect = null;

            for (int i = 0; i < options.length(); i++) {
                JSONObject option = options.getJSONObject(i);

                RadioButton radio = new RadioButton(this.getActivity());
                radio.setId(i);
                radio.setText(this.getLocalizedValue(option, "label"));

                if (prompt.has("value") && radio.getText().equals(prompt.getString("value"))) {
                    preselect = radio;
                }

                this.mRadios.addView(radio, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            this.mRadios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

            if (preselect != null) {
                preselect.toggle();
            }
        }
    }

    public String description() {
        try {
            return this.getLocalizedValue(this.getPrompt(), "prompt");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return super.description();
    }

    protected int getCardLayoutResource() {
        return R.layout.card_question_select_one;
    }

    public void updateValue(Object value) {
        String selected = (String) value;

        try {
            final JSONArray options = this.mPrompt.getJSONArray("options");

            for (int i = 0; i < options.length(); i++) {
                JSONObject option = options.getJSONObject(i);

                RadioButton radio = (RadioButton) this.mRadios.getChildAt(i);

                String optionValue = option.getString("value");

                if (selected.equals(optionValue)) {
                    radio.setChecked(true);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.updateValue(this.key(), selected);
    }
}

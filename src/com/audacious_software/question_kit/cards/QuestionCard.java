package com.audacious_software.question_kit.cards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.audacious_software.question_kit.DateRangeActivity;
import com.audacious_software.question_kit.QuestionsActivity;
import com.audacious_software.question_kit.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class QuestionCard extends FrameLayout {
    private final CardView mCardView;
    private final JSONObject mPrompt;
    private final QuestionsActivity mActivity;

    public QuestionCard(QuestionsActivity activity, JSONObject prompt) {
        super(activity);
        
        this.mActivity = activity;

        this.mPrompt = prompt;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(params);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.mCardView = (CardView) inflater.inflate(this.getCardLayoutResource(), null);

        this.addView(this.mCardView);

        try {
            this.initializeView(this.mPrompt, this.mCardView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void initializeView(JSONObject prompt, ViewGroup parent) throws JSONException {
        TextView subclassLabel = parent.findViewById(R.id.label_subclass_me);

        String labelText = this.getClass().getCanonicalName();

        if (prompt.has("key")) {
            labelText += ": " + prompt.getString("key");
        }

        subclassLabel.setText(labelText);
    }

    protected int getCardLayoutResource() {
        return R.layout.card_question;
    }

    public void setIsFirstCard(boolean isFirstCard) {
        DisplayMetrics metrics = new DisplayMetrics();

        this.mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int padding = (int) (8 * metrics.density);

        FrameLayout.LayoutParams params = (LayoutParams) this.mCardView.getLayoutParams();

        if (isFirstCard) {
            params.setMargins(padding, padding, padding, padding);
        } else {
            params.setMargins(padding, 0, padding, padding);
        }

        this.mCardView.setLayoutParams(params);

        this.invalidate();
    }

    public String key() {
        try {
            return this.mPrompt.getString("key");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    protected void updateValue(String key, Object value) {
        this.mActivity.updateValue(key, value);
    }

    protected Activity getActivity() {
        return this.mActivity;
    }
}

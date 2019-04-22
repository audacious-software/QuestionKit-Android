package com.audacious_software.question_kit.cards;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.audacious_software.question_kit.QuestionsActivity;
import com.audacious_software.question_kit.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.cardview.widget.CardView;
import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

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

    protected String getLocalizedValue(JSONObject prompt, String key) throws JSONException {
        if (prompt.has(key)) {
            LocaleListCompat locales = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration());

            JSONObject holder = prompt.getJSONObject(key);

            for (int i = 0; i < locales.size(); i++) {
                String languageCode = locales.get(i).getLanguage();

                if (holder.has(languageCode)) {
                    return holder.getString(languageCode);
                }
            }
        }

        return key;
    }

    public abstract class QuestionAutofillSuggestionResults {
        public abstract void onSuggestions(List<String> suggestions);
    }

    public interface QuestionAutofillSuggestionProvider {
       void fetchSuggestions(String key, QuestionAutofillSuggestionResults resultsHandler);
    }

}

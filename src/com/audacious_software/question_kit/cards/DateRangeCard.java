package com.audacious_software.question_kit.cards;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.audacious_software.question_kit.DateRangeActivity;
import com.audacious_software.question_kit.QuestionsActivity;
import com.audacious_software.question_kit.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateRangeCard extends SingleLineTextInputCard {
    private boolean mWaitingForTouch = true;

    public DateRangeCard(QuestionsActivity activity, JSONObject prompt, String defaultLanguage) {
        super(activity, prompt, defaultLanguage);
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void initializeView(JSONObject prompt, ViewGroup parent) throws JSONException {
        super.initializeView(prompt, parent);

        TextInputEditText field = parent.findViewById(R.id.answer_field);

        final Activity activity = this.getActivity();
        final DateRangeCard me = this;

        field.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (me.mWaitingForTouch) {
                    me.mWaitingForTouch = false;
                    Intent intent = new Intent(activity, DateRangeActivity.class);
                    intent.putExtra(DateRangeActivity.QUESTION_KEY, me.key());

                    activity.startActivityForResult(intent, DateRangeActivity.SELECT_DATE_RANGE);

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            me.mWaitingForTouch = true;
                        }
                    }, 500);
                }

                return true;
            }
        });
    }

    protected void setupChangeListener(ViewGroup parent) {
        // Intentionally do nothing...
    }

    public void setSelectedRange(long start, long end) {
        TextInputEditText field = this.findViewById(R.id.answer_field);
        Context context = this.getContext();

        DateFormat format = android.text.format.DateFormat.getMediumDateFormat(context);

        Date startDate = new Date(start);
        Date endDate = new Date(end);

        String startString = format.format(startDate);
        String endString = format.format(endDate);

        if (startString.equals(endString)) {
            field.setText(startString);
        } else {
            field.setText(context.getString(R.string.question_kit_date_range, startString, endString));
        }

        List<Date> dates = new ArrayList<>();
        dates.add(startDate);

        if (start != end) {
            dates.add(endDate);
        }

        this.updateValue(this.key(), dates);
    }
}

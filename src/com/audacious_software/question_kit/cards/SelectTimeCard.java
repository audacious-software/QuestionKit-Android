package com.audacious_software.question_kit.cards;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.audacious_software.question_kit.QuestionsActivity;
import com.audacious_software.question_kit.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SelectTimeCard extends SingleLineTextInputCard {
    private boolean mWaitingForTouch = true;

    public SelectTimeCard(QuestionsActivity activity, JSONObject prompt) {
        super(activity, prompt);
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void initializeView(final JSONObject prompt, final ViewGroup parent) throws JSONException {
        super.initializeView(prompt, parent);

        TextInputEditText field = parent.findViewById(R.id.answer_field);

        final SelectTimeCard me = this;

        field.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (me.mWaitingForTouch) {
                    me.mWaitingForTouch = false;

                    int hour = 0;
                    int minute = 0;

                    if (prompt.has("default")) {

                        try {
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm");

                            Date date = format.parse(prompt.getString("default"));

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);

                            hour = calendar.get(Calendar.HOUR_OF_DAY);
                            minute = calendar.get(Calendar.MINUTE);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    TimePickerDialog picker = new TimePickerDialog(parent.getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                            me.setSelectedTime(hour, minute);

                        }
                    }, hour, minute, DateFormat.is24HourFormat(parent.getContext()));

                    picker.show();

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

    public void setSelectedTime(int hour, int minute) {
        TextInputEditText field = this.findViewById(R.id.answer_field);
        Context context = this.getContext();

        java.text.DateFormat format = DateFormat.getTimeFormat(context);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        field.setText(format.format(calendar.getTime()));

        this.updateValue(this.key(), hour + ":" + minute);
    }
}
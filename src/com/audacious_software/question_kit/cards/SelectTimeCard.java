package com.audacious_software.question_kit.cards;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.audacious_software.question_kit.QuestionsActivity;
import com.audacious_software.question_kit.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SelectTimeCard extends SingleLineTextInputCard {
    private boolean mWaitingForTouch = true;

    private boolean mDisplayingPicker = false;

    public SelectTimeCard(QuestionsActivity activity, JSONObject prompt, String defaultLanguage) {
        super(activity, prompt, defaultLanguage);
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void initializeView(final JSONObject prompt, final ViewGroup parent) throws JSONException {
        super.initializeView(prompt, parent);

        final SelectTimeCard me = this;

        final AppCompatAutoCompleteTextView field = parent.findViewById(R.id.answer_field);

        field.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (focused && me.mDisplayingPicker == false) {
                    me.mDisplayingPicker = true;

                    Handler handler = new Handler(Looper.getMainLooper());

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int hour = 0;
                            int minute = 0;

                            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());

                            String value = (String) me.fetchValue(me.key());

                            if (value != null) {
                                try {
                                    Date date = format.parse(value);

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);

                                    hour = calendar.get(Calendar.HOUR_OF_DAY);
                                    minute = calendar.get(Calendar.MINUTE);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (value == null && prompt.has("default")) {
                                try {
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

                            picker.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    me.mDisplayingPicker = false;
                                }
                            });
                             picker.show();
                        }
                    }, 100);
                }
            }
        });
    }

    public void updateValue(Object value) {
        String text = (String) value;

        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());

        try {
            Date date = format.parse(text);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            this.setSelectedTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    protected void setupChangeListener(ViewGroup parent) {
        // Intentionally do nothing...
    }

    public void setSelectedTime(int hour, int minute) {
        AppCompatAutoCompleteTextView field = this.findViewById(R.id.answer_field);

        Context context = this.getContext();

        java.text.DateFormat format = DateFormat.getTimeFormat(context);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        field.setText(format.format(calendar.getTime()));

        String minuteString = "" + minute;

        if (minute < 10) {
            minuteString = "0" + minute;
        }

        this.updateValue(this.key(), hour + ":" + minuteString);
    }
}

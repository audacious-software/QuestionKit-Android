package com.audacious_software.question_kit;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Schedule {
    private static final String CACHED_SCHEDULE_JSON = "com.audacious_software.question_kit.Schedule.CACHED_SCHEDULE_JSON";

    private static Schedule sInstance = null;
    private Context mContext = null;

    public static Schedule getInstance(Context context) {
        if (Schedule.sInstance == null) {
            Schedule.sInstance = new Schedule(context.getApplicationContext());
        }

        return Schedule.sInstance;
    }

    private Schedule(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void updateSchedule(JSONObject scheduleJson) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mContext);

            SharedPreferences.Editor e = prefs.edit();
            e.putString(Schedule.CACHED_SCHEDULE_JSON, scheduleJson.toString(2));
            e.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<ScheduledQuestionSet> pendingItems() {
        ArrayList<ScheduledQuestionSet> pending = new ArrayList<>();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mContext);

        String jsonDef = prefs.getString(Schedule.CACHED_SCHEDULE_JSON, null);

        if (jsonDef != null) {
            long now = System.currentTimeMillis();

            try {
                JSONObject schedule = new JSONObject(jsonDef);

                if (schedule.has("schedule") && schedule.has("questions")) {
                    JSONArray scheduledItems = schedule.getJSONArray("schedule");

                    JSONObject questions = schedule.getJSONObject("questions");

                    for (int i = 0; i < scheduledItems.length(); i++) {
                        JSONObject scheduledItem = scheduledItems.getJSONObject(i);

                        String questionsId = scheduledItem.getString("questions");

                        ScheduledQuestionSet scheduled = new ScheduledQuestionSet(questionsId, scheduledItem, questions.getJSONObject(questionsId));

                        if (scheduled.start.getTime() < now && scheduled.end.getTime() > now) {
                            pending.add(scheduled);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(pending, new Comparator<ScheduledQuestionSet>() {
            @Override
            public int compare(ScheduledQuestionSet one, ScheduledQuestionSet two) {
                return one.start.compareTo(two.start);
            }
        });

        return pending;
    }
}

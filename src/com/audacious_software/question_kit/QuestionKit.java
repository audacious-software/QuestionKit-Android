package com.audacious_software.question_kit;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuestionKit {
    private static final String SCHEDULE_URL = "com.audacious_software.question_kit.QuestionKit.SCHEDULE_URL";
    private static final String SCHEDULE_DEFINITION = "com.audacious_software.question_kit.QuestionKit.SCHEDULE_DEFINITION";

    private static QuestionKit sInstance = null;

    private Context mContext = null;

    public static QuestionKit getInstance(Context context) {
        if (QuestionKit.sInstance == null) {
            QuestionKit.sInstance = new QuestionKit(context);
        }

        return QuestionKit.sInstance;
    }

    private QuestionKit(final Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void setScheduleURL(String scheduleUrl) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mContext);

        SharedPreferences.Editor e = prefs.edit();
        e.putString(QuestionKit.SCHEDULE_URL, scheduleUrl);
        e.apply();

        this.refreshSchedule();
    }

    private void refreshSchedule() {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mContext);

        String url = prefs.getString(QuestionKit.SCHEDULE_URL, null);

        if (url != null) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject schedule = new JSONObject(response.body().string());

                            SharedPreferences.Editor e = prefs.edit();
                            e.putString(QuestionKit.SCHEDULE_DEFINITION, schedule.toString(2));
                            e.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                }
            });
        }
    }

    public List<ScheduledQuestionSet> schedule(Date start, Date end) {
        if (end == null) {
            end = new Date(Long.MAX_VALUE);
        }

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

        ArrayList<ScheduledQuestionSet> matches = new ArrayList<>();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mContext);

        String definition = prefs.getString(QuestionKit.SCHEDULE_DEFINITION, null);

        if (definition != null) {
            try {
                JSONObject scheduleDef = new JSONObject(definition);

                Iterator<String> sets = scheduleDef.keys();

                while (sets.hasNext()) {
                    String key = sets.next();

                    JSONObject setDef = scheduleDef.getJSONObject(key);

                    String setName = setDef.getString("name");

                    JSONObject setDefinition = setDef.getJSONObject("definition");
                    JSONArray scheduledItems = setDef.getJSONArray("schedule");

                    for (int i = 0; i < scheduledItems.length(); i++) {
                        JSONObject scheduledItem = scheduledItems.getJSONObject(i);

                        Date itemStart = format.parse(scheduledItem.getString("start"));

                        Date itemEnd = null;

                        if (scheduledItem.has("end")) {
                            itemEnd = format.parse(scheduledItem.getString("end"));
                        }

                        boolean include = false;

                        if (itemStart.getTime() > start.getTime() && itemStart.getTime() < end.getTime()) {
                            include = true;
                        } else if (itemEnd != null) {
                            if (itemEnd.getTime() > start.getTime() && itemEnd.getTime() < end.getTime()) {
                                include = true;
                            } else if (itemStart.getTime() < start.getTime() && itemEnd.getTime() > end.getTime()) {
                                include = true;
                            }
                        }

                        if (include) {
                            matches.add(new ScheduledQuestionSet(key, setName, setDefinition, itemStart, itemEnd));
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return matches;
    }
}

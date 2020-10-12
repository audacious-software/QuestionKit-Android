package com.audacious_software.question_kit;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScheduledQuestionSet {
    public String identifier = null;
    public String name = null;
    public JSONObject definition = null;

    public Date start = null;
    public Date end = null;

    public ScheduledQuestionSet(String identifier, String name, JSONObject definition, Date start, Date end) {
        this.identifier = identifier;
        this.name = name;
        this.definition = definition;
        this.start = start;
        this.end = end;
    }

    public ScheduledQuestionSet(String identifier, JSONObject scheduleJson, JSONObject questionsJson) throws JSONException {
        this.identifier = identifier;
        this.name = questionsJson.getString("name");
        this.definition = questionsJson.getJSONObject("definition");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            this.start = format.parse(scheduleJson.getString("date") + " " + scheduleJson.getString("start"));
            this.end = format.parse(scheduleJson.getString("date") + " " + scheduleJson.getString("end"));

            while (this.start.getTime() > this.end.getTime()) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(end);

                cal.add(Calendar.DATE, 1);

                this.end = cal.getTime();
            }
        } catch (ParseException e) {
            throw new JSONException("Invalid date information provided: " + scheduleJson.toString());
        }
    }
}

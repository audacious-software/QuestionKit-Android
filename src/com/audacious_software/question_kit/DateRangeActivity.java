package com.audacious_software.question_kit;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class DateRangeActivity extends AppCompatActivity {
    public static final String QUESTION_KEY = "com.audacious_software.question_kit.DateRangeActivity.QUESTION_KEY";
    public static final String RANGE_START = "com.audacious_software.question_kit.DateRangeActivity.RANGE_START";
    public static final String RANGE_END = "com.audacious_software.question_kit.DateRangeActivity.RANGE_END";
    public static final String SELECTION_INSTRUCTIONS = "com.audacious_software.question_kit.DateRangeActivity.RANGE_END";

    public static final int SELECT_DATE_RANGE = 128;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_date_range);

        final ActionBar actionbar = this.getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.question_kit_title_select_date_range);

        MaterialCalendarView calendar = this.findViewById(R.id.date_calendar);
        calendar.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            calendar.setElevation(4);
        }

        final DateRangeActivity me = this;

        calendar.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                CalendarDay first = dates.get(0);
                CalendarDay last = dates.get(dates.size() - 1);

                Intent intent = me.getIntent();
                intent.putExtra(DateRangeActivity.RANGE_START, first.getDate().getTime());
                intent.putExtra(DateRangeActivity.RANGE_END, last.getDate().getTime());

                me.setResult(DateRangeActivity.SELECT_DATE_RANGE, intent);
            }
        });

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Intent intent = me.getIntent();
                intent.putExtra(DateRangeActivity.RANGE_START, date.getDate().getTime());
                intent.putExtra(DateRangeActivity.RANGE_END, date.getDate().getTime());

                me.setResult(DateRangeActivity.SELECT_DATE_RANGE, intent);
            }
        });

        TextView instructions = this.findViewById(R.id.date_range_description);

        if (this.getIntent().hasExtra(DateRangeActivity.SELECTION_INSTRUCTIONS)) {
            instructions.setText(this.getIntent().getStringExtra(DateRangeActivity.SELECTION_INSTRUCTIONS));
        } else {
            instructions.setText(R.string.question_kit_instructions_select_date_range);
        }

        this.setResult(DateRangeActivity.SELECT_DATE_RANGE, this.getIntent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

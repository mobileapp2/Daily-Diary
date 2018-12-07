package in.oriange.dailydiary.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;

import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;

import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

import in.oriange.dailydiary.R;

public class SelectDateForPackage_Activity extends Activity {

    private Context context;
    private CalendarPickerView calendar;
    private AlertDialog theDialog;
    private CalendarPickerView dialogView;
    private final Set<Button> modeButtons = new LinkedHashSet<Button>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectdate_forpackage);

        init();
        getSessionData();
        setDefault();
        setEventHandlers();
        setUpToolBar();
    }

    private void init() {
        context = SelectDateForPackage_Activity.this;

        final Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, 0);
        startDate.add(Calendar.DAY_OF_MONTH, 2);

        final Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);
        endDate.add(Calendar.DAY_OF_MONTH, 2);

        calendar = findViewById(R.id.calendar_view);

        calendar.setCustomDayView(new DefaultDayViewAdapter());
        calendar.init(startDate.getTime(), endDate.getTime()).inMode(CalendarPickerView.SelectionMode.MULTIPLE);

    }

    private void getSessionData() {

    }

    private void setDefault() {

    }

    private void setEventHandlers() {

    }

    private void setUpToolBar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(Html.fromHtml("<font color='#00000'>Select Dates</font>"));
        mToolbar.setNavigationIcon(R.drawable.icon_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

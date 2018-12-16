package in.oriange.dailydiary.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;

import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.utilities.Utilities;

public class SelectDateForPackage_Activity extends Activity implements View.OnClickListener {

    private Context context;
    private CalendarPickerView calendar;
    private Button btn_next;

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

        calendar = findViewById(R.id.calendar_view);
        btn_next = findViewById(R.id.btn_next);

    }

    private void getSessionData() {

    }

    private void setDefault() {

        final Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, 0);
        startDate.add(Calendar.DAY_OF_MONTH, 2);

        final Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);
        endDate.add(Calendar.DAY_OF_MONTH, 2);

        calendar.setCustomDayView(new DefaultDayViewAdapter());
        calendar.init(startDate.getTime(), endDate.getTime()).inMode(CalendarPickerView.SelectionMode.MULTIPLE);
    }

    private void setEventHandlers() {
        btn_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                List<Date> selectedDates = calendar.getSelectedDates();

                if (selectedDates.size() != 0) {
                    startActivity(new Intent(context, SelectAddressForPackage_Activity.class).putExtra("selectedDates", (Serializable) selectedDates));
                } else {
                    Utilities.showMessageString(context, "Please select date");
                }
                break;
        }

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

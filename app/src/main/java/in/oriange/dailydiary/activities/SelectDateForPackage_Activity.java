package in.oriange.dailydiary.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import in.oriange.dailydiary.R;

import static android.widget.Toast.LENGTH_SHORT;

public class SelectDateForPackage_Activity extends Activity {

    private Context context;
    private static final String TAG = "SampleTimesSquareActivi";
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
        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);

        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);

        calendar.setCustomDayView(new DefaultDayViewAdapter());
        calendar.init(new Date(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);

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

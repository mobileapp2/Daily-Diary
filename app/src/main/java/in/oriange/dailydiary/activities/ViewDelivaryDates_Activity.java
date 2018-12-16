package in.oriange.dailydiary.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;

import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.models.PackagesModel;

public class ViewDelivaryDates_Activity extends Activity {


    private Context context;
    private CalendarPickerView calendar;

    private ArrayList<PackagesModel.DeliveryDates> delivaryDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_delivarydates);

        init();
        getSessionData();
        setDefault();
        setEventHandlers();
        setUpToolBar();
    }

    private void init() {
        context = ViewDelivaryDates_Activity.this;

        calendar = findViewById(R.id.calendar_view);
        delivaryDates = new ArrayList<>();

    }

    private void getSessionData() {

    }

    private void setDefault() {
        delivaryDates = (ArrayList<PackagesModel.DeliveryDates>) getIntent().getSerializableExtra("delivaryDates");


        ArrayList<Date> delivaryDate = new ArrayList();
        for (int i = 0; i < delivaryDates.size(); i++) {
            try {
                delivaryDate.add(new SimpleDateFormat("yyyy-MM-dd").parse(delivaryDates.get(i).getDelivery_Date()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        Date startDate = null, endDate = null;
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(delivaryDates.get(0).getDelivery_Date());
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(delivaryDates.get(delivaryDates.size() - 1).getDelivery_Date());

            Calendar c = Calendar.getInstance();
            c.setTime(endDate);
            c.add(Calendar.DATE, 1);
            endDate = c.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.init(startDate, endDate).inMode(CalendarPickerView.SelectionMode.MULTIPLE).withSelectedDates(delivaryDate);

    }

    private void setEventHandlers() {
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                setDefault();
            }

            @Override
            public void onDateUnselected(Date date) {
                setDefault();
            }
        });

    }

    private void setUpToolBar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(Html.fromHtml("<font color='#00000'>Delivary Dates</font>"));
        mToolbar.setNavigationIcon(R.drawable.icon_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}

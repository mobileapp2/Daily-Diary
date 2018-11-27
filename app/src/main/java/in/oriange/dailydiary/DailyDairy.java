package in.oriange.dailydiary;

import android.app.Application;
import android.content.Context;

public class DailyDairy extends Application {

    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

}

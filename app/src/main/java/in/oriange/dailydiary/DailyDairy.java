package in.oriange.dailydiary;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DailyDairy extends Application {

    private Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

}

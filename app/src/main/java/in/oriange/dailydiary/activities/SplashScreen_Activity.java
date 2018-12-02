package in.oriange.dailydiary.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.utilities.UserSessionManager;

public class SplashScreen_Activity extends Activity {


    private Context context;
    private ImageView imv_slash;
    private int secondsDelayed = 1;
    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        init();
    }

    private void init() {
        context = SplashScreen_Activity.this;
        session = new UserSessionManager(context);
//        imv_slash.startAnimation(zoomAnimation);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (session.isPinCodeSet()) {
                    startActivity(new Intent(context, MainDrawer_Activity.class));
                } else {
                    startActivity(new Intent(context, GetPinCode_Activity.class));
                }
                finish();
            }
        }, secondsDelayed * 500);
    }


}

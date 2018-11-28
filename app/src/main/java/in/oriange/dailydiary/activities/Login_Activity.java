package in.oriange.dailydiary.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import in.oriange.dailydiary.R;

public class Login_Activity extends AppCompatActivity {

    private Context context;
    private LinearLayout ll_mainlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        context = Login_Activity.this;
        ll_mainlayout = findViewById(R.id.ll_mainlayout);
    }

}

package in.oriange.dailydiary.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.fragments.BottomSheetMenu_Fragment;
import in.oriange.dailydiary.fragments.Home_Fragment;
import in.oriange.dailydiary.fragments.Notification_Fragment;
import in.oriange.dailydiary.fragments.Shopping_Fragment;
import in.oriange.dailydiary.fragments.Time_Fragment;
import in.oriange.dailydiary.utilities.UserSessionManager;

public class MainDrawer_Activity extends AppCompatActivity {

    private Context context;
    private UserSessionManager session;
    private SpaceNavigationView bottom_navigation;
    private FrameLayout fl_fragmentlayout;
    public static final String TAG = "bottom_sheet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        init();
        getSessionData();
        setBottomNavigation();
        setDefaults();
        setEventListner();

    }

    private void init() {
        context = MainDrawer_Activity.this;
        session = new UserSessionManager(context);
        bottom_navigation = findViewById(R.id.bottom_navigation);
        fl_fragmentlayout = findViewById(R.id.fl_fragmentlayout);

    }

    private void setBottomNavigation() {
        bottom_navigation.addSpaceItem(new SpaceItem("", R.drawable.icon_home));
        bottom_navigation.addSpaceItem(new SpaceItem("", R.drawable.icon_products));
        bottom_navigation.addSpaceItem(new SpaceItem("", R.drawable.icon_notification));
        bottom_navigation.addSpaceItem(new SpaceItem("", R.drawable.icon_time));
        bottom_navigation.shouldShowFullBadgeText(false);

        bottom_navigation.setCentreButtonIcon(R.drawable.icon_user);
        bottom_navigation.setCentreButtonIconColorFilterEnabled(false);
        bottom_navigation.showIconOnly();
    }

    private void getSessionData() {

    }


    private void setDefaults() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_fragmentlayout, new Home_Fragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    private void setEventListner() {
        bottom_navigation.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                new BottomSheetMenu_Fragment().show(getSupportFragmentManager(), TAG);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (itemIndex) {
                    case 0:
                        fragmentTransaction.replace(R.id.fl_fragmentlayout, new Home_Fragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;

                    case 1:
                        fragmentTransaction.replace(R.id.fl_fragmentlayout, new Shopping_Fragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;

                    case 2:
                        fragmentTransaction.replace(R.id.fl_fragmentlayout, new Notification_Fragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;

                    case 3:
                        fragmentTransaction.replace(R.id.fl_fragmentlayout, new Time_Fragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;

                }

            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                Log.d("onItemReselected ", "" + itemIndex + " " + itemName);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

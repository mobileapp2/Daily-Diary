package in.oriange.dailydiary.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.fragments.BottomSheetMenu_Fragment;

public class MainDrawer_Activity extends AppCompatActivity {

    private Context context;
    private SpaceNavigationView bottom_navigation;

    public static final String TAG = "bottom_sheet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        init();
        setBottomNavigation();
        setEventListner();
    }

    private void init() {
        bottom_navigation = findViewById(R.id.bottom_navigation);
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

    private void setEventListner() {
        bottom_navigation.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                new BottomSheetMenu_Fragment().show(getSupportFragmentManager(), TAG);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                Log.d("onItemClick ", "" + itemIndex + " " + itemName);
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                Log.d("onItemReselected ", "" + itemIndex + " " + itemName);
            }
        });


    }

}

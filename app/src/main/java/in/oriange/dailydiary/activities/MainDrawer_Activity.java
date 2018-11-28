package in.oriange.dailydiary.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import java.util.HashMap;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.fragments.BottomSheetMenu_Fragment;

public class MainDrawer_Activity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private Context context;
    private SpaceNavigationView bottom_navigation;

    public static final String TAG = "bottom_sheet";

    private SliderLayout mDemoSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        init();
        setBottomNavigation();
        setEventListner();
        setUpToolbar();

    }

    private void init() {
        bottom_navigation = findViewById(R.id.bottom_navigation);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        HashMap<String, String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

//        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
//        file_maps.put("Hannibal", R.drawable.hannibal);
//        file_maps.put("Big Bang Theory", R.drawable.hannibal);
//        file_maps.put("House of Cards", R.drawable.hannibal);
//        file_maps.put("Game of Thrones", R.drawable.hannibal);

        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
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

    private void setUpToolbar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.app_layout);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

package in.oriange.dailydiary.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.utilities.ApplicationConstants;
import in.oriange.dailydiary.utilities.Utilities;
import in.oriange.dailydiary.utilities.WebServiceCalls;

public class Home_Fragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private Context context;
    private SliderLayout mDemoSlider;
    private CoordinatorLayout main_content;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity();
        init(rootView);
        getSessionData();
        setDefault();
        setEventHandlers();
        return rootView;
    }

    private void init(View rootView) {
        mDemoSlider = rootView.findViewById(R.id.slider);
        main_content = getActivity().findViewById(R.id.main_content);

    }

    private void getSessionData() {

    }

    private void setDefault() {

        if (Utilities.isInternetAvailable(context)) {
            new GetBanners().execute();
        } else {
            Utilities.showSnackBar(main_content, "Please Check Internet Connection");
        }

    }

    private void setEventHandlers() {

    }

    private void setSlider(JSONArray bannerJsonArray) {

        for (int i = 0; i < bannerJsonArray.length(); i++) {
            try {
                JSONObject jsonObject = bannerJsonArray.getJSONObject(i);

                TextSliderView textSliderView = new TextSliderView(context);
                textSliderView
                        .description(jsonObject.getString("BannerText"))
                        .image(jsonObject.getString("BannerImagePath"))
//                        .image("http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg")
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);

                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putString("extra", jsonObject.getString("BannerText"));

                mDemoSlider.addSlider(textSliderView);
                mDemoSlider.setCurrentPosition(0, true);
                mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setDuration(5000);
                mDemoSlider.movePrevPosition();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
//        for (String name : url_maps.keySet()) {
//            TextSliderView textSliderView = new TextSliderView(context);
//            textSliderView
//                    .description(name)
//                    .image(url_maps.get(name))
//                    .setScaleType(BaseSliderView.ScaleType.Fit)
//                    .setOnSliderClickListener(this);
//
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle().putString("extra", name);
//
//            mDemoSlider.addSlider(textSliderView);
//
//            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
//            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
//            mDemoSlider.setDuration(5000);
//            mDemoSlider.addOnPageChangeListener(this);
//        }
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

    public class GetBanners extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "getBanners");
            res = WebServiceCalls.JSONAPICall(ApplicationConstants.banners, obj.toString());
            return res.trim();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    JSONObject mainObj = new JSONObject(result);
                    type = mainObj.getString("type");
                    message = mainObj.getString("message");
                    if (type.equalsIgnoreCase("success")) {
                        JSONArray jsonarr = mainObj.getJSONArray("data");
                        if (jsonarr.length() > 0) {
                            setSlider(jsonarr);

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
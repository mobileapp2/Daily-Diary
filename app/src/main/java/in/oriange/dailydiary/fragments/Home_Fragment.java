package in.oriange.dailydiary.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.activities.SetPinCode_Activity;
import in.oriange.dailydiary.adapters.GetTopProductsListAdapter;
import in.oriange.dailydiary.models.TopProductsModel;
import in.oriange.dailydiary.models.TopProductsPojo;
import in.oriange.dailydiary.utilities.ApplicationConstants;
import in.oriange.dailydiary.utilities.ConstantData;
import in.oriange.dailydiary.utilities.UserSessionManager;
import in.oriange.dailydiary.utilities.Utilities;
import in.oriange.dailydiary.utilities.WebServiceCalls;

public class Home_Fragment extends Fragment implements View.OnClickListener /*implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener*/ {

    private Context context;
    private UserSessionManager session;
    private SliderLayout mDemoSlider;
    private LinearLayout main_content;
    private EditText edt_location;
    private RecyclerView rv_topproducts;
    private ArrayList<TopProductsModel> topProductsList;
    private String state, city, locality, pincode;

    private ConstantData constantData;

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
        constantData = ConstantData.getInstance();
        session = new UserSessionManager(context);
        mDemoSlider = rootView.findViewById(R.id.slider);
        edt_location = rootView.findViewById(R.id.edt_location);
        main_content = getActivity().findViewById(R.id.main_content);
        rv_topproducts = rootView.findViewById(R.id.rv_topproducts);
        rv_topproducts.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        topProductsList = new ArrayList<>();
    }

    private void getSessionData() {

        try {
            JSONObject pincodeInfo = new JSONObject(session.getPincodeDetails().get(ApplicationConstants.KEY_PINCODE_INFO));
            state = pincodeInfo.getString("state");
            city = pincodeInfo.getString("city");
            locality = pincodeInfo.getString("locality");
            pincode = pincodeInfo.getString("pincode");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setDefault() {
        String placeStr = "";
        if (city.isEmpty()) {
            placeStr = locality + ", " + pincode;
        } else if (locality.isEmpty()) {
            placeStr = city + ", " + pincode;
        } else if (pincode.isEmpty()) {
            placeStr = city + ", " + locality;
        } else {
            placeStr = city + ", " + locality + ", " + pincode;
        }

        edt_location.setText(placeStr);


        if (Utilities.isInternetAvailable(context)) {
            new GetBanners().execute();
        } else {
            Utilities.showSnackBar(main_content, "Please Check Internet Connection");
        }

        if (constantData.getTopProductsList() == null) {
            if (Utilities.isInternetAvailable(context)) {
                new GetTopProducts().execute();
            } else {
                Utilities.showSnackBar(main_content, "Please Check Internet Connection");
            }
        } else {
            ArrayList<TopProductsModel> horiProductList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                horiProductList.add(constantData.getTopProductsList().get(i));
            }
            rv_topproducts.setAdapter(new GetTopProductsListAdapter(context, horiProductList));
        }
    }

    private void setEventHandlers() {
        edt_location.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edt_location:
                startActivity(new Intent(context, SetPinCode_Activity.class));
                break;
        }
    }


    private void setSlider(JSONArray bannerJsonArray) {

        try {
            for (int i = 0; i < bannerJsonArray.length(); i++) {
                JSONObject jsonObject = bannerJsonArray.getJSONObject(i);

                TextSliderView textSliderView = new TextSliderView(context);
                textSliderView
                        .description(jsonObject.getString("BannerText"))
                        .image(ApplicationConstants.BANNERIMAGE + "" + jsonObject.getString("BannerImagePath"))
//                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setScaleType(BaseSliderView.ScaleType.CenterInside);
//                        .setOnSliderClickListener(this);

                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putString("extra", jsonObject.getString("BannerText"));

                mDemoSlider.addSlider(textSliderView);
            }
            mDemoSlider.setCurrentPosition(0, true);
            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setDuration(5000);
            mDemoSlider.movePrevPosition();
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    public class GetTopProducts extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "GetItemsByPincode");
            obj.addProperty("pincode", "413512");
            res = WebServiceCalls.JSONAPICall(ApplicationConstants.getItem, obj.toString());
            return res.trim();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    JSONObject mainObj = new JSONObject(result);
                    topProductsList = new ArrayList<>();
                    TopProductsPojo empPojo = new Gson().fromJson(result, TopProductsPojo.class);
                    type = empPojo.getType();
                    message = empPojo.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        topProductsList = empPojo.getData();
                        ArrayList<TopProductsModel> horiProductList = new ArrayList<>();

                        if (topProductsList.size() > 0) {
                            constantData.setTopProductsList(topProductsList);
                            for (int i = 0; i < 3; i++) {
                                horiProductList.add(topProductsList.get(i));
                            }
                            rv_topproducts.setAdapter(new GetTopProductsListAdapter(context, horiProductList));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

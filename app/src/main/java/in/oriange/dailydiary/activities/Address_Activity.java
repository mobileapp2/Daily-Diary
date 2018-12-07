package in.oriange.dailydiary.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.models.AddressesModel;
import in.oriange.dailydiary.models.AddressesPojo;
import in.oriange.dailydiary.utilities.ApplicationConstants;
import in.oriange.dailydiary.utilities.UserSessionManager;
import in.oriange.dailydiary.utilities.Utilities;
import in.oriange.dailydiary.utilities.WebServiceCalls;

public class Address_Activity extends Activity {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;
    private ImageView imv_addnew;
    private LinearLayout ll_mainlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        init();
        setUpToolbar();
        getSessionData();
        setDefaults();
        setEventHandler();
    }

    private void init() {
        context = Address_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context, R.style.CustomDialogTheme);

        ll_mainlayout = findViewById(R.id.ll_mainlayout);
    }

    private void getSessionData() {

    }

    private void setDefaults() {
//        if (Utilities.isInternetAvailable(context)) {
//            new GetAddresses().execute();
//        } else {
//            Utilities.showSnackBar(ll_mainlayout, "Please Check Internet Connection");
//        }
    }

    private void setEventHandler() {

    }

//    public class GetAddresses extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String res = "[]";
//            JsonObject obj = new JsonObject();
//            obj.addProperty("type", "getAddress");
//            obj.addProperty("consumer_id", params[0]);
//            res = WebServiceCalls.JSONAPICall(ApplicationConstants.getAddress, obj.toString());
//            return res.trim();
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            String type = "", message = "";
//            try {
//                if (!result.equals("")) {
//                    JSONObject mainObj = new JSONObject(result);
//                    ArrayList<AddressesModel> addressList = new ArrayList<>();
//                    AddressesPojo pojoDetails = new Gson().fromJson(result, AddressesPojo.class);
//                    type = pojoDetails.getType();
//                    message = pojoDetails.getMessage();
//                    if (type.equalsIgnoreCase("success")) {
//                        addressList = pojoDetails.getData();
//
//                        if (addressList.size() > 0) {
//                            rv_address.setAdapter(new GetAddressListAdapter(context, addressList));
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private void setUpToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        imv_addnew = findViewById(R.id.imv_addnew);
        mToolbar.setTitle(Html.fromHtml("<font color='#00000'>Address</font>"));
        mToolbar.setNavigationIcon(R.drawable.icon_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

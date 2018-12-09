package in.oriange.dailydiary.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.adapters.GetAddressListAdapter;
import in.oriange.dailydiary.models.AddressesModel;
import in.oriange.dailydiary.models.AddressesPojo;
import in.oriange.dailydiary.utilities.ApplicationConstants;
import in.oriange.dailydiary.utilities.RecyclerItemClickListener;
import in.oriange.dailydiary.utilities.UserSessionManager;
import in.oriange.dailydiary.utilities.Utilities;
import in.oriange.dailydiary.utilities.WebServiceCalls;

public class SelectAddressForPackage_Activity extends Activity {

    private Context context;
    private UserSessionManager session;
    private LinearLayout ll_mainlayout;
    private LinearLayout ll_nothingtoshow;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv_address;

    private String ConsumerID;
    private List<Date> selectedDates;
    private static ArrayList<AddressesModel> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectaddress_forpackage);

        init();
        getSessionData();
        setDefault();
        setEventHandler();
        setUpToolBar();
    }

    private void init() {
        context = SelectAddressForPackage_Activity.this;
        session = new UserSessionManager(context);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        ll_mainlayout = findViewById(R.id.ll_mainlayout);
        ll_nothingtoshow = findViewById(R.id.ll_nothingtoshow);
        rv_address = findViewById(R.id.rv_address);
        rv_address.setLayoutManager(new LinearLayoutManager(context));

        selectedDates = new ArrayList<>();
    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            JSONObject json = user_info.getJSONObject(0);
            ConsumerID = json.getString("ConsumerID");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setDefault() {

        selectedDates = (List<Date>) getIntent().getSerializableExtra("selectedDates");


        if (Utilities.isInternetAvailable(context)) {
            new GetAddresses().execute(ConsumerID);
            swipeRefreshLayout.setRefreshing(true);
        } else {
            Utilities.showSnackBar(ll_mainlayout, "Please Check Internet Connection");
            swipeRefreshLayout.setRefreshing(false);
            ll_nothingtoshow.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    private void setEventHandler() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utilities.isInternetAvailable(context)) {
                    new GetAddresses().execute(ConsumerID);
                    swipeRefreshLayout.setRefreshing(true);
                } else {
                    Utilities.showSnackBar(ll_mainlayout, "Please Check Internet Connection");
                    swipeRefreshLayout.setRefreshing(false);
                    ll_nothingtoshow.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                }
            }
        });


        rv_address.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, ProceedToCheckout_Activity.class);
                intent.putExtra("selectedDates", (Serializable) selectedDates);
                intent.putExtra("selectedAddress", addressList.get(position));
                startActivity(intent);
            }
        }));
    }

    public class GetAddresses extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "getAddress");
            obj.addProperty("consumer_id", params[0]);
            res = WebServiceCalls.JSONAPICall(ApplicationConstants.getAddress, obj.toString());
            return res.trim();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            swipeRefreshLayout.setRefreshing(false);
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    JSONObject mainObj = new JSONObject(result);
                    addressList = new ArrayList<>();
                    AddressesPojo pojoDetails = new Gson().fromJson(result, AddressesPojo.class);
                    type = pojoDetails.getType();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        addressList = pojoDetails.getData();

                        if (addressList.size() > 0) {
                            rv_address.setAdapter(new GetAddressListAdapter(context, addressList));
                            ll_nothingtoshow.setVisibility(View.GONE);
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                        } else {
                            ll_nothingtoshow.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setVisibility(View.GONE);
                        }
                    } else {
                        ll_nothingtoshow.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                ll_nothingtoshow.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
            }
        }
    }

    private void setUpToolBar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(Html.fromHtml("<font color='#00000'>Select Address</font>"));
        mToolbar.setNavigationIcon(R.drawable.icon_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

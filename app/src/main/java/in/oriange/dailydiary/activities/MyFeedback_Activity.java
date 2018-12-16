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

import java.util.ArrayList;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.adapters.GetFeedbackListAdapter;
import in.oriange.dailydiary.adapters.GetPackageListAdapter;
import in.oriange.dailydiary.models.PackagesModel;
import in.oriange.dailydiary.models.PackagesPojo;
import in.oriange.dailydiary.utilities.ApplicationConstants;
import in.oriange.dailydiary.utilities.RecyclerItemClickListener;
import in.oriange.dailydiary.utilities.UserSessionManager;
import in.oriange.dailydiary.utilities.Utilities;
import in.oriange.dailydiary.utilities.WebServiceCalls;

public class MyFeedback_Activity extends Activity {

    private static Context context;
    private UserSessionManager session;
    private LinearLayout ll_mainlayout;
    private static LinearLayout ll_nothingtoshow;
    private static SwipeRefreshLayout swipeRefreshLayout;
    private static RecyclerView rv_package;

    private String ConsumerID;
    private static ArrayList<PackagesModel> packageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_feedback);

        init();
        getSessionData();
        setDefaults();
        setEventHandler();
        setUpToolbar();
    }

    private void init() {
        context = MyFeedback_Activity.this;
        session = new UserSessionManager(context);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        ll_mainlayout = findViewById(R.id.ll_mainlayout);
        ll_nothingtoshow = findViewById(R.id.ll_nothingtoshow);
        rv_package = findViewById(R.id.rv_package);
        rv_package.setLayoutManager(new LinearLayoutManager(context));

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

    private void setDefaults() {
        if (Utilities.isInternetAvailable(context)) {
            new GetFeedbacks().execute(ConsumerID);
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
                    new GetFeedbacks().execute(ConsumerID);
                    swipeRefreshLayout.setRefreshing(true);
                } else {
                    Utilities.showSnackBar(ll_mainlayout, "Please Check Internet Connection");
                    swipeRefreshLayout.setRefreshing(false);
                    ll_nothingtoshow.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                }
            }
        });

        rv_package.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(context, ViewFeedbackDetails_Activity.class).putExtra("packageDetails", packageList.get(position)));
            }
        }));

    }


    public static class GetFeedbacks extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "getPackageDetails");
            obj.addProperty("consumer_id", params[0]);
            res = WebServiceCalls.JSONAPICall(ApplicationConstants.Package, obj.toString());
            return res.trim();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            swipeRefreshLayout.setRefreshing(false);
            String type = "", message = "";
            try {
                if (!result.equals("")) {
                    packageList = new ArrayList<>();
                    PackagesPojo pojoDetails = new Gson().fromJson(result, PackagesPojo.class);
                    type = pojoDetails.getType();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        packageList = pojoDetails.getData();

                        if (packageList.size() > 0) {
                            rv_package.setAdapter(new GetFeedbackListAdapter(context, packageList));
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

    private void setUpToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(Html.fromHtml("<font color='#00000'>My Feedbacks</font>"));
        mToolbar.setNavigationIcon(R.drawable.icon_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

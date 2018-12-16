package in.oriange.dailydiary.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.models.PackagesModel;
import in.oriange.dailydiary.utilities.ApplicationConstants;
import in.oriange.dailydiary.utilities.UserSessionManager;
import in.oriange.dailydiary.utilities.Utilities;
import in.oriange.dailydiary.utilities.WebServiceCalls;

public class ViewFeedbackDetails_Activity extends Activity implements View.OnClickListener {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;
    private LinearLayout ll_mainlayout;
    private RecyclerView rv_itemlist;
    private TextView tv_feedback, tv_nofeedback, tv_addressdetails, tv_addressandfullname;

    private RatingBar rb_feedbackstars;
    private ImageView imv_edit;

    private String ConsumerID;
    private PackagesModel packageDetails;
    private ArrayList<PackagesModel.Items> packageItemsList;
    private ArrayList<PackagesModel.Feedback> feedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedbackdetails);

        init();
        setUpToolbar();
        getSessionData();
        setDefaults();
        setEventHandler();
    }

    private void init() {
        context = ViewFeedbackDetails_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context, R.style.CustomDialogTheme);

        ll_mainlayout = findViewById(R.id.ll_mainlayout);
        tv_addressandfullname = findViewById(R.id.tv_addressandfullname);
        tv_addressdetails = findViewById(R.id.tv_addressdetails);
        tv_feedback = findViewById(R.id.tv_feedback);
        tv_nofeedback = findViewById(R.id.tv_nofeedback);
        rb_feedbackstars = findViewById(R.id.rb_feedbackstars);

        rv_itemlist = findViewById(R.id.rv_itemlist);
        rv_itemlist.setLayoutManager(new LinearLayoutManager(context));

        packageItemsList = new ArrayList<>();
        feedback = new ArrayList<>();
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

    @SuppressLint("SetTextI18n")
    private void setDefaults() {
        packageDetails = (PackagesModel) getIntent().getSerializableExtra("packageDetails");
        packageItemsList = packageDetails.getItems();
        feedback = packageDetails.getFeedback();

        tv_addressandfullname.setText(packageDetails.getFull_name() + ", " + packageDetails.getAddress_name());

        tv_addressdetails.setText(packageDetails.getAddressline_one() + ", " + packageDetails.getAddressline_two() + ", " +
                packageDetails.getState_name() + ", " + packageDetails.getCity_name() + ", " + packageDetails.getPincode());

        if (feedback != null) {
            tv_feedback.setVisibility(View.VISIBLE);
            rb_feedbackstars.setVisibility(View.VISIBLE);
            tv_nofeedback.setVisibility(View.GONE);

            tv_feedback.setText(feedback.get(0).getFeedback());
            rb_feedbackstars.setRating(Float.parseFloat(feedback.get(0).getFeedbackID()));

        } else {
            tv_feedback.setVisibility(View.GONE);
            rb_feedbackstars.setVisibility(View.GONE);
            tv_nofeedback.setVisibility(View.VISIBLE);
        }

        rv_itemlist.setAdapter(new SelectedItemsAdapter());
    }

    private void setEventHandler() {
        imv_edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_edit:

                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.dialog_feedback, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                alertDialogBuilder.setTitle("Feedback");
                alertDialogBuilder.setView(promptView);

                final RatingBar rb_feedbackstars = promptView.findViewById(R.id.rb_feedbackstars);
                final MaterialEditText edt_feedback = promptView.findViewById(R.id.edt_feedback);


                if (feedback != null) {
                    edt_feedback.setText(feedback.get(0).getFeedback());
                    rb_feedbackstars.setRating(Float.parseFloat(feedback.get(0).getFeedbackID()));
                }

                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Utilities.isInternetAvailable(context)) {

                            if (feedback == null) {
                                new AddFeedback().execute("AddFeedback",
                                        ConsumerID,
                                        packageDetails.getPackageID(),
                                        edt_feedback.getText().toString().trim(),
                                        String.valueOf(rb_feedbackstars.getRating()));
                            } else {
                                new AddFeedback().execute("UpdateFeedback",
                                        ConsumerID,
                                        packageDetails.getPackageID(),
                                        edt_feedback.getText().toString().trim(),
                                        String.valueOf(rb_feedbackstars.getRating()));
                            }


                        } else {
                            Utilities.showSnackBar(ll_mainlayout, "Please Check Internet Connection");
                        }
                    }
                });

                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                final AlertDialog alertD = alertDialogBuilder.create();
                alertD.show();

                if (edt_feedback.getText().toString().isEmpty()) {
                    alertD.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }

                edt_feedback.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (TextUtils.isEmpty(s)) {
                            alertD.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        } else {
                            alertD.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }

                    }
                });


                break;
        }
    }

    public class SelectedItemsAdapter extends RecyclerView.Adapter<SelectedItemsAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_mypackageitems, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int pos) {
            int position = holder.getAdapterPosition();

            final PackagesModel.Items packageItem = packageItemsList.get(position);
            final int[] totalCount = {Integer.parseInt(packageItem.getQuantity())};
            final int[] unitPrice = {Integer.parseInt(packageItem.getUnitPrice())};

            holder.tv_productname.setText(packageItem.getItem_Name());
            holder.tv_productprice.setText("₹ " + packageItem.getUnitPrice());
            holder.tv_totalcount.setText("Quantity - " + packageItem.getQuantity());
            holder.tv_unit.setText("Unit - " + packageItem.getUoM_Name());
            holder.tv_totalrate.setText("Total Amount - ₹ " + totalCount[0] * unitPrice[0]);

            Picasso.with(context)
                    .load(ApplicationConstants.PRODUCTIMAGE + "" + packageItem.getItem_image())
                    .into(holder.imv_productimage, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.tv_nopreview.setVisibility(View.GONE);
                            holder.imv_productimage.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {
                            holder.tv_nopreview.setVisibility(View.VISIBLE);
                            holder.imv_productimage.setVisibility(View.GONE);
                        }
                    });

        }

        @Override
        public int getItemCount() {
            return packageItemsList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private ImageView imv_productimage;
            private TextView tv_nopreview, tv_productname, tv_productprice, tv_totalcount, tv_unit, tv_totalrate;

            private MyViewHolder(View view) {
                super(view);
                imv_productimage = view.findViewById(R.id.imv_productimage);
                tv_nopreview = view.findViewById(R.id.tv_nopreview);
                tv_productname = view.findViewById(R.id.tv_productname);
                tv_productprice = view.findViewById(R.id.tv_productprice);
                tv_totalcount = view.findViewById(R.id.tv_totalcount);
                tv_unit = view.findViewById(R.id.tv_unit);
                tv_totalrate = view.findViewById(R.id.tv_totalrate);

            }
        }
    }

    public class AddFeedback extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";
            JsonObject obj = new JsonObject();
            obj.addProperty("type", params[0]);
            obj.addProperty("consumer_id", params[1]);
            obj.addProperty("package_id", params[2]);
            obj.addProperty("feedback_message", params[3]);
            obj.addProperty("rating", params[4]);
            res = WebServiceCalls.JSONAPICall(ApplicationConstants.Feedback, obj.toString());
            return res.trim();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String type = "", message = "";
            try {
                pd.dismiss();
                if (!result.equals("")) {
                    JSONObject mainObj = new JSONObject(result);
                    type = mainObj.getString("type");
                    message = mainObj.getString("message");
                    if (type.equalsIgnoreCase("success")) {

                        new MyFeedback_Activity.GetFeedbacks().execute(ConsumerID);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                        builder.setMessage("Feedback Submitted Successfully");
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();

                            }
                        });
                        AlertDialog alertD = builder.create();
                        alertD.show();
                    } else {

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
            }
        }
    }


    private void setUpToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        imv_edit = findViewById(R.id.imv_edit);
        mToolbar.setTitle(Html.fromHtml("<font color='#00000'>Feedback Details</font>"));
        mToolbar.setNavigationIcon(R.drawable.icon_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}

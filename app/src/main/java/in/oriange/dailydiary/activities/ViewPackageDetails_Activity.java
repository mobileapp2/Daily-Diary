package in.oriange.dailydiary.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
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

public class ViewPackageDetails_Activity extends Activity implements View.OnClickListener {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;
    private LinearLayout ll_mainlayout;
    private RecyclerView rv_itemlist;
    private TextView tv_addressdetails, tv_addressandfullname, tv_status;
    private ImageView imv_delete;
    private CardView cv_payment;
    private Button btn_next, btn_delivarydates;
    private String ConsumerID;
    private PackagesModel packageDetails;
    private ArrayList<PackagesModel.Items> packageItemsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_packagedetails);

        init();
        setUpToolbar();
        getSessionData();
        setDefaults();
        setEventHandler();
    }

    private void init() {
        context = ViewPackageDetails_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context, R.style.CustomDialogTheme);

        ll_mainlayout = findViewById(R.id.ll_mainlayout);
        cv_payment = findViewById(R.id.cv_payment);
        tv_addressandfullname = findViewById(R.id.tv_addressandfullname);
        tv_addressdetails = findViewById(R.id.tv_addressdetails);
        tv_status = findViewById(R.id.tv_status);

        btn_next = findViewById(R.id.btn_next);
        btn_delivarydates = findViewById(R.id.btn_delivarydates);

        rv_itemlist = findViewById(R.id.rv_itemlist);
        rv_itemlist.setLayoutManager(new LinearLayoutManager(context));

        packageItemsList = new ArrayList<>();


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

        String statusId = packageDetails.getStatus_Id();

        if (statusId.equals("2") || statusId.equals("3")) {
            cv_payment.setVisibility(View.VISIBLE);
        } else {
            cv_payment.setVisibility(View.GONE);
        }


        tv_addressandfullname.setText(packageDetails.getFull_name() + ", " + packageDetails.getAddress_name());

        tv_addressdetails.setText(packageDetails.getAddressline_one() + ", " + packageDetails.getAddressline_two() + ", " +
                packageDetails.getState_name() + ", " + packageDetails.getCity_name() + ", " + packageDetails.getPincode());

        tv_status.setText(packageDetails.getStatus_name());

        packageItemsList = packageDetails.getItems();

        rv_itemlist.setAdapter(new SelectedItemsAdapter());

    }

    private void setEventHandler() {
        btn_next.setOnClickListener(this);
        btn_delivarydates.setOnClickListener(this);
        imv_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:

                int itemsAmount = 0, totalFinalAmount = 0;
                for (int i = 0; i < packageItemsList.size(); i++) {
                    itemsAmount = itemsAmount + Integer.parseInt(packageItemsList.get(i).getUnitPrice())
                            * Integer.parseInt(packageItemsList.get(i).getQuantity());
                }

                totalFinalAmount = itemsAmount * packageDetails.getDeliveryDates().size();

                Intent intent = new Intent(context, PaymentGateway_Activity.class);
                intent.putExtra("packageId", packageDetails.getPackageID());
                intent.putExtra("totalFinalAmount", totalFinalAmount);
                intent.putExtra("packageName", packageDetails.getPackageName());
                startActivity(intent);

                finish();

                break;
            case R.id.btn_delivarydates:
                startActivity(new Intent(context, ViewDelivaryDates_Activity.class)
                        .putExtra("delivaryDates", packageDetails.getDeliveryDates()));
                break;
            case R.id.imv_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                builder.setMessage("Are you sure you want to delete this package?");
                builder.setIcon(R.drawable.icon_alertred);
                builder.setTitle("Alert");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (Utilities.isInternetAvailable(context)) {
                            new DeletePackage().execute(packageDetails.getPackageID());
                        } else {
                            Utilities.showSnackBar(ll_mainlayout, "Please Check Internet Connection");
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertD = builder.create();
                alertD.show();
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

    public class DeletePackage extends AsyncTask<String, Void, String> {

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
            obj.addProperty("type", "deletePackage");
            obj.addProperty("package_id", params[0]);
            res = WebServiceCalls.JSONAPICall(ApplicationConstants.Package, obj.toString());
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

                        new MyPackage_Activity.GetPackages().execute(ConsumerID);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                        builder.setMessage("Package Deleted Successfully");
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
        imv_delete = findViewById(R.id.imv_delete);
        mToolbar.setTitle(Html.fromHtml("<font color='#00000'>Package Details</font>"));
        mToolbar.setNavigationIcon(R.drawable.icon_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.models.AddressesModel;
import in.oriange.dailydiary.models.PackageItemsModel;
import in.oriange.dailydiary.utilities.ApplicationConstants;
import in.oriange.dailydiary.utilities.ConstantData;
import in.oriange.dailydiary.utilities.UserSessionManager;
import in.oriange.dailydiary.utilities.Utilities;
import in.oriange.dailydiary.utilities.WebServiceCalls;

public class CreatePackage_Activity extends Activity {


    private Context context;
    private UserSessionManager session;
    private ConstantData constantData;
    private LinearLayout ll_mainlayout;
    private RecyclerView rv_itemlist;
    private TextView tv_addressdetails, tv_emailandmobileno, tv_noofdays, tv_totalamount;
    private Button btn_next;

    private String ConsumerID;
    private List<Date> selectedDates;
    private AddressesModel selectedAddress;
    private ArrayList<PackageItemsModel> selectedItemsList;
    private int totalFinalAmount;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_package);

        init();
        getSessionData();
        setDefault();
        setEventHandler();
        setUpToolBar();
    }

    private void init() {
        context = CreatePackage_Activity.this;
        session = new UserSessionManager(context);
        constantData = ConstantData.getInstance();

        rv_itemlist = findViewById(R.id.rv_itemlist);
        rv_itemlist.setLayoutManager(new LinearLayoutManager(context));

        ll_mainlayout = findViewById(R.id.ll_mainlayout);
        tv_addressdetails = findViewById(R.id.tv_addressdetails);
        tv_emailandmobileno = findViewById(R.id.tv_emailandmobileno);
        tv_totalamount = findViewById(R.id.tv_totalamount);
        tv_noofdays = findViewById(R.id.tv_noofdays);

        btn_next = findViewById(R.id.btn_next);
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
    private void setDefault() {
        selectedDates = (List<Date>) getIntent().getSerializableExtra("selectedDates");
        selectedAddress = (AddressesModel) getIntent().getSerializableExtra("selectedAddress");
        selectedItemsList = constantData.getPackageItemsList();

        for (PackageItemsModel packageItemsModel : selectedItemsList) {
            String rate = packageItemsModel.getTotalProductrate().substring(2);
            totalFinalAmount = totalFinalAmount + Integer.parseInt(rate);
        }

        tv_noofdays.setText("For " + selectedDates.size() + " Days");

        totalFinalAmount = totalFinalAmount * selectedDates.size();
        tv_totalamount.setText("Total Amount ₹ " + totalFinalAmount);

        tv_addressdetails.setText(selectedAddress.getAddressline_one() + ", " + selectedAddress.getAddressline_two() + ", " +
                selectedAddress.getState_name() + ", " + selectedAddress.getCity_name() + ", " + selectedAddress.getPincode());

        tv_emailandmobileno.setText(selectedAddress.getMobile_number() + ", " + selectedAddress.getEmail());

        rv_itemlist.setAdapter(new SelectedItemsAdapter());

    }

    private void setEventHandler() {
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText edt_packagename = new EditText(context);
                float dpi = context.getResources().getDisplayMetrics().density;
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                alertDialogBuilder.setTitle("Package Name");

                alertDialogBuilder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createPackageJson(edt_packagename.getText().toString().trim());
                    }
                });

                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                final AlertDialog alertD = alertDialogBuilder.create();
                alertD.setView(edt_packagename, (int) (19 * dpi), (int) (5 * dpi), (int) (14 * dpi), (int) (5 * dpi));
                alertD.show();
                alertD.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                edt_packagename.addTextChangedListener(new TextWatcher() {
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

            }
        });
    }

    private void createPackageJson(String packageName) {

        JsonArray itemJsonArray = new JsonArray();
        for (int i = 0; i < selectedItemsList.size(); i++) {
            JsonObject familyJSONObj = new JsonObject();
            familyJSONObj.addProperty("item_id", selectedItemsList.get(i).getItemID());
            familyJSONObj.addProperty("quantity", selectedItemsList.get(i).getTotalProductCount());
            itemJsonArray.add(familyJSONObj);
        }

        JsonArray datesJsonArray = new JsonArray();
        for (int i = 0; i < selectedDates.size(); i++) {
            datesJsonArray.add(new JsonPrimitive(dateFormat.format(selectedDates.get(i).getTime())));
        }


        JsonObject mainObj = new JsonObject();

        mainObj.addProperty("type", "addPackage");
        mainObj.addProperty("package_name", packageName);
        mainObj.addProperty("address_id", selectedAddress.getAddress_id());
        mainObj.addProperty("start_date", dateFormat.format(selectedDates.get(0).getTime()));
        mainObj.addProperty("consumer_id", ConsumerID);
        mainObj.add("items", itemJsonArray);
        mainObj.add("delivery_date", datesJsonArray);

        Log.i("CREATEPACKAGEJSON", mainObj.toString());

        if (Utilities.isInternetAvailable(context)) {
            new CreatePackage().execute(mainObj.toString());
        } else {
            Utilities.showSnackBar(ll_mainlayout, "Please Check Internet Connection");
        }

    }

    public class SelectedItemsAdapter extends RecyclerView.Adapter<SelectedItemsAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_packageitems, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int pos) {
            int position = holder.getAdapterPosition();

            holder.tv_srno.setText("" + (position + 1));
            holder.tv_itemname.setText(selectedItemsList.get(position).getItem_Name());
            holder.tv_totalrate.setText(selectedItemsList.get(position).getTotalProductrate());


        }

        @Override
        public int getItemCount() {
            return selectedItemsList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_srno, tv_itemname, tv_totalrate;

            private MyViewHolder(View view) {
                super(view);
                tv_srno = view.findViewById(R.id.tv_srno);
                tv_itemname = view.findViewById(R.id.tv_itemname);
                tv_totalrate = view.findViewById(R.id.tv_totalrate);

            }
        }
    }

    public class CreatePackage extends AsyncTask<String, Void, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context, R.style.CustomDialogTheme);
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "[]";
            res = WebServiceCalls.JSONAPICall(ApplicationConstants.Package, params[0]);
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                        builder.setMessage("Order Placed Successfully");
                        builder.setIcon(R.drawable.icon_success);
                        builder.setTitle("Success");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                constantData.setPackageItemsList(new ArrayList<PackageItemsModel>());
                                finishAffinity();
                                startActivity(new Intent(context, MainDrawer_Activity.class));
                            }
                        });
                        AlertDialog alertD = builder.create();
                        alertD.show();

                    } else {

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void setUpToolBar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(Html.fromHtml("<font color='#00000'>My Package</font>"));
        mToolbar.setNavigationIcon(R.drawable.icon_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

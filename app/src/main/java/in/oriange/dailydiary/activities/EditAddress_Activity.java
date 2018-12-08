package in.oriange.dailydiary.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.models.AddressesModel;
import in.oriange.dailydiary.utilities.ApplicationConstants;
import in.oriange.dailydiary.utilities.UserSessionManager;
import in.oriange.dailydiary.utilities.Utilities;
import in.oriange.dailydiary.utilities.WebServiceCalls;

public class EditAddress_Activity extends Activity implements View.OnClickListener {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;
    private LinearLayout ll_mainlayout;
    private ImageView imv_save, imv_delete;
    private MaterialEditText edt_fullname, edt_addressname, edt_addressone, edt_addresstwo, edt_country,
            edt_state, edt_city, edt_pincode, edt_moblieno, edt_email;

    private AddressesModel addressesModel;
    private String ConsumerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);

        init();
        setUpToolbar();
        getSessionData();
        setDefaults();
        setEventHandler();
    }

    private void init() {
        context = EditAddress_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context, R.style.CustomDialogTheme);

        ll_mainlayout = findViewById(R.id.ll_mainlayout);

        edt_fullname = findViewById(R.id.edt_fullname);
        edt_addressname = findViewById(R.id.edt_addressname);
        edt_addressone = findViewById(R.id.edt_addressone);
        edt_addresstwo = findViewById(R.id.edt_addresstwo);
        edt_country = findViewById(R.id.edt_country);
        edt_state = findViewById(R.id.edt_state);
        edt_city = findViewById(R.id.edt_city);
        edt_pincode = findViewById(R.id.edt_pincode);
        edt_moblieno = findViewById(R.id.edt_moblieno);
        edt_email = findViewById(R.id.edt_email);
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
        addressesModel = (AddressesModel) getIntent().getSerializableExtra("addressDetails");

        edt_fullname.setText(addressesModel.getFull_name());
        edt_addressname.setText(addressesModel.getAddress_name());
        edt_addressone.setText(addressesModel.getAddressline_one());
        edt_addresstwo.setText(addressesModel.getAddressline_two());
        edt_country.setText(addressesModel.getCountry_name());
        edt_state.setText(addressesModel.getState_name());
        edt_city.setText(addressesModel.getCity_name());
        edt_pincode.setText(addressesModel.getPincode());
        edt_moblieno.setText(addressesModel.getMobile_number());
        edt_email.setText(addressesModel.getEmail());
    }

    private void setEventHandler() {
        imv_save.setOnClickListener(this);
        imv_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_save:

                if (edt_fullname.getText().toString().trim().isEmpty()) {
                    edt_fullname.setError("Please enter full name");
                    return;
                }
                if (edt_addressname.getText().toString().trim().isEmpty()) {
                    edt_addressname.setError("Please enter address name");
                    return;
                }
                if (edt_addressone.getText().toString().trim().isEmpty()) {
                    edt_addressone.setError("Please enter address one");
                    return;
                }
                if (edt_country.getText().toString().trim().isEmpty()) {
                    edt_country.setError("Please enter country");
                    return;
                }
                if (edt_state.getText().toString().trim().isEmpty()) {
                    edt_state.setError("Please enter state");
                    return;
                }
                if (edt_city.getText().toString().trim().isEmpty()) {
                    edt_city.setError("Please enter city");
                    return;
                }
                if (edt_pincode.getText().toString().trim().isEmpty()) {
                    edt_pincode.setError("Please enter pincode");
                    return;
                }
                if (!Utilities.isMobileNo(edt_moblieno)) {
                    edt_moblieno.setError("Please enter mobile no.");
                    return;
                }
                if (!Utilities.isEmailValid(edt_email)) {
                    edt_email.setError("Please enter email");
                    return;
                }

                if (Utilities.isInternetAvailable(context)) {
                    new AddAddress().execute(
                            edt_addressname.getText().toString().trim(),
                            edt_fullname.getText().toString().trim(),
                            edt_moblieno.getText().toString().trim(),
                            edt_email.getText().toString().trim(),
                            edt_addressone.getText().toString().trim(),
                            edt_addresstwo.getText().toString().trim(),
                            edt_pincode.getText().toString().trim(),
                            edt_city.getText().toString().trim(),
                            edt_state.getText().toString().trim(),
                            edt_country.getText().toString().trim(),
                            addressesModel.getAddress_id());
                } else {
                    Utilities.showSnackBar(ll_mainlayout, "Please Check Internet Connection");
                }
                break;


            case R.id.imv_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                builder.setMessage("Are you sure you want to delete this record?");
                builder.setIcon(R.drawable.icon_alertred);
                builder.setTitle("Alert");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (Utilities.isInternetAvailable(context)) {
                            new DeleteAddress().execute(addressesModel.getAddress_id());
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

    public class AddAddress extends AsyncTask<String, Void, String> {

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
            obj.addProperty("type", "UpdateAddress");
            obj.addProperty("address_name", params[0]);
            obj.addProperty("full_name", params[1]);
            obj.addProperty("mobile_number", params[2]);
            obj.addProperty("email", params[3]);
            obj.addProperty("addressline_one", params[4]);
            obj.addProperty("addressline_two", params[5]);
            obj.addProperty("pincode", params[6]);
            obj.addProperty("city_name", params[7]);
            obj.addProperty("state_name", params[8]);
            obj.addProperty("country_name", params[9]);
            obj.addProperty("address_id", params[10]);
            res = WebServiceCalls.JSONAPICall(ApplicationConstants.ConsumerCrud, obj.toString());
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

                        new Address_Activity.GetAddresses().execute(ConsumerID);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                        builder.setMessage("Address Details Updated Successfully");
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

    public class DeleteAddress extends AsyncTask<String, Void, String> {

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
            obj.addProperty("type", "DeleteAddress");
            obj.addProperty("address_id", params[0]);
            res = WebServiceCalls.JSONAPICall(ApplicationConstants.ConsumerCrud, obj.toString());
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

                        new Address_Activity.GetAddresses().execute(ConsumerID);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                        builder.setMessage("Address Details Deleted Successfully");
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
        imv_save = findViewById(R.id.imv_save);
        imv_delete = findViewById(R.id.imv_delete);
        mToolbar.setTitle(Html.fromHtml("<font color='#00000'>Edit Address</font>"));
        mToolbar.setNavigationIcon(R.drawable.icon_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

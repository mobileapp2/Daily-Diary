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
import in.oriange.dailydiary.utilities.ApplicationConstants;
import in.oriange.dailydiary.utilities.UserSessionManager;
import in.oriange.dailydiary.utilities.Utilities;
import in.oriange.dailydiary.utilities.WebServiceCalls;

public class AddAddress_Activity extends Activity implements View.OnClickListener {

    private Context context;
    private UserSessionManager session;
    private ProgressDialog pd;
    private LinearLayout ll_mainlayout;
    private ImageView imv_save;
    private MaterialEditText edt_fullname, edt_addressname, edt_addressone, edt_addresstwo, edt_country,
            edt_state, edt_city, edt_pincode, edt_moblieno, edt_email;

    private String ConsumerID, Mobile_Number, Email_ID, TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        init();
        setUpToolbar();
        getSessionData();
        setDefaults();
        setEventHandler();
    }

    private void init() {
        context = AddAddress_Activity.this;
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
            Mobile_Number = json.getString("Mobile_Number");
            Email_ID = json.getString("Email_ID");

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            JSONObject pincodeInfo = new JSONObject(session.getPincodeDetails().get(ApplicationConstants.KEY_PINCODE_INFO));
            edt_state.setText(pincodeInfo.getString("state"));
            edt_city.setText(pincodeInfo.getString("city"));
            edt_pincode.setText(pincodeInfo.getString("pincode"));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setDefaults() {

        TYPE = getIntent().getStringExtra("TYPE");

        edt_moblieno.setText(Mobile_Number);
        edt_email.setText(Email_ID);
    }

    private void setEventHandler() {
        imv_save.setOnClickListener(this);
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
                            ConsumerID);
                } else {
                    Utilities.showSnackBar(ll_mainlayout, "Please Check Internet Connection");
                }
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
            obj.addProperty("type", "AddAddress");
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
            obj.addProperty("consumer_id", params[10]);
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                        builder.setMessage("Address Details Saved Successfully");
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


                        if (TYPE.equals("1")) {
                            new MyAddress_Activity.GetAddresses().execute(ConsumerID);
                        } else if (TYPE.equals("2")) {
                            new SelectAddressForPackage_Activity.GetAddresses().execute(ConsumerID);
                        }

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
        mToolbar.setTitle(Html.fromHtml("<font color='#00000'>Add Address</font>"));
        mToolbar.setNavigationIcon(R.drawable.icon_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}

package in.oriange.dailydiary.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.utilities.ApplicationConstants;
import in.oriange.dailydiary.utilities.UserSessionManager;
import in.oriange.dailydiary.utilities.Utilities;
import in.oriange.dailydiary.utilities.WebServiceCalls;
import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;

public class PaymentGateway_Activity extends Activity {

    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;
    private String ConsumerID, First_Name, Middle_Name, Last_Name, Mobile_Number, Email_ID, packageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);

        init();
        getSessionData();
        setDefault();


    }

    private void init() {
        context = PaymentGateway_Activity.this;
        session = new UserSessionManager(context);
        pd = new ProgressDialog(context);

    }

    private void getSessionData() {
        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            JSONObject json = user_info.getJSONObject(0);
            ConsumerID = json.getString("ConsumerID");
            First_Name = json.getString("First_Name");
            Middle_Name = json.getString("Middle_Name");
            Last_Name = json.getString("Last_Name");
            Mobile_Number = json.getString("Mobile_Number");
            Email_ID = json.getString("Email_ID");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefault() {

        int totalFinalAmount = getIntent().getIntExtra("totalFinalAmount", 0);
        String packageName = getIntent().getStringExtra("packageName");
        packageId = getIntent().getStringExtra("packageId");


        callInstamojoPay(
                Email_ID,
                Mobile_Number,
                String.valueOf(totalFinalAmount),
                packageName,
                First_Name + Middle_Name + Last_Name);

    }


    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        final Activity activity = this;
        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
            pay.put("send_sms", true);
            pay.put("send_email", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);
    }

    InstapayListener listener;

    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {
//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                if (Utilities.isInternetAvailable(context)) {
                    new UpdatePackageStatus().execute(packageId, "1");
                } else {
                    Utilities.showMessageString(context, "Please Check Internet Connection");
                }

            }

            @Override
            public void onFailure(int code, String reason) {
//                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG).show();
//
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                builder.setMessage(reason);
                builder.setIcon(R.drawable.icon_alertred);
                builder.setTitle("Fail");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();

                    }
                });
                AlertDialog alertD = builder.create();
                alertD.show();
            }
        };
    }

    public class UpdatePackageStatus extends AsyncTask<String, Void, String> {

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
            obj.addProperty("type", "updatePackageStatus");
            obj.addProperty("package_id", params[0]);
            obj.addProperty("status_id", params[1]);
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                        builder.setMessage("Payment Done Successfully");
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

                        new MyPackage_Activity.GetPackages().execute(ConsumerID);
                    } else {

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                Utilities.showAlertDialog(context, "Please Try Again", "Server not responding", false);
            }
        }
    }


}

package in.oriange.dailydiary.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.utilities.ApplicationConstants;
import in.oriange.dailydiary.utilities.UserSessionManager;
import in.oriange.dailydiary.utilities.Utilities;
import in.oriange.dailydiary.utilities.WebServiceCalls;

public class Register_Activity extends Activity implements View.OnClickListener {

    private Context context;
    private UserSessionManager session;
    private LinearLayout ll_mainlayout;
    private MaterialEditText edt_firstname, edt_middlename, edt_lastname, edt_moblieno, edt_email, edt_password;
    private Button btn_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        setDefault();
        setEventHandlers();
    }

    private void init() {
        context = Register_Activity.this;
        session = new UserSessionManager(context);

        ll_mainlayout = findViewById(R.id.ll_mainlayout);
        edt_firstname = findViewById(R.id.edt_firstname);
        edt_middlename = findViewById(R.id.edt_middlename);
        edt_lastname = findViewById(R.id.edt_lastname);
        edt_moblieno = findViewById(R.id.edt_moblieno);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);

        btn_register = findViewById(R.id.btn_register);
    }

    private void setDefault() {

    }

    private void setEventHandlers() {
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:

                if (edt_firstname.getText().toString().trim().isEmpty()) {
                    edt_firstname.setError("Please enter first name");
                    return;
                }
                if (edt_lastname.getText().toString().trim().isEmpty()) {
                    edt_lastname.setError("Please enter last name");
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
                if (edt_password.getText().toString().trim().isEmpty()) {
                    edt_password.setError("Please enter password");
                    return;
                }

                if (Utilities.isInternetAvailable(context)) {
                    new RegisterUser().execute(
                            edt_firstname.getText().toString().trim(),
                            edt_middlename.getText().toString().trim(),
                            edt_lastname.getText().toString().trim(),
                            edt_moblieno.getText().toString().trim(),
                            edt_email.getText().toString().trim(),
                            edt_password.getText().toString().trim());
                } else {
                    Utilities.showSnackBar(ll_mainlayout, "Please Check Internet Connection");
                }


                break;
        }
    }

    public class RegisterUser extends AsyncTask<String, Void, String> {

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
            JSONObject obj = new JSONObject();
            try {
                obj.put("type", "doRegistraion");
                obj.put("first_name", params[0]);
                obj.put("middle_name", params[1]);
                obj.put("last_name", params[2]);
                obj.put("mobile_number", params[3]);
                obj.put("email", params[4]);
                obj.put("password", params[5]);
                obj.put("profile_image", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            res = WebServiceCalls.JSONAPICall(ApplicationConstants.ConsumerData, obj.toString());
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
//                        JSONArray jsonarr = mainObj.getJSONArray("data");
//                        if (jsonarr.length() > 0) {
//                            for (int i = 0; i < 1; i++) {
//                                session.createUserLoginSession(jsonarr.toString());
//                            }
//                            finish();
//                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                        builder.setMessage("User Registered Successfully");
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
                        Utilities.showSnackBar(ll_mainlayout, message);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

package in.oriange.dailydiary.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.utilities.ApplicationConstants;
import in.oriange.dailydiary.utilities.UserSessionManager;
import in.oriange.dailydiary.utilities.Utilities;
import in.oriange.dailydiary.utilities.WebServiceCalls;

public class Login_Activity extends Activity implements View.OnClickListener {

    private Context context;
    private UserSessionManager session;
    private LinearLayout ll_mainlayout;
    private MaterialEditText edt_username, edt_password;
    private TextView tv_forgotpass, tv_register;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        setDefault();
        setEventHandlers();
    }

    private void init() {
        context = Login_Activity.this;
        session = new UserSessionManager(context);

        ll_mainlayout = findViewById(R.id.ll_mainlayout);
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);

        tv_forgotpass = findViewById(R.id.tv_forgotpass);
        tv_register = findViewById(R.id.tv_register);

        btn_login = findViewById(R.id.btn_login);

    }

    private void setDefault() {

    }

    private void setEventHandlers() {
        btn_login.setOnClickListener(this);
        tv_forgotpass.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (edt_username.getText().toString().isEmpty()) {
                    edt_username.setError("Please Enter Username");
                    return;
                }
                if (edt_password.getText().toString().isEmpty()) {
                    edt_password.setError("Please Enter Password");
                    return;
                }

                if (Utilities.isInternetAvailable(context)) {
                    new LoginUser().execute(edt_username.getText().toString().trim(), edt_password.getText().toString().trim());
                } else {
                    Utilities.showSnackBar(ll_mainlayout, "Please check your internet connection");
                }
                break;

            case R.id.tv_forgotpass:
                break;

            case R.id.tv_register:
                startActivity(new Intent(context, Register_Activity.class));
//                finish();
                break;

        }
    }

    public class LoginUser extends AsyncTask<String, Void, String> {

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
                obj.put("type", "doLogin");
                obj.put("mobile_number", params[0]);
                obj.put("password", params[1]);
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
                        JSONArray jsonarr = mainObj.getJSONArray("data");
                        if (jsonarr.length() > 0) {
                            for (int i = 0; i < 1; i++) {
                                session.createUserLoginSession(jsonarr.toString());
                            }
                            finish();
                        }
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

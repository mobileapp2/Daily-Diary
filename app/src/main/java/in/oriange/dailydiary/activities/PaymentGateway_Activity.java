package in.oriange.dailydiary.activities;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import in.oriange.dailydiary.R;
import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;

public class PaymentGateway_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);
        // Call the function callInstamojo to start payment here

        callInstamojoPay("priyeshpawar07@gmail.com", "8149115089", "10", "Test Buy", "Priyesh Pawar");
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
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onFailure(int code, String reason) {
                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
                        .show();
            }
        };
    }

}

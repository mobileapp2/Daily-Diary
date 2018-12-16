package in.oriange.dailydiary.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.activities.Login_Activity;
import in.oriange.dailydiary.activities.MyAddress_Activity;
import in.oriange.dailydiary.activities.MyFeedback_Activity;
import in.oriange.dailydiary.activities.MyPackage_Activity;
import in.oriange.dailydiary.activities.Register_Activity;
import in.oriange.dailydiary.utilities.ApplicationConstants;
import in.oriange.dailydiary.utilities.UserSessionManager;
import in.oriange.dailydiary.utilities.Utilities;

public class BottomSheetMenu_Fragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private Context context;
    private UserSessionManager session;
    private Button btn_login, btn_register, btn_logout;
    private TextView tv_name, tv_email, tv_mobileno;
    private LinearLayout ll_loginregister, ll_profileinfo, ll_address, ll_mypackage, ll_feedback;
    private String First_Name, Middle_Name, Last_Name, Mobile_Number, Email_ID;

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_bottomsheet_menu, null);
        dialog.setContentView(view);

        init(view);
        setDefaults();
        setEventListner();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (session.isUserLoggedIn()) {
            ll_loginregister.setVisibility(View.GONE);
            ll_profileinfo.setVisibility(View.VISIBLE);
            btn_logout.setVisibility(View.VISIBLE);
        } else {
            ll_loginregister.setVisibility(View.VISIBLE);
            ll_profileinfo.setVisibility(View.GONE);
            btn_logout.setVisibility(View.GONE);
        }

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            JSONObject json = user_info.getJSONObject(0);
            First_Name = json.getString("First_Name");
            Middle_Name = json.getString("Middle_Name");
            Last_Name = json.getString("Last_Name");
            Mobile_Number = json.getString("Mobile_Number");
            Email_ID = json.getString("Email_ID");
        } catch (Exception e) {
            e.printStackTrace();
        }

        tv_name.setText(First_Name + " " + Middle_Name + " " + Last_Name);
        tv_email.setText(Email_ID);
        tv_mobileno.setText(Mobile_Number);


    }

    private void init(View view) {
        context = getActivity();
        session = new UserSessionManager(context);
        btn_login = view.findViewById(R.id.btn_login);
        btn_register = view.findViewById(R.id.btn_register);
        btn_logout = view.findViewById(R.id.btn_logout);

        tv_name = view.findViewById(R.id.tv_name);
        tv_email = view.findViewById(R.id.tv_email);
        tv_mobileno = view.findViewById(R.id.tv_mobileno);

        ll_loginregister = view.findViewById(R.id.ll_loginregister);
        ll_profileinfo = view.findViewById(R.id.ll_profileinfo);
        ll_address = view.findViewById(R.id.ll_address);
        ll_mypackage = view.findViewById(R.id.ll_mypackage);
        ll_feedback = view.findViewById(R.id.ll_feedback);

    }

    private void setDefaults() {

    }

    private void setEventListner() {
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        ll_address.setOnClickListener(this);
        ll_mypackage.setOnClickListener(this);
        ll_feedback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(context, Login_Activity.class));
                break;

            case R.id.btn_register:
                startActivity(new Intent(context, Register_Activity.class));
                break;

            case R.id.btn_logout:
                openLogoutDialog();
                break;

            case R.id.ll_address:
                if (session.isUserLoggedIn()) {
                    startActivity(new Intent(context, MyAddress_Activity.class));
                } else {
                    Utilities.showMessageString(context, "Please login first");
                    startActivity(new Intent(context, Login_Activity.class));
                }
                break;

            case R.id.ll_mypackage:
                if (session.isUserLoggedIn()) {
                    startActivity(new Intent(context, MyPackage_Activity.class));
                } else {
                    Utilities.showMessageString(context, "Please login first");
                    startActivity(new Intent(context, Login_Activity.class));
                }
                break;

            case R.id.ll_feedback:
                if (session.isUserLoggedIn()) {
                    startActivity(new Intent(context, MyFeedback_Activity.class));
                } else {
                    Utilities.showMessageString(context, "Please login first");
                    startActivity(new Intent(context, Login_Activity.class));
                }
                break;

        }
    }

    public void openLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
        builder.setMessage("Are you sure you want to logout?");
        builder.setTitle("Alert");
        builder.setIcon(R.drawable.icon_alertred);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                session.logoutUser();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertD = builder.create();
        alertD.show();
    }
}

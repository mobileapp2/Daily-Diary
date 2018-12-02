package in.oriange.dailydiary.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Locale;

import in.oriange.dailydiary.R;
import in.oriange.dailydiary.utilities.UserSessionManager;
import in.oriange.dailydiary.utilities.Utilities;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static in.oriange.dailydiary.utilities.Utilities.hideSoftKeyboard;
import static in.oriange.dailydiary.utilities.Utilities.isLocationEnabled;
import static in.oriange.dailydiary.utilities.Utilities.isPinCode;

public class GetPinCode_Activity extends Activity implements View.OnClickListener {

    private Context context;
    private UserSessionManager session;
    private FusedLocationProviderClient locationProviderClient;
    private Button btn_pincodefromloc, btn_search, btn_select;
    private EditText edt_piccode;
    private CardView cv_locationdetails;
    private TextView tv_state, tv_city, tv_locality, tv_pincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_pincode);

        init();
        setDefaults();
        setEventListner();
    }

    private void init() {
        context = GetPinCode_Activity.this;
        session = new UserSessionManager(context);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        btn_pincodefromloc = findViewById(R.id.btn_pincodefromloc);
        btn_search = findViewById(R.id.btn_search);
        btn_select = findViewById(R.id.btn_select);
        edt_piccode = findViewById(R.id.edt_piccode);
        tv_state = findViewById(R.id.tv_state);
        tv_city = findViewById(R.id.tv_city);
        tv_locality = findViewById(R.id.tv_locality);
        tv_pincode = findViewById(R.id.tv_pincode);
        cv_locationdetails = findViewById(R.id.cv_locationdetails);
    }

    private void setDefaults() {
        requestPermission();
    }

    private void setEventListner() {
        btn_pincodefromloc.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_select.setOnClickListener(this);

        edt_piccode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    hideSoftKeyboard(GetPinCode_Activity.this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pincodefromloc:
                if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermission();
                    return;
                } else {
                    if (!isLocationEnabled(context)) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
                        alertDialog.setTitle("GPS Settings");
                        alertDialog.setCancelable(false);
                        alertDialog.setIcon(R.drawable.icon_location);
                        alertDialog.setMessage("GPS is not enabled. Please turn on the location from settings.");
                        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        });
                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.show();
                        return;
                    } else {

                        locationProviderClient.getLastLocation()
                                .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        if (location != null) {
                                            getAddressDetails(location.getLatitude(), location.getLongitude());
                                        }
                                    }
                                })
                                .addOnFailureListener((Activity) context, new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Utilities.showAlertDialog(context, "Alert", "Unable to detect your current location, please try again", false);
                                        cv_locationdetails.setVisibility(View.GONE);

                                    }
                                });

                    }
                }


                break;
            case R.id.btn_search:
                if (!isPinCode(edt_piccode)) {
                    return;
                }
                getAddressFromPincode(edt_piccode.getText().toString().trim());


                break;
            case R.id.btn_select:

                if (tv_pincode.getText().toString().trim().isEmpty()) {
                    Utilities.showAlertDialog(context, "Alert", "Unable to add the selected pincode", false);
                    return;
                }

                JsonObject obj = new JsonObject();
                obj.addProperty("state", tv_state.getText().toString().trim());
                obj.addProperty("city", tv_city.getText().toString().trim());
                obj.addProperty("locality", tv_locality.getText().toString().trim());
                obj.addProperty("pincode", tv_pincode.getText().toString().trim());
                session.setPinCode(obj.toString());
                finishAffinity();
                startActivity(new Intent(context, MainDrawer_Activity.class));
                break;
        }
    }

    private void getAddressDetails(double latitude, double longitude) {

        try {
            Geocoder geocoder;
            List<Address> addresses;

            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses != null && !addresses.isEmpty()) {
                tv_state.setText(addresses.get(0).getAdminArea());
                tv_city.setText(addresses.get(0).getLocality());
                tv_locality.setText(addresses.get(0).getSubLocality());
                tv_pincode.setText(addresses.get(0).getPostalCode());
            } else {
                Utilities.showAlertDialog(context, "Alert", "Unable to get address from this location. Please try again or enter pincode manually", false);
                cv_locationdetails.setVisibility(View.GONE);
            }

            cv_locationdetails.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Utilities.showAlertDialog(context, "Alert", "Unable to get address from this location. Please try again or enter pincode manually", false);
            cv_locationdetails.setVisibility(View.GONE);
        }

    }

    private void getAddressFromPincode(String zipCode) {
        final Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> addresses = geocoder.getFromLocationName(zipCode, 1);
            if (addresses != null && !addresses.isEmpty()) {
                tv_state.setText(addresses.get(0).getAdminArea());
                tv_city.setText(addresses.get(0).getSubAdminArea());
                tv_locality.setText(addresses.get(0).getSubLocality());
                tv_pincode.setText(addresses.get(0).getPostalCode());

                cv_locationdetails.setVisibility(View.VISIBLE);
            } else {
                Utilities.showAlertDialog(context, "Alert", "Unable to get address from this pincode. Please try again or enter correct pincode", false);
                cv_locationdetails.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Utilities.showAlertDialog(context, "Alert", "Unable to get address from this pincode. Please try again or enter correct pincode", false);
            cv_locationdetails.setVisibility(View.GONE);
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideSoftKeyboard(GetPinCode_Activity.this);
    }
}
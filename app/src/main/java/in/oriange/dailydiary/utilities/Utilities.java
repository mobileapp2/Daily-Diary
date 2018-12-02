package in.oriange.dailydiary.utilities;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.oriange.dailydiary.R;

public class Utilities {

    public static NotificationManager mManager;
    public static SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat dfDate2 = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat dfDate3 = new SimpleDateFormat("MM/dd/yyyy");
    public static SimpleDateFormat dfDate4 = new SimpleDateFormat("yyyy/MM/dd");
    static AlertDialog.Builder alertDialog;

    public static boolean isEmailValid(EditText edt) {
        edt.setError(null);
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = edt.getText().toString().trim();
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isInternetAvailable(Context context) {
        try {
//            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
//            return !ipAddr.equals("");

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null;

        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isMobileNo(EditText edt) {
        edt.setError(null);
        if ((edt.getText().toString().trim().length() == 10)
                && (isValidMobileno(edt.getText().toString().trim())))
            return true;
        else {
            return false;
        }
    }

    private static boolean isValidMobileno(String mobileno) {
        String Mobile_PATTERN = "^[6-9]{1}[0-9]{9}$";                                               //^[+]?[0-9]{10,13}$
        Pattern pattern = Pattern.compile(Mobile_PATTERN);
        Matcher matcher = pattern.matcher(mobileno);
        return matcher.matches();
    }

    public static String ConvertDateFormat(DateFormat dateFormat, int day, int month, int year) {
        String startDateString = String.valueOf(year) + "-"
                + String.valueOf(month) + "-"
                + String.valueOf(day);
        Date startDate;
        String newDateString = "";
        try {
            startDate = dfDate.parse(startDateString);
            newDateString = dateFormat.format(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDateString;
    }

    public static void showMessageString(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("deprecation")
    public static void showAlertDialog(Context context, String title,
                                       String message, Boolean status) {
        alertDialog = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        if (status != null)
            alertDialog.setIcon((status) ? R.drawable.icon_success : R.drawable.icon_alertred);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertD = alertDialog.create();
        alertD.show();
    }

    public static boolean isPinCode(EditText edt) {
        edt.setError(null);
        if ((edt.getText().toString().trim().length() == 6)
                && (isValidPinCode(edt.getText().toString().trim())))
            return true;
        else {
            edt.setError("Please Enter Valid Pincode");
            return false;
        }
    }

    private static boolean isValidPinCode(String pinCode) {
        String PINCODE_PATTERN = "^[1-9][0-9]{5}$";                                        //^[+]?[0-9]{10,13}$
        Pattern pattern = Pattern.compile(PINCODE_PATTERN);
        Matcher matcher = pattern.matcher(pinCode);
        return matcher.matches();
    }


    public static void showSnackBar(ViewGroup viewGroup, String message) {
        Snackbar snackbar = Snackbar
                .make(viewGroup, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static String changeDateFormat(String currentFormat, String requiredFormat, String dateString) {
        String result = "";
        if (dateString.equals("")) {
            return "";
        }
        SimpleDateFormat formatterOld = new SimpleDateFormat(currentFormat, Locale.getDefault());
        SimpleDateFormat formatterNew = new SimpleDateFormat(requiredFormat, Locale.getDefault());
        Date date = null;
        try {
            date = formatterOld.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            result = formatterNew.format(date);
        }
        return result;
    }

    public static String html2text(String html) {
        return android.text.Html.fromHtml(html).toString().trim();

    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }


}

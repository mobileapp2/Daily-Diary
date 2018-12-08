package in.oriange.dailydiary.models;

import android.text.TextUtils;

import java.io.Serializable;

public class AddressesModel implements Serializable {

    private String pincode;

    private String city_name;

    private String mobile_number;

    private String email;

    private String address_id;

    private String addressline_one;

    private String address_name;

    private String addressline_two;

    private String state_name;

    private String country_name;

    private String full_name;

    public String getPincode() {
        if (TextUtils.isEmpty(pincode)) {
            return " - ";
        } else {
            return pincode;
        }
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity_name() {
        if (TextUtils.isEmpty(city_name)) {
            return "-";
        } else {
            return city_name;
        }
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getMobile_number() {
        if (TextUtils.isEmpty(mobile_number)) {
            return "-";
        } else {
            return mobile_number;
        }
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getEmail() {
        if (TextUtils.isEmpty(email)) {
            return "-";
        } else {
            return email;
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress_id() {
        if (TextUtils.isEmpty(address_id)) {
            return "-";
        } else {
            return address_id;
        }
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getAddressline_one() {
        if (TextUtils.isEmpty(addressline_one)) {
            return "-";
        } else {
            return addressline_one;
        }
    }

    public void setAddressline_one(String addressline_one) {
        this.addressline_one = addressline_one;
    }

    public String getAddress_name() {
        if (TextUtils.isEmpty(address_name)) {
            return "-";
        } else {
            return address_name;
        }
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getAddressline_two() {
        if (TextUtils.isEmpty(addressline_two)) {
            return "-";
        } else {
            return addressline_two;
        }
    }

    public void setAddressline_two(String addressline_two) {
        this.addressline_two = addressline_two;
    }

    public String getState_name() {
        if (TextUtils.isEmpty(state_name)) {
            return "-";
        } else {
            return state_name;
        }
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCountry_name() {
        if (TextUtils.isEmpty(country_name)) {
            return "-";
        } else {
            return country_name;
        }
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getFull_name() {
        if (TextUtils.isEmpty(full_name)) {
            return "-";
        } else {
            return full_name;
        }
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    @Override
    public String toString() {
        return "ClassPojo [pincode = " + pincode + ", city_name = " + city_name + ", mobile_number = " + mobile_number + ", email = " + email + ", address_id = " + address_id + ", addressline_one = " + addressline_one + ", address_name = " + address_name + ", addressline_two = " + addressline_two + ", state_name = " + state_name + ", country_name = " + country_name + ", full_name = " + full_name + "]";
    }
}

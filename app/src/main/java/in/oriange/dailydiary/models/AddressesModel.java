package in.oriange.dailydiary.models;

public class AddressesModel {

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

    public String getPincode ()
    {
        return pincode;
    }

    public void setPincode (String pincode)
    {
        this.pincode = pincode;
    }

    public String getCity_name ()
    {
        return city_name;
    }

    public void setCity_name (String city_name)
    {
        this.city_name = city_name;
    }

    public String getMobile_number ()
    {
        return mobile_number;
    }

    public void setMobile_number (String mobile_number)
    {
        this.mobile_number = mobile_number;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getAddress_id ()
    {
        return address_id;
    }

    public void setAddress_id (String address_id)
    {
        this.address_id = address_id;
    }

    public String getAddressline_one ()
    {
        return addressline_one;
    }

    public void setAddressline_one (String addressline_one)
    {
        this.addressline_one = addressline_one;
    }

    public String getAddress_name ()
    {
        return address_name;
    }

    public void setAddress_name (String address_name)
    {
        this.address_name = address_name;
    }

    public String getAddressline_two ()
    {
        return addressline_two;
    }

    public void setAddressline_two (String addressline_two)
    {
        this.addressline_two = addressline_two;
    }

    public String getState_name ()
    {
        return state_name;
    }

    public void setState_name (String state_name)
    {
        this.state_name = state_name;
    }

    public String getCountry_name ()
    {
        return country_name;
    }

    public void setCountry_name (String country_name)
    {
        this.country_name = country_name;
    }

    public String getFull_name ()
    {
        return full_name;
    }

    public void setFull_name (String full_name)
    {
        this.full_name = full_name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [pincode = "+pincode+", city_name = "+city_name+", mobile_number = "+mobile_number+", email = "+email+", address_id = "+address_id+", addressline_one = "+addressline_one+", address_name = "+address_name+", addressline_two = "+addressline_two+", state_name = "+state_name+", country_name = "+country_name+", full_name = "+full_name+"]";
    }
}

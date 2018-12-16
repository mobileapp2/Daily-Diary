package in.oriange.dailydiary.models;

import java.io.Serializable;
import java.util.ArrayList;

public class PackagesModel implements Serializable {

    private ArrayList<Items> Items;

    private ArrayList<DeliveryDates> DeliveryDates;

    private String status_Id;

    private String city_name;

    private String addressline_two;

    private String state_name;

    private String country_name;

    private String addressId;

    private String pincode;

    private String StartDate;

    private String status_name;

    private String PackageName;

    private String address_name;

    private String addressline_one;

    private ArrayList<Feedback> Feedback;

    private String PackageID;

    private String DeliveryConflict;

    private String full_name;

    public ArrayList<Items> getItems() {
        return Items;
    }

    public void setItems(ArrayList<Items> Items) {
        this.Items = Items;
    }

    public ArrayList<DeliveryDates> getDeliveryDates() {
        return DeliveryDates;
    }

    public void setDeliveryDates(ArrayList<DeliveryDates> DeliveryDates) {
        this.DeliveryDates = DeliveryDates;
    }

    public String getStatus_Id() {
        return status_Id;
    }

    public void setStatus_Id(String status_Id) {
        this.status_Id = status_Id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getAddressline_two() {
        return addressline_two;
    }

    public void setAddressline_two(String addressline_two) {
        this.addressline_two = addressline_two;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String StartDate) {
        this.StartDate = StartDate;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String PackageName) {
        this.PackageName = PackageName;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getAddressline_one() {
        return addressline_one;
    }

    public void setAddressline_one(String addressline_one) {
        this.addressline_one = addressline_one;
    }

    public ArrayList<Feedback> getFeedback() {
        return Feedback;
    }

    public void setFeedback(ArrayList<Feedback> Feedback) {
        this.Feedback = Feedback;
    }

    public String getPackageID() {
        return PackageID;
    }

    public void setPackageID(String PackageID) {
        this.PackageID = PackageID;
    }

    public String getDeliveryConflict() {
        return DeliveryConflict;
    }

    public void setDeliveryConflict(String DeliveryConflict) {
        this.DeliveryConflict = DeliveryConflict;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }


    public class Items implements Serializable {
        private String item_image;

        private String Quantity;

        private String IsActive;

        private String UnitPrice;

        private String Item_Name;

        private String UoM_Name;

        public String getItem_image() {
            return item_image;
        }

        public void setItem_image(String item_image) {
            this.item_image = item_image;
        }

        public String getQuantity() {
            return Quantity;
        }

        public void setQuantity(String Quantity) {
            this.Quantity = Quantity;
        }

        public String getIsActive() {
            return IsActive;
        }

        public void setIsActive(String IsActive) {
            this.IsActive = IsActive;
        }

        public String getUnitPrice() {
            return UnitPrice;
        }

        public void setUnitPrice(String UnitPrice) {
            this.UnitPrice = UnitPrice;
        }

        public String getItem_Name() {
            return Item_Name;
        }

        public void setItem_Name(String Item_Name) {
            this.Item_Name = Item_Name;
        }

        public String getUoM_Name() {
            return UoM_Name;
        }

        public void setUoM_Name(String UoM_Name) {
            this.UoM_Name = UoM_Name;
        }
    }

    public class DeliveryDates implements Serializable {
        private String Delivery_Date;

        public String getDelivery_Date() {
            return Delivery_Date;
        }

        public void setDelivery_Date(String Delivery_Date) {
            this.Delivery_Date = Delivery_Date;
        }

    }

    public class Feedback implements Serializable {
        private String FeedbackID;

        private String StarRating;

        private String Feedback;

        public String getFeedbackID() {
            return FeedbackID;
        }

        public void setFeedbackID(String FeedbackID) {
            this.FeedbackID = FeedbackID;
        }

        public String getStarRating() {
            return StarRating;
        }

        public void setStarRating(String StarRating) {
            this.StarRating = StarRating;
        }

        public String getFeedback() {
            return Feedback;
        }

        public void setFeedback(String Feedback) {
            this.Feedback = Feedback;
        }
    }

}

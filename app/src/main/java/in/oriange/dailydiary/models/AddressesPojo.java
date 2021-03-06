package in.oriange.dailydiary.models;

import java.util.ArrayList;

public class AddressesPojo {

    private String message;

    private ArrayList<AddressesModel> data;

    private String type;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<AddressesModel> getData() {
        return data;
    }

    public void setData(ArrayList<AddressesModel> data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", data = " + data + ", type = " + type + "]";
    }
}

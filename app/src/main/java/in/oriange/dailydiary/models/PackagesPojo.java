package in.oriange.dailydiary.models;

import java.util.ArrayList;

public class PackagesPojo {

    private String message;

    private ArrayList<PackagesModel> data;

    private String type;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<PackagesModel> getData() {
        return data;
    }

    public void setData(ArrayList<PackagesModel> data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

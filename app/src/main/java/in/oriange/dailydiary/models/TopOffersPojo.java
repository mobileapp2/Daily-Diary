package in.oriange.dailydiary.models;

import java.util.ArrayList;

public class TopOffersPojo {

    private String message;

    private ArrayList<TopOffersModel> data;

    private String type;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<TopOffersModel> getData() {
        return data;
    }

    public void setData(ArrayList<TopOffersModel> data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

package in.oriange.dailydiary.utilities;

import java.util.ArrayList;

import in.oriange.dailydiary.models.PackageItemsModel;
import in.oriange.dailydiary.models.TopProductsModel;

public class ConstantData {

    public static ConstantData _instance;

    private ConstantData() {
    }

    public static ConstantData getInstance() {
        if (_instance == null) {
            _instance = new ConstantData();
        }
        return _instance;
    }

    public static ConstantData get_instance() {
        return _instance;
    }

    public static void set_instance(ConstantData _instance) {
        ConstantData._instance = _instance;
    }


    private ArrayList<TopProductsModel> topProductsList;

    private ArrayList<PackageItemsModel> packageItemsList;

    public ArrayList<TopProductsModel> getTopProductsList() {
        return topProductsList;
    }

    public void setTopProductsList(ArrayList<TopProductsModel> topProductsList) {
        this.topProductsList = topProductsList;
    }

    public ArrayList<PackageItemsModel> getPackageItemsList() {
        return packageItemsList;
    }

    public void setPackageItemsList(ArrayList<PackageItemsModel> packageItemsList) {
        this.packageItemsList = packageItemsList;
    }
}
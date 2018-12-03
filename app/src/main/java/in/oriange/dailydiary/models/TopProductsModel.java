package in.oriange.dailydiary.models;

public class TopProductsModel {


    private String ItemID;

    private String item_image;

    private String UoM_ID;

    private String IsActive;

    private String UnitPrice;

    private String Item_Name;

    private String UoM_Name;

    private String totalProductCount;

    private String totalProductrate;

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String ItemID) {
        this.ItemID = ItemID;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getUoM_ID() {
        return UoM_ID;
    }

    public void setUoM_ID(String UoM_ID) {
        this.UoM_ID = UoM_ID;
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

    public String getTotalProductCount() {
        return totalProductCount;
    }

    public void setTotalProductCount(String totalProductCount) {
        this.totalProductCount = totalProductCount;
    }

    public String getTotalProductrate() {
        return totalProductrate;
    }

    public void setTotalProductrate(String totalProductrate) {
        this.totalProductrate = totalProductrate;
    }
}

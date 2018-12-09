package in.oriange.dailydiary.models;

public class PackageItemsModel {


    private String ItemID;

    private String item_image;

    private String UoM_ID;

    private String IsActive;

    private String UnitPrice;

    private String Item_Name;

    private String UoM_Name;

    private String totalProductCount;

    private String totalProductrate;

    public PackageItemsModel(String ItemID, String item_image, String UoM_ID, String IsActive, String UnitPrice, String Item_Name, String UoM_Name, String totalProductCount, String totalProductrate) {
        this.ItemID = ItemID;
        this.item_image = item_image;
        this.UoM_ID = UoM_ID;
        this.IsActive = IsActive;
        this.UnitPrice = UnitPrice;
        this.Item_Name = Item_Name;
        this.UoM_Name = UoM_Name;
        this.totalProductCount = totalProductCount;
        this.totalProductrate = totalProductrate;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
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

    public void setUoM_ID(String uoM_ID) {
        UoM_ID = uoM_ID;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public String getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        UnitPrice = unitPrice;
    }

    public String getItem_Name() {
        return Item_Name;
    }

    public void setItem_Name(String item_Name) {
        Item_Name = item_Name;
    }

    public String getUoM_Name() {
        return UoM_Name;
    }

    public void setUoM_Name(String uoM_Name) {
        UoM_Name = uoM_Name;
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

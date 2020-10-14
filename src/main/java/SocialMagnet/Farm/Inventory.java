package SocialMagnet.Farm;

/**
 * Inventory
 */
public class Inventory {
    private String owner;
    private String CropName;
    private int Quantity;

    public Inventory(String owner, String CropName, int Quantity) {
        // User should have multiple Crops in the inventory.
        this.owner = owner;
        this.CropName = CropName;
        this.Quantity = Quantity;
    }

    public String getOwner() {
        return owner;
    }

    public String getCropName() {
        return CropName;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setCropName(String CropName) {
        this.CropName = CropName;
    }

    public void setQuantity(int newQty) {
        this.Quantity = newQty;
    }

    public String toString() {
        return "Inventory [Owner = " + owner + ", CropName = " + CropName + ", Quantity = " + Quantity + "]";
    }

    
}
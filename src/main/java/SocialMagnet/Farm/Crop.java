package SocialMagnet.Farm;

/**
 * Crop
 */
public class Crop {
    private String name;
    private int cost;
    private int maturityTime; 
    private int XP;
    private int minYield;
    private int maxYield;
    private int salePrice;

    public Crop(String name, int cost, int maturityTime, int XP, int minYield, int maxYield, int salePrice) {
        this.name = name;
        this.cost = cost;
        this.maturityTime = maturityTime;
        this.XP = XP;
        this.minYield = minYield;
        this.maxYield = maxYield;
        this.salePrice = salePrice;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public int getMaturityTime() {
        return maturityTime;
    }

    public int getXP() {
        return XP;
    }

    public int getMinYield() {
        return minYield;
    }

    public int getMaxYield() {
        return maxYield;
    }

    public int getSalePrice() {
        return salePrice;
    }

    
}
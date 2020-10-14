package SocialMagnet.Farm;

import java.util.*;

/**
 * PlotCrop
 */
public class PlotCropSteal {
    private String stealerID;
    private String userID;
    private int plotID;
    private int amountStolen;
    private double cumPercStolen;

    public PlotCropSteal(String stealerID, String userID, int plotID, int amountStolen, double cumPercStolen) {
        this.stealerID = stealerID;
        this.userID = userID;
        this.plotID = plotID;
        this.amountStolen = amountStolen;
        this.cumPercStolen = cumPercStolen;
    }

    public String getStealerID() {
        return stealerID;
    }

    public String getUserID() {
        return userID;
    }

    public int getPlotID() {
        return plotID;
    }

    public int getAmountStolen() {
        return amountStolen;
    }

    public double getTotalPercStolen() {
        return cumPercStolen;
    }

    
}
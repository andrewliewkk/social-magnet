package SocialMagnet.Farm;

import java.util.*;

/**
 * PlotCrop
 */
public class PlotCrop {
    private String userID;
    private int plotID;
    private String cropName;
    private Date timePlanted;
    private int yield;

    public PlotCrop(String userID, int plotID, String cropName, Date timePlanted, int yield) {
        this.userID = userID;
        this.plotID = plotID;
        this.cropName = cropName;
        this.timePlanted = timePlanted;
        this.yield = yield;
    }

    public String getUserID() {
        return userID;
    }

    public int getPlotID() {
        return plotID;
    }

    public String getCropName() {
        return cropName;
    }

    public Date getTimePlanted() {
        return timePlanted;
    }

    public int getYield() {
        return yield;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public void setTimePlanted(Date timePlanted) {
        this.timePlanted = timePlanted;
    }

    public void setYield(int yield) {
        this.yield = yield;
    }

    public long getProgress() {
        long millis = System.currentTimeMillis();  
        Date currentTime =new java.util.Date(millis);  

        long diff = currentTime.getTime() - this.timePlanted.getTime();
        long diffMinutes = diff / (60000);
        return diffMinutes;
    }
}
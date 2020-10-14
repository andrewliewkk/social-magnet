package SocialMagnet.Farm;

/**
 * Plot
 */
public class Plot {
    private int plotID;
    private String userID;

    public Plot(int plotID, String userID) {
        this.plotID = plotID;
        this.userID = userID;
    }

    public int getPlotID() {
        return plotID;
    }

    public String getUserID() {
        return userID;
    }
}
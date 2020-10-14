package SocialMagnet.Farm;

/**
 * Rank
 */
public class Rank {
    private String rank;
    private int XP;
    private int plotsAllowed;

    public Rank(String rank, int XP, int plotsAllowed) {
        this.rank = rank;
        this.XP = XP;
        this.plotsAllowed = plotsAllowed;
    }

    public String getRank() {
        return rank;
    }

    public int getXP() {
        return XP;
    }

    public int getPlots() {
        return plotsAllowed;
    }



}
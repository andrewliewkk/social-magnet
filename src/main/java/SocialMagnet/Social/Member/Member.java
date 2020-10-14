package SocialMagnet.Social.Member;
import SocialMagnet.Farm.*;

/**
 * Member
 */
public class Member {
    private String userID;
    private String password;
    private String fullName;
    private int XP;
    private int gold;
    private String rank;
    private RankDAO rd;

    public Member(String userID, String password, String fullName, int XP, int gold) {
        this.userID = userID;
        this.password = password;
        this.fullName = fullName;
        this.XP = XP;
        this.gold = gold;
        this.rank = calculateRank(XP);
    }

    public String getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public int getXP() {
        return XP;
    }

    public int getGold() {
        return gold;
    }

    public String getRank(){
        return rank;
    }

    public void setGold(int gold){
        this.gold = gold;
    }

    public void setXP(int XP){
        this.XP = XP;
    }

    public void setRank(String newRank) {
        this.rank = newRank;
    }

    public String calculateRank (int XP) {
        rd = new RankDAO();
        return rd.retrieveRank(XP);
    }    

    public void addGold(int gold) {
        this.gold += gold;
    }

    public void addXP(int XP) {
        this.XP += XP;
    }
}
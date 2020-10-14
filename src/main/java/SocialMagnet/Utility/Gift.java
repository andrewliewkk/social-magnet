package SocialMagnet.Utility;
import java.util.*;

/**
 * Gift
 */
public class Gift {
    private int giftID;
    private String senderID;
    private String receiverID;
    private Date date;
    private String giftContent;
    private boolean isAccepted;

    public Gift(int giftID, String senderID, String receiverID, Date date, String giftContent, boolean isAccepted) {
        this.giftID = giftID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.date = date;
        this.giftContent = giftContent;
        this.isAccepted = isAccepted;
    }

    public int getGiftID() {
        return giftID;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public Date getDate() {
        return date;
    }

    public String getGiftContent() {
        return giftContent;
    }

    public boolean getAcceptance() {
        return isAccepted;
    }

    public void setAcceptance(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }
}
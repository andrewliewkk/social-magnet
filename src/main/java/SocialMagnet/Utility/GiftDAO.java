package SocialMagnet.Utility;

import java.sql.*;
import java.util.*;
import java.text.*;

import SocialMagnet.Farm.Crop;
import SocialMagnet.Social.Member.*;
import SocialMagnet.Social.Post.*;

/**
 * PostDAO
 */
public class GiftDAO {

    private PostDAO postDAO = new PostDAO();

    public static ArrayList<Gift> dbMgr(String sqlstmt, boolean update) {
        String host = "localhost";
        int port = 3306;
        String dbName = "magnet";
        String dbURL = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false";

        String username = "root";
        String password = "";
        String sql = sqlstmt; // change statement here
        Statement stmt = null;
        Connection conn = null;

        ArrayList<Gift> giftList = new ArrayList();

        try {
            Class.forName("com.mysql.jdbc.Driver").getConstructor().newInstance();
            conn = DriverManager.getConnection(dbURL, username, password);
            stmt = conn.createStatement();

            if (!update) {
                ResultSet rs = stmt.executeQuery(sql);
                
                while (rs.next()) {
                    int giftID = rs.getInt("GiftID");
                    String senderID = rs.getString("SenderID");
                    String receiverID = rs.getString("ReceiverID");
                    String dateGivenString = rs.getString("DateSent");
                    String giftContent = rs.getString("CropName");
                    boolean isAccepted = rs.getBoolean("isAccepted");

                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
                    java.util.Date timeGifted = dateFormat.parse(dateGivenString);

                    giftList.add(new Gift(giftID, senderID, receiverID, timeGifted, giftContent, isAccepted));
                }
            } else {
                stmt.executeUpdate(sql);
            }
            
        } catch (Exception e) {
            System.out.println("DB Error!");
            System.out.println(e.getMessage());
        } finally {
            try {
                if (stmt != null) { 
                    stmt.close(); 
                }
            } catch (Exception e) {
                System.out.println("Close Error!");
                System.out.println(e.getMessage());
            } 
            try {
                if (conn != null) { 
                    conn.close(); 
                }
            } catch (Exception e) {
                System.out.println("Close Error!");
                System.out.println(e.getMessage());
            }
        }
        
        return giftList;
    }

    public ArrayList<Gift> retrieveAll(){
        ArrayList<Gift> giftList = dbMgr("select * from gift;", false);
        return giftList;
    }

    public boolean hasExceededFiveToday(Member user){
        long millis = System.currentTimeMillis();  
        java.util.Date currentTime = new java.util.Date(millis); 
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
        String sqlDate = dateFormat.format(currentTime).substring(0,10); 

        String userID = user.getUserID();
        ArrayList<Gift> giftList = dbMgr("select * from gift where SenderID = '" + userID + "';", false);
        ArrayList<Gift> listToday = new ArrayList<>();

        for (Gift gift : giftList) {
            java.util.Date giftDate = gift.getDate();
            String giftDateString = dateFormat.format(giftDate).substring(0,10);
            if (giftDateString.equals(sqlDate)) {
                listToday.add(gift);
            }
        }

        return listToday.size() >= 5;
    }

    public boolean hasSentToFriendToday(Member user, Member friend){
        long millis = System.currentTimeMillis();  
        java.util.Date currentTime = new java.util.Date(millis); 
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
        String sqlDate = dateFormat.format(currentTime).substring(0,10); 

        String userID = user.getUserID();
        ArrayList<Gift> giftList = dbMgr("select * from gift where SenderID = '" + userID + 
                "' and ReceiverID = '" + friend.getUserID() + 
                "';", false);

        ArrayList<Gift> listToday = new ArrayList<>();

        for (Gift gift : giftList) {
            java.util.Date giftDate = gift.getDate();
            String giftDateString = dateFormat.format(giftDate).substring(0,10);
            if (giftDateString.equals(sqlDate)) {
                listToday.add(gift);
            }
        }

        return listToday.size() == 1;
    }

    public void sendGift(Member user, Member friend, Crop seed) {
        
        String userID = user.getUserID();
        String receiverID = friend.getUserID();

        long millis = System.currentTimeMillis();  
        
        java.util.Date currentTime = new java.util.Date(millis); 
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
        String sqlDate = dateFormat.format(currentTime);  
        
        dbMgr("INSERT INTO gift (`SenderID`, `ReceiverID`, `DateSent`, `CropName`, `isAccepted`)" +
            " VALUES ('" + userID + "', '" + receiverID + "', '" + 
                sqlDate + "', '" + seed.getName() + "', FALSE);", true);

        String content = ": Here is a bag " + seed.getName() + " seeds for you. - City Farmers";

        // Retrieve the latest gift, get the giftID of this gift
        // Add into wallpost table which stores which wall is the post on.
        ArrayList<Gift> giftList = dbMgr("SELECT * FROM gift ORDER BY GiftID DESC LIMIT 1", false);

        int giftID = 1;
        if (giftList.size() > 0) {
            Gift latestGift = giftList.get(0);
            giftID = latestGift.getGiftID();
        }

        // Calls this method where a new post is created to be displayed on the receiver's wall
        postDAO.newGiftPost(userID, content, receiverID, giftID);
    }

    public ArrayList<Gift> acceptGifts(Member user) {
        String userID = user.getUserID();
        ArrayList<Gift> giftList = dbMgr("select * from gift where ReceiverID = '" + 
                userID + "' AND isAccepted = FALSE;", false);

        // I'm not removing the gifts - in the case that the user re-sends the
        // gift in the same day that the friend accepts it.
        dbMgr("UPDATE gift SET isAccepted = TRUE WHERE ReceiverID = '" + userID + "';", true);
        
        return giftList;
    }

    
}
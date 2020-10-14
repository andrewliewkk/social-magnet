package SocialMagnet.Social.Member;

import java.sql.*;
import java.util.*;

/**
 * MemberDAO
 */
public class MemberDAO {

    public static ArrayList<Member> dbMgr(String sqlstmt, boolean update) {
        String host = "localhost";
        int port = 3306;
        String dbName = "magnet";
        String dbURL = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false";

        String username = "root";
        String password = "";
        String sql = sqlstmt; // change statement here
        Statement stmt = null;
        Connection conn = null;

        ArrayList<Member> memberList = new ArrayList();

        try {
            Class.forName("com.mysql.jdbc.Driver").getConstructor().newInstance();
            conn = DriverManager.getConnection(dbURL, username, password);
            stmt = conn.createStatement();

            if (!update) {
                ResultSet rs = stmt.executeQuery(sql);
                
                while (rs.next()) {
                    String ID = rs.getString("UserID");
                    String pass = rs.getString("Password");
                    String fullName = rs.getString("Fullname");
                    int XP = rs.getInt("Xp");
                    int gold = rs.getInt("Gold");
                    memberList.add(new Member(ID, pass, fullName, XP, gold));
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
        
        return memberList;
    }

    public ArrayList<Member> retrieveAll(){
        ArrayList<Member> memberList = dbMgr("select * from member;", false);
        return memberList;
    }

    public void addUser(String ID, String pass, String fullName) {
        dbMgr("INSERT INTO member (`UserID`, `Password`, `Fullname`) VALUES ('"+ID+"', '"+pass+"', '"+fullName+"');", true);
    }

    public Member getUser(String userID){
        ArrayList<Member> memberList = dbMgr("select * from member where UserID = '" + userID + "';", false);
        if (memberList.isEmpty()) {
            return null;
        }
        return memberList.get(0);
        
    }

    public void deleteUser(String userID){
        dbMgr("delete from member where UserID = '" + userID + "';", true);
    }

    public boolean clearSubtractGold(Member user) {
        String userID = user.getUserID();
        int currentGold = dbMgr("SELECT * FROM member where userID = '" + userID + "';", false).get(0).getGold();

        if (currentGold < 5) {
            return false;
        } 

        dbMgr("UPDATE member SET `Gold` = " + (currentGold - 5) + " WHERE UserID = '" + userID + "';", true);
        user.addGold(-5);
        return true;
    }

    public ArrayList<Member> retrieveLikers(int postID){
        ArrayList<Member> memberList = dbMgr(" select * from member where userid in (select UserID from post_like where PostID = '" + postID + "' and isLike = " + 1 + ");", false);
        return memberList;
    }

    public ArrayList<Member> retrieveDislikers(int postID){
        ArrayList<Member> memberList = dbMgr(" select * from member where userid in (select UserID from post_like where PostID = '" + postID + "' and isLike = " + 0 + ");", false);
        return memberList;
    }

    public ArrayList<Member> getFriends(String userID){
        ArrayList<Member> friendList = dbMgr(" select * from member where userid in (select FriendID as userid from friend where UserID = '" + userID + "') or userid in (select UserID as userid from friend where FriendID = '" + userID + "');", false);
        return friendList;
    }


    public ArrayList<Member> getRequests(String userID){
        ArrayList<Member> requestList = dbMgr(" select * from member where userid in (select SenderID as userid from friend_request where ReceiverID = '" + userID + "');", false);
        return requestList;
    }

    public ArrayList<Member> getRequestsMade(String userID){
        ArrayList<Member> requestList = dbMgr(" select * from member where userid in (select ReceiverID as userid from friend_request where SenderID = '" + userID + "');", false);
        return requestList;
    }

    public void sendFriendRequest(String senderID, String receiverID) {
        // This is for the re[Q]uest function.
        // To-do -> exception check: check if sender and receiver are already friends.
        dbMgr("INSERT INTO friend_request (`SenderID`, `ReceiverID`) VALUES ('" + senderID + "', '" + receiverID + "');", true);

        // Shift the line below into control.
        System.out.println("A friend request is sent to " + receiverID +".");
    }

    public void acceptFriendRequest(String userID, String friendID) {
        // This is for the [A]ccept function.
        // The person who is performing the accepting is the receiver.

        // Update the friend tables.
        dbMgr("INSERT INTO friend (`UserID`, `FriendID`) VALUES ('" + userID + "', '" + friendID + "');", true);

        // Adding in this as well - to update the reverse side.
        dbMgr("INSERT INTO friend (`UserID`, `FriendID`) VALUES ('" + friendID + "', '" + userID + "');", true);

        // Delete friend request that was made previously.
        dbMgr("DELETE FROM friend_request WHERE SenderID = '" + friendID + "' AND ReceiverID = '" + userID + "';", true);
    }

    public void rejectFriendRequest(String senderID, String receiverID) {
        // This is for the [R]eject function.
        // The person who is performing the accepting is the receiver.

        // Update the friend request table.
        dbMgr("DELETE FROM friend_request WHERE SenderID = '" + senderID + "' AND ReceiverID = '" + receiverID + "';", true);
    }

    public void unFriend(String userID, String friendID) {
        // This is for the [U]nfriend function.
        // The person performing the unfriend action is the sender.

        // Delete from friend table where userID and friendID is on either sides of the table on the DB
        dbMgr("delete from friend WHERE (UserID = '" + userID + "' and FriendID = '" + friendID + "') or (FriendID = '" + userID + "' and UserID = '" + friendID + "');", true);

    }

    public void setGold(int gold, String userID){
        dbMgr("update member set Gold = "+gold+" where UserID ='"+userID+"';", true);
    }

    public void setXP(int xp, String userID){
        dbMgr("update member set Xp = "+xp+" where UserID ='"+userID+"';", true);
    }

    public void addHarvest(String userID, int gold, int XP) {
        dbMgr("update member set Gold = "+gold+", Xp = "+XP+" where UserID ='"+userID+"';", true);
    }

}
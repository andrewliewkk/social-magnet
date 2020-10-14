package SocialMagnet.Social.Post;

import java.sql.*;
import java.util.*;

/**
 * ReplyDAO
 */
public class ReplyDAO {

    public static ArrayList<Reply> dbMgr(String sqlstmt, boolean update) {
        String host = "localhost";
        int port = 3306;
        String dbName = "magnet";
        String dbURL = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false";

        String username = "root";
        String password = "";
        String sql = sqlstmt; // change statement here
        Statement stmt = null;
        Connection conn = null;

        ArrayList<Reply> replyList = new ArrayList();

        try {
            Class.forName("com.mysql.jdbc.Driver").getConstructor().newInstance();
            conn = DriverManager.getConnection(dbURL, username, password);
            stmt = conn.createStatement();

            if (!update) {
                ResultSet rs = stmt.executeQuery(sql);
                
                while (rs.next()) {
                    int replyID = rs.getInt("ReplyID");
                    int postID = rs.getInt("PostID");
                    String userID = rs.getString("UserID");
                    String content = rs.getString("Content");
                    
                    replyList.add(new Reply(replyID, postID, userID, content));
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
        
        return replyList;
    }

    public ArrayList<Reply> retrieveAll(){
        ArrayList<Reply> replyList = dbMgr("select * from reply;", false);
        return replyList;
    }

    public boolean replyPost(int postID, String userID, String content) {
        dbMgr("INSERT INTO reply (`PostID`, `UserID`, `Content`) VALUES ('"+postID+"', '"+userID+"', '"+content+"');", true);
        return true;
    }

   public ArrayList<Reply> retrieveReplies (int postID) {
        ArrayList<Reply> replyList = dbMgr("select * from reply where postID = '" + postID + "' order by ReplyID asc;", false);
        return replyList;
   }
}
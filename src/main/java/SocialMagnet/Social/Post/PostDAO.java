package SocialMagnet.Social.Post;

import java.sql.*;
import java.util.*;

import SocialMagnet.Social.Member.*;

/**
 * PostDAO
 */
public class PostDAO {

    public static ArrayList<Post> dbMgr(String sqlstmt, boolean update) {
        String host = "localhost";
        int port = 3306;
        String dbName = "magnet";
        String dbURL = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false";

        String username = "root";
        String password = "";
        String sql = sqlstmt; // change statement here
        Statement stmt = null;
        Connection conn = null;

        ArrayList<Post> postList = new ArrayList();

        try {
            Class.forName("com.mysql.jdbc.Driver").getConstructor().newInstance();
            conn = DriverManager.getConnection(dbURL, username, password);
            stmt = conn.createStatement();

            if (!update) {
                ResultSet rs = stmt.executeQuery(sql);
                
                while (rs.next()) {
                    int postID = rs.getInt("PostID");
                    String posterID = rs.getString("PosterID");
                    String content = rs.getString("Content");
                    String postee = rs.getString("Wall_UserID");
                    int giftID = rs.getInt("GiftID");
                    postList.add(new Post(postID, posterID, content, postee));
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
        
        return postList;
    }

    public ArrayList<Post> retrieveAll(){
        ArrayList<Post> postList = dbMgr("select * from post;", false);
        return postList;
    }

    public ArrayList<Post> retrieveAllByUserID(String posterID){
        ArrayList<Post> postList = dbMgr("select * from post where PosterID='"+posterID+"';", false);
        return postList;
    }

    public ArrayList<Post> retrieveWallPost(int postID){
        ArrayList<Post> postList = dbMgr("select * from post where PostID in (select PostID from wallpost where PostID = '" + postID + "');", false);
        return postList;
    }

    public void newPost(String posterID, String content, String wall_userID, ArrayList<Member> taggedMembers) {
        // Insert post into Post table
        dbMgr("INSERT INTO post (`PosterID`, `Content`, `Wall_UserID`) VALUES ('"+ posterID +"', \"" + content + "\", '" + wall_userID + "');", true);

        // Retrieve the latest post posted by this user, and get the PostID of the post
        // Add into wallpost table which stores which wall is the post on. (Can be multiple user walls as when they are tagged)
        int postID = getLatestPostID(posterID);

        dbMgr("INSERT INTO wallpost (`Wall_UserID`, `PostID`) VALUES ('" + wall_userID + "', '" + postID + "');", true);

        if (!taggedMembers.isEmpty()) {
            for (Member m : taggedMembers) {
                String tagged_userid = m.getUserID();
                dbMgr("INSERT IGNORE INTO wallpost (`Wall_UserID`, `PostID`) VALUES ('" + tagged_userid + "', '" + postID + "');", true);
            }
        }

    }

    public void newGiftPost(String posterID, String content, String wall_userID, int giftID) {
        // Insert post into Post table
        dbMgr("INSERT INTO post (`PosterID`, `Content`, `Wall_UserID`, `GiftID`) VALUES ('"+ posterID +"', '" + content + "', '" + wall_userID + "', '" + giftID + "');", true);

        // Retrieve the latest post posted by this user, and get the PostID of the post
        // Add into wallpost table which stores which wall is the post on. (Can be multiple user walls as when they are tagged)
        int postID = getLatestPostID(posterID);

        dbMgr("INSERT INTO wallpost (`Wall_UserID`, `PostID`) VALUES ('" + wall_userID + "', '" + postID + "');", true);
    }

    public int getLatestPostID(String posterID) {

        ArrayList<Post> postList = dbMgr("SELECT * FROM post WHERE PosterID = '" + posterID + "' ORDER BY PostID DESC LIMIT 1", false);

        int postID = 1;
        if (postList.size() > 0) {
            Post latestPost = postList.get(0);
            postID = latestPost.getPostID();
        }

        return postID;
    }

   public ArrayList<Post> retrieveWall (String userID) {
        // retrieves top 5 post 
        ArrayList<Post> postList = dbMgr("select * from post where PostID in (select PostID from wallpost where Wall_UserID = '" + userID + "') order by PostID desc limit 5;", false);
        return postList;
   }

   public ArrayList<Post> retrieveNewsFeed (String userID) {
        // retrieves top 5 post 
        ArrayList<Post> postList = dbMgr("select * from post where GiftID IS NULL and PostID in (select PostID from wallpost " +
            " where Wall_UserID in (select FriendID from friend where UserID = '" + userID + 
            "' or FriendID = '" + userID + "')) " +
            "order by PostID desc limit 5;", false);
        return postList;
    }

   public boolean likePost(int postID, String userID, boolean isLike) {
       int isTrue;
       if (isLike) {
           isTrue = 1;
       } else {
           isTrue = 0;
       }
        ArrayList<Post> isAdd = dbMgr("REPLACE INTO post_like (`PostID`, `UserID`, `isLike`) VALUES ('"+postID+"', '"+userID+"', '"+isTrue+"');", true);
        return true;
   }


   //if poster deletes post, it is deleted for everyone.
   //if tagged member deletes post, only deleted from his wall.
   public boolean deletePost(String userID, int postID){
        ArrayList<Post> post = dbMgr("select * from post where PostID = '" + postID + "';", false);
        String poster = post.get(0).getPosterID();
        boolean isPoster = userID.equals(poster);
        if (isPoster){
            dbMgr("delete from post where PostID = '" + postID + "';", true);
            dbMgr("delete from wallpost where PostID = '" + postID + "';", true);
            dbMgr("delete from reply where PostID = '" + postID + "';", true);
            dbMgr("delete from post_like where PostID = '" + postID + "';", true);
        } else {
            dbMgr("delete from wallpost where PostID = '" + postID + "' and Wall_UserID = '" + userID + "';", true);
        }
        return true;
   }
}
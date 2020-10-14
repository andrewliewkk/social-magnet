package SocialMagnet.Control;
import java.util.*;

/**
 * ThreadCtrl
 */


import SocialMagnet.Social.Member.*;
import SocialMagnet.Social.Post.*;

public class ThreadCtrl {
    private MemberDAO memberDAO = new MemberDAO();
    private PostDAO postDAO = new PostDAO();
    private ReplyDAO replyDAO = new ReplyDAO();

    public Dictionary getWall(Member user) {
        
        Dictionary threadID = new Hashtable();
        ArrayList<Post> postList = new ArrayList(); // Array of posts retrieved

        postList = postDAO.retrieveWall(user.getUserID());
        
        int index = 0;
        for (Post post:postList) {
            index += 1;
            threadID.put(index, post);
        }
        return threadID;
    }
    
    public boolean likePost(int postID, String userID, boolean isLike) {
        boolean isAdd = postDAO.likePost(postID, userID, isLike);
        return isAdd;
    }

    public ArrayList<Member> retrieveLikers(int postID) {
        return memberDAO.retrieveLikers(postID);
    }

    public ArrayList<Member> retrieveDislikers(int postID) {
        return memberDAO.retrieveDislikers(postID);
    }

    public boolean replyPost(int postID, String userID, String message) {
        boolean isAdd = replyDAO.replyPost(postID, userID, message);
        return isAdd;
    }

    public ArrayList<Reply> retrieveReplies(int postID) {
        return replyDAO.retrieveReplies(postID);
    }

    public boolean deletePost(String userID, int postID){
        boolean isDeleted = postDAO.deletePost(userID, postID);
        return isDeleted;
    }
}
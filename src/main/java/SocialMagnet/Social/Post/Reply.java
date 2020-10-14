package SocialMagnet.Social.Post;

/**
 * Reply
 */
public class Reply {
    private int replyID; 
    private int postID;
    private String userID;
    private String content;

    public Reply(int replyID, int postID, String userID, String content) {
        this.replyID = replyID;
        this.postID = postID;
        this.userID = userID;
        this.content = content;
    }

    public int getReplyID() {
        return replyID;
    }

    public int getPostID() {
        return postID;
    }

    public String getUserID() {
        return userID;
    }

    public String getContent() {
        return content;
    }

    
}
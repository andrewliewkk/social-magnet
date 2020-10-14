package SocialMagnet.Social.Post;

/**
 * Post
 */
public class Post {
    private int postID; 
    private String posterID; // id of the poster is stored as String
    private String content;
    private String userWallID;

    public Post(int postID, String posterID, String content, String userWallID) {
        this.postID = postID;
        this.posterID = posterID;
        this.content = content;
        this.userWallID = userWallID;
    }

    public int getPostID() {
        return postID;
    }

    public String getPosterID() {
        return posterID;
    }

    public String getContent() {
        return content;
    }

    public String getUserWallID() {
        return userWallID;
    }

    public void printPost(int index) {
        System.out.println(index + " " + posterID + ": " + content);
    }
}
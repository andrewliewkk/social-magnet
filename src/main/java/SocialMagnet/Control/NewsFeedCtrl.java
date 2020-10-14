package SocialMagnet.Control;

/**
 * NewsFeedCtrl
 */

import java.util.*;

import SocialMagnet.Social.Member.*;
import SocialMagnet.Social.Post.*;

public class NewsFeedCtrl {
    
    private PostDAO postDAO = new PostDAO();

    public Dictionary getNewsFeed(Member user) {
        Dictionary threadID = new Hashtable();
        ArrayList<Post> postList = new ArrayList(); 
        
        // To obtain the newsfeed
        postList = postDAO.retrieveNewsFeed(user.getUserID());

        int index = 0;
        for (Post post:postList) {
            index += 1;
            threadID.put(index, post);
        }
        return threadID;
    }  
}
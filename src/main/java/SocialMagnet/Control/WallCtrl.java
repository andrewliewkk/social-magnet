package SocialMagnet.Control;

/**
 * WallCtrl
 */

import java.util.*;

import SocialMagnet.Social.Member.*;
import SocialMagnet.Social.Post.*;

public class WallCtrl {
    private MemberDAO memberDAO = new MemberDAO();
    private PostDAO postDAO = new PostDAO();

    public Dictionary getWall(Member user) {
        
        Dictionary threadID = new Hashtable();
        ArrayList<Post> postList = new ArrayList(); // Array of posts retrieved
        
        //HARDCODED VALUES
        postList = postDAO.retrieveWall(user.getUserID());
        
        int index = 0;
        for (Post post:postList) {
            index += 1;
            threadID.put(index, post);
        }
        return threadID;
    }
    
    public void newPost(String posterID, String content, String wall_userID) {
        ArrayList<Member> taggedMembers = new ArrayList<>();
        String newContent = "";
        String[] content_split = content.split(" ");
        
        // Loops each word in the content of the post. 
        // If word starts with @, checks to see if member exist by calling getUser() method
        // If member exist, the @ from the word will be removed and stored in the new String
        ArrayList<Member> friendList = memberDAO.getFriends(posterID);

        for (String st : content_split) {
            if (st.startsWith("@")) {
                String tagName = st.substring(1);
                Member m = memberDAO.getUser(tagName);

                // O(n2) algorithm - check if member is in friend list, if so, add to tagged members
                // can't find a better way to do it now because contains() doesn't seem to work

                for (Member friendToCheck : friendList) {
                    if (m != null && friendToCheck.getFullName().equals(m.getFullName())){
                        st = tagName;
                        taggedMembers.add(m);
                    }
                    
                }

                
            }
            newContent += st + " ";
        }
        
        newContent = newContent.substring(0, newContent.length() - 1); //Remove space char from the back
        postDAO.newPost(posterID, newContent, wall_userID, taggedMembers);

    }


    public int getGoldRank(Member user) {
        String userID = user.getUserID();
        ArrayList<Member> friends = memberDAO.getFriends(userID);
        // treeset to sort gold
        TreeMap<Integer, Integer> goldRank = new TreeMap<>(Collections.reverseOrder());
        Iterator<Member> iter = friends.iterator();
        Integer count = 0;
        while (iter.hasNext()) {
            Member person = iter.next();
            if (goldRank.containsKey(person.getGold())) {
                count = goldRank.get(person.getGold());
                goldRank.put(person.getGold(),++count);
            } else {
                goldRank.put(person.getGold(),1);
            }
            
        }
        Set<Integer> keys = goldRank.keySet();
        int rank = 1;
        Iterator<Integer> order = keys.iterator();
        while (order.hasNext()) {
            int gold = order.next();
            int n = goldRank.get(gold);
            if (user.getGold() >= gold) {
                return rank;
            } else {
                rank += n;
            }
        }
        return rank;
    }
}
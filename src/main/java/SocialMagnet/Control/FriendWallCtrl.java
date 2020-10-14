package SocialMagnet.Control;

/**
 * FriendWallCtrl
 */

import java.util.*;

import SocialMagnet.Social.Member.*;
import SocialMagnet.Social.Post.*;

public class FriendWallCtrl {
    private MemberDAO memberDAO = new MemberDAO();
    private PostDAO postDAO = new PostDAO();

    public Dictionary getFriendWall(Member user) {
        
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

    public HashMap<Member, Boolean> getFriendsAndCommon(Member user, Member friend) {

        // Currently this code creates a list of usernames,
        // and for each friend in friendsList, performs a .equals() check.
        // I tried using .contains() but this seems to perform a == check instead of .equals()
        // If you guys have a better method, pls modify this code!
        ArrayList<Member> userFriendsList = memberDAO.getFriends(user.getUserID());
        ArrayList<Member> friendsList = memberDAO.getFriends(friend.getUserID());

        ArrayList<String> userFriendUsernames = new ArrayList<>();
        for (Member m : userFriendsList) {
            userFriendUsernames.add(m.getFullName());            
        }

        // To store Members, and whether they are common elements in both.
        HashMap<Member, Boolean> map = new HashMap<>();

        for (Member member : friendsList) {
            
            boolean isCommon = false;
            
            // Perform the O(n2) comparison here.
            for (String string : userFriendUsernames) {
                if (string.equals(member.getFullName())) {
                    isCommon = true;
                }
            }

            // Check if the member is the user himself.
            if (! (member.getFullName().equals(user.getFullName()))) {
                map.put(member, isCommon);
            }
        }
        
        return map;

    }

    public void newPost(String posterID, String content, String wall_userID) {
        // This is for the [P]ost function on the friend's wall.

        ArrayList<Member> taggedMembers = new ArrayList<>();
        String newContent = "";
        String[] content_split = content.split(" "); // Splits the content of the message by spaces 

        ArrayList<Member> friendList = memberDAO.getFriends(posterID);
        
        // Loops each word in the content of the post. 
        // If word starts with @, checks to see if member exist by calling getUser() method
        // If member exist, the @ from the word will be removed and stored in the new String

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
}
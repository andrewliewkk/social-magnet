package SocialMagnet.Control;

/**
 * VisitFriendCtrl
 */
import SocialMagnet.Social.Member.*;
import java.util.*;

public class VisitFriendCtrl {
    private MemberDAO memberDAO = new MemberDAO();

    public ArrayList<Member> getFriends(Member user) {
        
        ArrayList<Member> friendList = new ArrayList<>(); // Array of friends retrieved
        friendList = memberDAO.getFriends(user.getUserID());

        return friendList;
    }  
}
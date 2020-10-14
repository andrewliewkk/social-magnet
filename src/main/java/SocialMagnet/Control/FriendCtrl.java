package SocialMagnet.Control;

import java.sql.SQLException;

/**
 * FriendCtrl
 */

import java.util.*;
import SocialMagnet.Exceptions.FriendException;
import SocialMagnet.Social.Member.*;

public class FriendCtrl {
    private MemberDAO memberDAO = new MemberDAO();

    public ArrayList<Member> getFriends(Member user) {   
        ArrayList<Member> friendList = new ArrayList<>(); // Array of friends retrieved
        friendList = memberDAO.getFriends(user.getUserID());

        return friendList;
    }  

    public ArrayList<Member> getRequests(Member user) {
        ArrayList<Member> requestList = new ArrayList<>(); // Array of friend requests retrieved
        requestList = memberDAO.getRequests(user.getUserID());

        return requestList;
    }

    public void sendFriendRequest(String senderID, String receiverID) throws FriendException{
        Member user = memberDAO.getUser(receiverID);
        // Checks to see if userID exist
        if (user == null) {
            throw new FriendException("Username does not exist.");
        }
        // Checks to see if senderID is the same as the ReceiverID 
        if (senderID.equals(receiverID)) {
            throw new FriendException("Cannot send friend request to yourself.");
        }
        // Checks to see if the username given is already a friend
        ArrayList<Member> friendList = memberDAO.getFriends(senderID);
        ArrayList<String> friendListNames = new ArrayList<>();
        for (Member friend : friendList) {
            friendListNames.add(friend.getUserID());
        }
        if (friendListNames.contains(receiverID)) {
            throw new FriendException(receiverID + " is already a friend.");
        }

        // Checks to see if friend request is already made
        ArrayList<Member> requestList = memberDAO.getRequestsMade(senderID);
        ArrayList<String> requestListNames = new ArrayList<>();
        for (Member request : requestList) {
            requestListNames.add(request.getUserID());
        }
        if (requestListNames.contains(receiverID)) {
            throw new FriendException("Friend request has already been sent to " + receiverID + ".");
        }

        memberDAO.sendFriendRequest(senderID, receiverID);
        
    }
    
    public void acceptFriendRequest(String userID, String friendID) throws SQLException {
        memberDAO.acceptFriendRequest(userID, friendID);

    }
    
    public void rejectFriendRequest(String senderID, String receiverID) throws SQLException {
        // This is for the [R]eject function.
        // The person who is performing the accepting is the receiver.
        memberDAO.rejectFriendRequest(senderID, receiverID);
    }

    public void unFriend(String userID, String friendID) throws SQLException{
        // The person performing the unfriend action is the sender.
        memberDAO.unFriend(userID, friendID);
    }

}
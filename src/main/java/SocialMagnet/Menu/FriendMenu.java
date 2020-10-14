package SocialMagnet.Menu;

import java.sql.SQLException;

/**
 * FriendMenu
 */

import java.util.*;

import SocialMagnet.Control.FriendCtrl;
import SocialMagnet.Exceptions.FriendException;
import SocialMagnet.Social.Member.Member;
import SocialMagnet.Menu.FriendWallMenu;

public class FriendMenu {
    private FriendCtrl ctrl = new FriendCtrl();
    private FriendWallMenu friendWallMenu = new FriendWallMenu();
    
    public void displayFriends(Member user) {
        String choice;
        do {
            System.out.println();
            System.out.println("== Social Magnet :: My Friends ==");
            System.out.println("Welcome, " + user.getFullName());
            System.out.println();
            System.out.println("My Friends:");

            // Retrieve friends and display here
            Map<Integer, Member> friendMap = new HashMap<>();
            ArrayList<Member> friendList = ctrl.getFriends(user);
            
            int index = 0;
            for (Member friend:friendList) {
                index += 1;
                friendMap.put(index, friend);
                System.out.println(index + ". " + friend.getUserID());
            }

            System.out.println();
            System.out.println("My Requests:");

            // Retrieve requests and display here in a Map which can be used to retrieve index of friend selected
            Map<Integer, Member> requestMap = new HashMap<>();
            ArrayList<Member> requestList = ctrl.getRequests(user);

            for (Member request : requestList) {
                index += 1;
                requestMap.put(index, request);
                System.out.println(index + ". " + request.getUserID());
            }

            Scanner sc = new Scanner(System.in);
            System.out.print("[M]ain | [U]nfriend | re[Q]uest | [A]ccept | [R]eject | [V]iew > ");
            choice = sc.nextLine();
            String choicePath = choice.substring(0,1);
            int choiceNum = 0;
            if (choicePath.equals("U") || choicePath.equals("A") || choicePath.equals("R") || choicePath.equals("V")) {
                try {
                    choiceNum = Integer.parseInt(choice.substring(1));
                    choice = choicePath;
                } catch (Exception e) {
                    choice = "invalid";
                }
            }

            Member memberSelected = null;
            if (choicePath.equals("A") || choicePath.equals("R")) {
                memberSelected = (Member)requestMap.get(choiceNum);
                if (memberSelected == null) {
                    choice = "invalid";
                }
            } else if (choicePath.equals("U") || choicePath.equals("V")) {
                memberSelected = (Member)friendMap.get(choiceNum);
                if (memberSelected == null) {
                    choice = "invalid";
                }
            }

            switch (choice) {
                case "M":
                    break;
                case "U":
                    unFriend(user, memberSelected);
                    break;
                case "Q":
                    sendFriendRequest(user.getUserID());
                    break;
                case "A":
                    acceptFriendRequest(user, memberSelected);
                    break;
                case "R":
                    rejectFriendRequest(user, memberSelected);
                    break;
                case "V":
                    friendWallMenu.displayFriendWall(user, memberSelected);
                    break;
                default:
                    System.out.println("Please enter a valid choice!");
            }
        } while (!choice.equals("M") && !choice.equals("V"));
    }

    public void sendFriendRequest(String senderID) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the username > ");
        String userID = sc.nextLine();

        try {
            ctrl.sendFriendRequest(senderID, userID);
        } catch (FriendException e) {
            System.out.println(e.getMessage());
        }
    }

    public void acceptFriendRequest(Member user, Member memberSelected) {
        String userID = user.getUserID();
        String friendID = memberSelected.getUserID();
        try {
            ctrl.acceptFriendRequest(userID, friendID);
            System.out.println(friendID +" is now your friend.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void rejectFriendRequest(Member user, Member memberSelected) {
        String receiverID = user.getUserID();
        String senderID = memberSelected.getUserID();
        try {
            ctrl.rejectFriendRequest(senderID, receiverID);
            System.out.println(senderID + " friend request has been rejected.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void unFriend(Member user, Member memberSelected) {
        String userID = user.getUserID();
        String friendID = memberSelected.getUserID();
        try {
            ctrl.unFriend(userID, friendID);
            System.out.println(friendID + " has been removed from your circle of friends.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    
}
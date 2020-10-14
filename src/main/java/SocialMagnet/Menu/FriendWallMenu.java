package SocialMagnet.Menu;

/**
 * FriendWallMenu
 */

import java.util.*;

import SocialMagnet.Control.FriendWallCtrl;
import SocialMagnet.Social.Member.Member;
import SocialMagnet.Menu.ThreadMenu;
import SocialMagnet.Social.Post.Post;
import SocialMagnet.Utility.*;

public class FriendWallMenu {
    private FriendWallCtrl ctrl = new FriendWallCtrl();
    private ThreadMenu threadMenu = new ThreadMenu();
    private MenuUtility menuUtility = new MenuUtility();

    public void displayFriendWall(Member user, Member friend) {
        String choice;
        do {
            System.out.println();
            System.out.println("== Social Magnet :: " + friend.getUserID() + "'s Wall ==");
            System.out.println("Welcome, " + user.getFullName());
            System.out.println();
            System.out.println("About " + friend.getUserID());
            System.out.println("Full Name: " + friend.getFullName());
            //System.out.println("Farmer and gold");

            // Retrieve wall and display here
            Dictionary threadID = ctrl.getFriendWall(friend);
            menuUtility.displayPosts(threadID);

            // Retrieve friends and display here
            System.out.println(friend.getUserID() + "'s Friend");
            HashMap<Member, Boolean> friendsAndCommon = ctrl.getFriendsAndCommon(user, friend);
            menuUtility.displayFriends(friendsAndCommon);

            System.out.println();
            Scanner sc = new Scanner(System.in);
            System.out.print("[M]ain | [T]hread | [P]ost > ");
            choice = sc.nextLine();
            Post threadPost = null;
            int thread = 0;
            if ((choice.substring(0,1)).equals("T")) {
                try {
                    thread = Integer.parseInt(choice.substring(1));
                    threadPost = (Post)threadID.get(thread);
                    choice = choice.substring(0,1);
                    if (threadPost == null) {
                        choice = "invalid";
                    } 
                } catch (Exception e) {
                    choice = "invalid";
                }
            }

            switch (choice) {
                case "M":
                    break;
                case "T":
                    threadMenu.displayThread(threadPost, thread, user);
                    break;
                case "P":
                    newPost(user, friend);
                    break;
                default:
                    System.out.println("Please enter a valid choice!");
            }
        } while (!choice.equals("M") && !choice.equals("T"));
    }

    public void newPost(Member user, Member friend) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your message > ");
        String message = sc.nextLine();
        ctrl.newPost(user.getUserID(), message, friend.getUserID());
        System.out.println("Message successfully posted.");
        
    }



}
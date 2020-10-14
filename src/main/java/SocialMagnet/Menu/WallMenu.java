package SocialMagnet.Menu;
/**
 * WallMenu
 */

import java.util.*;

import SocialMagnet.Control.WallCtrl;
import SocialMagnet.Exceptions.GiftException;
import SocialMagnet.Control.GiftCtrl;
import SocialMagnet.Social.Member.Member;
import SocialMagnet.Menu.ThreadMenu;
import SocialMagnet.Menu.MenuUtility;
import SocialMagnet.Social.Post.Post;
import SocialMagnet.Utility.*;
import java.text.*;
import java.awt.font.TextAttribute;

public class WallMenu {
    private WallCtrl ctrl = new WallCtrl();
    private GiftCtrl giftCtrl = new GiftCtrl();
    private ThreadMenu threadMenu = new ThreadMenu();
    private MenuUtility menuUtility = new MenuUtility();

    public void displayWall(Member user) {
        String choice;
        do {
            System.out.println();
            System.out.println("== Social Magnet :: My Wall ==");
            System.out.println("About " + user.getUserID());
            System.out.println("Full Name: " + user.getFullName());
            String rank = user.getRank();
            int goldRank = ctrl.getGoldRank(user);
            String abbrev = Nth.ordinalAbbrev(goldRank);
            String first = rank + ", " + goldRank;
            String last = " richest";
            String middle = abbrev;
            // to convert middle to superscript
            System.out.println(first + middle + last);
            System.out.println();

            // Retrieve wall and display here
            Dictionary threadID = ctrl.getWall(user);

            if (threadID.isEmpty()) {
                System.out.println("No posts found.");
            }

            menuUtility.displayPosts(threadID);

            System.out.println();
            Scanner sc = new Scanner(System.in);
            System.out.print("[M]ain | [T]hread | [A]ccept Gift | [P]ost > ");
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
                    displayThread(threadPost, thread, user);
                    break;
                case "A":
                    acceptGifts(user);                    
                    break;
                case "P":
                    newPost(user);
                    break;
                default:
                    System.out.println("Please enter a valid choice!");
            }
        } while (!choice.equals("M") && !choice.equals("T"));
    }

    public void acceptGifts(Member user) {
        try {
            giftCtrl.acceptGifts(user);
            System.out.println("Gifts accepted.");
        } catch (GiftException e) {
            System.out.println(e.getMessage());
        }
    }

    public void newPost(Member user) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your message > ");
        String message = sc.nextLine();
        ctrl.newPost(user.getUserID(), message, user.getUserID());
        System.out.println("Message successfully posted.");
        
    }

    public void displayThread(Post post, int index, Member user) {
        threadMenu.displayThread(post, index, user);
    }
}
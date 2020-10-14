package SocialMagnet.Menu;

/**
 * NewsFeedMenu
 */

import java.util.*;

import SocialMagnet.Control.NewsFeedCtrl;
import SocialMagnet.Social.Member.Member;
import SocialMagnet.Menu.ThreadMenu;
import SocialMagnet.Menu.MenuUtility;
import SocialMagnet.Social.Post.Post;

public class NewsFeedMenu {
    private NewsFeedCtrl ctrl = new NewsFeedCtrl();
    private ThreadMenu threadMenu = new ThreadMenu();
    private MenuUtility menuUtility = new MenuUtility();
  
    // prints out the News Feed to the console
    public void displayNewsFeed(Member user) {
        String choice;
        do {
            System.out.println();
            System.out.println("== Social Magnet :: News Feed ==");

            // Retrieve NewsFeed posts
            Dictionary threadID = ctrl.getNewsFeed(user);

            if (threadID.isEmpty()) {
                System.out.println("No posts found.");
            }

            menuUtility.displayPosts(threadID);

            System.out.println();

            Scanner sc = new Scanner(System.in);
            System.out.print("[M]ain | [T]hread > ");
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
                default:
                    choice = "invalid";
                    System.out.println("Please enter a valid choice!");
            }
        } while (choice.equals("invalid"));
    }

}
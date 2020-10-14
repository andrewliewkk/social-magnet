package SocialMagnet.Menu;

/**
 * MainMenu
 */

import java.util.*;

import SocialMagnet.Social.Member.Member;
import SocialMagnet.Menu.*;

public class MainMenu {
    MenuUtility menuUtility = new MenuUtility();
    NewsFeedMenu newsFeedMenu = new NewsFeedMenu();
    WallMenu wallMenu = new WallMenu();
    FriendMenu friendMenu = new FriendMenu();
    FarmMenu farmMenu = new FarmMenu();

    // prints out the main page to the console
    public void displayMain(Member user) {
        int choice;
        do {
            System.out.println();
            System.out.println("== Social Magnet :: Main Menu ==");
            System.out.println("Welcome, " + user.getFullName() + "!");
            System.out.println("1. News Feed");
            System.out.println("2. My Wall");
            System.out.println("3. My Friends");
            System.out.println("4. City Farmers");
            System.out.println("5. Logout");
            System.out.print("Enter your choice > ");
            choice = menuUtility.checkInput();
            switch (choice) {
                case 1:
                    newsFeedMenu.displayNewsFeed(user);
                    break;
                case 2:
                    wallMenu.displayWall(user);
                    break;
                case 3:
                    friendMenu.displayFriends(user);
                    break;
                case 4:
                    farmMenu.displayFarm(user);
                    break;
                case 5:
                    System.out.println("Logging out...");
                    break;    
                default:
                    System.out.println("Please enter a choice between 1 & 5!");
            }
        } while (choice != 5);
    }
}
package SocialMagnet.Menu;

import java.util.*;
import SocialMagnet.Control.VisitFriendCtrl;
import SocialMagnet.Social.Member.Member;
import SocialMagnet.Menu.MenuUtility;

/**
 * VisitFriendMenu
 */
public class VisitFriendMenu {
    private MenuUtility menuUtility = new MenuUtility();
    private VisitFriendCtrl ctrl = new VisitFriendCtrl();
    private FriendFarmMenu friendFarmMenu = new FriendFarmMenu();

    public boolean displayVisitFriends(Member user) {
        String choice;
        do {
            menuUtility.displayCityFarmersHeader("Visit Friend", user);

            // To remember the indexes of friends displayed
            Map<Integer, Member> friendMap = new HashMap<>();
            ArrayList<Member> friendList = ctrl.getFriends(user);

            // Display Friends
            int index = 0;
            for (Member friend : friendList) {
                index += 1;
                friendMap.put(index, friend);
                System.out.println(index + ". " + friend.getUserID());
            }
            
            System.out.println();
            System.out.print("[M]ain | [C]ity Farmer Main | Select choice > ");

            Scanner sc = new Scanner(System.in);
            choice = sc.nextLine();
            int choiceNum = 0;

            if (!choice.equals("M") && !choice.equals("C")) {
                try {
                    choiceNum = Integer.parseInt(choice);
                    if (friendMap.get(choiceNum) == null) {
                        choice = "invalid";
                    } else {
                        Member friend = friendMap.get(choiceNum);
                        friendFarmMenu.displayFriendFarm(user, friend);
                        choice = "C";
                        break;
                    }
                } catch (Exception e)  {
                    choice = "invalid";
                }
            }

            System.out.println("Choice on visitor's menu: " + choice);
            switch (choice) {
                case "M":
                    return true;
                case "C":
                    break;
                default:
                    System.out.println("Please enter a valid choice!");
            }
        } while (!choice.equals("M") && !choice.equals("C"));
        return false;
    }
}
package SocialMagnet.Menu;

/**
 * FarmMenu
 */

import java.util.*;

//import SocialMagnet.Control.FarmCtrl;
import SocialMagnet.Menu.*;
import SocialMagnet.Social.Member.Member;

public class FarmMenu {
    //private FarmCtrl ctrl = new FarmCtrl();
    private MenuUtility menuUtility = new MenuUtility();
    private FarmlandMenu farmLandMenu = new FarmlandMenu();
    private StoreMenu storeMenu = new StoreMenu();
    private InventoryMenu inventoryMenu = new InventoryMenu();
    private VisitFriendMenu visitFriendMenu = new VisitFriendMenu();
    private GiftMenu giftMenu = new GiftMenu();

    public void displayFarm(Member user) {
        String choice;
        boolean goToMain = false;
        do {
            menuUtility.displayCityFarmersHeader("", user);
            System.out.println("1. My Farmland");
            System.out.println("2. My Store");
            System.out.println("3. My Inventory");
            System.out.println("4. Visit Friend");
            System.out.println("5. Send Gift");
            System.out.print("[M]ain | Enter your choice > ");
            Scanner sc = new Scanner(System.in);
            choice = sc.next();          

            switch (choice) {
                case "M":
                    break;
                case "1":
                    goToMain = farmLandMenu.displayFarmland(user);
                    break;
                case "2":
                    goToMain = storeMenu.displayStore(user);
                    break;
                case "3":
                    goToMain = inventoryMenu.displayInventory(user);
                    break;
                case "4":
                    goToMain = visitFriendMenu.displayVisitFriends(user);
                    break;
                case "5":
                    goToMain = giftMenu.displayGift(user);
                    break;
                default:
                    System.out.println("Please enter a valid choice!");
            }
        } while (!choice.equals("M") && !goToMain);
    }
}
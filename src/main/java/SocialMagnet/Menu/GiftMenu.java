package SocialMagnet.Menu;

/**
 * GiftMenu
 */

import java.util.*;

import SocialMagnet.Control.GiftCtrl;
import SocialMagnet.Control.StoreCtrl;
import SocialMagnet.Exceptions.GiftException;
import SocialMagnet.Social.Member.*;
import SocialMagnet.Menu.ThreadMenu;
import SocialMagnet.Social.Post.*;
import SocialMagnet.Utility.*;
import SocialMagnet.Farm.*;

public class GiftMenu {
    private GiftCtrl giftCtrl = new GiftCtrl();
    private StoreCtrl storeCtrl = new StoreCtrl();
    private ThreadMenu threadMenu = new ThreadMenu();
    private MenuUtility menuUtility = new MenuUtility();

    public boolean displayGift(Member user) {
        String choice;

        do {
            menuUtility.displayCityFarmersHeader("Send a Gift", user);

            // Get the inventory of seeds
            ArrayList<Crop> seedList = storeCtrl.getSeedList();
            Map<Integer, Crop> seedDict = new HashMap<>();

            // Checks to see if there are seeds in the database
            int index = 0;
            if (seedList.isEmpty()) {
                System.out.println("No seeds available.");
            } else {
                System.out.println("Gifts Available:");
                for (Crop seed : seedList) {
                    index += 1;
                    System.out.println(index + ". 1 Bag of " + seed.getName() + " Seeds");
                    seedDict.put(index, seed);
                }
            }

            System.out.println();
            Scanner sc = new Scanner(System.in);
            System.out.print("[R]eturn to main | Select choice > ");
            choice = null;
            Crop seed = null;
            int seedChoice = 0;
            
            if (sc.hasNextInt()) {
                choice = "GIVE";
                seedChoice = sc.nextInt();
                seed = (Crop)seedDict.get(seedChoice);
            } else {
                choice = sc.next();
                if (choice.equals("GIVE")) {
                    choice = "invalid";
                }
            }  

            switch (choice) {
                case "R":
                    break;
                case "GIVE":
                    sendToFriends(user, seed);
                    break;
                default:
                    System.out.println("Please enter a valid choice!");
            }
        } while (!choice.equals("R"));
        return false;
    }

    public void sendToFriends(Member sender, Crop seed) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Send to> ");
        String receivers = sc.next();
        try {
            giftCtrl.sendToFriends(sender, receivers, seed);
            System.out.println("Gift posted to friends' wall.");
        } catch (GiftException e){
            System.out.println(e.getMessage());
        }
    }
}
package SocialMagnet.Menu;

/**
 * InventoryMenu
 */

import java.util.*;

import SocialMagnet.Control.InventoryCtrl;
import SocialMagnet.Menu.MenuUtility;
import SocialMagnet.Social.Member.Member;
import SocialMagnet.Farm.Crop;
import SocialMagnet.Farm.Inventory;

public class InventoryMenu {
    private InventoryCtrl ctrl = new InventoryCtrl();
    private MenuUtility menuUtility = new MenuUtility();   

    public boolean displayInventory(Member user) {
        String choice;
        do {
            menuUtility.displayCityFarmersHeader("My Inventory", user);

            // Display Seeds here
            ArrayList<Inventory> seedList = ctrl.getInvSeedList(user);

            int index = 0;
            if (seedList.isEmpty()) {
                System.out.println("No seeds available.");
            } else {
                System.out.println("My Seeds:");
                for (Inventory seed:seedList) {
                    index += 1;
                    System.out.println(index + ". " + seed.getQuantity() + " Bags of " + seed.getCropName());
                }
            }
            
            
            System.out.println();
            System.out.print("[M]ain | City [F]armers | Select choice > ");
            Scanner sc = new Scanner(System.in);
            choice = sc.next();

            switch (choice) {
                case "M":
                    return true;
                case "F":
                    break;
                default:
                    System.out.println("Please enter a valid choice!");
            }
        } while (!choice.equals("M") && !choice.equals("F"));
        return false;
    }
}
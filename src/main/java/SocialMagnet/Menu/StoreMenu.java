package SocialMagnet.Menu;

/**
 * StoreMenu
 */

import java.util.*;

import SocialMagnet.Control.StoreCtrl;
import SocialMagnet.Menu.MenuUtility;
import SocialMagnet.Social.Member.Member;
import SocialMagnet.Farm.Crop;
import SocialMagnet.Exceptions.StoreException;

public class StoreMenu {
    private StoreCtrl ctrl = new StoreCtrl();
    private MenuUtility menuUtility = new MenuUtility();   

    public boolean displayStore(Member user) {
        String choice;
        do {
            menuUtility.displayCityFarmersHeader("My Store", user);

            System.out.println("Seeds Available:");

            // Display Seeds here
            Map<Integer, Crop> seedDict = new HashMap<>();
            ArrayList<Crop> seedList = ctrl.getSeedList();

            int index = 0;
            for (Crop seed:seedList) {
                index += 1;
                seedDict.put(index, seed);
                String seedName = seed.getName();
                int spaces = 12 - seedName.length();
                String whitespace = " ".repeat(spaces);
                System.out.println(index + ". " + seedName + whitespace + "- " + seed.getCost() + " gold");
                System.out.println("   Harvest in: " + seed.getMaturityTime() + " mins");
                System.out.println("   XP Gained: " + seed.getXP());
            }
            
            System.out.println();
            System.out.print("[M]ain | City [F]armers | Select choice > ");
            Scanner sc = new Scanner(System.in);
            int seedChoice = 0;
            Crop seed = null;
            if (sc.hasNextInt()) {
                choice = "BUY";
                seedChoice = sc.nextInt();
                seed = (Crop)seedDict.get(seedChoice);
            } else {
                choice = sc.next();
                if (choice.equals("BUY")) {
                    choice = "invalid";
                }
            }  

            switch (choice) {
                case "M":
                    return true;
                case "F":
                    break;
                case "BUY":
                    System.out.print("Enter quantity > ");
                    int quantity;
                    try {
                        quantity = sc.nextInt();
                        ctrl.buySeed(user, seed, quantity);
                        int cost = quantity * seed.getCost();
                        System.out.println(quantity + " bags of seeds purchased for " + cost + " gold.");
                    } catch (InputMismatchException e) {
                        System.out.println("Please enter a valid quantity!");
                    } catch (StoreException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Please enter a valid choice!");
            }
        } while (!choice.equals("M") && !choice.equals("F"));
        return false;
    }
}
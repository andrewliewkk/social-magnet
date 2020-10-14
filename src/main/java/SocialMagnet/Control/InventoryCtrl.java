package SocialMagnet.Control;

/**
 * InventoryCtrl
 */

import java.util.*;

import SocialMagnet.Social.Member.*;
import SocialMagnet.Farm.*;

public class InventoryCtrl {
    private InventoryDAO inventoryDAO = new InventoryDAO();

    public ArrayList<Inventory> getInvSeedList(Member user) {
        ArrayList<Inventory> seedList = new ArrayList<>(); // Array of seeds retrieved
        seedList = inventoryDAO.retrieveInventory(user.getUserID());

        return seedList;
    }  
}
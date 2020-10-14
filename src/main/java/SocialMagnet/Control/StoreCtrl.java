package SocialMagnet.Control;

/**
 * StoreCtrl
 */

import java.util.*;

import SocialMagnet.Social.Member.*;
import SocialMagnet.Farm.*;
import SocialMagnet.Exceptions.StoreException;

public class StoreCtrl {
    private CropDAO cropDAO = new CropDAO();
    private InventoryDAO inventoryDAO = new InventoryDAO();
    private MemberDAO memberDAO = new MemberDAO();

    public ArrayList<Crop> getSeedList() {
        ArrayList<Crop> seedList = new ArrayList<>(); // Array of seeds retrieved
        seedList = cropDAO.getAllCrops();

        return seedList;
    }  

    public void buySeed(Member user, Crop seed, int quantity) throws StoreException{
        int gold = user.getGold();
        int totalCost = quantity * seed.getCost();

        if (gold < totalCost) {
            throw new StoreException("Not enough gold!");
        }else if (quantity <= 0) {
            throw new StoreException("Invalid quantity!");
        }else {
            int newGold = gold - totalCost;
            user.setGold(newGold);
            String userID = user.getUserID();
            memberDAO.setGold(newGold, userID);
            inventoryDAO.buySeed(userID, seed.getName(), quantity);
        }
    }
}
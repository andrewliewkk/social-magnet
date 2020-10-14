package SocialMagnet.Control;

/**
 * Test Store
 */

import org.junit.jupiter.api.*;
import java.util.*;

import SocialMagnet.Social.Member.*;
import SocialMagnet.Farm.*;
import SocialMagnet.Exceptions.StoreException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestStore {
    private MemberDAO memberDAO = new MemberDAO();
    private StoreCtrl ctrl = new StoreCtrl();
    private CropDAO cropDAO = new CropDAO();
    private InventoryCtrl inventoryCtrl = new InventoryCtrl();
    
    @BeforeAll
    public void resetUser() {
        Member testUser = memberDAO.getUser("tester");
        if (testUser != null) {
            memberDAO.deleteUser("tester");
        }
        memberDAO.addUser("tester", "password", "Tester");
    }

    @Test
    public void testBuySeed() {
        Member testUser = memberDAO.getUser("tester");
        testUser.setGold(40);
        Crop seed = cropDAO.retrieveCropByName("Papaya");
        try {
            ctrl.buySeed(testUser, seed, 2);
        } catch (Exception e) {
            
        }
        ArrayList<Inventory> inv = inventoryCtrl.getInvSeedList(testUser);

        int amt = 0;
        for (Inventory userInv:inv) {
            if (userInv.getCropName().equals("Papaya")) {
                amt = userInv.getQuantity();
            }
        }

        Assertions.assertEquals(2, amt);
    }

    @Test
    public void testBuySeedNoGold() {
        Member testUser = memberDAO.getUser("tester");
        testUser.setGold(0);
        Crop seed = cropDAO.retrieveCropByName("Papaya");

        Exception e = Assertions.assertThrows(StoreException.class, () ->
            ctrl.buySeed(testUser, seed, 2));

        Assertions.assertEquals("Not enough gold!", e.getMessage());
    }

    @Test
    public void testBuySeedInvalidQty() {
        Member testUser = memberDAO.getUser("tester");
        testUser.setGold(50);
        Crop seed = cropDAO.retrieveCropByName("Papaya");

        Exception e = Assertions.assertThrows(StoreException.class, () ->
            ctrl.buySeed(testUser, seed, -1));

        Assertions.assertEquals("Invalid quantity!", e.getMessage());
    }

    @AfterAll
    public void removeUser() {
        Member testUser = memberDAO.getUser("tester");
        if (testUser != null) {
            memberDAO.deleteUser("tester");
        }
    }

}
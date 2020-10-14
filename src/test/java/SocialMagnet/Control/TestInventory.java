package SocialMagnet.Control;

/**
 * Test Inventory
 */

import org.junit.jupiter.api.*;
import java.util.*;

import SocialMagnet.Social.Member.*;
import SocialMagnet.Farm.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestInventory {
    private MemberDAO memberDAO = new MemberDAO();
    private InventoryCtrl ctrl = new InventoryCtrl();
    
    // @BeforeAll
    // public void resetUser() {
    //     Member testUser = memberDAO.getUser("tester");
    //     if (testUser != null) {
    //         memberDAO.deleteUser("tester");
    //     }
    //     memberDAO.addUser("tester", "password", "Tester");
    // }

    @Test
    public void testInventoryCheckPapaya() {
        Member testUser = memberDAO.getUser("apple");
        ArrayList<Inventory> inv = ctrl.getInvSeedList(testUser);

        int amt = 0;
        for (Inventory userInv:inv) {
            if (userInv.getCropName().equals("Papaya")) {
                amt = userInv.getQuantity();
            }
        }

        Assertions.assertEquals(5, amt);
    }
    @Test
    public void testInventoryCheckPumpkin() {
        Member testUser = memberDAO.getUser("apple");
        ArrayList<Inventory> inv = ctrl.getInvSeedList(testUser);

        int amt = 0;
        for (Inventory userInv:inv) {
            if (userInv.getCropName().equals("Pumpkin")) {
                amt = userInv.getQuantity();
            }
        }

        Assertions.assertEquals(5, amt);
    }

    @Test
    public void testInventoryCheckSunflower() {
        Member testUser = memberDAO.getUser("apple");
        ArrayList<Inventory> inv = ctrl.getInvSeedList(testUser);

        int amt = 0;
        for (Inventory userInv:inv) {
            if (userInv.getCropName().equals("Sunflower")) {
                amt = userInv.getQuantity();
            }
        }

        Assertions.assertEquals(4, amt);
    }

    @Test
    public void testInventoryCheckWatermelon() {
        Member testUser = memberDAO.getUser("apple");
        ArrayList<Inventory> inv = ctrl.getInvSeedList(testUser);

        int amt = 0;
        for (Inventory userInv:inv) {
            if (userInv.getCropName().equals("Watermelon")) {
                amt = userInv.getQuantity();
            }
        }

        Assertions.assertEquals(3, amt);
    }


    // @AfterAll
    // public void removeUser() {
    //     Member testUser = memberDAO.getUser("tester");
    //     if (testUser != null) {
    //         memberDAO.deleteUser("tester");
    //     }
    // }

}
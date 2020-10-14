package SocialMagnet.Control;

/**
 * Test Gift
 */

import org.junit.jupiter.api.*;
import java.util.*;
import java.sql.SQLException;

import SocialMagnet.Social.Member.*;
import SocialMagnet.Exceptions.FriendException;
import SocialMagnet.Farm.*;
import SocialMagnet.Control.GiftCtrl;
import SocialMagnet.Exceptions.GiftException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestGift {
    private MemberDAO memberDAO = new MemberDAO();
    private CropDAO cropDAO = new CropDAO();
    private InventoryDAO invDAO = new InventoryDAO();
    private GiftCtrl giftCtrl = new GiftCtrl();
    private FriendCtrl friendCtrl = new FriendCtrl(); 

    // Member Information of 8 members for testing
    String[][] testMembers = {  {"tester", "password", "Tester"} ,

                                // friends of tester
                                {"tester2", "password", "Tester2"} ,
                                {"tester3", "password", "Tester3"} ,
                                {"tester4", "password", "Tester4"} ,
                                {"tester5", "password", "Tester5"} ,
                                {"tester6", "password", "Tester6"} ,
                                {"tester7", "password", "Tester7"} ,

                                // Not a friend of tester
                                {"testerNF", "password", "TesterNF"} ,                
                            };

    // Use papaya as test crop
    Crop testPapaya = cropDAO.retrieveCropByName("Papaya");

    @BeforeAll
    public void resetUser() {
        
        // Populate members using memberDAO
        for (int index = 0; index < testMembers.length; index ++ ) {
            String[] memberInfo = testMembers[index];
            if (memberDAO.getUser(memberInfo[0]) != null) {
                memberDAO.deleteUser(memberInfo[0]);
            }
            memberDAO.addUser(memberInfo[0], memberInfo[1],
                    memberInfo[2]);
        } 

        // Make tester friends with everyone except TesterNF
        for (int index = 1; index < testMembers.length - 1; index ++ ) {
            String[] memberInfo = testMembers[index];
            friendCtrl.sendFriendRequest("tester", memberInfo[0]);

            try {
                friendCtrl.acceptFriendRequest("tester", memberInfo[0]);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        // Make tester7 friends with everyone except TesterNF
        for (int index = 1; index < testMembers.length - 2; index ++ ) {
            String[] memberInfo = testMembers[index];
            friendCtrl.sendFriendRequest("tester7", memberInfo[0]);

            try {
                friendCtrl.acceptFriendRequest("tester7", memberInfo[0]);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean sendAndAcceptGift(String testerID, String targetID) throws GiftException {
        Member testUser = memberDAO.getUser(testerID);
        Member testUserTarget = memberDAO.getUser(targetID);
        try {
            giftCtrl.sendToFriends(testUser, targetID, testPapaya);
        } catch (GiftException e) {
            throw new GiftException(e.getMessage());
        }

        // Prepare to check if inventory was updated
        ArrayList<Inventory> invList = invDAO.retrieveInventory(targetID);
        int origAmount = 0;
        if (invList != null && invList.size() != 0)  {
            origAmount = invList.get(0).getQuantity();
        }

        try {
            giftCtrl.acceptGifts(testUserTarget);
        } catch (GiftException e) {
            throw new GiftException(e.getMessage());
        }

        ArrayList<Inventory> newInvList = invDAO.retrieveInventory(targetID);
        int newAmount = newInvList.get(0).getQuantity();

        if (newInvList == null || newInvList.size() == 0) {
            return false;
        } else {
            return newAmount == origAmount + 1;
        }

    }

    @Test
    public void testBaseFunction() {
        try {
            Assertions.assertEquals(true, sendAndAcceptGift("tester", "tester2"));
        } catch (GiftException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testSendToSelf() {
        Exception e = Assertions.assertThrows(GiftException.class, () ->
            sendAndAcceptGift("tester", "tester"));
        Assertions.assertEquals("Cannot send gift to yourself!", e.getMessage());
    }

    @Test
    public void testSendToSixFriends() {
        // First, use tester7 to send to 5 friends
        for (int index = 0; index <= 5; index ++) {
            try {
                String targetMemberID = testMembers[index][0];
                Assertions.assertEquals(true, sendAndAcceptGift("tester7", targetMemberID));
            } catch (GiftException e) {
                System.out.println(e.getMessage());
            }
        }

        // Try to send to 6th friend
        Exception e = Assertions.assertThrows(GiftException.class, () ->
            sendAndAcceptGift("tester7", "tester"));
        Assertions.assertEquals("Cannot send more than 5 gifts a day!", e.getMessage());
    }

    @Test
    public void testSendToStranger() {
        Exception e = Assertions.assertThrows(GiftException.class, () ->
            sendAndAcceptGift("tester", "testerNF"));
        Assertions.assertEquals("Your input(s) did not match any of your friends.", e.getMessage());
    }

    @Test
    public void testInvalidInputWithStranger() {
        Exception e = Assertions.assertThrows(GiftException.class, () ->
            sendAndAcceptGift("tester", "tester3,testerNF"));
        Assertions.assertEquals("Your input(s) did not match any of your friends.", e.getMessage());
    }

    @Test
    public void testInvalidInput() {
        Exception e = Assertions.assertThrows(GiftException.class, () ->
            sendAndAcceptGift("tester", "donaldTrump,!@#"));
        Assertions.assertEquals("Your input(s) did not match any of your friends.", e.getMessage());
    }

    @Test
    public void testMultipleAccept() {
        Member testUser = memberDAO.getUser("tester");
        Member testUser5 = memberDAO.getUser("tester5");

        // Tester sends gift to tester5
        try {
            giftCtrl.sendToFriends(testUser, "tester5", testPapaya);
        } catch (GiftException e) {
            System.out.println(e.getMessage());
        }

        // tester5 tries to accept gift once
        try {
            giftCtrl.acceptGifts(testUser5);
        } catch (GiftException e) {
            System.out.println(e.getMessage());
        }

        // tester5 tries to accept gifts again
        Exception e = Assertions.assertThrows(GiftException.class, () ->
            giftCtrl.acceptGifts(testUser5));
        Assertions.assertEquals("You have claimed all your gifts.", e.getMessage());
    }

    @AfterAll
    public void removeUser() {

        for (String[] memberInfo : testMembers) {
            String testID = memberInfo[0];
            Member testUser = memberDAO.getUser(testID);
            if (testUser != null) {
                memberDAO.deleteUser(testID);
            } 
        }
  
    }


}
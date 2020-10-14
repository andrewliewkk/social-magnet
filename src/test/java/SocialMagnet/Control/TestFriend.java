package SocialMagnet.Control;

/**
 * Test Friend
 */

import org.junit.jupiter.api.*;
import java.util.*;

import SocialMagnet.Social.Member.*;
import SocialMagnet.Exceptions.FriendException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestFriend {
    private MemberDAO memberDAO = new MemberDAO();
    private FriendCtrl ctrl = new FriendCtrl();
    
    @BeforeAll
    public void resetUser() {
        Member testUser = memberDAO.getUser("tester");
        if (testUser != null) {
            memberDAO.deleteUser("tester");
        }
        memberDAO.addUser("tester", "password", "Tester");

        Member testUser2 = memberDAO.getUser("tester2");
        if (testUser2 != null) {
            memberDAO.deleteUser("tester2");
        }
        memberDAO.addUser("tester2", "password", "Tester2");

        Member testUser3 = memberDAO.getUser("tester3");
        if (testUser3 != null) {
            memberDAO.deleteUser("tester3");
        }
    }

    @AfterEach
    public void removeRequest() {
        try {
            ctrl.rejectFriendRequest("tester", "tester2");
        } catch (Exception e){

        }
        try {
            ctrl.unFriend("tester", "tester2");
        } catch (Exception e){

        }
    }

    @Test
    public void testSendRequest() {
        ctrl.sendFriendRequest("tester", "tester2");
        Member testUser2 = memberDAO.getUser("tester2");
        ArrayList<Member> reqList = ctrl.getRequests(testUser2);
        Assertions.assertEquals("tester", reqList.get(0).getUserID());
    }

    @Test
    public void testInvalidUser() {
        this.testSendRequest();
        Exception e = Assertions.assertThrows(FriendException.class, () ->
            ctrl.sendFriendRequest("tester", "tester3"));
        Assertions.assertEquals("Username does not exist.", e.getMessage());
    }

    @Test
    public void testRepeat() {
        this.testSendRequest();
        Exception e = Assertions.assertThrows(FriendException.class, () ->
            ctrl.sendFriendRequest("tester", "tester2"));
        Assertions.assertEquals("Friend request has already been sent to tester2.", e.getMessage());
    }

    @Test
    public void testYourself() {
        this.testSendRequest();
        Exception e = Assertions.assertThrows(FriendException.class, () ->
            ctrl.sendFriendRequest("tester", "tester"));
        Assertions.assertEquals("Cannot send friend request to yourself.", e.getMessage());
    }

    @Test
    public void testAccept() {
        this.testSendRequest();
        try {
            ctrl.acceptFriendRequest("tester2", "tester");
        } catch (Exception e) {

        }
        Member testUser2 = memberDAO.getUser("tester2");
        ArrayList<Member> friendList = ctrl.getFriends(testUser2);
        Assertions.assertEquals("tester", friendList.get(0).getUserID());
    }

    @Test
    public void testSendToFriend() {
        this.testAccept();
        Exception e = Assertions.assertThrows(FriendException.class, () ->
            ctrl.sendFriendRequest("tester", "tester2"));
        Assertions.assertEquals("tester2 is already a friend.", e.getMessage());
    }

    @Test
    public void testGetFriend() {
        Member apple = memberDAO.getUser("apple");
        ArrayList<Member> friendList = ctrl.getFriends(apple);

        Assertions.assertEquals("david", friendList.get(0).getUserID());
    }



    @AfterAll
    public void removeUser() {
        Member testUser = memberDAO.getUser("tester");
        if (testUser != null) {
            memberDAO.deleteUser("tester");
        }   
    }

}
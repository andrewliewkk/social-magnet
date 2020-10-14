package SocialMagnet.Control;

/**
 * Test Thread
 */

import org.junit.jupiter.api.*;
import java.util.*;

import SocialMagnet.Social.Member.*;
import SocialMagnet.Social.Post.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestThread { 
    private PostDAO postDAO = new PostDAO();
    private MemberDAO memberDAO = new MemberDAO();
    private ThreadCtrl ctrl = new ThreadCtrl();
    private WallCtrl wallCtrl = new WallCtrl();
    
    // @BeforeAll
    // public void resetUser() {
    //     Member testUser = memberDAO.getUser("tester");
    //     if (testUser != null) {
    //         memberDAO.deleteUser("tester");
    //     }
    //     memberDAO.addUser("tester", "password", "Tester");
    // }

    @Test
    public void testLikePost() {
        ctrl.likePost(13, "kenny", true);

        Assertions.assertEquals("kenny", ctrl.retrieveLikers(13).get(0).getUserID());
    }

    @Test
    public void testDislikePost() {
        ctrl.likePost(13, "david", false);

        Assertions.assertEquals("david", ctrl.retrieveDislikers(13).get(0).getUserID());
    }

    @Test
    public void testReply() {
        ctrl.replyPost(13, "kenny", "hey, this is a reply");

        Assertions.assertEquals("hey, this is a reply", ctrl.retrieveReplies(13).get(0).getContent());
    }

    @Test
    public void testDeleteOwnPost() {
        ctrl.deletePost("apple", 12);
        
        Assertions.assertEquals(new ArrayList<>(), postDAO.retrieveWallPost(12));
    }

    @Test
    public void testDeleteTag() {
        ctrl.deletePost("apple", 9);
        
        Assertions.assertEquals("Hello World! @apple ", postDAO.retrieveWallPost(9).get(0).getContent());
    }

    // @AfterAll
    // public void removeUser() {
    //     Member testUser = memberDAO.getUser("tester");
    //     if (testUser != null) {
    //         memberDAO.deleteUser("tester");
    //     }
    // }

}
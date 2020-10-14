package SocialMagnet.Control;

/**
 * Test NewsFeed
 */

import org.junit.jupiter.api.*;
import java.util.*;

import SocialMagnet.Social.Member.*;
import SocialMagnet.Social.Post.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestNewsFeed {
    private PostDAO postDAO = new PostDAO();
    private MemberDAO memberDAO = new MemberDAO();
    private NewsFeedCtrl ctrl = new NewsFeedCtrl();
    private WallCtrl wallCtrl = new WallCtrl();
    private FriendCtrl friendCtrl = new FriendCtrl();
    
    @BeforeAll
    public void resetUser() {
        Member testUser = memberDAO.getUser("tester");
        if (testUser != null) {
            ArrayList<Post> postList = postDAO.retrieveAllByUserID("tester");
            for (Post post:postList) {
                postDAO.deletePost("tester", post.getPostID());
            }
            memberDAO.deleteUser("tester");
        }
        memberDAO.addUser("tester", "password", "Tester");

        Member testUser2 = memberDAO.getUser("tester2");
        if (testUser2 != null) {
            ArrayList<Post> postList2 = postDAO.retrieveAllByUserID("tester2");
            for (Post post:postList2) {
                postDAO.deletePost("tester2", post.getPostID());
            }
            memberDAO.deleteUser("tester2");
        }
        memberDAO.addUser("tester2", "password", "Tester2");

        try {
            friendCtrl.sendFriendRequest("tester", "tester2");
            friendCtrl.acceptFriendRequest("tester2", "tester");
        } catch (Exception e) {

        }

        wallCtrl.newPost("tester", "my own message", "tester");
        wallCtrl.newPost("tester2","my friends message", "tester2");
    }

    @Test
    public void testGetNewsFeed() {
        Member testUser = memberDAO.getUser("tester");
        Dictionary<Integer,Post> newsFeed = ctrl.getNewsFeed(testUser);

        Post p1 = new Post(1,"tester", "my own message", "tester");

        Dictionary<Integer,Post> control = new Hashtable();
        control.put(1, p1);

        Assertions.assertEquals(control.get(1).getContent(), newsFeed.get(2).getContent());
    }

    @Test
    public void testGetNewsFeedFriend() {
        Member testUser = memberDAO.getUser("tester");
        Dictionary<Integer,Post> newsFeed = ctrl.getNewsFeed(testUser);

        Post p2 = new Post(1, "tester2", "my friends message", "tester2");

        Dictionary<Integer,Post> control = new Hashtable();
        control.put(1, p2);

        Assertions.assertEquals(control.get(1).getContent(), newsFeed.get(1).getContent());
    }

    @AfterAll
    public void removeUser() {
        try {
            friendCtrl.unFriend("tester", "tester2");
        } catch (Exception e) {

        }

        Member testUser = memberDAO.getUser("tester");
        if (testUser != null) {
            ArrayList<Post> postList = postDAO.retrieveAllByUserID("tester");
            for (Post post:postList) {
                postDAO.deletePost("tester", post.getPostID());
            }
            memberDAO.deleteUser("tester");
        }

        Member testUser2 = memberDAO.getUser("tester2");
        if (testUser2 != null) {
            ArrayList<Post> postList2 = postDAO.retrieveAllByUserID("tester2");
            for (Post post:postList2) {
                postDAO.deletePost("tester2", post.getPostID());
            }
            memberDAO.deleteUser("tester2");
        }
    }

}
package SocialMagnet.Control;

/**
 * Test Friend Wall
 */

import org.junit.jupiter.api.*;

import java.util.*;

import SocialMagnet.Social.Member.*;
import SocialMagnet.Social.Post.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestFriendWall {
    private PostDAO postDAO = new PostDAO();
    private MemberDAO memberDAO = new MemberDAO();
    private FriendWallCtrl ctrl = new FriendWallCtrl();

    @Test
    public void testGetFriendWall() {
        Member testUser = memberDAO.getUser("apple");
        Dictionary<Integer,Post> wall = ctrl.getFriendWall(testUser);

        Post p1 = null;

        for (int i = 1; i <= 5; i++) {
            if (wall.get(i) != null) {
                if (wall.get(i).getPostID() == 14) {
                    p1 = wall.get(i);
                }
            }
        }

        Assertions.assertEquals("Welcome to my wall!", p1.getContent());
    }

    @Test
    public void testGetCommonFriend() {
        Member apple = memberDAO.getUser("apple");
        Member david = memberDAO.getUser("david");
        HashMap<Member, Boolean> map = ctrl.getFriendsAndCommon(apple, david);

        Member common = null;

        Set<Member> keys = map.keySet();
        for (Member key:keys) {
            if (map.get(key)) {
                common = key;
            }
        }

        Assertions.assertEquals("james", common.getUserID());
    } 

}
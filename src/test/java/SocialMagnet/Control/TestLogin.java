package SocialMagnet.Control;

/**
 * Test Login
 */

import org.junit.jupiter.api.*;
import java.util.*;

import SocialMagnet.Exceptions.LoginException;
import SocialMagnet.Social.Member.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestLogin {
    private MemberDAO memberDAO = new MemberDAO();
    private LoginCtrl ctrl = new LoginCtrl();
    
    @BeforeAll
    public void resetUser() {
        Member testUser = memberDAO.getUser("tester");
        if (testUser != null) {
            memberDAO.deleteUser("tester");
        }
        memberDAO.addUser("tester", "password", "Tester");
    }

    @Test
    public void testSuccessfulLogin() {
        Member user = null;
        try {
            user = ctrl.userLogin("tester", "password");
        } catch (Exception e) {

        }
        Assertions.assertEquals("tester", user.getUserID());
    }

    @Test
    public void testUnsuccessfulLogin() {
        Exception e = Assertions.assertThrows(LoginException.class, () ->
            ctrl.userLogin("tester", "idkthepass"));

        Assertions.assertEquals("Invalid username or password!", e.getMessage());
    }

    @AfterAll
    public void removeUser() {
        Member testUser = memberDAO.getUser("tester");
        if (testUser != null) {
            memberDAO.deleteUser("tester");
        }   
    }

}
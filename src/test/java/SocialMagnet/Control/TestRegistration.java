package SocialMagnet.Control;

/**
 * Test Register
 */

import org.junit.jupiter.api.*;
import java.util.*;

import SocialMagnet.Exceptions.RegistrationException;
import SocialMagnet.Social.Member.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestRegistration {
    private MemberDAO memberDAO = new MemberDAO();
    private RegisterCtrl ctrl = new RegisterCtrl();
    
    @BeforeAll
    public void resetUser() {
        Member testUser = memberDAO.getUser("tester");
        if (testUser != null) {
            memberDAO.deleteUser("tester");
        }   
    }

    @Test
    public void testAddUser() {
        ctrl.userRegister("tester", "Tester", "password", "password");
        Assertions.assertEquals("tester", memberDAO.getUser("tester").getUserID());
    }

    @Test
    public void testRepeated() {
        Exception e = Assertions.assertThrows(RegistrationException.class, () ->
            ctrl.userRegister("tester", "Tester", "password", "password"));
        Assertions.assertEquals("This username has already been taken!", e.getMessage());
    }

    @Test
    public void testBlankUserID() {
        Exception e = Assertions.assertThrows(RegistrationException.class, () ->
            ctrl.userRegister("", "Tester", "password", "password"));
        Assertions.assertEquals("Your username must be alphanumeric!", e.getMessage());
    }

    @Test
    public void testBlankFullName() {
        Exception e = Assertions.assertThrows(RegistrationException.class, () ->
            ctrl.userRegister("tester2", "", "password", "password"));
        Assertions.assertEquals("Your name cannot be blank!", e.getMessage());
    }

    @Test
    public void testBadUserID() {
        Exception e = Assertions.assertThrows(RegistrationException.class, () ->
            ctrl.userRegister("tester!23", "Tester", "password", "password"));
        Assertions.assertEquals("Your username must be alphanumeric!", e.getMessage());
    }

    @Test
    public void testWrongCfmPass() {
        Exception e = Assertions.assertThrows(RegistrationException.class, () ->
            ctrl.userRegister("tester3", "Tester", "password123", "password"));
        Assertions.assertEquals("Your passwords did not match!", e.getMessage());
    }

    @AfterAll
    public void removeUser() {
        Member testUser = memberDAO.getUser("tester");
        if (testUser != null) {
            memberDAO.deleteUser("tester");
        }   

        Member testUser2 = memberDAO.getUser("tester2");
        if (testUser2 != null) {
            memberDAO.deleteUser("tester2");
        }   

        Member testUser3 = memberDAO.getUser("tester3");
        if (testUser3 != null) {
            memberDAO.deleteUser("tester3");
        }   
    }

}
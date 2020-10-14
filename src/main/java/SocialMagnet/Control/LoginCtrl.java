package SocialMagnet.Control;

/**
 * LoginCtrl
 */

import SocialMagnet.Social.Member.*;
import SocialMagnet.Exceptions.LoginException;

public class LoginCtrl {
    private MemberDAO memberDAO = new MemberDAO();

    // Returns a member if username valid password valid, else null
    public Member userLogin(String userID, String password) throws LoginException{
        Member user = memberDAO.getUser(userID);
        if (user == null || !( (user.getPassword()).equals(password) ) ) {
            throw new LoginException("Invalid username or password!");
        }
        return user;
    }
}
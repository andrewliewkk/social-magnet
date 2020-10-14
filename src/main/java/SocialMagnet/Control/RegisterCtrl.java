package SocialMagnet.Control;

/**
 * RegisterCtrl
 */

import SocialMagnet.Exceptions.RegistrationException;
import SocialMagnet.Social.Member.MemberDAO;

public class RegisterCtrl {
    private MemberDAO memberDAO = new MemberDAO();

    // Add a new user if all checks are passed
    public void userRegister(String userID, String fullName, String password, String cfmPassword) throws RegistrationException{
        if ((memberDAO.getUser(userID)) != null) {
            throw new RegistrationException("This username has already been taken!");
        } else if (!(userID.matches("[A-Za-z0-9]+"))) {
            throw new RegistrationException("Your username must be alphanumeric!");
        } else if (fullName.isEmpty()) {
            throw new RegistrationException("Your name cannot be blank!");
        } else if (!(password.equals(cfmPassword))) {
            throw new RegistrationException("Your passwords did not match!");
        } else {
            memberDAO.addUser(userID, password, fullName);
        }
    }
}
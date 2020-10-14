package SocialMagnet.Menu;

/**
 * LoginMenu
 */

import java.util.*;

import SocialMagnet.Control.LoginCtrl;
import SocialMagnet.Exceptions.LoginException;
import SocialMagnet.Social.Member.Member;

public class LoginMenu {
    private LoginCtrl ctrl = new LoginCtrl();
    private MainMenu mainMenu = new MainMenu();

    // If user credentials correct, send to main page, else send to welcome page
    public void displayLogin() {
        System.out.println();
        System.out.println("== Social Magnet :: Login ==");
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your username > ");
        String userID = sc.nextLine();
        System.out.print("Enter your password > ");
        String password = sc.nextLine();

        try {
            Member user = ctrl.userLogin(userID, password);
            mainMenu.displayMain(user);
        } catch (LoginException e) {
            System.out.println();
            System.out.println(e.getMessage());
        }
    }
}
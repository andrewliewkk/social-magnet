package SocialMagnet.Menu;

/**
 * RegisterMenu
 */

import java.util.*;

import SocialMagnet.Control.RegisterCtrl;
import SocialMagnet.Exceptions.RegistrationException;
import SocialMagnet.Farm.PlotDAO;

public class RegisterMenu {
    private RegisterCtrl ctrl = new RegisterCtrl();

    // Display registration menu
    public void displayRegister() {
        System.out.println();
        System.out.println("== Social Magnet :: Registration ==");
        Scanner sc;
        PlotDAO plotDAO = new PlotDAO();
    
        sc = new Scanner(System.in);
        System.out.print("Enter your username > ");
        String userID = sc.nextLine();
        System.out.print("Enter your Full name > ");
        String fullName = sc.nextLine();
        System.out.print("Enter your password > ");
        String password = sc.nextLine();
        System.out.print("Confirm your password > ");
        String cfmPassword = sc.nextLine();

        System.out.println();

        try {
            ctrl.userRegister(userID, fullName, password, cfmPassword);
            plotDAO.createNewPlotsForNewUser(userID);
            System.out.println(userID + ", your account is successfully created!");
        } catch (RegistrationException e) {
            System.out.println(e.getMessage());
        }        
    }
}
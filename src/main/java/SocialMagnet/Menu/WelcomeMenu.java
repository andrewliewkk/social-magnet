package SocialMagnet.Menu;

// Insert import statments here
import java.util.*;

import SocialMagnet.Social.Post.*;
import SocialMagnet.Social.Member.*;

import SocialMagnet.Menu.*;


// This class handles menu interactions

public class WelcomeMenu {
    private MenuUtility menuUtility = new MenuUtility();
    private RegisterMenu registerMenu = new RegisterMenu();
    private LoginMenu loginMenu = new LoginMenu();

    // prints out the welcome page to the console
    public void displayWelcome() {
        System.out.println();
        System.out.println("== Social Magnet :: Welcome ==");
        System.out.println("Good morning, anonymous!");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Enter your choice > ");
    }

    // takes in user input and invokes the relevant methods
    public void start() {
        int choice;
        do {
            displayWelcome();
            choice = menuUtility.checkInput();
            switch (choice) {
                case 1:
                    registerMenu.displayRegister();
                    break;
                case 2:
                    loginMenu.displayLogin();
                    break;
                case 3:
                    System.out.println("Thank you for using SocialMagnet!");
                    break;
                default:
                    System.out.println("Please enter a choice between 1 & 3!");
            }
        } while (choice != 3);
    }

}
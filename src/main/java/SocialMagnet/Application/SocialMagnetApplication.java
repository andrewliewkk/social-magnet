package SocialMagnet.Application;
/**
 * SocialMagnetApplication
 * This is the main class to be run
 */

import SocialMagnet.Menu.*;

public class SocialMagnetApplication {
    public static void main(String[] args) {
        try {
            WelcomeMenu menu = new WelcomeMenu();
            menu.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());          
        }
    }
}
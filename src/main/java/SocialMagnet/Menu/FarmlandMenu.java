package SocialMagnet.Menu;

/**
 * FarmlandMenu
 */

import java.util.*;

import SocialMagnet.Control.FarmlandCtrl;
import SocialMagnet.Exceptions.FarmlandException;
import SocialMagnet.Menu.MenuUtility;
import SocialMagnet.Social.Member.Member;
import SocialMagnet.Farm.*;

public class FarmlandMenu {
    private FarmlandCtrl ctrl = new FarmlandCtrl();
    private MenuUtility menuUtility = new MenuUtility();

    public boolean displayFarmland(Member user) {
        String choice;
        boolean goToMain = false;
        boolean goToFarm = false;
        do {
            menuUtility.displayCityFarmersHeader("My Farmland", user);

            int plotNum = ctrl.getPlotNum(user);

            System.out.println("You have " + plotNum + " plots of land.");

            // Display plots here
            ctrl.createPlots(user);
            Map<Integer, Plot> plotMap = displayPlots(user);
            
            System.out.print("[M]ain | City [F]armers | [P]lant | [C]lear | [H]arvest > ");
            Scanner sc = new Scanner(System.in);
            choice = sc.next();     
            
            
            Plot plot = null;
            PlotCrop plotCrop = null;
            int plotChoice = 0;
            if ((choice.substring(0,1)).equals("P")) {
                try {
                    plotChoice = Integer.parseInt(choice.substring(1));
                    plot = (Plot)plotMap.get(plotChoice);
                    plotCrop = ctrl.retrievePlot(user, plot.getPlotID());
                    choice = choice.substring(0,1);
                    if (plotCrop != null) {
                        choice = "invalid";
                    } 
                } catch (Exception e) {
                    choice = "invalid";
                }
            }else if ((choice.substring(0,1)).equals("C")) {
                try {
                    plotChoice = Integer.parseInt(choice.substring(1));
                    plot = (Plot)plotMap.get(plotChoice);
                    plotCrop = ctrl.retrievePlot(user, plot.getPlotID());
                    choice = choice.substring(0,1);
                    if (plotCrop == null) {
                        choice = "invalid";
                    } 
                } catch (Exception e) {
                    choice = "invalid";
                }
            }

            switch (choice) {
                case "M":
                    goToMain = true;
                    break;
                case "F":
                    break;
                case "P":
                    String returnTo = plantCrop(plot, user);
                    if (returnTo.equals("main")) {
                        goToMain = true;
                    } else if (returnTo.equals("farm")) {
                        goToFarm = true;
                    }
                    break;
                case "C":
                    if (menuUtility.displayConfirm()) {
                        try {
                            ctrl.clearCrop(user, plotCrop);
                        } catch (FarmlandException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    break;
                case "H":
                    harvest(user);
                    break;
                default:
                    System.out.println("Please enter a valid choice!");
            }
        } while (!choice.equals("M") && !choice.equals("F") && !goToMain &&!goToFarm);
        return goToMain;
    }

    public Map<Integer, Plot> displayPlots(Member user) {
        ArrayList<Plot> plotList = ctrl.retrievePlots(user);
        Map<Integer, Plot> map = new HashMap<>();
        int index = 1;
        int plotID;
        PlotCrop plotCrop;
        for (Plot plot: plotList) {
            plotID = plot.getPlotID();
            plotCrop = ctrl.retrievePlot(user, plotID);
            if (plotCrop == null) {
                System.out.println(index + ". <empty>");
            } else {
                String cropName = plotCrop.getCropName();
                String progress = ctrl.calculateProgress(plotCrop);

                int spaces = 12 - cropName.length();
                String whitespace = " ".repeat(spaces);
                System.out.println(index + ". " + cropName + whitespace + progress);
            }
            // Convert to integer?
            map.put(index, plot);
            index++;
        }
        return map;
    }
    
    public String plantCrop(Plot plot, Member user) {
        Scanner sc = new Scanner(System.in);
        System.out.println();

        ArrayList<Inventory> invList = ctrl.retrieveInventory(user); // get all crops in users inv
        int index = 1;
        if (invList.isEmpty()) {
            System.out.println("< No seeds available >");
            return "";
        } else { 
            System.out.println("Select the crop:");
            for (Inventory inv : invList) {
                System.out.println(index + ". " + inv.getCropName());
                index++;
            }
        }
        

        System.out.print("[M]ain | City [F]armers | Select Choice > ");
        
        Inventory invCrop = null;
        int cropChoice = 0;
        String choice = "";
        
        try {
            cropChoice = sc.nextInt();
            invCrop = invList.get(cropChoice-1);
            choice = "P";
        } catch (InputMismatchException e) {
            choice = sc.next();
        } catch (IndexOutOfBoundsException e) {
            choice = "invalid";
        }

        switch (choice) {
            case "M":
                return "main";
            case "F":
                return "farm";
            case "P":
                ctrl.plantPlotCrop(user, invCrop, plot);
                System.out.println(invCrop.getCropName() + " planted on plot " + 
                        plot.getPlotID() + ".");
                break;
            default:
                System.out.println("Please enter a valid choice!");
        }
        return "";
    }

    public void harvest(Member user) {
        Map<String, Map<String, Integer>> harvested = ctrl.harvestAll(user);
        Set<String> cropsHarvested = harvested.keySet();
        Map<String, Integer> harvestDetails;
        if (harvested.isEmpty()) {
            System.out.println("Nothing to harvest!");
        } else {
            for (String cropName: cropsHarvested) {
                harvestDetails = harvested.get(cropName);
                System.out.println("You have harvested " + harvestDetails.get("yield") + " " + cropName + " for " + 
                harvestDetails.get("xp") + " XP and " + harvestDetails.get("gold") + " gold.");
            }
        }
    }
}
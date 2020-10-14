package SocialMagnet.Menu;

import java.util.*;
import SocialMagnet.Control.FriendFarmCtrl;
import SocialMagnet.Exceptions.StealException;
import SocialMagnet.Social.Member.Member;
import SocialMagnet.Social.Member.MemberDAO;
import SocialMagnet.Farm.*;

/**
 * FriendFarmMenu
 */
public class FriendFarmMenu {
    private FriendFarmCtrl ctrl = new FriendFarmCtrl();
    private CropDAO cropDAO = new CropDAO();
    private RankDAO rankDAO = new RankDAO();
    private PlotDAO plotDAO = new PlotDAO();

    public boolean displayFriendFarm(Member user, Member friend) {

        String choice;

        do {
            System.out.println("Name: " + friend.getFullName());
            System.out.println("Title: " + friend.getRank());
            System.out.println("Gold: " + friend.getGold() + " gold");

            displayPlots(friend);

            // Suspect there is a typo in the doc. It should be City Farmers instead of City Famers.
            System.out.println();
            System.out.print("[M]ain | City [F]armers | [S]teal > ");

            Scanner sc = new Scanner(System.in);
            choice = sc.next();

            switch (choice) {
                case "M":
                    return true;

                case "F":
                    break;

                case "S":        
                    processSteals(user, friend);
                    break;

                default:
                    System.out.println("Please enter a valid choice!");
            }
        } while (!choice.equals("M") && !choice.equals("F") && !choice.equals("S"));
        return false;
    }

    public boolean checkMapHasNonZeroSteals(Map<String, Integer> stolenMap) {
        Collection<Integer> stealValues = stolenMap.values();
        boolean allNonZeroSteals = false;
        for (Integer integer : stealValues) {

            if (integer > 0) {
                return true;
            }
        }
        return allNonZeroSteals;
    }

    public boolean checkAllPlotsAlreadyStolen(Map<String, Integer> stolenMap) {
        Collection<Integer> stealValues = stolenMap.values();
        boolean allAlreadyStolen = true;
        for (Integer integer : stealValues) {
            // -1 was used to denote that the user already stole.
            if (integer > -1) {
                return false;
            }
        }
        return allAlreadyStolen;
    }

    public void processSteals(Member user, Member friend) {
        MemberDAO memberDAO = new MemberDAO();
        try {
            Map<String, Integer> stolenMap = ctrl.steal(user, friend);
            String output = "";

            String currentRank = user.getRank();

            // For display purposes.
            output = "You have successfully stolen ";
            int totalXP = 0;
            int totalGold = 0;

            Set<String> keySet = stolenMap.keySet();

            String insertPlural = "s"; // add "s" if plural.

            for (String cropName : keySet) {
                int numberStolen = stolenMap.get(cropName);

                if (numberStolen >= 1) {
                    if (numberStolen == 1) {
                        insertPlural = "";
                    }

                    output += "" + numberStolen + " " + cropName + insertPlural + ", ";
                    Crop crop = cropDAO.retrieveCropByName(cropName);
                    totalXP += crop.getXP() * numberStolen;
                    totalGold += crop.getSalePrice() * numberStolen;
                }
            }

            output = output.substring(0, output.length() - 2);
            output += " for " + totalXP + " XP, and " + totalGold + " gold.";

            // Update Xp and Gold for the user
            user.addGold(totalGold);
            user.addXP(totalXP);
            memberDAO.setGold(user.getGold(), user.getUserID());
            memberDAO.setXP(user.getXP(), user.getUserID());

            System.out.println(output);

            String newRank = rankDAO.retrieveRank(user.getXP());
            if (!currentRank.equals(newRank)) {
                user.setRank(newRank);
                plotDAO.createNewPlots(user.getUserID(), newRank);
                System.out.println("Congratulations! You have attained the rank of " + newRank + "!");
            }

        } catch (StealException e) {
            System.out.println(e.getMessage());
        }
    }

    // Retrieves and displays the user's farmland
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
  
            map.put(index, plot);
            index++;
        }
        return map;
    }
}
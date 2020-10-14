package SocialMagnet.Control;

/**
 * FarmlandCtrl
 */


import SocialMagnet.Social.Member.*;
import SocialMagnet.Farm.*;
import java.util.*;
import SocialMagnet.Exceptions.FarmlandException;

public class FarmlandCtrl {
    private RankDAO rankDAO = new RankDAO();
    private PlotDAO plotDAO = new PlotDAO();
    private PlotCropDAO plotCropDAO = new PlotCropDAO();
    private PlotCropStealDAO plotCropStealDAO = new PlotCropStealDAO();
    private InventoryDAO inventoryDAO = new InventoryDAO();
    private CropDAO cropDAO = new CropDAO();
    private MemberDAO memberDAO = new MemberDAO();

    public int getPlotNum(Member user) {
        String rank = user.getRank();
        int plotNum = rankDAO.retrievePlotNumByRank(rank);
        if (plotNum < 0) {
            System.out.println("Rank Error!");
        }
        return plotNum;
    }

    public void createPlots(Member user) {
        int plotNum = getPlotNum(user);
        ArrayList<Plot> plotList = retrievePlots(user);
        String userID = user.getUserID();
        if (plotList.size() < plotNum) {
            for(int i = 0; i < plotNum - plotList.size(); i++) {
                plotDAO.createBlankPlot(userID);
            }
        }

    }

    public ArrayList<Plot> retrievePlots(Member user) {
        String userID = user.getUserID();
        ArrayList<Plot> plotList = plotDAO.getPlotsByUser(userID);
        return plotList;
    }

    public PlotCrop retrievePlot(Member user, int plotID) {
        String userID = user.getUserID();
        return plotCropDAO.getPlotCropByID(userID, plotID);
    }

    public ArrayList<Inventory> retrieveInventory(Member user) {
        String userID = user.getUserID();
        return inventoryDAO.retrieveInventory(userID);
    }

    public void plantPlotCrop(Member user, Inventory invCrop, Plot plot) {
        inventoryDAO.plantCrop(invCrop);
        Crop crop = cropDAO.retrieveCropByName(invCrop.getCropName());
        int plotID = plot.getPlotID();
        plotCropDAO.plantCrop(user, plotID, crop);
    }

    // This method takes in the PlotCrop and retreieves the time it was planted from the database
    // and does the calculations
    public String calculateProgress(PlotCrop plotCrop) {
        String cropName = plotCrop.getCropName();
        Crop crop = cropDAO.retrieveCropByName(cropName);
        int growthTime = crop.getMaturityTime();
        int timeElapsed = (int)plotCrop.getProgress();

        String progressBar = "[";
        int percentDone = timeElapsed * 100 / growthTime;
        if (percentDone >= 100) {
            progressBar = "[##########]";
        } else {
            int percentDoneCopy = percentDone;
            // Loops the number of times didvided by 10 to display the progress bar
            while (percentDoneCopy >= 10) {
                percentDoneCopy -= 10;
                progressBar += "#";
            }
            while (progressBar.length() < 11) {
                progressBar += "-";
            }
            progressBar += "]";
        }

        // The percentage completed exceeds 200, meaning twice the amount of time need to grow has doubled
        // it will consider th crop as wilted
        if (percentDone >= 200) {
            return "[  wilted  ]";
        }else if (percentDone >= 100){
            percentDone = 100;
            return progressBar + " " + percentDone + "%";
        }else {
            return progressBar + " " + percentDone + "%";
        }
    }

    public void clearCrop(Member user, PlotCrop plotCrop) throws FarmlandException {
        
        int plotID = plotCrop.getPlotID();
        String userID = user.getUserID();

        // True: isCleared is true: need to remove 5 gold from the farmer.
        boolean clearSuccess = plotCropDAO.removePlotCrop(user, plotID, true);
        if (!clearSuccess) {
            throw new FarmlandException("Insufficient gold!");
        } else {
            String cropName = plotCrop.getCropName();
            System.out.println("Wilted " + cropName + "s cleared for 5 gold!");
        }

    }

    public Map<String, Map<String, Integer>> harvestAll(Member user) { //returns <cropName <xp 10, gold 10, amt 200> >

        ArrayList<Plot> plotList = retrievePlots(user);
        ArrayList<PlotCrop> plotCropList = plotCropDAO.getPlotCropsByUser(user, plotList);

        Map<String, Map<String, Integer>> toReturn = new HashMap<>();

        String currentRank = user.getRank();
        int newGold = 0;
        int newXP = 0;

        for (PlotCrop plotCrop : plotCropList) {

            if (plotCrop != null && calculateProgress(plotCrop).equals("[##########] 100%")) {
                String userID = user.getUserID();
                int plotID = plotCrop.getPlotID();
                int yield = plotCrop.getYield();
                
                // Calculate less yield due to steals
                int lessYield = plotCropStealDAO.calculatePlotCropSteals(user, plotID);
                yield -= lessYield;

                String cropName = plotCrop.getCropName();
                Crop crop = cropDAO.retrieveCropByName(cropName);
                int salePrice = crop.getSalePrice();
                int XP = crop.getXP();

                int totalPrice = salePrice * yield;
                int totalXP = XP * yield;

                Map<String, Integer> details = new HashMap<>();

                if (toReturn.get(cropName) == null) {
                    details.put("yield", yield);
                    details.put("gold", totalPrice);
                    details.put("xp", totalXP);
                    toReturn.put(cropName, details);
                } else {
                    details = toReturn.get(cropName);
                    details.put("yield", details.get("yield") + yield);
                    details.put("gold", details.get("gold") + totalPrice);
                    details.put("xp", details.get("xp") + totalXP);
                }

                user.addGold(totalPrice);
                user.addXP(totalXP);
                
                // false: isCleared is false: no need to remove 5 gold from farmer as farmer is harvesting
                newGold = user.getGold();
                newXP = user.getXP();
                memberDAO.addHarvest(userID, newGold, newXP);
                plotCropDAO.removePlotCrop(user, plotID, false);
                plotCropStealDAO.removePlotCropSteals(user, plotID);
            }
        }

        // Upon harvesting, the user might gain enough Xp to rank up, as such we do a check here after harvesting
        String newRank = rankDAO.retrieveRank(newXP);
        if (!currentRank.equals(newRank)) {
            user.setRank(newRank);
            plotDAO.createNewPlots(user.getUserID(), newRank);
            System.out.println("Congratulations! You have attained the rank of " + newRank + "!");
        }

        return toReturn;
    }
}
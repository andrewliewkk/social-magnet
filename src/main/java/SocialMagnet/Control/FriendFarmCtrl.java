package SocialMagnet.Control;

/**
 * FriendFarmCtrl
 */

import SocialMagnet.Social.Member.Member;
import SocialMagnet.Exceptions.StealException;
import SocialMagnet.Farm.*;
import java.util.*;

public class FriendFarmCtrl {
    private PlotDAO plotDAO = new PlotDAO();
    private PlotCropDAO plotCropDAO = new PlotCropDAO();
    private PlotCropStealDAO plotCropStealDAO = new PlotCropStealDAO();
    private CropDAO cropDAO = new CropDAO();

    public Map<String, Integer> steal(Member user, Member friend) throws StealException {
        
        ArrayList<Plot> plotList = retrievePlots(friend);

        // <cropName, qty>
        Map<String, Integer> stolenMap = new HashMap<>();

        // up to 20% per plot of land.
        for (Plot plot : plotList) {
            
            int plotID = plot.getPlotID();
            PlotCrop plotCrop = retrievePlot(friend, plotID);
            
            // if the plot is not empty
            if (plotCrop != null) {

                String cropName = plotCrop.getCropName();
                Crop crop = cropDAO.retrieveCropByName(cropName);

                long progress = plotCrop.getProgress();
                double percDone = 100 * progress / crop.getMaturityTime();

                int min = 1;
                int max = 5;

                // if the crop is ready to be harvested
                if (percDone >= 100 && percDone < 200) {
                    double percStolen =  (Math.random() * ( max - min )) + min;
                    int totalYield = plotCrop.getYield();
                    
                    int stolen = (int)(percStolen * totalYield / 100);

                    // To check if user has already stole from this friend
                    if (plotCropStealDAO.checkIfStoleAlready(user, friend, plotID)) {
                        // If user already stole, put -1 in the map.
                        if (!stolenMap.containsKey(cropName)) {
                            stolenMap.put(cropName, -1);
                        }
                        
                    } else {
                        int maxStealable = plotCropStealDAO.retrieveMaxToSteal(user, friend, plotID);
                        stolen = Math.min(maxStealable, stolen);
                        String stealStatus = plotCropStealDAO.steal(user, friend, plotID, stolen);

                        if (stolenMap.get(cropName) == null) {
                            stolenMap.put(cropName, stolen);
                        } else {
                            int currentStolen = stolenMap.get(cropName);
                            stolenMap.put(cropName, currentStolen + stolen);
                        }
                    } 
                }
            }
        }

        // Check if there are any plots that are not stolen from yet, else throw exception
        Collection<Integer> stealValues = stolenMap.values();
        boolean allAlreadyStolen = true;
        boolean nothingToSteal = true;
        for (Integer integer : stealValues) {
            if (integer > -1) {
                allAlreadyStolen = false;
            }
            if (integer > 0) {
                nothingToSteal = false;
            }
        }

        // If there are no crops planted or crops are not ready for harvest
        if (stolenMap.isEmpty()) {
            throw new StealException("No crops available to steal!");
        }

        // If crops has 
        if (allAlreadyStolen) {
            throw new StealException("You already stole from " + friend.getUserID() + "!");
        }
        if (nothingToSteal) {
            throw new StealException("Steal yielded 0 items. Unlucky!");
        }

        return stolenMap;
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
    
}
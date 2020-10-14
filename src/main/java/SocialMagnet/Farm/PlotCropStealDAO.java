package SocialMagnet.Farm;

import SocialMagnet.Social.Member.*;
import java.sql.*;
import java.util.*;
import java.text.*;

/**
 * PlotCropDAO  
 */
public class PlotCropStealDAO {

    private MemberDAO memberDAO = new MemberDAO();
    private PlotCropDAO plotCropDAO = new PlotCropDAO();

    public static ArrayList<PlotCropSteal> dbMgr(String sqlstmt, boolean update) {
        String host = "localhost";
        int port = 3306;
        String dbName = "magnet";
        String dbURL = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false";

        String username = "root";
        String password = "";
        String sql = sqlstmt; // change statement here
        Statement stmt = null;
        Connection conn = null;

        ArrayList<PlotCropSteal> plotCropStealList = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver").getConstructor().newInstance();
            conn = DriverManager.getConnection(dbURL, username, password);
            stmt = conn.createStatement();

            if (!update) {
                ResultSet rs = stmt.executeQuery(sql);
                
                while (rs.next()) {
                    String stealerID = rs.getString("StealerID");
                    String userID = rs.getString("UserID");
                    int plotID = rs.getInt("PlotID");
                    int amountStolen = rs.getInt("AmountStolen");
                    int cumPercStolen = rs.getInt("CumPercStolen");

                    plotCropStealList.add(new PlotCropSteal(stealerID, userID, plotID, amountStolen, cumPercStolen));
                }
            } else {
                stmt.executeUpdate(sql);
            }
            
        } catch (Exception e) {
            System.out.println("DB Error!");
            System.out.println(e.getMessage());
        } finally {
            try {
                if (stmt != null) { 
                    stmt.close(); 
                }
            } catch (Exception e) {
                System.out.println("Close Error!");
                System.out.println(e.getMessage());
            } 
            try {
                if (conn != null) { 
                    conn.close(); 
                }
            } catch (Exception e) {
                System.out.println("Close Error!");
                System.out.println(e.getMessage());
            }
        }

        return plotCropStealList;
    }

    public boolean checkIfStoleAlready(Member user, Member friend, int plotID) {
        String stealerID = user.getUserID();
        String friendID = friend.getUserID();

        // first, check if user alr stole.
        ArrayList<PlotCropSteal> plotCropStealFromUser = dbMgr("select * from plot_crop_steal where StealerID = '" + stealerID + "' and UserID = '" + friendID + "' and PlotID = '" +plotID+ "';", false);

        if (plotCropStealFromUser.size() != 0) {
            return true;
        }
        return false;
    }

    public int retrieveMaxToSteal(Member user, Member friend, int plotID) {
        String stealerID = user.getUserID();
        String friendID = friend.getUserID();
        ArrayList<PlotCropSteal> plotCropSteal = dbMgr("select * from plot_crop_steal where UserID = '" + friendID + "' and PlotID = " + plotID + ";", false);

        PlotCrop plotCrop = plotCropDAO.getPlotCropByID(friendID, plotID);

        int yield = plotCrop.getYield();
        int maxPossibleStolen = (int)(0.2 * yield);
        int stolenToDate = 0;
        
        for (PlotCropSteal pcs : plotCropSteal) {
            stolenToDate += pcs.getAmountStolen();
        }

        double cumPercStolen = 100 * stolenToDate / yield;

        if (cumPercStolen >= 20) {
            return 0;
        }
        return maxPossibleStolen - stolenToDate;
    }

    public String steal(Member user, Member friend, int plotID, int stolen) {      
        String stealerID = user.getUserID();
        String friendID = friend.getUserID();
        PlotCrop plotCrop = plotCropDAO.getPlotCropByID(friendID, plotID);
        ArrayList<PlotCropSteal> plotCropSteal = dbMgr("select * from plot_crop_steal where UserID = '" + friendID + "' and PlotID = " + plotID + ";", false);

        int yield = plotCrop.getYield();
        
        // int thisSteal =  Math.min(maxPossibleStolen - stolenToDate, stolen);
        // int totalStolen = thisSteal + stolenToDate;

        double newCumPercStolen = 100 * stolen / yield;

        dbMgr("INSERT INTO plot_crop_steal VALUES ('" + stealerID +"','" + friendID + "'," + plotID + "," + stolen + "," + newCumPercStolen + ");", true);
        dbMgr("UPDATE plot_crop_steal SET CumPercStolen = " + newCumPercStolen + " WHERE UserID = '" + friendID + "' and plotID = " + plotID + ";", true);

        // status = ;

        return "successful steal";
    }


    public int calculatePlotCropSteals(Member user, int plotID) {
        int lessYield = 0;

        // Retrieve the amount of less yield.
        ArrayList<PlotCropSteal> plotCropStealArr = dbMgr("select * from plot_crop_steal where UserID = '" + user.getUserID() + "' and plotID = " + plotID + ";", false);
        for (PlotCropSteal plotCropSteal : plotCropStealArr) {
            lessYield += plotCropSteal.getAmountStolen();
        }

        return lessYield;
    }

    public void removePlotCropSteals(Member user, int plotID) {
        // Remove the plot crop steals.
        dbMgr("DELETE FROM plot_crop_steal WHERE UserID = '" + user.getUserID() + "' and PlotID = '" + plotID + "';", true);       
    }
}
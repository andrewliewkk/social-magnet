package SocialMagnet.Farm;

import java.sql.*;
import java.util.*;

/**
 * PlotDAO
 */
public class PlotDAO {

    private RankDAO rankDAO = new RankDAO();

    public static ArrayList<Plot> dbMgr(String sqlstmt, boolean update) {
        String host = "localhost";
        int port = 3306;
        String dbName = "magnet";
        String dbURL = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false";

        String username = "root";
        String password = "";
        String sql = sqlstmt; // change statement here
        Statement stmt = null;
        Connection conn = null;

        ArrayList<Plot> plotList = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver").getConstructor().newInstance();
            conn = DriverManager.getConnection(dbURL, username, password);
            stmt = conn.createStatement();

            if (!update) {
                ResultSet rs = stmt.executeQuery(sql);
                
                while (rs.next()) {
                    int plotID = rs.getInt("PlotID");
                    String userID = rs.getString("UserID");
                    plotList.add(new Plot(plotID, userID));
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

        return plotList;
    }

    public ArrayList<Plot> getPlotsByUser(String userID) {
        
        ArrayList<Plot> plotList = dbMgr("select * from plot where UserID = '" + userID + "';", false);
        return plotList;
    }

    public void createBlankPlot(String userID) {
        dbMgr("INSERT INTO plot (`UserID`) VALUES ('"+userID+"');", true);
    }

    public void createNewPlots(String userID, String rank) {
        int numberPlots = rankDAO.retrievePlotNumByRank(rank);
        ArrayList<Plot> plotList = getPlotsByUser(userID);
        int currentPlots = plotList.size();
        int differenceInPlots = numberPlots - currentPlots;
        
        for (int i = 0; i < differenceInPlots; i++) {
            dbMgr("INSERT INTO plot (`PlotID`, `UserID`) VALUES (" + (i + 1 + currentPlots) + ",'" +userID+ "');", true);
        }

    }

    public void createNewPlotsForNewUser(String userID) {
        String rank = rankDAO.retrieveRank(0); // hardcoded 0 as xp will be 0 for new user
        int numberPlots = rankDAO.retrievePlotNumByRank(rank);
        
        for (int i = 0; i < numberPlots; i++) {
            dbMgr("INSERT INTO plot (`PlotID`, `UserID`) VALUES (" + (i + 1) + ",'" +userID+ "');", true);
        }

    }


}
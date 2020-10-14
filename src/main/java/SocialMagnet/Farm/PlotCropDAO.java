package SocialMagnet.Farm;

import SocialMagnet.Social.Member.*;
import java.sql.*;
import java.util.*;
import java.text.*;

/**
 * PlotCropDAO  
 */
public class PlotCropDAO {

    private MemberDAO memberDAO = new MemberDAO();

    public static ArrayList<PlotCrop> dbMgr(String sqlstmt, boolean update) {
        String host = "localhost";
        int port = 3306;
        String dbName = "magnet";
        String dbURL = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false";

        String username = "root";
        String password = "";
        String sql = sqlstmt; // change statement here
        Statement stmt = null;
        Connection conn = null;

        ArrayList<PlotCrop> plotCropList = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver").getConstructor().newInstance();
            conn = DriverManager.getConnection(dbURL, username, password);
            stmt = conn.createStatement();

            if (!update) {
                ResultSet rs = stmt.executeQuery(sql);
                
                while (rs.next()) {
                    String userID = rs.getString("UserID");
                    int plotID = rs.getInt("PlotID");
                    String cropName = rs.getString("CropName");
                    String timePlantedSQL = rs.getString("TimePlanted");
                    int yield = rs.getInt("Yield");

                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
                    java.util.Date timePlanted = dateFormat.parse(timePlantedSQL);

                    plotCropList.add(new PlotCrop(userID, plotID, cropName, timePlanted, yield));
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

        return plotCropList;
    }

    public ArrayList<PlotCrop> getPlotCropsByUser(Member user, ArrayList<Plot> plotList) {
        ArrayList<PlotCrop> plotCropList = new ArrayList<PlotCrop>();
        String userID = user.getUserID();

        for (Plot plot: plotList) {
            int plotID = plot.getPlotID();
            try {
                PlotCrop plotCrop = dbMgr("select * from plot_crop where UserID = '" + userID + "' and PlotID = " + plotID + ";", false).get(0);
                plotCropList.add(plotCrop);
            } catch (IndexOutOfBoundsException e) {
                PlotCrop plotCrop = null;
                plotCropList.add(plotCrop);
            }
        }
        return plotCropList;
    }

    public PlotCrop getPlotCropByID(String userID, int plotID) {
        PlotCrop plotCrop;
        try {
            plotCrop = dbMgr("select * from plot_crop where UserID = '" + userID + "' and PlotID = '"+plotID+"';", false).get(0);
        } catch (IndexOutOfBoundsException e) {
            plotCrop = null;
        }
        return plotCrop;
    }
    
    public void plantCrop(Member user, int plotID, Crop crop) {
        String userName = user.getUserID();
        String cropName = crop.getName();
        long millis = System.currentTimeMillis();  
        
        java.util.Date currentTime =new java.util.Date(millis); 
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
        String sqlDate = dateFormat.format(currentTime);  
        
        Random r = new Random();
        int minYield = crop.getMinYield();
        int maxYield = crop.getMaxYield() + 1;
        int yield = r.nextInt(maxYield - minYield) + minYield;

        dbMgr("insert into plot_crop values ('"+userName+"','"+plotID+"','"+cropName+"','"+sqlDate+"',"+yield+");", true);
    }

    public boolean removePlotCrop(Member user, int plotID, boolean haveToClear) {

        boolean clearSuccess = true;

        if (haveToClear) {
            // Check if more than 5 gold in memberDAO
            clearSuccess = memberDAO.clearSubtractGold(user);
        }
        
        if (clearSuccess) {
            dbMgr("delete from plot_crop where UserID = '" + user.getUserID() + "' and plotID = '" + plotID + "';", true);
        }

        return clearSuccess;
    }

    public void setNewTimePlanted(Member user, int plotID, java.util.Date newDateTime) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
        String sqlDate = dateFormat.format(newDateTime);  

        dbMgr("UPDATE plot_crop SET TimePlanted = '" + sqlDate + "' WHERE UserID = '" + user.getUserID() + 
                "' and plotID = '" + plotID + "';", true);
    }

}
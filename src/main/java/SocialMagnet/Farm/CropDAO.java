package SocialMagnet.Farm;

import java.sql.*;
import java.util.*;

/**
 * CropDAO
 */
public class CropDAO {

    public static ArrayList<Crop> dbMgr(String sqlstmt, boolean update) {
        String host = "localhost";
        int port = 3306;
        String dbName = "magnet";
        String dbURL = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false";

        String username = "root";
        String password = "";
        String sql = sqlstmt; // change statement here
        Statement stmt = null;
        Connection conn = null;

        ArrayList<Crop> cropList = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver").getConstructor().newInstance();
            conn = DriverManager.getConnection(dbURL, username, password);
            stmt = conn.createStatement();

            if (!update) {
                ResultSet rs = stmt.executeQuery(sql);
                
                while (rs.next()) {
                    String name = rs.getString("Name");
                    int cost = rs.getInt("Cost");
                    int time = rs.getInt("Time");
                    int XP = rs.getInt("Xp");
                    int minYield = rs.getInt("MinYield");
                    int maxYield = rs.getInt("MaxYield");
                    int salePrice = rs.getInt("SalePrice");

                    cropList.add(new Crop(name, cost, time, XP, minYield, maxYield, salePrice));
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

        return cropList;
    }

    public ArrayList<Crop> getAllCrops() {
        return dbMgr("select * from crop", false);
    }

    public Crop retrieveCropByName(String name) {
        try {
            return dbMgr("select * from crop where Name = '"+name+"';", false).get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

}
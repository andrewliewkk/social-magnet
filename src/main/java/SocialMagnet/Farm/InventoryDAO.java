package SocialMagnet.Farm;

import java.sql.*;
import java.util.*;

import SocialMagnet.Social.Member.Member;
import SocialMagnet.Utility.Gift;

/**
 * InventoryDAO
 */
public class InventoryDAO {

    public static ArrayList<Inventory> dbMgr(String sqlstmt, boolean update) {
        String host = "localhost";
        int port = 3306;
        String dbName = "magnet";
        String dbURL = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false";

        String username = "root";
        String password = "";
        String sql = sqlstmt; // change statement here
        Statement stmt = null;
        Connection conn = null;

        ArrayList<Inventory> inventoryList = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver").getConstructor().newInstance();
            conn = DriverManager.getConnection(dbURL, username, password);
            stmt = conn.createStatement();

            if (!update) {
                ResultSet rs = stmt.executeQuery(sql);
                
                while (rs.next()) {
                    String userID = rs.getString("UserID");
                    String cropName = rs.getString("CropName");
                    int quantity = rs.getInt("Quantity");

                    inventoryList.add(new Inventory(userID, cropName, quantity));
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

        return inventoryList;
    }

    public ArrayList<Inventory> retrieveInventory(String userID) {
        return dbMgr("select * from inventory where UserID = '"+userID+"' and Quantity != 0;", false);
    }

    public void processGifts(Member user, ArrayList<Gift> gifts) {
        String userID = user.getUserID();

        for (Gift gift : gifts) {
            String cropName = gift.getGiftContent();
            ArrayList<Inventory> inv = dbMgr("select * from inventory where UserID = '"+userID+"' and CropName = '"+cropName+"';", false);
            if (inv.size() != 0) {
                dbMgr("UPDATE inventory SET Quantity = Quantity + 1 where UserID = '" + userID + "' and CropName = '" + cropName + "';", true);
            } else {
                dbMgr("insert into inventory values ('"+userID+"','"+cropName+"',"+ 1 +");", true);
            }
            ArrayList<Inventory> userInventory = retrieveInventory(userID);
            for (Inventory inventory : userInventory) {
                String seed = inventory.getCropName();
                if (seed.equals(gift.getGiftContent())) {
                    int currentQty = inventory.getQuantity();
                    inventory.setQuantity(++currentQty);
                }
            }
        }

    }
    
    public void plantCrop(Inventory invCrop) {
        String userID = invCrop.getOwner();
        String cropName = invCrop.getCropName();
        dbMgr("update inventory set Quantity = Quantity - 1 where UserID ='"+userID+"' and CropName = '"+cropName+"';", true);
    }

    public void buySeed(String userID, String cropName, int quantity) {
        try {
            Inventory inv = dbMgr("select * from inventory where UserID = '"+userID+"' and CropName = '"+cropName+"';", false).get(0);
            if (inv != null) {
                dbMgr("update inventory set Quantity = Quantity + "+quantity+" where UserID ='"+userID+"' and CropName = '"+cropName+"';", true);
            }else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            dbMgr("insert into inventory values ('"+userID+"','"+cropName+"',"+quantity+");", true);
        }
    }
}
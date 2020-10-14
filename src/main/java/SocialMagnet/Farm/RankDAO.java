package SocialMagnet.Farm;

import java.sql.*;
import java.util.*;

/**
 * RankDAO
 */
public class RankDAO {

    public static ArrayList<Rank> dbMgr(String sqlstmt, boolean update) {
        String host = "localhost";
        int port = 3306;
        String dbName = "magnet";
        String dbURL = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false";

        String username = "root";
        String password = "";
        String sql = sqlstmt; // change statement here
        Statement stmt = null;
        Connection conn = null;

        ArrayList<Rank> rankList = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver").getConstructor().newInstance();
            conn = DriverManager.getConnection(dbURL, username, password);
            stmt = conn.createStatement();

            if (!update) {
                ResultSet rs = stmt.executeQuery(sql);
                
                while (rs.next()) {
                    String rank = rs.getString("Rank");
                    int XP = rs.getInt("Xp");
                    int plots = rs.getInt("Plots");
                    rankList.add(new Rank(rank, XP, plots));
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

        return rankList;
    }

    public ArrayList<Rank> retrieveAll(){
        ArrayList<Rank> rankList = dbMgr("select * from rank;", false);
        return rankList;
    }

    public String retrieveRank(int XP){
        ArrayList<Rank> rankList = dbMgr("select * from rank where XP = (select max(XP) from rank where xp <= " + XP + ");" , false);
        if (rankList.isEmpty()) {
            return null;
        }
        return rankList.get(0).getRank();
    }

    public int retrievePlotNumByRank(String rank) {
        ArrayList<Rank> rankList = dbMgr("select * from rank where rank = '" + rank + "';", false);
        if (rankList.isEmpty()) {
            return -1;
        }
        return rankList.get(0).getPlots();
    }
}
package datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by maria on 3/30/2017.
 */
public class ClientGateway {

    public static ResultSet selectAllInfo() {
        try {
            Connection conn = DBConn.getInstance().getConnection();
            String sql = "SELECT * FROM client";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet res = stmt.executeQuery();
            return res;
        } catch(SQLException e) {
            System.err.println(e);
            return null;
        }
    }

    public static ResultSet selectTransactions(int clientID) {
        try {
            Connection conn = DBConn.getInstance().getConnection();
            String sql = "SELECT * FROM clienttransactions WHERE clientID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, clientID);
            ResultSet res = stmt.executeQuery();
            return res;
        } catch(SQLException e) {
            System.err.println(e);
            return null;
        }
    }

    public static String selectName(int clID) {
        try {
            Connection conn = DBConn.getInstance().getConnection();
            String sql = "SELECT name FROM client WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, clID);
            ResultSet res = stmt.executeQuery();
            res.next();
            return res.getString(1);
        } catch(SQLException e) {
            System.err.println(e);
            return null;
        }
    }

    public static String selectCNP(int clID) {
        try {
            Connection conn = DBConn.getInstance().getConnection();
            String sql = "SELECT cnp FROM client WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, clID);
            ResultSet res = stmt.executeQuery();
            res.next();
            return res.getString(1);
        } catch(SQLException e) {
            System.err.println(e);
            return null;
        }
    }

    public static String selectAdress(int clID) {
        try {
            Connection conn = DBConn.getInstance().getConnection();
            String sql = "SELECT address FROM client WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, clID);
            ResultSet res = stmt.executeQuery();
            res.next();
            return res.getString(1);
        } catch(SQLException e) {
            System.err.println(e);
            return null;
        }
    }

    public static boolean edit(int id, String name, long CNP, String adress) {
        try {
            Connection conn = DBConn.getInstance().getConnection();
            String sql = "UPDATE client SET name = ?, cnp = ?, address = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setLong(2, CNP);
            stmt.setString(3, adress);
            stmt.setInt(4, id);
            stmt.executeUpdate();
            return true;
        } catch(SQLException e) {
            System.err.println("Eroare in metoda edit() din clientGateway " + e);
            return false;
        }
    }
}

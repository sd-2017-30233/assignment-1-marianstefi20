package datasource;

/**
 * @author: marian
 * A helper class designed to contain all the methods that communicate with the database
 * (basic CRUD ops. are here, and they are used throughout the files
 */

import java.sql.*;
import java.util.ArrayList;

public class UserGateway {
    private String name = null;
    private String role = null;


    public static ResultSet selectAllInfo() {
        try {
            Connection conn = DBConn.getInstance().getConnection();
            String sql = "SELECT id,name,role FROM users";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet res = stmt.executeQuery();
            return res;
        } catch(SQLException e) {
            System.err.println(e);
            return null;
        }
    }


    // check if username and password exists - available only to users - employees or administrators
    public static boolean ifuserexists(String username, String password) {
        try {
            Connection conn = DBConn.getInstance().getConnection();
            String sql = "SELECT username, password FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet res = stmt.executeQuery();
            return res.next(); //careful careful with this one
        } catch(SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    public static int selectID(String username, String password) {
        try {
            Connection conn = DBConn.getInstance().getConnection();
            String sql = "SELECT id FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet res = stmt.executeQuery();
            res.next();
            return res.getInt(1);
        } catch(SQLException e) {
            System.err.println(e);
            return -1;
        }
    }

    public static int selectRole(String username, String password) {
        try {
            Connection conn = DBConn.getInstance().getConnection();
            String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet res = stmt.executeQuery();
            res.next();
            return res.getInt(1);
        } catch(SQLException e) {
            System.err.println(e);
            return -1;
        }
    }

    public static String selectName(int id) {
        try {
            Connection conn = DBConn.getInstance().getConnection();
            String sql = "SELECT name FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            res.next();
            return res.getString(1);
        } catch(SQLException e) {
            System.err.println(e);
            return null;
        }
    }

    public static String selectUsername(int id) {
        try {
            Connection conn = DBConn.getInstance().getConnection();
            String sql = "SELECT username FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            res.next();
            return res.getString(1);
        } catch(SQLException e) {
            System.err.println(e);
            return null;
        }
    }

    public static String selectPassword(int id) {
        try {
            Connection conn = DBConn.getInstance().getConnection();
            String sql = "SELECT password FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            res.next();
            return res.getString(1);
        } catch(SQLException e) {
            System.err.println(e);
            return null;
        }
    }
}
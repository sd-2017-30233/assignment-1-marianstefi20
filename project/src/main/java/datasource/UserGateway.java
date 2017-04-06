package datasource;

/**
 * @author: marian
 * A helper class designed to contain all the methods that communicate with the database
 * (basic CRUD ops. are here, and they are used throughout the files
 */

import java.sql.*;
import java.util.ArrayList;

public class UserGateway {

    public ResultSet selectAllInfo() {
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
    public boolean ifuserexists(String username, String password) {
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

    public int selectID(String username, String password) {
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

    public int selectRole(int id) {
        try {
            Connection conn = DBConn.getInstance().getConnection();
            String sql = "SELECT role FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1,id);
            ResultSet res = stmt.executeQuery();
            res.next();
            return res.getInt(1);
        } catch(SQLException e) {
            System.err.println(e);
            return -1;
        }
    }

    public String selectName(int id) {
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

    public String selectUsername(int id) {
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

    public String selectPassword(int id) {
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

    public boolean insert(String name, int role, String username, String password) {
        try {
            Connection conn = DBConn.getInstance().getConnection();
            String sql = "INSERT INTO users VALUES(?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, role);
            stmt.setString(3, username);
            stmt.setString(4, password);
            stmt.executeQuery();
            return true;
        } catch(SQLException e) {
            System.err.println("Eroare in metoda edit() din clientGateway " + e);
            return false;
        }
    }

    public boolean update(int id, String name, int role, String username, String password) {
        try {
            Connection conn = DBConn.getInstance().getConnection();
            String sql = "UPDATE users SET name = ?, role = ?, username = ?, password = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, role);
            stmt.setString(3, username);
            stmt.setString(4, password);
            stmt.setInt(5, id);
            stmt.executeUpdate();
            return true;
        } catch(SQLException e) {
            System.err.println("Eroare in metoda edit() din clientGateway " + e);
            return false;
        }
    }

    public boolean delete(int id) {
        try {
            Connection conn = DBConn.getInstance().getConnection();
            String sql = "DELETE FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeQuery();
            return true;
        } catch(SQLException e) {
            System.err.println("Eroare in metoda edit() din clientGateway " + e);
            return false;
        }
    }
}
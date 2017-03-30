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
}
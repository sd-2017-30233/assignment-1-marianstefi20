package bussiness;

import datasource.ClientGateway;
import datasource.DataMapper;
import datasource.UserGateway;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by maria on 3/30/2017.
 */
public class UserController {

    public static int getID(String username, String password) {return UserGateway.selectID(username, password);}

    public static int getRole(String username, String password) {
        return UserGateway.selectRole(username, password);
    }

    public static String getName(int employeeID) {
        return UserGateway.selectName(employeeID);
    }

    public static String getUsername(int employeeID) {
        return UserGateway.selectUsername(employeeID);
    }

    public static String getPassword(int employeeID) {
        return UserGateway.selectPassword(employeeID);
    }

    public static ArrayList<ArrayList<String>> getAllEmployeesAsTable() {
        ResultSet rs = UserGateway.selectAllInfo();
        return DataMapper.getAllasTable(rs);
    }

}

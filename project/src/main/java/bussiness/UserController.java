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
    private int id;
    private String name;
    private int role;
    private String username;
    private String password;
    private UserGateway userGateway = new UserGateway();

    public UserController(String name, Integer role, String username, String password) {
        this.name = name;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    public UserController(String username, String password) {
        this.username = username;
        this.password = password;
        this.id = userGateway.selectID(username, password);
        this.name = userGateway.selectName(id);
        this.role = userGateway.selectRole(id);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void insert(UserController user) {
        String name = user.getName();
        int role = user.getRole();
        String username = user.getUsername();
        String password = user.getPassword();
        userGateway.insert(name, role, username, password);
    }

    public void update() {
        userGateway.update(id, name, role, username, password);
    }

    public void delete() {
        userGateway.delete(id);
    }

    public ArrayList<ArrayList<String>> getAllEmployeesAsTable() {
        ResultSet rs = userGateway.selectAllInfo();
        return DataMapper.getAllasTable(rs);
    }

}

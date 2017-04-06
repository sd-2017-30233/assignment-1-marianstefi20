package bussiness;

import datasource.ClientGateway;
import datasource.DataMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by maria on 3/30/2017.
 */
public class ClientController {
    private int id;
    private String name;
    private long cnp;
    private String adress;
    private ClientGateway clientGateway = new ClientGateway();

    // A constructor that usually handles inserts
    public ClientController(String name, Long cnp, String adress) {
        this.name = name;
        this.cnp = cnp;
        this.adress = adress;
    }

    // A constructor that usually handles an existing client
    public ClientController(int clientID) {
        this.id = clientID;
        this.name = clientGateway.selectName(id);
        this.cnp = clientGateway.selectCNP(id);
        this.adress = clientGateway.selectAdress(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCnp() {
        return cnp;
    }

    public void setCnp(long cnp) {
        this.cnp = cnp;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void insert(ClientController client) {
        String name = client.name;
        Long cnp = client.cnp;
        String adress = client.adress;
        clientGateway.insert(name, cnp, adress);
    }

    public boolean update() {
        return clientGateway.update(id, name, cnp, adress);
    }

    public void delete() {
        clientGateway.delete(id);
    }

    public ArrayList<ArrayList<String>> getTransactionsAsTable() {
        ResultSet rs = clientGateway.selectTransactions(id);
        return DataMapper.getAllasTable(rs);
    }

    // General methods that are not specific to a single client
    public static JSONArray getAllAsJSON() {
        ResultSet all = ClientGateway.selectAllInfo();
        try {
            return DataMapper.convertToJSON(all);
        } catch(Exception e) {
            System.err.println("Eroare in Client Controoler in getAllAsJSON() " + e);
        }
        return null;
    }

    public static ArrayList<ArrayList<String>> getAllClientsAsTable() {
        ResultSet rs = ClientGateway.selectAllInfo();
        return DataMapper.getAllasTable(rs);
    }

}

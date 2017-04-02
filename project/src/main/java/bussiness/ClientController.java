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

    public static ArrayList<ArrayList<String>> getTransactionsAsTable(int clientID) {
        ResultSet rs = ClientGateway.selectTransactions(clientID);
        return DataMapper.getAllasTable(rs);
    }

    public static String getName(int clientID) {
        return ClientGateway.selectName(clientID);
    }

    public static String getCNP(int clientID) {
        return ClientGateway.selectCNP(clientID);
    }

    public static String getAdress(int clientID) {
        return ClientGateway.selectAdress(clientID);
    }

    public static boolean edit(int id, String name, Long cnp, String adress) {
        return ClientGateway.edit(id, name, cnp, adress);
    }
}

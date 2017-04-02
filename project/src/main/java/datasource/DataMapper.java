package datasource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by maria on 4/2/2017.
 */
public class DataMapper {

    /**
     * Many thanks to the guys from stack overflow http://stackoverflow.com/questions/36562487/resultset-to-json-using-gson
     * @param resultSet
     * @return
     * @throws Exception
     */
    public static JSONArray convertToJSON(ResultSet resultSet) throws SQLException,JSONException {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < total_rows; i++) {
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getObject(i + 1));
            }
            jsonArray.put(obj);
        }
        return jsonArray;
    }

    public static ArrayList<ArrayList<String>> getAllasTable(ResultSet rs) {
        ArrayList<ArrayList<String>> tableArray = new ArrayList<>();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int nrOfColumns = rsmd.getColumnCount();
            while(rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                for(int i=1;i<=nrOfColumns;i++) {
                    row.add(rs.getString(i));
                }
                tableArray.add(row);
            }
        } catch(SQLException e) {
            System.err.println("A aparut o eroare in getAllasTable din clientController " + e);
        }
        return tableArray;
    }
}

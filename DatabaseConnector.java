import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;

public final class DatabaseConnector {

    private static Connection connect = null;
    private static Statement statement = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;

    public static void establishConnection() throws Exception {
        try {
            close();
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://71.8.120.253:8001/companiondiscoverer?user=root&password=password");
        }
        catch (Exception e) {
            throw e;
        }
    }

    // You need to close the resultSet
    private static void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

    public static void addAccount(String params[]) throws SQLException, Exception {
        establishConnection();
        String username = params[0].substring(params[0].lastIndexOf('=') + 1);
        String password = params[1].substring(params[1].lastIndexOf('=') + 1);
        String firstName = params[2].substring(params[2].lastIndexOf('=') + 1);
        String lastName = params[3].substring(params[3].lastIndexOf('=') + 1);
        String email = params[4].substring(params[4].lastIndexOf('=') + 1);
        email = email.replace("%40", "@");
        String mobileNumber = params[5].substring(params[5].lastIndexOf('=') + 1);
        String insertStatement = "INSERT INTO account VALUES ("
            + "default," 
            + "'" + firstName + "',"
            + "'" + lastName + "'," 
            + "'" + mobileNumber + "'," 
            + "'" + email + "'," 
            + "'" + username + "'," 
            + "'" + password + "'," 
            + "default," 
            + "'User'" 
            + ");";
        preparedStatement = connect.prepareStatement(insertStatement);
        preparedStatement.executeUpdate();

        String query = "SELECT profile_id FROM account WHERE username='" + username + "' AND type='User';";  // gets profile id
        JSONArray jsonArr = execute(query);
        int profile_id = profile_id = jsonArr.getJSONObject(0).getInt("Profile_ID");
        insertStatement = "INSERT INTO profile VALUES (" + profile_id + ");";
        manipulate(insertStatement);
    }

    public static void addAnimal(String params[]) throws SQLException, Exception {
        establishConnection();
        String name = params[0].substring(params[0].lastIndexOf('=') + 1);
        String type = params[1].substring(params[1].lastIndexOf('=') + 1);
        String breed = params[2].substring(params[2].lastIndexOf('=') + 1);
        String size = params[3].substring(params[3].lastIndexOf('=') + 1);
        String color = params[4].substring(params[4].lastIndexOf('=') + 1);
        String age = params[5].substring(params[5].lastIndexOf('=') + 1);
        String sex = params[6].substring(params[6].lastIndexOf('=') + 1);
        String description = params[7].substring(params[7].lastIndexOf('=') + 1);
        description = description.replace("+", " ");        
        String picURL = params[8].substring(params[8].lastIndexOf('=') + 1);
        String adoptionFee = params[9].substring(params[9].lastIndexOf('=') + 1);
        String insertStatement = "INSERT INTO animal VALUES ("
            + "default," 
            + "'" + description + "',"
            + age + "," 
            + "'" + name + "'," 
            + "'" + type + "'," 
            + "'" + breed + "'," 
            + "'" + size + "',"
            + "'" + color + "'," 
            + "default," 
            + "default," 
            + "'" + sex + "'," 
            + "'" + picURL + "'" 
            + adoptionFee 
            + ");";
    }

    public static String authenticate(String params[]) throws SQLException, Exception {
        establishConnection();
        String username = params[0].substring(params[0].lastIndexOf('=') + 1);
        String password = params[1].substring(params[1].lastIndexOf('=') + 1);
        String query = "SELECT * FROM account WHERE username='" + username + "' AND password='" + password + "' AND type='Admin';";
        statement = connect.createStatement();
        resultSet = statement.executeQuery(query);
        JSONArray jsonArr = ResultSetConverter.ResultSetToJSON(resultSet);
        String permissionLevel = "invalid";
        if (!jsonArr.toString().equals("[]")) {
            permissionLevel = "admin";
        }

        query = "SELECT * FROM account WHERE username='" + username + "' AND password='" + password + "' AND type='User';";
        statement = connect.createStatement();
        resultSet = statement.executeQuery(query);
        jsonArr = ResultSetConverter.ResultSetToJSON(resultSet);
        
        if (!jsonArr.toString().equals("[]")) {
            permissionLevel = "profile";
        }
        
        if (permissionLevel.equals("admin")) {
            return "/html/admin.html";
        }
        else if (permissionLevel.equals("profile")) {
            return "/html/user.html";
        }
        return "/html/invalidlogin.html";
    }

    public static String adoptAnimal(String[] params) throws Exception {
        establishConnection();
        String username = params[0].substring(params[0].lastIndexOf('=') + 1);
        String animal_id = params[1].substring(params[1].lastIndexOf('=') + 1);
        try {
        String query = "SELECT profile_id FROM account WHERE username='" + username + "' AND type='User';";  // gets profile id
        JSONArray jsonArr = execute(query);
        int profile_id = -1;
        if (jsonArr.toString().equals("[]")) {
            return "/html/failure.html";
        }
        else {
            profile_id = jsonArr.getJSONObject(0).getInt("Profile_ID");
        }
        query = "SELECT count(*) FROM animal WHERE animal_id=" + animal_id + " AND available=1;";
        jsonArr = execute(query);
        if (jsonArr.toString().equals("[]")) {
            return "/html/failure.html";
        }
        query = "UPDATE animal SET available=0 WHERE animal_id=" + animal_id + ";";
        manipulate(query);
        query = "INSERT into adoption values (" + animal_id + ", " + profile_id + ", default);";
        manipulate(query);
        return "/html/success.html";
        }
        catch (Exception e) {
            return "/html/failure.html";
        }
    }

    private static JSONArray execute(String query) throws Exception {
        statement = connect.createStatement();
        resultSet = statement.executeQuery(query);
        return ResultSetConverter.ResultSetToJSON(resultSet);
    }

    private static void manipulate(String query) throws Exception {
        preparedStatement = connect.prepareStatement(query);
        preparedStatement.executeUpdate();
    }

    public static JSONArray getAnimals(String[] params) throws Exception {
        establishConnection();
        String query = "SELECT * FROM Animal";
        if (params != null) {
            query += " WHERE ";
            int paramNumber = params.length;
            for (int i = 0; i < paramNumber; i++) {
                String key = params[i].substring(0, params[i].lastIndexOf('='));
                String value = params[i].substring(params[i].lastIndexOf('=') + 1); // value
                if (!value.isEmpty()) {
                    query += key + "='" + value + "' AND ";
                }
            }
        }
        query += "'1'='1';";
        SocketServer.timestamp(query);
        JSONArray jsonArr = null;
        statement = connect.createStatement();
        resultSet = statement.executeQuery(query);
        jsonArr = ResultSetConverter.ResultSetToJSON(resultSet);
        return jsonArr;
    }

    public static void updateAnimal(int id, String columnName, Object data) throws SQLException {
        String updateStatement = "UPDATE animal SET " + columnName + " = " + data + " WHERE animal_ID = " + id;
        statement = connect.createStatement();
        resultSet = statement.executeQuery(updateStatement); 
    }

    public static void makeAdoption() {
        
    }

    public static JSONArray getDonations() throws Exception {
        
        establishConnection();
        JSONArray jsonArr = null;
        statement = connect.createStatement();
        resultSet = statement.executeQuery("select * from donation");
        jsonArr = ResultSetConverter.ResultSetToJSON(resultSet);
        return jsonArr;
    
    }
}


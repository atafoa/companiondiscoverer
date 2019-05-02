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
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://71.8.120.253:8001/companiondiscoverer?user=root&password=password");
        }
        catch (Exception e) {
            throw e;
        }
    }

    public static JSONArray readDatabase() throws Exception {
        JSONArray jsonArr = null;
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://71.8.120.253:8001/companiondiscoverer?"
                            + "user=root&password=password");

            statement = connect.createStatement();

            resultSet = statement
                    .executeQuery("select * from test.testtable");
            jsonArr = ResultSetConverter.ResultSetToJSON(resultSet);
            writeResultSet(resultSet);

        }
        catch (Exception e) {
            throw e;
        }
        finally {
            close();
        }
        return jsonArr;
    }

    private static void writeResultSet(ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getSTring(2);
            String user = resultSet.getString("name");
            System.out.println("User: " + user);
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
            + "'Profile'" 
            + ");";
        preparedStatement = connect.prepareStatement(insertStatement);
        preparedStatement.executeUpdate();
        close();
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
            + ");";
        preparedStatement = connect.prepareStatement(insertStatement);
        preparedStatement.executeUpdate();
        close();
    }

    public static String authenticate(String params[]) throws SQLException, Exception {
        establishConnection();
        String username = params[0].substring(params[0].lastIndexOf('=') + 1);
        String password = params[1].substring(params[1].lastIndexOf('=') + 1);
        String query = "SELECT * FROM account WHERE username='" + username + "' AND password='" + password + "' AND type='Admin';";
        statement = connect.createStatement();
        resultSet = statement.executeQuery(query);
        JSONArray jsonArr = ResultSetConverter.ResultSetToJSON(resultSet);

        if (!jsonArr.toString().equals("[]")) {
            return "admin";
        }

        query = "SELECT * FROM account WHERE username='" + username + "' AND password='" + password + "' AND type='Profile';";
        statement = connect.createStatement();
        resultSet = statement.executeQuery(query);
        jsonArr = ResultSetConverter.ResultSetToJSON(resultSet);
        
        if (!jsonArr.toString().equals("[]")) {
            return "profile";
        }
        return "invalid";
    }

    //public static ResultSet getUsers() {
        // select users
    //}

    public static void addAnimal(String desc, int age, String name, String type, String breed, String size, String color, boolean available, char sex, String pictureURL) throws SQLException {
        String insertStatement = "INSERT INTO animal VALUES (" + desc + "," + age + "," + name + "," + type + "," + breed + "," + size + "," + color + "," + available + "," + new Date() + "," + sex + "," + pictureURL + ");";
        statement = connect.createStatement();
        resultSet = statement.executeQuery(insertStatement);
    }

    //public static ResultSet getAnimal() {
        // select users
    //}

    public static void updateAnimal(int id, String columnName, Object data) throws SQLException {
        String updateStatement = "UPDATE animal SET " + columnName + " = " + data + " WHERE animal_ID = " + id;
        statement = connect.createStatement();
        resultSet = statement.executeQuery(updateStatement); 
    }

    public static void makeAdoption() {
        
    }
}


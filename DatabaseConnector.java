import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseConnector {
    private static Connection connect = null;
    private static Statement statement = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;

    public static JSONArray readDatabase() throws Exception {
        JSONArray jsonArr = null;
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://71.8.120.253:8001/test?"
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

}

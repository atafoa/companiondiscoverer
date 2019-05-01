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

    public static void readDatabase() throws Exception {
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
            writeResultSet(resultSet);

        }
        catch (Exception e) {
            throw e;
        }
        finally {
            close();
        }

    }

    private static void writeMetaData(ResultSet resultSet) throws SQLException {
        //  Now get some metadata from the database
        // Result set get the result of the SQL query

        System.out.println("The columns in the table are: ");

        System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
        for (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++) {
            System.out.println("Column " + i + " " + resultSet.getMetaData().getColumnName(i));
        }
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

    public static void addAccount(String firstName, String lastName, int mobileNumber, String email, String username, String password, String type) throws SQLException {
        String insertStatement = "INSERT INTO account VALUES (" + firstName + "," + lastName + "," + mobileNumber + "," + email + "," + username + "," + password + "," + new Date() + "," + type + ");";
        statement = connect.createStatement();
        resultSet = statement.executeQuery(insertStatement);
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


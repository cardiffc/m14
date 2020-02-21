import java.sql.*;
import java.util.TreeMap;

public class DBConnection
{
    private static Connection connection;

    private static String dbName = "learn";
    private static String dbUser = "root";
    private static String dbPass = "test";
    private static String URL = "jdbc:mysql://localhost:3306/learn";
    public static Connection getConnection()
    {
        if(connection == null)
        {
            try {

                connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + dbName +
                    "?user=" + dbUser + "&password=" + dbPass + "&useSSL=true");
               // connection = DriverManager.getConnection(URL, dbUser,dbPass);
                connection.createStatement().execute("DROP TABLE IF EXISTS voter_count");
                String create = "CREATE TABLE voter_count(" +
                        "id int not null auto_increment, "+
                        "name tinytext not null, "+
                        "birthDate date not null, " +
                        "count int not null, "+
                        "primary key(id), " +
                        //"key(name(50)), " +
                        "unique key (name(50), birthDate))";
                connection.createStatement().execute(create);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void executeMultiInsert(StringBuilder query2) throws SQLException
    {
        String query = "INSERT INTO voter_count(name,birthDate,count) " +
            "VALUES" + query2.toString() +
            " ON DUPLICATE KEY UPDATE count=count + 1";
        DBConnection.getConnection().createStatement().execute(query);
    }
    public static void printVoterCounts() throws SQLException
    {

        String sql = "SELECT name, birthDate, count FROM voter_count WHERE count > 1";
        ResultSet rs = DBConnection.getConnection().createStatement().executeQuery(sql);
        while(rs.next())
        {
            System.out.println("\t" + rs.getString("name") + " (" +
                    rs.getString("birthDate") + ") - " + rs.getInt("count"));
        }
    }
}

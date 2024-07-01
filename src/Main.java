import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/books";
    private static final String username = "root";
    private static final String password = "";

    public static void main(String[] args) {
        try {
            Connection connection = getConnection();
            Methods methods = new Methods(connection);
            methods.createTables();
            methods.menu();
        } catch (Exception e) {
            System.out.println("Error!! : " + e.getMessage());
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}

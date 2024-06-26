import java.sql.Connection;
import java.sql.DriverManager;


public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/books";
        String username = "root";
        String password = "";
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, username, password);
            Methods.createTables(connection);
            Methods.menu(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
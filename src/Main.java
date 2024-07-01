import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/books";
    private static final String username = "root";
    private static final String password = "";

    public static void main(String[] args) {
        Methods method = new Methods();
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            method.createTables(connection);
            method.menu(connection);
        } catch (Exception e) {
            System.out.println("Error!! : " + e.getMessage());
        }
    }
}
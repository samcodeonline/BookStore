import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/books";
        String username = "root";
        String password = "";
        Connection connection;

        try {
            connection = DriverManager.getConnection(url, username, password);
            Methods.createTable(connection);
            Scanner intTypes = new Scanner(System.in);
            boolean running = true;
            while (running) {
                Methods.menu();
                int choose = intTypes.nextInt();
                switch (choose) {
                    case 1 -> Methods.insertion(connection);
                    case 2 -> Methods.update(connection);
                    case 3 -> Methods.deleteAuthor(connection);
                    case 4 -> Methods.delete(connection);
                    case 5 -> Methods.search(connection);
                    case 6 -> Methods.insertMember(connection);
                    case 7 -> Methods.signIn(connection);
                    case 8 -> Methods.Join(connection);
                    case 9 -> Methods.insertBookWithAuthors(connection);
                    case 10 -> {
                        System.out.println("Exit");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice, please select a valid option.");
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
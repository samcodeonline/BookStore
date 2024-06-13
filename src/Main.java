import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/books";
        String username = "root";
        String password = "";
        Connection connection;

        try {
            connection = DriverManager.getConnection(url, username, password);
            Statement stmt = connection.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS author_table (author_id INT AUTO_INCREMENT, author_name VARCHAR(55), PRIMARY KEY (author_id))";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS book (id INT AUTO_INCREMENT, book_name VARCHAR(55), author_id INT, PRIMARY KEY (id), FOREIGN KEY (author_id) REFERENCES author_table(author_id))";
            stmt.executeUpdate(sql);

            String createMemberTable = "CREATE TABLE IF NOT EXISTS library_member (member_id INT AUTO_INCREMENT, name VARCHAR(55), age_group VARCHAR(10), personal_info VARCHAR(10), gender VARCHAR(10), father_name VARCHAR(55), cnic VARCHAR(15), email VARCHAR(55), password VARCHAR(255), contact_number VARCHAR(15), PRIMARY KEY (member_id))";
            stmt.executeUpdate(createMemberTable);

            System.out.println("Tables created successfully...");
            Scanner intTypes = new Scanner(System.in);
            boolean running = true;
            while (running) {
                Methods.menu();
                int choose = intTypes.nextInt();
                switch (choose) {
                    case 1 -> Methods.insertion(connection);
                    case 2 -> Methods.update(connection);
                    case 3 -> Methods.delete(connection);
                    case 4 -> Methods.search(connection);
                    case 5 -> Methods.insertAuthor(connection);
                    case 6 -> Methods.insertMember(connection);
                    case 7 -> Methods.signIn(connection);
                    case 8 -> Methods.displayJoinResults(connection);
                    case 9 -> {
                        System.out.println("Exit");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice, please select a valid option.");
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
/*
 see many to many relation
 the thing is that we can
 have a separate table for another author and
  then can create and a join between the created
   table and the book table 
*/
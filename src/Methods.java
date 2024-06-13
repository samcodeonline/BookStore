import java.sql.*;
import java.util.Scanner;

public class Methods {
    public static void menu() {
        System.out.println("1. Insert Books");
        System.out.println("2. Update Book");
        System.out.println("3. Delete Record");
        System.out.println("4. Search Record");
        System.out.println("5. Insert Authors");
        System.out.println("6. Insert Members");
        System.out.println("7. Member Sign In");
        System.out.println("8. Display Join Query Results");
        System.out.println("9. Exit");
    }

    public static int getAuthorId(Connection connection, String authorName) throws SQLException {
        String query = "SELECT author_id FROM author_table WHERE author_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, authorName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("author_id");
            }
        }
        return -1;
    }

    public static void insertAuthor(Connection connection) throws SQLException {
        Scanner stringTypes = new Scanner(System.in);
        System.out.println("Author Name: ");
        String authorName = stringTypes.nextLine();

        String query = "INSERT INTO author_table (author_name) VALUES (?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, authorName);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("Author inserted with ID: " + rs.getInt(1));
            }
        }
    }

    public static void insertMember(Connection connection) throws SQLException {
        Scanner stringTypes = new Scanner(System.in);
        System.out.println("Name: ");
        String name = stringTypes.nextLine();
        System.out.println("Age Group: ");
        String ageGroup = stringTypes.nextLine();
        System.out.println("Personal Info (Student, Working, Other): ");
        String personalInfo = stringTypes.nextLine();
        System.out.println("Gender: ");
        String gender = stringTypes.nextLine();
        System.out.println("Father Name: ");
        String fatherName = stringTypes.nextLine();
        System.out.println("CNIC: ");
        String CNIC = stringTypes.nextLine();
        System.out.println("Email: ");
        String email = stringTypes.nextLine();
        System.out.println("Contact Number: ");
        String contactNumber = stringTypes.nextLine();
        System.out.println("Password: ");
        String passwordInput = stringTypes.nextLine();

        String memberQuery = "INSERT INTO library_member (name, age_group, personal_info, gender, father_name, cnic, email, contact_number, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement memberStatement = connection.prepareStatement(memberQuery);
        memberStatement.setString(1, name);
        memberStatement.setString(2, ageGroup);
        memberStatement.setString(3, personalInfo);
        memberStatement.setString(4, gender);
        memberStatement.setString(5, fatherName);
        memberStatement.setString(6, CNIC);
        memberStatement.setString(7, email);
        memberStatement.setString(8, contactNumber);
        memberStatement.setString(9, passwordInput);
        memberStatement.executeUpdate();
        System.out.println("Member registered successfully.");
    }

    public static void signIn(Connection connection) throws SQLException {
        Scanner stringTypes = new Scanner(System.in);
        System.out.println("Enter Name: ");
        String signInName = stringTypes.nextLine();
        System.out.println("Enter Password: ");
        String signInPassword = stringTypes.nextLine();
        String signInQuery = "SELECT * FROM library_member WHERE name = ? AND password = ?";
        PreparedStatement signInStatement = connection.prepareStatement(signInQuery);
        signInStatement.setString(1, signInName);
        signInStatement.setString(2, signInPassword);
        ResultSet signInResultSet = signInStatement.executeQuery();

        if (signInResultSet.next()) {
            System.out.println("Sign in successful. Welcome " + signInName + "!");
        } else {
            System.out.println("Invalid name or password. Please try again.");
        }
    }

    public static void insertion(Connection connection) throws SQLException {
        Scanner stringTypes = new Scanner(System.in);
        System.out.println("Enter the Book Count: ");
        int book_count = stringTypes.nextInt();
        stringTypes.nextLine();
        for (int bookCount = 0; bookCount < book_count; bookCount++) {
            System.out.print("Book Name: ");
            String book_name = stringTypes.nextLine();
            System.out.print("Author Name: ");
            String author_name = stringTypes.nextLine();

            int authorId = getAuthorId(connection, author_name);
            if (authorId == -1) {
                authorId = insertAuthorAndReturnId(connection, author_name);
            }

            String insertionQuery = "INSERT INTO book (book_name, author_id) VALUES (?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertionQuery);
            insertStatement.setString(1, book_name);
            insertStatement.setInt(2, authorId);
            insertStatement.executeUpdate();

            System.out.println("Record inserted");
        }
    }

    private static int insertAuthorAndReturnId(Connection connection, String authorName) throws SQLException {
        String query = "INSERT INTO author_table (author_name) VALUES (?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, authorName);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public static void update(Connection connection) throws SQLException {
        Scanner stringTypes = new Scanner(System.in);
        System.out.print("Tell the Book id: ");
        int book_update_id = stringTypes.nextInt();
        stringTypes.nextLine();
        System.out.print("Book Name: ");
        String update_book_name = stringTypes.nextLine();
        System.out.print("Author Name: ");
        String update_author_name = stringTypes.nextLine();

        int updateAuthorId = getAuthorId(connection, update_author_name);
        if (updateAuthorId == -1) {
            updateAuthorId = insertAuthorAndReturnId(connection, update_author_name);
        }

        String updateQuery = "UPDATE book SET book_name = ?, author_id = ? WHERE id = ?";
        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
        updateStatement.setString(1, update_book_name);
        updateStatement.setInt(2, updateAuthorId);
        updateStatement.setInt(3, book_update_id);
        updateStatement.executeUpdate();

        System.out.println("Record updated.");
    }

    public static void delete(Connection connection) throws SQLException {
        Scanner intTypes = new Scanner(System.in);
        System.out.print("Tell the Book id: ");
        int book_delete_id = intTypes.nextInt();

        String deleteQuery = "DELETE FROM book WHERE id = ?";
        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
        deleteStatement.setInt(1, book_delete_id);
        deleteStatement.executeUpdate();

        System.out.println("Record deleted.");
    }

    public static void search(Connection connection) throws SQLException {
        Scanner stringTypes = new Scanner(System.in);
        System.out.print("Enter Book Name to Search: ");
        String book_name = stringTypes.nextLine();

        String searchQuery = "SELECT * FROM book WHERE book_name LIKE ?";
        PreparedStatement searchStatement = connection.prepareStatement(searchQuery);
        searchStatement.setString(1, "%" + book_name + "%");
        ResultSet searchResultSet = searchStatement.executeQuery();

        while (searchResultSet.next()) {
            int book_id = searchResultSet.getInt("id");
            String name = searchResultSet.getString("book_name");
            int author_id = searchResultSet.getInt("author_id");
            System.out.println("Book ID: " + book_id + ", Book Name: " + name + ", Author ID: " + author_id);
        }
    }

    public static void displayJoinResults(Connection connection) throws SQLException {
        String joinQuery = "SELECT book.id, book.book_name, author_table.author_name " +
                "FROM book " +
                "JOIN author_table ON book.author_id = author_table.author_id";
        PreparedStatement joinStatement = connection.prepareStatement(joinQuery);
        ResultSet joinResultSet = joinStatement.executeQuery();

        while (joinResultSet.next()) {
            int book_id = joinResultSet.getInt("id");
            String book_name = joinResultSet.getString("book_name");
            String author_name = joinResultSet.getString("author_name");
            System.out.println("Book ID: " + book_id + ", Book Name: " + book_name + ", Author Name: " + author_name);
        }
    }
}

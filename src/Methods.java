
import java.sql.*;
import java.util.Scanner;



public class Methods {
    public static void menu() {
        System.out.println("1. Insert Books");
        System.out.println("2. Update Book");
        System.out.println("3. Delete Author");
        System.out.println("4. Delete Full Record");
        System.out.println("5. Search Record");
        System.out.println("6. Insert Members");
        System.out.println("7. Member Sign In");
        System.out.println("8. See Library Data");
        System.out.println("9. Insert Books with Authors");
        System.out.println("10. Exit");
    }

    public static void createTable(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS authors (author_id INT AUTO_INCREMENT, author_name VARCHAR(55), PRIMARY KEY (author_id))";
        stmt.executeUpdate(sql);

        sql = "CREATE TABLE IF NOT EXISTS books (book_id INT AUTO_INCREMENT, book_name VARCHAR(55), PRIMARY KEY (book_id))";
        stmt.executeUpdate(sql);

        sql = "CREATE TABLE IF NOT EXISTS author_book (author_id INT, book_id INT, PRIMARY KEY (author_id, book_id), FOREIGN KEY (author_id) REFERENCES authors(author_id) ON DELETE CASCADE ON UPDATE CASCADE, FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE ON UPDATE CASCADE)";
        stmt.executeUpdate(sql);

        String createMemberTable = "CREATE TABLE IF NOT EXISTS library_member (member_id INT AUTO_INCREMENT, name VARCHAR(55), age_group VARCHAR(10), personal_info VARCHAR(10), gender VARCHAR(10), father_name VARCHAR(55), cnic VARCHAR(15), email VARCHAR(55), password VARCHAR(255), contact_number VARCHAR(15), PRIMARY KEY (member_id))";
        stmt.executeUpdate(createMemberTable);

        System.out.println("Tables created successfully...");
    }
//    public static void insertAuthor(Connection connection) throws SQLException {
//        Scanner stringTypes = new Scanner(System.in);
//        System.out.println("Author Name: ");
//        String authorName = stringTypes.nextLine();
//
//        String query = "INSERT INTO authors (author_name) VALUES (?)";
//        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
//            pstmt.setString(1, authorName);
//            pstmt.executeUpdate();
//            ResultSet rs = pstmt.getGeneratedKeys();
//            if (rs.next()) {
//                System.out.println("Author inserted with ID: " + rs.getInt(1));
//            }
//        }
//    }
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
        try (PreparedStatement memberStatement = connection.prepareStatement(memberQuery)) {
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
    }

    public static void signIn(Connection connection) throws SQLException {
        Scanner stringTypes = new Scanner(System.in);
        System.out.println("Enter First Name: ");
        String signInName = stringTypes.nextLine();
        System.out.println("Enter Password: ");
        String signInPassword = stringTypes.nextLine();
        String signInQuery = "SELECT * FROM library_member WHERE name = ? AND password = ?";
        try (PreparedStatement signInStatement = connection.prepareStatement(signInQuery)) {
            signInStatement.setString(1, signInName);
            signInStatement.setString(2, signInPassword);
            ResultSet signInResultSet = signInStatement.executeQuery();

            if (signInResultSet.next()) {
                System.out.println("Sign in successful. Welcome " + signInName + "!");
            } else {
                System.out.println("Invalid name or password. Please try again.");
            }
        }
    }

    public static int getAuthorId(Connection connection, String authorName) throws SQLException {
        String query = "SELECT author_id FROM authors WHERE author_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, authorName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("author_id");
            }
        }
        return -1;
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

            String insertionQuery = "INSERT INTO books (book_name) VALUES (?)";
            int bookId;
            try (PreparedStatement insertStatement = connection.prepareStatement(insertionQuery, Statement.RETURN_GENERATED_KEYS)) {
                insertStatement.setString(1, book_name);
                insertStatement.executeUpdate();
                ResultSet rs = insertStatement.getGeneratedKeys();
                if (rs.next()) {
                    bookId = rs.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve book ID.");
                }
            }

            String authorBookQuery = "INSERT INTO author_book (author_id, book_id) VALUES (?, ?)";
            try (PreparedStatement authorBookStatement = connection.prepareStatement(authorBookQuery)) {
                authorBookStatement.setInt(1, authorId);
                authorBookStatement.setInt(2, bookId);
                authorBookStatement.executeUpdate();
            }
        }
    }

    private static int insertAuthorAndReturnId(Connection connection, String authorName) throws SQLException {
        String query = "INSERT INTO authors (author_name) VALUES (?)";
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

        String updateQuery = "UPDATE books SET book_name = ? WHERE book_id = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setString(1, update_book_name);
            updateStatement.setInt(2, book_update_id);
            updateStatement.executeUpdate();
            System.out.println("Record updated.");
        }

        String authorBookQuery = "UPDATE author_book SET author_id = ? WHERE book_id = ?";
        try (PreparedStatement authorBookStatement = connection.prepareStatement(authorBookQuery)) {
            authorBookStatement.setInt(1, updateAuthorId);
            authorBookStatement.setInt(2, book_update_id);
            authorBookStatement.executeUpdate();
        }
    }

    public static void delete(Connection connection) throws SQLException {
        Scanner intTypes = new Scanner(System.in);
        System.out.print("Tell the Book id: ");
        int book_delete_id = intTypes.nextInt();

        String deleteQuery = "DELETE FROM books WHERE book_id = ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setInt(1, book_delete_id);
            deleteStatement.executeUpdate();
            System.out.println("Record deleted.");
        }

        String authorBookQuery = "DELETE FROM author_book WHERE book_id = ?";
        try (PreparedStatement authorBookStatement = connection.prepareStatement(authorBookQuery)) {
            authorBookStatement.setInt(1, book_delete_id);
            authorBookStatement.executeUpdate();
        }
    }

    public static void deleteAuthor(Connection connection) {
    Scanner scanner = new Scanner(System.in);
    try {
        System.out.print("Tell the Author id: ");
        int author_delete_id = scanner.nextInt();

        // Delete the author from the authors table
        String deleteAuthorQuery = "DELETE FROM authors WHERE author_id = ?";
        try (PreparedStatement deleteAuthorStatement = connection.prepareStatement(deleteAuthorQuery)) {
            deleteAuthorStatement.setInt(1, author_delete_id);
            int rowsAffected = deleteAuthorStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Author record deleted.");
            } else {
                System.out.println("No author found with the given id.");
            }
        }

        // Ensure the books related to the author are preserved
        // No need for additional queries since the foreign key constraints handle the relationship

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

    public static void search(Connection connection) throws SQLException {
        Scanner stringTypes = new Scanner(System.in);
        System.out.print("Enter Book Name or Author Name to Search: ");
        String userInput = stringTypes.nextLine();

        boolean dataFound = false;

        // Search by Book Name
        String searchBookQuery = "SELECT b.book_id, b.book_name, a.author_name " +
                "FROM books b " +
                "JOIN author_book ab ON b.book_id = ab.book_id " +
                "JOIN authors a ON ab.author_id = a.author_id " +
                "WHERE b.book_name LIKE ?";
        try (PreparedStatement searchBookStatement = connection.prepareStatement(searchBookQuery)) {
            searchBookStatement.setString(1, "%" + userInput + "%");
            ResultSet bookResultSet = searchBookStatement.executeQuery();

            while (bookResultSet.next()) {
                dataFound = true;
                int book_id = bookResultSet.getInt("book_id");
                String book_name = bookResultSet.getString("book_name");
                String author_name = bookResultSet.getString("author_name");
                System.out.println("Book ID: " + book_id + ", Book Name: " + book_name + ", Author Name: " + author_name);
            }
        }

        // If no book found, search by Author Name
        if (!dataFound) {
            String searchAuthorQuery = "SELECT b.book_id, b.book_name, a.author_name " +
                    "FROM books b " +
                    "JOIN author_book ab ON b.book_id = ab.book_id " +
                    "JOIN authors a ON ab.author_id = a.author_id " +
                    "WHERE a.author_name LIKE ?";
            try (PreparedStatement searchAuthorStatement = connection.prepareStatement(searchAuthorQuery)) {
                searchAuthorStatement.setString(1, "%" + userInput + "%");
                ResultSet authorResultSet = searchAuthorStatement.executeQuery();

                while (authorResultSet.next()) {
                    dataFound = true;
                    int book_id = authorResultSet.getInt("book_id");
                    String book_name = authorResultSet.getString("book_name");
                    String author_name = authorResultSet.getString("author_name");
                    System.out.println("Book ID: " + book_id + ", Book Name: " + book_name + ", Author Name: " + author_name);
                }
            }
        }

        if (!dataFound) {
            System.out.println("That data not found.");
        }

//        stringTypes.close(); // Close the scanner
    }

    public static void Join(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose tables to join:");
        System.out.println("1. INNER JOIN");
        System.out.println("2. LEFT JOIN");
        System.out.println("3. RIGHT JOIN");
        System.out.println("4. INNER JOIN (ThreeTables)");
        System.out.println("5. FULL OUTER JOIN");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String query = "";

        switch (choice) {
            case 1:
//                INNER JOIN
                query = "SELECT b.book_id, b.book_name, a.author_id, a.author_name  FROM books b  JOIN author_book ab ON b.book_id = ab.book_id  JOIN authors a ON ab.author_id = a.author_id";
                break;
            case 2:
//                LEFT JOIN
                query = "SELECT b.book_id, b.book_name, ab.author_id   FROM books b   LEFT JOIN author_book ab ON b.book_id = ab.book_id ";
                break;
            case 3:
//                RIGHT JOIN
                query = "SELECT b.book_id, b.book_name, ab.author_id  FROM books b RIGHT JOIN author_book ab ON b.book_id = ab.book_id;";
                break;
            case 4:
//                INNER JOIN for three tables
                query = "SELECT b.book_id, b.book_name, a.author_id, a.author_name  FROM books b  JOIN author_book ab ON b.book_id = ab.book_id  JOIN authors a ON ab.author_id = a.author_id";
                break;
            case 5:
//                FULL OUTER JOIN
                query = "SELECT b.book_id, b.book_name, ab.author_id  FROM books b  LEFT JOIN author_book ab ON b.book_id = ab.book_id  UNION  SELECT b.book_id, b.book_name, ab.author_id  FROM books b RIGHT JOIN author_book ab ON b.book_id = ab.book_id";
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
                return;
        }

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                if (choice == 1 || choice == 4) {
                    int bookId = rs.getInt("book_id");
                    String bookName = rs.getString("book_name");
                    int authorId = rs.getInt("author_id");
                    String authorName = rs.getString("author_name");
                    System.out.println("Book ID: " + bookId + ", Book Name: " + bookName +
                            ", Author ID: " + authorId + ", Author Name: " + authorName);
                } else if (choice == 2) {
                    int bookId = rs.getInt("book_id");
                    String bookName = rs.getString("book_name");
                    int authorId = rs.getInt("author_id");
                    System.out.println("Book ID: " + bookId + ", Book Name: " + bookName +
                            ", Author ID: " + authorId);
                } else if (choice == 3) {
                    int bookId = rs.getInt("book_id");
                    String bookName = rs.getString("book_name");
                    int authorId = rs.getInt("author_id");
                    System.out.println("Book ID: " + bookId + ", Book Name: " + bookName +
                            ", Author ID: " + authorId);
                } else if(choice == 5){
                    while (rs.next()) {
                        int bookId = rs.getInt("book_id");
                        String bookName = rs.getString("book_name");
                        int authorId = rs.getInt("author_id");

                        System.out.println("Book ID: " + bookId + ", Book Name: " + (bookName != null ? bookName : "NULL") +
                                ", Author ID: " + (authorId != 0 ? authorId : "NULL"));
                    }
                }
            }
        }
    }

    public static void insertBookWithAuthors(Connection connection) throws SQLException {
        Scanner stringTypes = new Scanner(System.in);
        System.out.println("Enter the Book Name: ");
        String book_name = stringTypes.nextLine();
        System.out.println("Enter the number of authors for this book: ");
        int authorCount = stringTypes.nextInt();
        stringTypes.nextLine(); // Consume newline

        // Insert book first
        String bookQuery = "INSERT INTO books (book_name) VALUES (?)";
        int bookId;
        try (PreparedStatement bookStatement = connection.prepareStatement(bookQuery, Statement.RETURN_GENERATED_KEYS)) {
            bookStatement.setString(1, book_name);
            bookStatement.executeUpdate();
            ResultSet rs = bookStatement.getGeneratedKeys();
            if (rs.next()) {
                bookId = rs.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve book ID.");
            }
        }

        // Insert authors and establish relationships
        for (int i = 0; i < authorCount; i++) {
            System.out.print("Author Name: ");
            String author_name = stringTypes.nextLine();

            int authorId = getAuthorId(connection, author_name);
            if (authorId == -1) {
                authorId = insertAuthorAndReturnId(connection, author_name);
            }

            String authorBookQuery = "INSERT INTO author_book (author_id, book_id) VALUES (?, ?)";
            try (PreparedStatement authorBookStatement = connection.prepareStatement(authorBookQuery)) {
                authorBookStatement.setInt(1, authorId);
                authorBookStatement.setInt(2, bookId);
                authorBookStatement.executeUpdate();
            }
        }

        System.out.println("Book and author relationships inserted successfully.");
    }

}
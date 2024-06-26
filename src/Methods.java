import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.Scanner;

public class Methods {

    public static void createTables(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();

        String createLibraryMemberTable = "CREATE TABLE IF NOT EXISTS library_member (" +
                "member_id INT AUTO_INCREMENT, " +
                "name VARCHAR(55), " +
                "age_group VARCHAR(10), " +
                "personal_info VARCHAR(10), " +
                "gender VARCHAR(10), " +
                "father_name VARCHAR(55), " +
                "cnic VARCHAR(15), " +
                "email VARCHAR(55), " +
                "password VARCHAR(255), " +
                "contact_number VARCHAR(15), " +
                "PRIMARY KEY (member_id))";
        stmt.executeUpdate(createLibraryMemberTable);

        String createBooksTable = "CREATE TABLE IF NOT EXISTS books (" +
                "book_id INT AUTO_INCREMENT, " +
                "book_name VARCHAR(55), " +
                "PRIMARY KEY (book_id))";
        stmt.executeUpdate(createBooksTable);

        String createAuthorsTable = "CREATE TABLE IF NOT EXISTS authors (" +
                "author_id INT AUTO_INCREMENT, " +
                "author_name VARCHAR(55), " +
                "PRIMARY KEY (author_id))";
        stmt.executeUpdate(createAuthorsTable);

        String createAuthorBookTable = "CREATE TABLE IF NOT EXISTS author_book (" +
                "author_id INT, " +
                "book_id INT, " +
                "PRIMARY KEY (author_id, book_id), " +
                "FOREIGN KEY (author_id) REFERENCES authors(author_id) ON DELETE CASCADE ON UPDATE CASCADE, " +
                "FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE ON UPDATE CASCADE)";
        stmt.executeUpdate(createAuthorBookTable);

        String createIssuedBooksTable = "CREATE TABLE IF NOT EXISTS issued_books (" +
                "issue_id INT AUTO_INCREMENT, " +
                "member_id INT, " +
                "book_id INT, " +
                "issue_date DATE, " +
                "return_date DATE, " +
                "PRIMARY KEY (issue_id), " +
                "FOREIGN KEY (member_id) REFERENCES library_member(member_id) ON DELETE CASCADE ON UPDATE CASCADE, " +
                "FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE ON UPDATE CASCADE)";
        stmt.executeUpdate(createIssuedBooksTable);

        System.out.println("Tables created successfully...");
    }

    public static void menu(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nLibrary System Menu:");
            System.out.println("1. Insert Book");
            System.out.println("2. View Books (With Authors)");
            System.out.println("3. Delete Book");
            System.out.println("4. Update Book");
            System.out.println("5. Add Author");
            System.out.println("6. View Authors (With Books)");
            System.out.println("7. Delete Author");
            System.out.println("8. Update Author");
            System.out.println("9. Search Book");
            System.out.println("10. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    insertBookMenu(connection);
                    break;
                case 2:
                    viewBooksWithAuthors(connection);
                    break;
                case 3:
                    deleteBook(connection);
                    break;
                case 4:
                    updateBook(connection);
                    break;
                case 5:
                    addAuthor(connection);
                    break;
                case 6:
                    viewAuthorsWithBooks(connection);
                    break;
                case 7:
                    deleteAuthor(connection);
                    break;
                case 8:
                    updateAuthor(connection);
                    break;
                case 9:
                    searchBook(connection);
                    break;
                case 10:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    // Insert Book Menu
    private static void insertBookMenu(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose an option:");
        System.out.println("1. Select Author");
        System.out.println("2. New Author");
        System.out.println("3. Without Author");
        System.out.println("4. Exit");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                insertBookWithExistingAuthor(connection);
                break;
            case 2:
                insertBookWithNewAuthor(connection);
                break;
            case 3:
                insertBookWithoutAuthor(connection);
                break;
            case 4:
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
        }
    }

    private static void insertBookWithExistingAuthor(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Book Name: ");
        String bookName = scanner.nextLine();

        // Insert book first
        String bookQuery = "INSERT INTO books (book_name) VALUES (?)";
        int bookId;
        try (PreparedStatement bookStatement = connection.prepareStatement(bookQuery, Statement.RETURN_GENERATED_KEYS)) {
            bookStatement.setString(1, bookName);
            bookStatement.executeUpdate();
            ResultSet rs = bookStatement.getGeneratedKeys();
            if (rs.next()) {
                bookId = rs.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve book ID.");
            }
        }

        // Display existing authors and provide options
        listAuthors(connection);
        System.out.println("Enter the Author ID to associate with this book: ");
        int authorId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String authorBookQuery = "INSERT INTO author_book (author_id, book_id) VALUES (?, ?)";
        try (PreparedStatement authorBookStatement = connection.prepareStatement(authorBookQuery)) {
            authorBookStatement.setInt(1, authorId);
            authorBookStatement.setInt(2, bookId);
            authorBookStatement.executeUpdate();
        }

        System.out.println("Book and author relationship inserted successfully.");
    }

    private static void insertBookWithNewAuthor(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Book Name: ");
        String bookName = scanner.nextLine();

        // Insert book first
        String bookQuery = "INSERT INTO books (book_name) VALUES (?)";
        int bookId;
        try (PreparedStatement bookStatement = connection.prepareStatement(bookQuery, Statement.RETURN_GENERATED_KEYS)) {
            bookStatement.setString(1, bookName);
            bookStatement.executeUpdate();
            ResultSet rs = bookStatement.getGeneratedKeys();
            if (rs.next()) {
                bookId = rs.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve book ID.");
            }
        }

        System.out.println("Enter the Author Name: ");
        String authorName = scanner.nextLine();

        // Insert author
        int authorId = insertAuthorAndReturnId(connection, authorName);

        String authorBookQuery = "INSERT INTO author_book (author_id, book_id) VALUES (?, ?)";
        try (PreparedStatement authorBookStatement = connection.prepareStatement(authorBookQuery)) {
            authorBookStatement.setInt(1, authorId);
            authorBookStatement.setInt(2, bookId);
            authorBookStatement.executeUpdate();
        }

        System.out.println("Book and author relationship inserted successfully.");
    }

    private static void insertBookWithoutAuthor(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the Book Name: ");
        String bookName = scanner.nextLine();

        // Insert book
        String bookQuery = "INSERT INTO books (book_name) VALUES (?)";
        try (PreparedStatement bookStatement = connection.prepareStatement(bookQuery)) {
            bookStatement.setString(1, bookName);
            bookStatement.executeUpdate();
            System.out.println("Book inserted successfully.");
        }
    }

    // View Books with Authors
    private static void viewBooksWithAuthors(Connection connection) throws SQLException {
        String query = "SELECT b.book_id, b.book_name, a.author_name FROM books b " +
                "LEFT JOIN author_book ab ON b.book_id = ab.book_id " +
                "LEFT JOIN authors a ON ab.author_id = a.author_id";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Books with Authors:");
            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String bookName = rs.getString("book_name");
                String authorName = rs.getString("author_name");
                System.out.println("Book ID: " + bookId + ", Book Name: " + bookName + ", Author Name: " + (authorName != null ? authorName : "No Author"));
            }
        }
    }

    // Delete Book
    private static void deleteBook(Connection connection) throws SQLException {
        listBooks(connection);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Book ID to delete: ");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String deleteQuery = "DELETE FROM books WHERE book_id = ?";
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, bookId);
            int rowsAffected = deleteStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book deleted successfully.");
            } else {
                System.out.println("Failed to delete book.");
            }
        }
    }

    // Update Book
    private static void updateBook(Connection connection) throws SQLException {
        listBooks(connection);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Book ID to update: ");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Enter new Book Name: ");
        String bookName = scanner.nextLine();

        // Update Book Name
        String updateBookQuery = "UPDATE books SET book_name = ? WHERE book_id = ?";
        try (PreparedStatement updateBookStmt = connection.prepareStatement(updateBookQuery)) {
            updateBookStmt.setString(1, bookName);
            updateBookStmt.setInt(2, bookId);
            int bookRowsAffected = updateBookStmt.executeUpdate();
            if (bookRowsAffected > 0) {
                System.out.println("Book updated successfully.");
            } else {
                System.out.println("Failed to update book.");
                return;
            }
        }

        System.out.println("Do you want to update the author as well? (yes/no): ");
        String updateAuthor = scanner.nextLine().toLowerCase();

        if (updateAuthor.equals("yes")) {
            listAuthors(connection);
            System.out.println("Select Author ID to update (or enter 0 to add a new author): ");
            int authorId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (authorId == 0) {
                System.out.println("Enter new Author Name: ");
                String authorName = scanner.nextLine();

                String insertAuthorQuery = "INSERT INTO authors (author_name) VALUES (?)";
                try (PreparedStatement insertAuthorStmt = connection.prepareStatement(insertAuthorQuery, Statement.RETURN_GENERATED_KEYS)) {
                    insertAuthorStmt.setString(1, authorName);
                    int authorRowsAffected = insertAuthorStmt.executeUpdate();
                    if (authorRowsAffected > 0) {
                        ResultSet generatedKeys = insertAuthorStmt.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            authorId = generatedKeys.getInt(1);
                            System.out.println("New author added with ID: " + authorId);
                        }
                    }
                }
            } else {
                System.out.println("Enter new Author Name: ");
                String authorName = scanner.nextLine();

                String updateAuthorQuery = "UPDATE authors SET author_name = ? WHERE author_id = ?";
                try (PreparedStatement updateAuthorStmt = connection.prepareStatement(updateAuthorQuery)) {
                    updateAuthorStmt.setString(1, authorName);
                    updateAuthorStmt.setInt(2, authorId);
                    int authorRowsAffected = updateAuthorStmt.executeUpdate();
                    if (authorRowsAffected > 0) {
                        System.out.println("Author updated successfully.");
                    } else {
                        System.out.println("Failed to update author.");
                    }
                }
            }

            // Check if book is already linked to an author
            String checkLinkQuery = "SELECT * FROM author_book WHERE book_id = ?";
            try (PreparedStatement checkLinkStmt = connection.prepareStatement(checkLinkQuery)) {
                checkLinkStmt.setInt(1, bookId);
                ResultSet rs = checkLinkStmt.executeQuery();
                if (rs.next()) {
                    // Update the existing relationship
                    String updateBookAuthorQuery = "UPDATE author_book SET author_id = ? WHERE book_id = ?";
                    try (PreparedStatement updateBookAuthorStmt = connection.prepareStatement(updateBookAuthorQuery)) {
                        updateBookAuthorStmt.setInt(1, authorId);
                        updateBookAuthorStmt.setInt(2, bookId);
                        int rowsAffected = updateBookAuthorStmt.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Book-Author relationship updated successfully.");
                        } else {
                            System.out.println("Failed to update Book-Author relationship.");
                        }
                    }
                } else {
                    // Insert new relationship
                    String insertBookAuthorQuery = "INSERT INTO author_book (author_id, book_id) VALUES (?, ?)";
                    try (PreparedStatement insertBookAuthorStmt = connection.prepareStatement(insertBookAuthorQuery)) {
                        insertBookAuthorStmt.setInt(1, authorId);
                        insertBookAuthorStmt.setInt(2, bookId);
                        int rowsAffected = insertBookAuthorStmt.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Book-Author relationship created successfully.");
                        } else {
                            System.out.println("Failed to create Book-Author relationship.");
                        }
                    }
                }
            }
        }
    }


    // Add Author
    private static void addAuthor(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Author Name: ");
        String authorName = scanner.nextLine();

        String query = "INSERT INTO authors (author_name) VALUES (?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, authorName);
            pstmt.executeUpdate();
            System.out.println("Author inserted successfully.");
        }
    }

    // View Authors with Books
    private static void viewAuthorsWithBooks(Connection connection) throws SQLException {
        String query = "SELECT a.author_id, a.author_name, b.book_name FROM authors a " +
                "LEFT JOIN author_book ab ON a.author_id = ab.author_id " +
                "LEFT JOIN books b ON ab.book_id = b.book_id";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Authors with Books:");
            while (rs.next()) {
                int authorId = rs.getInt("author_id");
                String authorName = rs.getString("author_name");
                String bookName = rs.getString("book_name");
                System.out.println("Author ID: " + authorId + ", Author Name: " + authorName + ", Book Name: " + (bookName != null ? bookName : "No Book"));
            }
        }
    }

    // Delete Author
    private static void deleteAuthor(Connection connection) throws SQLException {
        listAuthors(connection);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Author ID to delete: ");
        int authorId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String deleteQuery = "DELETE FROM authors WHERE author_id = ?";
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, authorId);
            int rowsAffected = deleteStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Author deleted successfully.");
            } else {
                System.out.println("Failed to delete author.");
            }
        }
    }

    // Update Author
    private static void updateAuthor(Connection connection) throws SQLException {
        listAuthors(connection);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Author ID to update: ");
        int authorId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Enter new Author Name: ");
        String authorName = scanner.nextLine();

        String updateQuery = "UPDATE authors SET author_name = ? WHERE author_id = ?";
        try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
            updateStmt.setString(1, authorName);
            updateStmt.setInt(2, authorId);
            int rowsAffected = updateStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Author updated successfully.");
            } else {
                System.out.println("Failed to update author.");
            }
        }
    }

    // Search Book
    private static void searchBook(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Book Name or Author Name to search: ");
        String input = scanner.nextLine();

        String searchBookQuery = "SELECT b.book_id, b.book_name, a.author_name FROM books b " +
                "JOIN author_book ab ON b.book_id = ab.book_id " +
                "JOIN authors a ON ab.author_id = a.author_id " +
                "WHERE b.book_name LIKE ?";
        try (PreparedStatement searchBookStmt = connection.prepareStatement(searchBookQuery)) {
            searchBookStmt.setString(1, "%" + input + "%");
            ResultSet rs = searchBookStmt.executeQuery();
            boolean bookFound = false;
            while (rs.next()) {
                bookFound = true;
                int bookId = rs.getInt("book_id");
                String bookName = rs.getString("book_name");
                String authorName = rs.getString("author_name");
                System.out.println("Book ID: " + bookId + ", Book Name: " + bookName + ", Author Name: " + authorName);
            }

            if (!bookFound) {
                String searchAuthorQuery = "SELECT b.book_id, b.book_name, a.author_name FROM books b " +
                        "JOIN author_book ab ON b.book_id = ab.book_id " +
                        "JOIN authors a ON ab.author_id = a.author_id " +
                        "WHERE a.author_name LIKE ?";
                try (PreparedStatement searchAuthorStmt = connection.prepareStatement(searchAuthorQuery)) {
                    searchAuthorStmt.setString(1, "%" + input + "%");
                    rs = searchAuthorStmt.executeQuery();
                    while (rs.next()) {
                        int bookId = rs.getInt("book_id");
                        String bookName = rs.getString("book_name");
                        String authorName = rs.getString("author_name");
                        System.out.println("Book ID: " + bookId + ", Book Name: " + bookName + ", Author Name: " + authorName);
                    }
                }
            }
        }
    }

    // List Books
    private static void listBooks(Connection connection) throws SQLException {
        String query = "SELECT b.book_id, b.book_name, a.author_name FROM books b " +
                "LEFT JOIN author_book ab ON b.book_id = ab.book_id " +
                "LEFT JOIN authors a ON ab.author_id = a.author_id";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Existing books in the library:");
            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String bookName = rs.getString("book_name");
                String authorName = rs.getString("author_name");
                System.out.println("Book ID: " + bookId + ", Book Name: " + bookName + ", Author Name: " + (authorName != null ? authorName : "No Author"));
            }
        }
    }
    // List Authors
    private static void listAuthors(Connection connection) throws SQLException {
        String query = "SELECT author_id, author_name FROM authors";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Existing authors:");
            while (rs.next()) {
                int authorId = rs.getInt("author_id");
                String authorName = rs.getString("author_name");
                System.out.println("Author ID: " + authorId + ", Author Name: " + authorName);
            }
        }
    }

    // Insert Author and Return ID
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
}
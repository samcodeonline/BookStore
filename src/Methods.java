import java.sql.*;
import java.util.*;

public class Methods {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Statement statement = null;

    public void createTables(Connection connection) {
        String createBooksTable = "CREATE TABLE IF NOT EXISTS books (" +
                "book_id INT AUTO_INCREMENT, " +
                "book_name VARCHAR(55), " +
                "PRIMARY KEY (book_id))";

        String createAuthorsTable = "CREATE TABLE IF NOT EXISTS authors (" +
                "author_id INT AUTO_INCREMENT, " +
                "author_name VARCHAR(55), " +
                "PRIMARY KEY (author_id))";

        String createAuthorBookTable = "CREATE TABLE IF NOT EXISTS author_book (" +
                "author_id INT, " +
                "book_id INT, " +
                "PRIMARY KEY (author_id, book_id), " +
                "FOREIGN KEY (author_id) REFERENCES authors(author_id) ON DELETE CASCADE ON UPDATE CASCADE, " +
                "FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE ON UPDATE CASCADE)";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createBooksTable);
            stmt.executeUpdate(createAuthorsTable);
            stmt.executeUpdate(createAuthorBookTable);
            System.out.println("Tables created successfully...");
        } catch (SQLException e) {
            System.out.println("There was an error : " + e.getMessage());
        }
    }

    public void menu(Connection connection) throws SQLException {

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nBook Store Menu:");
            System.out.println("1. Insert Book");
            System.out.println("2. View Books (With Authors)");
            System.out.println("3. Delete Book");
            System.out.println("4. Update Book");
            System.out.println("5. Add Author");
            System.out.println("6. Add Multiple Authors to Book");
            System.out.println("7. View Authors (With Books)");
            System.out.println("8. Delete Author");
            System.out.println("9. Update Author");
            System.out.println("10. Search Book");
            System.out.println("11. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    insertBookMenu(connection, scanner);
                    break;
                case 2:
                    viewBooksWithAuthors(connection);
                    break;
                case 3:
                    deleteBook(connection, scanner);
                    break;
                case 4:
                    updateBookMenu(connection, scanner);
                    break;
                case 5:
                    addAuthor(connection, scanner);
                    break;
                case 6:
                    addMultipleAuthorsToBook(connection, scanner);
                    break;
                case 7:
                    viewAuthorsWithBooks(connection);
                    break;
                case 8:
                    deleteAuthor(connection, scanner);
                    break;
                case 9:
                    updateAuthorMenu(connection, scanner);
                    break;
                case 10:
                    searchBook(connection, scanner);
                    break;
                case 11:
                    running = false;
                    System.out.println("Goodbye!");
                    stmt.close();
                    rs.close();
                    scanner.close();
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    private void insertBookMenu(Connection connection, Scanner scanner) {

        System.out.println("Choose an option:");
        System.out.println("1. Select Author");
        System.out.println("2. New Author");
        System.out.println("3. Without Author");
        System.out.println("4. Exit");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                insertBookWithExistingAuthor(connection, scanner);
                break;
            case 2:
                insertBookWithNewAuthor(connection, scanner);
                break;
            case 3:
                insertBookWithoutAuthor(connection, scanner);
                break;
            case 4:
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
        }

    }

    private void updateBookMenu(Connection connection, Scanner scanner) {

        listBooks(connection);
        System.out.println("Enter Book ID to update: ");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Choose an option:");
        System.out.println("1. Update existing book");
        System.out.println("2. Insert new book");
        System.out.println("3. Exit");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                updateExistingBook(connection, scanner, bookId);
                break;
            case 2:
                insertBookMenu(connection, scanner);
                break;
            case 3:
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
        }

    }

    private void updateExistingBook(Connection connection, Scanner scanner, int bookId) {
        System.out.println("Enter new Book Name: ");
        String newBookName = scanner.nextLine();
        updateBook(connection, bookId, newBookName);

        System.out.println("Choose an option:");
        System.out.println("1. Update Author");
        System.out.println("2. Exit");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 1) {
            listAuthors(connection);
            System.out.println("Enter the Author ID to associate with this book: ");
            int authorId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            associateBookWithAuthor(connection, authorId, bookId);
        }
    }

    private void updateAuthorMenu(Connection connection, Scanner scanner) {

        listAuthors(connection);
        System.out.println("Enter Author ID to update: ");
        int authorId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Choose an option:");
        System.out.println("1. Update existing author");
        System.out.println("2. Insert new author");
        System.out.println("3. Exit");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                updateExistingAuthor(connection, scanner, authorId);
                break;
            case 2: {
                System.out.println("Enter Name: ");
                String authorName = scanner.nextLine();
                insertAuthor(connection, authorName);
            }
            break;
            case 3:
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
        }

    }

    private void updateExistingAuthor(Connection connection, Scanner scanner, int authorId) {
        System.out.println("Enter new Author Name: ");
        String newAuthorName = scanner.nextLine();
        updateAuthor(connection, authorId, newAuthorName);

        System.out.println("Choose an option:");
        System.out.println("1. Update Book");
        System.out.println("2. Exit");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 1) {
            listBooks(connection);
            System.out.println("Enter the Book ID to associate with this author: ");
            int bookId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            associateBookWithAuthor(connection, authorId, bookId);
        }
    }

    private void insertBookWithExistingAuthor(Connection connection, Scanner scanner) {

        System.out.println("Enter the Book Name: ");
        String bookName = scanner.nextLine();
        int bookId = insertBook(connection, bookName);

        listAuthors(connection);
        System.out.println("Enter the Author ID to associate with this book: ");
        int authorId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        associateBookWithAuthor(connection, authorId, bookId);

        System.out.println("Book and author relationship inserted successfully.");

    }

    private void insertBookWithNewAuthor(Connection connection, Scanner scanner) {

        System.out.println("Enter the Book Name: ");
        String bookName = scanner.nextLine();
        int bookId = insertBook(connection, bookName);

        System.out.println("Enter the Author Name: ");
        String authorName = scanner.nextLine();
        int authorId = insertAuthor(connection, authorName);

        associateBookWithAuthor(connection, authorId, bookId);

        System.out.println("Book and author relationship inserted successfully.");

    }

    private void insertBookWithoutAuthor(Connection connection, Scanner scanner) {

        System.out.println("Enter the Book Name: ");
        String bookName = scanner.nextLine();
        insertBook(connection, bookName);
        System.out.println("Book inserted successfully.");

    }


    private void deleteBook(Connection connection, Scanner scanner) {

        listBooks(connection);
        System.out.println("Enter Book ID to delete: ");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Choose an option:");
        System.out.println("1. Delete Book with Author");
        System.out.println("2. Delete Book with Multiple Authors");
        System.out.println("3. Delete Book Without Authors");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        switch (choice) {
            case 1:
                deleteBookWithAuthor(connection, bookId);
                break;
            case 2:
                deleteBookWithMultipleAuthors(connection, bookId);
                break;
            case 3:
                deleteBookWithoutAuthors(connection, bookId);
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
        }
    }


    private void deleteBookWithAuthor(Connection connection, int bookId) {
        String query = "DELETE FROM books WHERE book_id = ?";
        try {
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
            System.out.println("Book deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());

        }
    }

    private void deleteBookWithMultipleAuthors(Connection connection, int bookId) {
        try {
            String query = "DELETE FROM author_book WHERE book_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
            deleteBookWithAuthor(connection, bookId);
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());
        }
    }

    private void deleteBookWithoutAuthors(Connection connection, int bookId) {
        deleteBookWithAuthor(connection, bookId);
    }

    private void updateBook(Connection connection, int bookId, String newBookName) {
        try {
            String query = "UPDATE books SET book_name = ? WHERE book_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, newBookName);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
            System.out.println("Book updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());

        }
    }

    private void addAuthor(Connection connection, Scanner scanner) {

        System.out.println("Enter the Author Name: ");
        String authorName = scanner.nextLine();

        insertAuthor(connection, authorName);
        System.out.println("Author added successfully.");

    }

    public void viewAuthorsWithBooks(Connection connection) {
        String query = "SELECT a.author_id, a.author_name, b.book_name FROM authors a " +
                "LEFT JOIN author_book ab ON a.author_id = ab.author_id " +
                "LEFT JOIN books b ON ab.book_id = b.book_id";

        try {
            Statement stmt = connection.createStatement();
             rs = stmt.executeQuery(query);
            System.out.println("Authors with Books:");
            while (rs.next()) {
                int authorId = rs.getInt("author_id");
                String authorName = rs.getString("author_name");
                String bookName = rs.getString("book_name");
                System.out.println("Author ID: " + authorId + ", Author Name: " + authorName + ", Book Name: " + (bookName != null ? bookName : "No Book"));
            }
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());

        }
    }

    private void deleteAuthor(Connection connection, Scanner scanner) {

        listAuthors(connection);
        System.out.println("Enter Author ID to delete: ");
        int authorId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Choose an option:");
        System.out.println("1. Delete Book with Author");
        System.out.println("2. Delete Book with Multiple Authors");
        System.out.println("3. Delete Book Without Authors");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                deleteAuthorWithBooks(connection, authorId);
                break;
            case 2:
                deleteAuthorWithMultipleBooks(connection, authorId);
                break;
            case 3:
                deleteAuthorWithoutBooks(connection, authorId);
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
        }

    }

    private void deleteAuthorWithBooks(Connection connection, int authorId) {
        try {
            String query = "DELETE FROM authors WHERE author_id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, authorId);
            stmt.executeUpdate();
            System.out.println("Author and associated books deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());

        }
    }

    private void deleteAuthorWithMultipleBooks(Connection connection, int authorId) {
        String query = "DELETE FROM author_book WHERE author_id = ?";
        try {
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, authorId);
            stmt.executeUpdate();
            deleteAuthorWithBooks(connection, authorId);
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());

        }
    }

    private void deleteAuthorWithoutBooks(Connection connection, int authorId) {
        deleteAuthorWithBooks(connection, authorId);
    }

    private void updateAuthor(Connection connection, int authorId, String newAuthorName) {
        try {
            String query = "UPDATE authors SET author_name = ? WHERE author_id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, newAuthorName);
            stmt.setInt(2, authorId);
            stmt.executeUpdate();
            System.out.println("Author updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());

        }
    }

    private void searchBook(Connection connection, Scanner scanner) {

        System.out.println("Enter Book Name or Author Name to search: ");
        String searchTerm = scanner.nextLine();

        String query = "SELECT b.book_id, b.book_name, a.author_name FROM books b " +
                "LEFT JOIN author_book ab ON b.book_id = ab.book_id " +
                "LEFT JOIN authors a ON ab.author_id = a.author_id " +
                "WHERE b.book_name LIKE ? OR a.author_name LIKE ?";

        try {
            stmt = connection.prepareStatement(query);

            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setString(2, "%" + searchTerm + "%");

             rs = stmt.executeQuery();
            System.out.println("Search Results:");
            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String bookName = rs.getString("book_name");
                String authorName = rs.getString("author_name");
                System.out.println("Book ID: " + bookId + ", Book Name: " + bookName + ", Author Name: " + (authorName != null ? authorName : "No Author"));
            }
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());

        }

    }

    private int insertBook(Connection connection, String bookName) {
        try {
            String bookQuery = "INSERT INTO books (book_name) VALUES (?)";
            stmt = connection.prepareStatement(bookQuery, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, bookName);
            stmt.executeUpdate();
             rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve book ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());

        }
        return -1;
    }

    private int insertAuthor(Connection connection, String authorName) {
        try {
            String authorQuery = "INSERT INTO authors (author_name) VALUES (?)";
            stmt = connection.prepareStatement(authorQuery, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, authorName);
            stmt.executeUpdate();
             rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve author ID.");
            }

        } catch (SQLException e) {
            System.out.println("The error occurred: " + e.getMessage());
        }
        return -1;
    }

    private void associateBookWithAuthor(Connection connection, int authorId, int bookId) {
        try {
            String authorBookQuery = "INSERT INTO author_book (author_id, book_id) VALUES (?, ?)";
            stmt = connection.prepareStatement(authorBookQuery);
            stmt.setInt(1, authorId);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());
        }
    }

    private void listBooks(Connection connection) {
        String query = "SELECT * FROM books";
        try  {
            Statement stmt = connection.createStatement() ;
            rs = stmt.executeQuery(query);
            System.out.println("Books:");
            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String bookName = rs.getString("book_name");
                System.out.println("Book ID: " + bookId + ", Book Name: " + bookName);
            }
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());
        }
    }

    private void listAuthors(Connection connection) {
        String query = "SELECT * FROM authors";
        try  {
            Statement stmt = connection.createStatement();   rs = stmt.executeQuery(query);
            System.out.println("Authors:");
            while (rs.next()) {
                int authorId = rs.getInt("author_id");
                String authorName = rs.getString("author_name");
                System.out.println("Author ID: " + authorId + ", Author Name: " + authorName);
            }
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());
        }
    }

    public void addMultipleAuthorsToBook(Connection connection, Scanner scanner) {
        listBooks(connection);
        System.out.println("Enter the Book ID: ");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Enter the Book Name: ");
        String bookName = scanner.nextLine();

        System.out.println("Enter the number of authors to add: ");
        int authorCount = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        ArrayList<String> authors = new ArrayList<>();
        for (int i = 0; i < authorCount; i++) {
            System.out.println("Enter Author Name " + (i + 1) + ": ");
            String authorName = scanner.nextLine();
            authors.add(authorName);
        }

        // Ensure the book exists
        insertBookIfNotExists(connection, bookId, bookName);

        // Insert authors and associate them with the book
        for (String author : authors) {
            int authorId = getOrInsertAuthor(connection, author);
            if (authorId != -1) {
                associateBookWithAuthor(connection, authorId, bookId);
            }
        }

        System.out.println("Successfully added authors to the book.");
    }

    private void insertBookIfNotExists(Connection connection, int bookId, String bookName) {
        try {
            String query = "INSERT INTO books (book_id, book_name) VALUES (?, ?) ON DUPLICATE KEY UPDATE book_name = VALUES(book_name)";
             stmt = connection.prepareStatement(query);
            stmt.setInt(1, bookId);
            stmt.setString(2, bookName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private int getOrInsertAuthor(Connection connection, String authorName) {
        // Check if the author exists
        try {
            String selectQuery = "SELECT author_id FROM authors WHERE author_name = ?";
            String insertQuery = "INSERT INTO authors (author_name) VALUES (?)";
            stmt = connection.prepareStatement(selectQuery);
            stmt.setString(1, authorName);
             rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("author_id");
            }
            // Insert the author if not exists

            stmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, authorName);
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return -1;
    }

    public void viewBooksWithAuthors(Connection connection) {
        String query = "SELECT b.book_id, b.book_name, a.author_name FROM books b " +
                "LEFT JOIN author_book ab ON b.book_id = ab.book_id " +
                "LEFT JOIN authors a ON ab.author_id = a.author_id";

        try {
            statement = connection.createStatement();
             rs = stmt.executeQuery(query);

            // LinkedHashMap to maintain insertion order
            Map<Integer, Book> bookMap = new LinkedHashMap<>();

            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String bookName = rs.getString("book_name");
                String authorName = rs.getString("author_name");

                // If the book is not already in the map, add it
                bookMap.putIfAbsent(bookId, new Book(bookId, bookName));

                // Add author to the book's author list
                if (authorName != null) {
                    bookMap.get(bookId).addAuthor(authorName);
                }
            }

            // Print the results
            System.out.println("Books with Authors:");
            for (Book book : bookMap.values()) {
                System.out.println(book);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Helper class to hold book data
    class Book {
        private int id;
        private String name;
        private List<String> authors;

        public Book(int id, String name) {
            this.id = id;
            this.name = name;
            this.authors = new ArrayList<>();
        }

        public void addAuthor(String author) {
            this.authors.add(author);
        }

        @Override
        public String toString() {
            String authorsString = authors.isEmpty() ? "No Author" : String.join(", ", authors);
            return "Book ID: " + id + ", Book Name: " + name + ", Author Name(s): " + authorsString;
        }
    }


}
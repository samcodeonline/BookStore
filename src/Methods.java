import java.sql.*;
import java.util.*;

public class Methods {
    private PreparedStatement stmt = null;
    private ResultSet rs = null;
    private Statement statement = null;
    private final Connection connection;
    private final Scanner scanner = new Scanner(System.in);
    private final HelperMethods helper;
    public Methods(Connection connection) {
        this.connection = connection;
        this.helper = new HelperMethods(connection); // Pass the connection to HelperMethods
    }

    public void createTables() {
        try (Statement stmt = connection.createStatement()) {
            helper.executeUpdate(stmt, createBooksTableQuery());
            helper.executeUpdate(stmt, createAuthorsTableQuery());
            helper.executeUpdate(stmt, createAuthorBookTableQuery());
            System.out.println("Tables created successfully...");
        } catch (SQLException e) {
            System.out.println("There was an error : " + e.getMessage());
        }
    }

    public void menu() {
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

            int choice = helper.nextInt(scanner);
            helper.nextLine(scanner);
            switch (choice) {
                case 1:
                    insertBookMenu();
                    break;
                case 2:
                    viewBooksWithAuthors();
                    break;
                case 3:
                    deleteBook();
                    break;
                case 4:
                    updateBookMenu();
                    break;
                case 5:
                    addAuthor();
                    break;
                case 6:
                    addMultipleAuthorsToBook();
                    break;
                case 7:
                    viewAuthorsWithBooks();
                    break;
                case 8:
                    deleteAuthor();
                    break;
                case 9:
                    updateAuthorMenu();
                    break;
                case 10:
                    searchBook();
                    break;
                case 11:
                    running = false;
                    System.out.println("Goodbye!");
                    closeResources();
                    scanner.close();
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    private void closeResources() {
        try {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
            if (statement != null) statement.close();
        } catch (SQLException e) {
            System.out.println("Error closing resources: " + e.getMessage());
        }
    }

    private void insertBookMenu() {
        System.out.println("Choose an option:");
        System.out.println("1. Select Author");
        System.out.println("2. New Author");
        System.out.println("3. Without Author");
        System.out.println("4. Exit");

        int choice = helper.nextInt(scanner);
        helper.nextLine(scanner);

        switch (choice) {
            case 1:
                insertBookWithExistingAuthor();
                break;
            case 2:
                insertBookWithNewAuthor();
                break;
            case 3:
                insertBookWithoutAuthor();
                break;
            case 4:
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
        }
    }

    private void updateBookMenu() {
        listBooks();
        System.out.println("Enter Book ID to update: ");
        int bookId = helper.nextInt(scanner);
        helper.nextLine(scanner);

        System.out.println("Choose an option:");
        System.out.println("1. Update existing book");
        System.out.println("2. Insert new book");
        System.out.println("3. Exit");

        int choice = helper.nextInt(scanner);
        helper.nextLine(scanner);

        switch (choice) {
            case 1:
                updateExistingBook(bookId);
                break;
            case 2:
                insertBookMenu();
                break;
            case 3:
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
        }
    }

    private void updateExistingBook(int bookId) {
        System.out.println("Enter new Book Name: ");
        String newBookName = helper.nextLine(scanner);
        updateBook(bookId, newBookName);

        System.out.println("Choose an option:");
        System.out.println("1. Update Author");
        System.out.println("2. Exit");

        int choice = helper.nextInt(scanner);
        helper.nextLine(scanner);

        if (choice == 1) {
            listAuthors();
            System.out.println("Enter the Author ID to associate with this book: ");
            int authorId = helper.nextInt(scanner);
            helper.nextLine(scanner);
            associateBookWithAuthor(authorId, bookId);
        }
    }

    private void updateAuthorMenu() {
        listAuthors();
        System.out.println("Enter Author ID to update: ");
        int authorId = helper.nextInt(scanner);
        helper.nextLine(scanner);

        System.out.println("Choose an option:");
        System.out.println("1. Update existing author");
        System.out.println("2. Insert new author");
        System.out.println("3. Exit");

        int choice = helper.nextInt(scanner);
        helper.nextLine(scanner);

        switch (choice) {
            case 1:
                updateExistingAuthor(authorId);
                break;
            case 2: {
                System.out.println("Enter Name: ");
                String authorName = helper.nextLine(scanner);
                insertAuthor(authorName);
            }
            break;
            case 3:
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
        }
    }

    private void updateExistingAuthor(int authorId) {
        System.out.println("Enter new Author Name: ");
        String newAuthorName = helper.nextLine(scanner);
        updateAuthor(authorId, newAuthorName);

        System.out.println("Choose an option:");
        System.out.println("1. Update Book");
        System.out.println("2. Exit");

        int choice = helper.nextInt(scanner);
        helper.nextLine(scanner);

        if (choice == 1) {
            listBooks();
            System.out.println("Enter the Book ID to associate with this author: ");
            int bookId = helper.nextInt(scanner);
            helper.nextLine(scanner);
            associateBookWithAuthor(authorId, bookId);
        }
    }

    private void insertBookWithExistingAuthor() {
        System.out.println("Enter the Book Name: ");
        String bookName = helper.nextLine(scanner);
        int bookId = insertBook(bookName);

        listAuthors();
        System.out.println("Enter the Author ID to associate with this book: ");
        int authorId = helper.nextInt(scanner);
        helper.nextLine(scanner);

        associateBookWithAuthor(authorId, bookId);

        System.out.println("Book and author relationship inserted successfully.");
    }

    private void insertBookWithNewAuthor() {
        System.out.println("Enter the Book Name: ");
        String bookName = helper.nextLine(scanner);
        int bookId = insertBook(bookName);

        System.out.println("Enter the Author Name: ");
        String authorName = helper.nextLine(scanner);
        int authorId = insertAuthor(authorName);

        associateBookWithAuthor(authorId, bookId);

        System.out.println("Book and author relationship inserted successfully.");
    }

    private void insertBookWithoutAuthor() {
        System.out.println("Enter the Book Name: ");
        String bookName = helper.nextLine(scanner);
        insertBook(bookName);
        System.out.println("Book inserted successfully.");
    }

    private void deleteBook() {
        listBooks();
        System.out.println("Enter Book ID to delete: ");
        int bookId = helper.nextInt(scanner);
        helper.nextLine(scanner);

        System.out.println("Choose an option:");
        System.out.println("1. Delete Book with Author");
        System.out.println("2. Delete Book with Multiple Authors");
        System.out.println("3. Delete Book Without Authors");

        int choice = helper.nextInt(scanner);
        helper.nextLine(scanner);
        switch (choice) {
            case 1:
                deleteBookWithAuthor(bookId);
                break;
            case 2:
                deleteBookWithMultipleAuthors(bookId);
                break;
            case 3:
                deleteBookWithoutAuthors(bookId);
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
        }
    }

    private void deleteBookWithAuthor(int bookId) {
        try {
            stmt = helper.prepareStatement(deleteBookQuery());
            helper.setIntParameter(stmt, 1, bookId);
            helper.executeUpdate(stmt);
            System.out.println("Book deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());
        }
    }

    private void deleteBookWithMultipleAuthors(int bookId) {
        try {
            stmt = helper.prepareStatement(deleteAuthorBookByBookQuery());
            helper.setIntParameter(stmt, 1, bookId);
            helper.executeUpdate(stmt);
            deleteBookWithAuthor(bookId);
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());
        }
    }

    private void deleteBookWithoutAuthors(int bookId) {
        deleteBookWithAuthor(bookId);
    }

    private void updateBook(int bookId, String newBookName) {
        try {
            stmt = helper.prepareStatement(updateBookQuery());
            helper.setStringParameter(stmt, 1, newBookName);
            helper.setIntParameter(stmt, 2, bookId);
            helper.executeUpdate(stmt);
            System.out.println("Book updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());
        }
    }

    private void addAuthor() {
        System.out.println("Enter the Author Name: ");
        String authorName = helper.nextLine(scanner);
        insertAuthor(authorName);
        System.out.println("Author added successfully.");
    }

    public void viewAuthorsWithBooks() {
        try {
            statement = connection.createStatement();
            rs = helper.executeQueryWithStatement(statement, viewAuthorsWithBooksQuery());
            System.out.println("Authors with Books:");
            while (helper.nextResultSet(rs)) {
                int authorId = helper.getIntFromResultSet(rs, "author_id");
                String authorName = helper.getStringFromResultSet(rs, "author_name");
                String bookName = helper.getStringFromResultSet(rs, "book_name");
                System.out.println("Author ID: " + authorId + ", Author Name: " + authorName + ", Book Name: " + (bookName != null ? bookName : "No Book"));
            }
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());
        }
    }

    private void deleteAuthor() {
        listAuthors();
        System.out.println("Enter Author ID to delete: ");
        int authorId = helper.nextInt(scanner);
        helper.nextLine(scanner);

        System.out.println("Choose an option:");
        System.out.println("1. Delete Book with Author");
        System.out.println("2. Delete Book with Multiple Authors");
        System.out.println("3. Delete Book Without Authors");

        int choice = helper.nextInt(scanner);
        helper.nextLine(scanner);

        switch (choice) {
            case 1:
                deleteAuthorWithBooks(authorId);
                break;
            case 2:
                deleteAuthorWithMultipleBooks(authorId);
                break;
            case 3:
                deleteAuthorWithoutBooks(authorId);
                break;
            default:
                System.out.println("Invalid choice. Please select a valid option.");
        }
    }

    private void deleteAuthorWithBooks(int authorId) {
        try {
            stmt = helper.prepareStatement(deleteAuthorQuery());
            helper.setIntParameter(stmt, 1, authorId);
            helper.executeUpdate(stmt);
            System.out.println("Author and associated books deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());
        }
    }

    private void deleteAuthorWithMultipleBooks(int authorId) {
        try {
            stmt = helper.prepareStatement(deleteAuthorBookByAuthorQuery());
            helper.setIntParameter(stmt, 1, authorId);
            helper.executeUpdate(stmt);
            deleteAuthorWithBooks(authorId);
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());
        }
    }

    private void deleteAuthorWithoutBooks(int authorId) {
        deleteAuthorWithBooks(authorId);
    }

    private void updateAuthor(int authorId, String newAuthorName) {
        try {
            stmt = helper.prepareStatement(updateAuthorQuery());
            helper.setStringParameter(stmt, 1, newAuthorName);
            helper.setIntParameter(stmt, 2, authorId);
            helper.executeUpdate(stmt);
            System.out.println("Author updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());
        }
    }

    private void searchBook() {
        System.out.println("Enter Book Name or Author Name to search: ");
        String searchTerm = helper.nextLine(scanner);

        try {
            stmt = helper.prepareStatement(searchBookQuery());
            helper.setStringParameter(stmt, 1, "%" + searchTerm + "%");
            helper.setStringParameter(stmt, 2, "%" + searchTerm + "%");

            rs = helper.executeQueryWithStatement(stmt);
            System.out.println("Search Results:");
            while (helper.nextResultSet(rs)) {
                int bookId = helper.getIntFromResultSet(rs, "book_id");
                String bookName = helper.getStringFromResultSet(rs, "book_name");
                String authorName = helper.getStringFromResultSet(rs, "author_name");
                System.out.println("Book ID: " + bookId + ", Book Name: " + bookName + ", Author Name: " + (authorName != null ? authorName : "No Author"));
            }
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());
        }
    }

    private int insertBook(String bookName) {
        try {
            stmt = helper.prepareStatementWithGeneratedKeys(insertBookQuery());
            helper.setStringParameter(stmt, 1, bookName);
            helper.executeUpdate(stmt);
            rs = stmt.getGeneratedKeys();
            if (helper.nextResultSet(rs)) {
                return helper.getGeneratedKeyFromResultSet(rs, 1);
            } else {
                throw new SQLException("Failed to retrieve book ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());
        }
        return -1;
    }

    private int insertAuthor(String authorName) {
        try {
            stmt = helper.prepareStatementWithGeneratedKeys(insertAuthorQuery());
            helper.setStringParameter(stmt, 1, authorName);
            helper.executeUpdate(stmt);
            rs = stmt.getGeneratedKeys();
            if (helper.nextResultSet(rs)) {
                return helper.getGeneratedKeyFromResultSet(rs, 1);
            } else {
                throw new SQLException("Failed to retrieve author ID.");
            }
        } catch (SQLException e) {
            System.out.println("The error occurred: " + e.getMessage());
        }
        return -1;
    }

    private void associateBookWithAuthor(int authorId, int bookId) {
        try {
            stmt = helper.prepareStatement(associateBookWithAuthorQuery());
            helper.setIntParameter(stmt, 1, authorId);
            helper.setIntParameter(stmt, 2, bookId);
            helper.executeUpdate(stmt);
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());
        }
    }

    private void listBooks() {
        try {
            statement = connection.createStatement();
            rs = helper.executeQueryWithStatement(statement, listBooksQuery());
            System.out.println("Books:");
            while (helper.nextResultSet(rs)) {
                int bookId = helper.getIntFromResultSet(rs, "book_id");
                String bookName = helper.getStringFromResultSet(rs, "book_name");
                System.out.println("Book ID: " + bookId + ", Book Name: " + bookName);
            }
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());
        }
    }

    private void listAuthors() {
        try {
            statement = connection.createStatement();
            rs = helper.executeQueryWithStatement(statement, listAuthorsQuery());
            System.out.println("Authors:");
            while (helper.nextResultSet(rs)) {
                int authorId = helper.getIntFromResultSet(rs, "author_id");
                String authorName = helper.getStringFromResultSet(rs, "author_name");
                System.out.println("Author ID: " + authorId + ", Author Name: " + authorName);
            }
        } catch (SQLException e) {
            System.out.println("Error!! : " + e.getMessage());
        }
    }

    public void addMultipleAuthorsToBook() {
        listBooks();
        System.out.println("Enter the Book ID: ");
        int bookId = helper.nextInt(scanner);
        helper.nextLine(scanner);

        System.out.println("Enter the Book Name: ");
        String bookName = helper.nextLine(scanner);

        System.out.println("Enter the number of authors to add: ");
        int authorCount = helper.nextInt(scanner);
        helper.nextLine(scanner);

        ArrayList<String> authors = new ArrayList<>();
        for (int i = 0; i < authorCount; i++) {
            System.out.println("Enter Author Name " + (i + 1) + ": ");
            String authorName = helper.nextLine(scanner);
            authors.add(authorName);
        }

        // Ensure the book exists
        insertBookIfNotExists(bookId, bookName);

        // Insert authors and associate them with the book
        for (String author : authors) {
            int authorId = getOrInsertAuthor(author);
            if (authorId != -1) {
                associateBookWithAuthor(authorId, bookId);
            }
        }

        System.out.println("Successfully added authors to the book.");
    }

    private void insertBookIfNotExists(int bookId, String bookName) {
        try {
            stmt = helper.prepareStatement(insertBookIfNotExistsQuery());
            helper.setIntParameter(stmt, 1, bookId);
            helper.setStringParameter(stmt, 2, bookName);
            helper.executeUpdate(stmt);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private int getOrInsertAuthor(String authorName) {
        try {
            stmt = helper.prepareStatement(selectAuthorByNameQuery());
            helper.setStringParameter(stmt, 1, authorName);
            rs = helper.executeQueryWithStatement(stmt);
            if (helper.nextResultSet(rs)) {
                return helper.getIntFromResultSet(rs, "author_id");
            }
            stmt = helper.prepareStatementWithGeneratedKeys(insertAuthorQuery());
            helper.setStringParameter(stmt, 1, authorName);
            helper.executeUpdate(stmt);
            rs = stmt.getGeneratedKeys();
            if (helper.nextResultSet(rs)) {
                return helper.getGeneratedKeyFromResultSet(rs, 1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public void viewBooksWithAuthors() {
        try {
            statement = connection.createStatement();
            rs = helper.executeQueryWithStatement(statement, viewBooksWithAuthorsQuery());

            // LinkedHashMap to maintain insertion order
            Map<Integer, Book> bookMap = new LinkedHashMap<>();

            while (helper.nextResultSet(rs)) {
                int bookId = helper.getIntFromResultSet(rs, "book_id");
                String bookName = helper.getStringFromResultSet(rs, "book_name");
                String authorName = helper.getStringFromResultSet(rs, "author_name");

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

    // Query methods
    private String createBooksTableQuery() {
        return "CREATE TABLE IF NOT EXISTS books (" +
                "book_id INT AUTO_INCREMENT, " +
                "book_name VARCHAR(55), " +
                "PRIMARY KEY (book_id))";
    }

    private String createAuthorsTableQuery() {
        return "CREATE TABLE IF NOT EXISTS authors (" +
                "author_id INT AUTO_INCREMENT, " +
                "author_name VARCHAR(55), " +
                "PRIMARY KEY (author_id))";
    }

    private String createAuthorBookTableQuery() {
        return "CREATE TABLE IF NOT EXISTS author_book (" +
                "author_id INT, " +
                "book_id INT, " +
                "PRIMARY KEY (author_id, book_id), " +
                "FOREIGN KEY (author_id) REFERENCES authors(author_id) ON DELETE CASCADE ON UPDATE CASCADE, " +
                "FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE ON UPDATE CASCADE)";
    }

    private String deleteBookQuery() {
        return "DELETE FROM books WHERE book_id = ?";
    }

    private String deleteAuthorBookByBookQuery() {
        return "DELETE FROM author_book WHERE book_id = ?";
    }

    private String updateBookQuery() {
        return "UPDATE books SET book_name = ? WHERE book_id = ?";
    }

    private String viewAuthorsWithBooksQuery() {
        return "SELECT a.author_id, a.author_name, b.book_name FROM authors a " +
                "LEFT JOIN author_book ab ON a.author_id = ab.author_id " +
                "LEFT JOIN books b ON ab.book_id = b.book_id";
    }

    private String deleteAuthorQuery() {
        return "DELETE FROM authors WHERE author_id = ?";
    }

    private String deleteAuthorBookByAuthorQuery() {
        return "DELETE FROM author_book WHERE author_id = ?";
    }

    private String updateAuthorQuery() {
        return "UPDATE authors SET author_name = ? WHERE author_id = ?";
    }

    private String searchBookQuery() {
        return "SELECT b.book_id, b.book_name, a.author_name FROM books b " +
                "LEFT JOIN author_book ab ON b.book_id = ab.book_id " +
                "LEFT JOIN authors a ON ab.author_id = a.author_id " +
                "WHERE b.book_name LIKE ? OR a.author_name LIKE ?";
    }

    private String insertBookQuery() {
        return "INSERT INTO books (book_name) VALUES (?)";
    }

    private String insertAuthorQuery() {
        return "INSERT INTO authors (author_name) VALUES (?)";
    }

    private String associateBookWithAuthorQuery() {
        return "INSERT INTO author_book (author_id, book_id) VALUES (?, ?)";
    }

    private String listBooksQuery() {
        return "SELECT * FROM books";
    }

    private String listAuthorsQuery() {
        return "SELECT * FROM authors";
    }

    private String insertBookIfNotExistsQuery() {
        return "INSERT INTO books (book_id, book_name) VALUES (?, ?) ON DUPLICATE KEY UPDATE book_name = VALUES(book_name)";
    }

    private String selectAuthorByNameQuery() {
        return "SELECT author_id FROM authors WHERE author_name = ?";
    }

    private String viewBooksWithAuthorsQuery() {
        return "SELECT b.book_id, b.book_name, a.author_name FROM books b " +
                "LEFT JOIN author_book ab ON b.book_id = ab.book_id " +
                "LEFT JOIN authors a ON ab.author_id = a.author_id";
    }

    // Inner class for Book representation
    class Book {
        private final int id;
        private final String name;
        private final List<String> authors;

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

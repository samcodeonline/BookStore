import java.sql.*;
import java.util.Scanner;

public class HelperMethods {
    private final Connection connection;

    public HelperMethods(Connection connection) {
        this.connection = connection;
    }

    void executeUpdate(PreparedStatement stmt) throws SQLException {
        stmt.executeUpdate();
    }

    void executeUpdate(Statement stmt, String query) throws SQLException {
        stmt.executeUpdate(query);
    }

    PreparedStatement prepareStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    PreparedStatement prepareStatementWithGeneratedKeys(String query) throws SQLException {
        return connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    }

    void setStringParameter(PreparedStatement stmt, int index, String value) throws SQLException {
        stmt.setString(index, value);
    }

    void setIntParameter(PreparedStatement stmt, int index, int value) throws SQLException {
        stmt.setInt(index, value);
    }

    ResultSet executeQueryWithStatement(Statement stmt, String query) throws SQLException {
        return stmt.executeQuery(query);
    }

    ResultSet executeQueryWithStatement(PreparedStatement stmt) throws SQLException {
        return stmt.executeQuery();
    }

    int getIntFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
        return rs.getInt(columnLabel);
    }

    String getStringFromResultSet(ResultSet rs, String columnLabel) throws SQLException {
        return rs.getString(columnLabel);
    }

    int getGeneratedKeyFromResultSet(ResultSet rs, int index) throws SQLException {
        return rs.getInt(index);
    }

    boolean nextResultSet(ResultSet rs) throws SQLException {
        return rs.next();
    }

    String nextLine(Scanner scanner) {
        return scanner.nextLine();
    }

    int nextInt(Scanner scanner) {
        return scanner.nextInt();
    }
}

package jsd.project.bomberman.jdbc;

import java.sql.*;

public class ConnectJBDC {
    private final String hostName = "localhost:3306";
    private final String dbName = "bomberman";
    private final String username = "root";
    private final String password = "nmthn@*3B";
    // jdbc:mysql://localhost:3306/
    private final String connectionURL = "jdbc:mysql://" + hostName + "/";
    // jdbc:mysql://localhost:3306/bomberman
    private final String dbConnectionURL = connectionURL + dbName;

    public Connection connectToDb() {
        Connection connection = null;
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connection = DriverManager.getConnection(connectionURL, username, password);
            if (!databaseExists(connection, dbName)) {
                createDatabase(connection, dbName);
            }
            connection = DriverManager.getConnection(dbConnectionURL, username, password);
            createHighScoreTable(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private boolean databaseExists(Connection connection, String dbName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeQuery("USE " + dbName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private void createDatabase(Connection connection, String dbName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createHighScoreTable(Connection connection) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS high_score (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "score INT NOT NULL" +
                ")";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addHighScore(int score) {
        String insertScoreSQL = "INSERT INTO high_score (score) VALUES (?)";

        try (Connection conn = connectToDb();
             PreparedStatement preparedStatement = conn.prepareStatement(insertScoreSQL)) {
            preparedStatement.setInt(1, score);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer getLastHighScore() {
        String selectLastScoreSQL = "SELECT score FROM high_score ORDER BY id DESC LIMIT 1";

        try (Connection conn = connectToDb();
             PreparedStatement preparedStatement = conn.prepareStatement(selectLastScoreSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("score");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}

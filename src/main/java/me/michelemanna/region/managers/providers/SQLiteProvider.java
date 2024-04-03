package me.michelemanna.region.managers.providers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.michelemanna.region.RegionPlugin;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class SQLiteProvider implements DatabaseProvider {

    private Connection connection;

    @Override
    public void connect() throws SQLException, ClassNotFoundException, IOException {
        File file = new File(RegionPlugin.getInstance().getDataFolder(), "database.db");
        if (!file.exists()) {
            file.createNewFile();
        }

        connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
        Statement statement = connection.createStatement();

        statement.execute(
                "CREATE TABLE IF NOT EXISTS regions (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "player VARCHAR(36) NOT NULL," +
                        "name TEXT NOT NULL," +
                        "world TEXT NOT NULL," +
                        "x DOUBLE NOT NULL," +
                        "y DOUBLE NOT NULL," +
                        "z DOUBLE NOT NULL," +
                        "x2 DOUBLE NOT NULL," +
                        "y2 DOUBLE NOT NULL," +
                        "z2 DOUBLE NOT NULL" +
                        ")"
        );
        statement.execute(
                "CREATE TABLE IF NOT EXISTS whitelist (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "region_id INTEGER NOT NULL," +
                        "player VARCHAR(36) NOT NULL" +
                        ")"
        );

        statement.close();
    }

    @Override
    public void disconnect() throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }

    @Override
    public void closeConnection(Connection connection) throws SQLException {

    }
}

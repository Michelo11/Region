package me.michelemanna.region.managers.providers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.michelemanna.region.RegionPlugin;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class MySQLProvider implements DatabaseProvider {
    private HikariDataSource dataSource;

    @Override
    public void connect() throws SQLException, ClassNotFoundException {
        ConfigurationSection cs = RegionPlugin.getInstance()
                .getConfig()
                .getConfigurationSection("mysql");
        Objects.requireNonNull(cs, "Unable to find the following key: mysql");
        HikariConfig config = new HikariConfig();

        Class.forName("com.mysql.cj.jdbc.Driver");

        config.setJdbcUrl("jdbc:mysql://" + cs.getString("host") + ":" + cs.getString("port") + "/" + cs.getString("database"));
        config.setUsername(cs.getString("username"));
        config.setPassword(cs.getString("password"));
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setConnectionTimeout(10000);
        config.setLeakDetectionThreshold(10000);
        config.setMaximumPoolSize(10);
        config.setMaxLifetime(60000);
        config.setPoolName("RegionPool");
        config.addDataSourceProperty("useSSL", cs.getBoolean("ssl"));

        this.dataSource = new HikariDataSource(config);

        Connection connection = dataSource.getConnection();

        Statement statement = connection.createStatement();

        statement.execute(
                "CREATE TABLE IF NOT EXISTS regions (" +
                        "id INT PRIMARY KEY AUTO_INCREMENT," +
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
                        "id INT PRIMARY KEY AUTO_INCREMENT," +
                        "region_id INTEGER NOT NULL," +
                        "player VARCHAR(36) NOT NULL," +
                        "FOREIGN KEY(region_id) REFERENCES regions(id)" +
                        ")"
        );

        statement.close();
        connection.close();
    }

    @Override
    public void disconnect() throws SQLException {
        this.dataSource.close();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    @Override
    public void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }
}

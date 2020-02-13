package me.Bruno.DataBaseUtility.DB.BaseTypes;

import com.zaxxer.hikari.HikariConfig;

public class MySQL extends Base {

    private HikariConfig config;

    public MySQL(String ip, String port, String database, String username, String password) {
        config = new HikariConfig();

        config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        config.addDataSourceProperty("serverName", ip);
        config.addDataSourceProperty("port", port == null ? "3306" : port);
        config.addDataSourceProperty("databaseName", database != null ? database : "database");
        config.addDataSourceProperty("user", username);
        config.addDataSourceProperty("password", password);
    }

    @Override
    public HikariConfig getSource() {
        return config;
    }

}
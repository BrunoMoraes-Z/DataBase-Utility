package me.Bruno.DataBaseUtility.DB.BaseTypes;

import com.zaxxer.hikari.HikariConfig;

public class MySQL extends Base {

    private HikariConfig source;

    public MySQL(String ip, String port, String database, String username, String password) {
        source = new HikariConfig();

        source.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        source.addDataSourceProperty("serverName", ip);
        source.addDataSourceProperty("port", port == null ? "3306" : port);
        source.addDataSourceProperty("databaseName", database != null ? database : "database");
        source.addDataSourceProperty("user", username);
        source.addDataSourceProperty("password", password);
        source.setAutoCommit(true);
    }

    @Override
    public HikariConfig getSource() {
        return source;
    }

}
package me.Bruno.DataBaseUtility.DB.BaseTypes;

import com.zaxxer.hikari.HikariConfig;

public class Oracle extends Base {

    private HikariConfig source;

    public Oracle(String ip, String port, String database, String username, String password) {
        source = new HikariConfig();
        source.setMaximumPoolSize(100);
        source.setDataSourceClassName("oracle.jdbc.pool.OracleDataSource");
        source.addDataSourceProperty("serverName", ip);
        source.addDataSourceProperty("port", port != null ? port : "1521");
        source.addDataSourceProperty("databaseName", database != null ? database : "XE");
        source.addDataSourceProperty("user", username);
        source.addDataSourceProperty("password", password);
    }

    public HikariConfig getSource() {
        return this.source;
    }

}

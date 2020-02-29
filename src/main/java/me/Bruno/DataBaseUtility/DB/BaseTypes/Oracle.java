package me.Bruno.DataBaseUtility.DB.BaseTypes;

import com.zaxxer.hikari.HikariConfig;

public class Oracle extends Base {

    private HikariConfig source;

    public Oracle(String ip, String port, String database, String username, String password) {
        source = new HikariConfig();
        source.setMaximumPoolSize(100);
        source.addDataSourceProperty("serverName", ip);
        source.addDataSourceProperty("port", port != null ? port : "1521");
        source.addDataSourceProperty("databaseName", database != null ? database : "XE");
        source.addDataSourceProperty("user", username);
        source.addDataSourceProperty("password", password);
        source.setConnectionTestQuery("SELECT 1 FROM DUAL");
        source.setJdbcUrl("jdbc:oracle:thin:@//" + ip + ":" + (port != null ? port : "1521") + "/" + (database != null ? database : "XE"));
        source.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        source.setAutoCommit(true);
    }

    public HikariConfig getSource() {
        return this.source;
    }

}

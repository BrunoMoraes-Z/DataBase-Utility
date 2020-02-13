package me.Bruno.DataBaseUtility.DB.BaseTypes;

import com.zaxxer.hikari.HikariConfig;

import java.io.File;

public class SQLite extends Base {

    private HikariConfig source;

    public SQLite(String path) {
        File dir = new File((path == null || path.isEmpty()) ? System.getProperty("user.dir") : path);
        
        if (!dir.exists()) {
            dir.mkdir();
        }
        
        File base = new File(dir.getAbsolutePath() + File.separator + "Database.db");

        source = new HikariConfig();
        source.setDriverClassName("org.sqlite.JDBC");
        source.setJdbcUrl("jdbc:sqlite:" + base.getAbsolutePath());
        source.setConnectionTestQuery("SELECT 1");
        source.setMaxLifetime(60000);
        source.setIdleTimeout(45000);
        source.setMaximumPoolSize(50);
    }

    public HikariConfig getSource() {
        return source;
    }

}
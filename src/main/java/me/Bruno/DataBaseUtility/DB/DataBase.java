package me.Bruno.DataBaseUtility.DB;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariDataSource;

import me.Bruno.DataBaseUtility.DB.BaseTypes.Base;
import me.Bruno.DataBaseUtility.DB.BaseTypes.MySQL;
import me.Bruno.DataBaseUtility.DB.BaseTypes.Oracle;
import me.Bruno.DataBaseUtility.DB.BaseTypes.SQLite;

public class DataBase {

    private DataBaseType type;
    private HikariDataSource ds;
    private Base base;
    private String ip;
    private String port;
    private String database;
    private String login;
    private String password;
    private String path;

    public DataBase() {
        build();
    }

    public DataBase(String path) {
        this.type = DataBaseType.SQLITE;
        this.path = path;
        build();
    }

    public DataBase(DataBaseType type) {
        this.type = type;
        build();
    }

    public DataBase(DataBaseType type, String ip, String port, String database, String login, String password) {
        this.type = type;
        this.ip = ip;
        this.port = port;
        this.database = database;
        this.login = login;
        this.password = password;
        build();
    }

    private void build() {
        if (this.type != null) {
            switch (this.type) {
            case ORACLE:
                this.base = new Oracle(this.ip, this.port, this.database, this.login, this.password);
                break;
            case MYSQL:
                this.base = new MySQL(this.ip, this.port, this.database, this.login, this.password);
                break;
            case SQLITE:
                this.base = new SQLite(this.path);
                break;
            }
            this.ds = new HikariDataSource(base.getSource());
        }
    }

    public DataBaseType getType() {
        return this.type;
    }

    public <T> T execute(ConnectionCallback<T> callback) {
        try (Connection con = this.ds.getConnection()) {
            return callback.doInConnection(con);
        } catch (Exception e) {
            throw new IllegalStateException("Error during execution.", e);
        }
    }

    public static interface ConnectionCallback<T> {
        public T doInConnection(Connection con) throws SQLException;
    }

}
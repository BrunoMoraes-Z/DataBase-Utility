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
    private String ip, port, database, login, password, path;

    public DataBase(String path) {
        this.type = DataBaseType.SQLITE;
        this.path = path;
    }

    public DataBase(DataBaseType type) {
        this.type = type;
    }

    public DataBase(DataBaseType type, String ip, String port, String database, String login, String password) {
        this.type = type;
        this.ip = ip;
        this.port = port;
        this.database = database;
        this.login = login;
        this.password = password;
    }

    public DataBaseType getType() {
        return this.type;
    }

    public Connection getConnection() {
        if (ds == null || ds.isClosed()) {
            buildSource();
        }
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException("Error during execution.", e);
        }
    }

    private void buildSource() {
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

    public HikariDataSource getDataSource() {
        return this.ds;
    }

    public <T> T execute(ConnectionCallback<T> callback) {
    	if (ds == null || ds.isClosed()) {
            buildSource();
        }
        Connection con = null;
        try {
            con = ds.getConnection();
            return callback.doInConnection(con);
        } catch (SQLException e) {
        	e.printStackTrace();
            try {
                if (con != null && !con.isClosed()) {
                    con.rollback();
                    con.close();
                    if (ds != null && !ds.isClosed()) {
                        this.ds.close();
                        this.ds = null;
                    }
                }
            } catch (Exception ex) {}
            throw new IllegalStateException("Error during execution.", e);
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                    if (ds != null && !ds.isClosed()) {
                        this.ds.close();
                        this.ds = null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static interface ConnectionCallback<T> {
        public T doInConnection(Connection con) throws SQLException;
    }

}
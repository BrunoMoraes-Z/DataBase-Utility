package me.Bruno.DataBaseUtility;

import java.sql.PreparedStatement;
import java.util.UUID;

import me.Bruno.DataBaseUtility.DB.DataBase;
import me.Bruno.DataBaseUtility.DB.DataBaseType;

public class Main {

    private static DataBase db = null;

    public static void main(String[] args) {
        
        boolean started = startBase(true);

        if (!started) {
            System.exit(-1);
        }

        createTables();

        for (int i = 0; i < 100; i++) {
            UUID uuid = UUID.randomUUID();
            boolean b1 = addPlayer(uuid);
            if (b1) {
                createSign("TESTE_" + i, uuid.toString());
            }
        }

    }

    public static boolean addPlayer(UUID uuid) {
        Boolean b = db.execute(con -> {
            try {
                PreparedStatement stm = con.prepareStatement("insert into `players` (uuid) values (?)");
                stm.setString(1, uuid.toString());
                stm.executeUpdate();
                return true;
            } catch (Exception e) {
                return false;
            }
        });
        return b;
    }

    public static boolean createSign(String location, String player) {
        Boolean b = db.execute(con -> {
            try {
                PreparedStatement stm = con.prepareStatement("insert into `chests` (id_player, location) values ((select id_player from `players` where uuid = ?), ?)");
                stm.setString(1, player);
                stm.setString(2, location);
                stm.executeUpdate();
                return true;
            } catch (Exception e) {
                return false;
            }
        });
        return b;
    }

    public static boolean startBase(Boolean useSqLite) {
        if (useSqLite) {
            db = new DataBase(DataBaseType.SQLITE);
        } else {
            db = new DataBase(DataBaseType.MYSQL, "localhost", "3306", "SellChest", "root", "");
        }
        return db != null;
    }

    public static void createTables() {
        db.execute(con -> {
            if (db.getType() == DataBaseType.SQLITE) {
                PreparedStatement stm = con.prepareStatement(
                        "create table if not exists players (id_player integer not null , uuid varchar(36) not null, PRIMARY KEY (id_player))");
                stm.executeUpdate();
                stm = con.prepareStatement(
                        "create table if not exists chests (id_chest integer not null , id_player integer not null, location varchar(200) not null, sold text, primary key (id_chest), foreign key (id_player) references players(id_player))");
                stm.executeUpdate();
            } else {
                PreparedStatement stm = con.prepareStatement(
                        "create table if not exists players (id_player integer not null auto_increment, uuid varchar(36) not null, PRIMARY KEY (id_player))");
                stm.executeUpdate();
                stm = con.prepareStatement(
                        "create table if not exists chests (id_chest integer not null auto_increment, id_player integer not null, location varchar(200) not null, sold text, primary key (id_chest), foreign key (id_player) references players(id_player))");
                stm.executeUpdate();
            }
            return null;
        });
    }

}

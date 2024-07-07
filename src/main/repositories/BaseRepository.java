package main.repositories;

import java.io.File;
import java.sql.*;

public class BaseRepository {
    private static final String _DATA_BASE_ = "albumDeFigurinhas.db";
    private static final String _CONNECTION_STRING_ = "jdbc:sqlite:" + _DATA_BASE_;

    public BaseRepository() {
        try {
            Class.forName("org.sqlite.JDBC");
            File f = new File(_DATA_BASE_);
            if (!f.exists()) {
                dbCreate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    // Método para criar as tabelas no banco de dados
    public static void dbCreate() {
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " nome TEXT NOT NULL,\n"
                + " senha TEXT NOT NULL,\n"
                + " perfil TEXT NOT NULL\n"
                + ");";

        String sqlFigurinhas = "CREATE TABLE IF NOT EXISTS figurinhas (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " nome TEXT NOT NULL,\n"
                + " numero INTEGER NOT NULL,\n"
                + " descricao TEXT,\n"
                + " pagina INTEGER NOT NULL,\n"
                + " tag TEXT,\n"
                + " foto BLOB,\n"
                + " album_id INTEGER,\n"  // Chave estrangeira para album
                + " FOREIGN KEY(album_id) REFERENCES album(id)\n"
                + ");";

        String sqlAlbum = "CREATE TABLE IF NOT EXISTS album (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " nome TEXT NOT NULL,\n"
                + " capa BLOB,\n"
                + " paginas INTEGER\n"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            if (conn != null) {
                stmt.execute(sqlUsuarios);
                stmt.execute(sqlFigurinhas);
                stmt.execute(sqlAlbum);
                System.out.println("Tables have been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(_CONNECTION_STRING_);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("Driver SQLite carregado com sucesso.");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver JDBC do SQLite não encontrado.");
            e.printStackTrace();
        }

        dbCreate();
    }
}

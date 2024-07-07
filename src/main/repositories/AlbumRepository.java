package main.repositories;

import main.entities.Album;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumRepository {
    public List<Album> getAllAlbums() {
        String sql = "SELECT * FROM album";
        List<Album> albums = new ArrayList<>();

        try (Connection conn = BaseRepository.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Album album = new Album(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("paginas"),
                        rs.getBytes("capa")
                );
                albums.add(album);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return albums;
    }

    public void addAlbum(Album album) {
        String sql = "INSERT INTO albums(nome, paginas, capa) VALUES(?, ?, ?)";

        try (Connection conn = BaseRepository.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, album.getNome());
            pstmt.setInt(2, album.getPaginas());
            pstmt.setBytes(3, album.getCapa());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateAlbum(Album album) {
        String sql = "UPDATE albums SET nome = ?, paginas = ?, capa = ? WHERE id = ?";

        try (Connection conn = BaseRepository.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, album.getNome());
            pstmt.setInt(2, album.getPaginas());
            pstmt.setBytes(3, album.getCapa());
            pstmt.setInt(4, album.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAlbum(int id) {
        String sql = "DELETE FROM albums WHERE id = ?";

        try (Connection conn = BaseRepository.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Album getAlbumByNome(String nome) {
        String sql = "SELECT * FROM albums WHERE nome = ?";
        Album album = null;

        try (Connection conn = BaseRepository.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            ResultSet rs = pstmt.executeQuery();
            album = new Album(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getInt("paginas"),
                    rs.getBytes("capa")
            );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return album;
    }
}
